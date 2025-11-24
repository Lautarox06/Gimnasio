package gym;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Clase principal que contiene el método main y la interfaz de usuario por consola.
 * Maneja el menú, la lectura de datos y captura las excepciones que puedan surgir.
 * * @author Juampi
 */
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Punto de entrada de la aplicación.
     * Inicializa el servicio, carga datos de prueba y lanza el bucle del menú.
     * @param args Argumentos de consola (no utilizados).
     */
    public static void main(String[] args) {
        GimnasioService servicio = new GimnasioService();

        // Datos hardcodeados para probar funcionalidades sin cargar todo a mano
        try {
            servicio.altaCliente(new Cliente("12345678", "Ana Perez", Plan.BASICO, LocalDate.now().minusMonths(3)));
            servicio.altaCliente(new Cliente("23456789", "Juan Gomez", Plan.FULL, LocalDate.now().minusMonths(13)));
        } catch (Exception e) {
            System.out.println("Aviso: No se pudieron cargar los datos de prueba: " + e.getMessage());
        }

        int op;
        do {
            mostrarMenu();
            // Usamos lectura recursiva para garantizar que ingrese un número
            op = leerEnteroRecursivo("Ingrese opción: ");

            try {
                procesarOpcion(op, servicio);
            } catch (SocioNoEncontradoException e) {
                System.out.println(">> Error de lógica: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(">> Dato inválido: " + e.getMessage());
            } catch (Exception e) {
                System.out.println(">> Ocurrió un error inesperado: " + e.getMessage());
            }

            // Hacemos una pausa para que el usuario pueda leer el resultado
            if(op != 0) esperarEnter();

        } while (op != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n==================================");
        System.out.println("   SISTEMA GESTIÓN GIMNASIO");
        System.out.println("==================================");
        System.out.println("1) Alta de Cliente");
        System.out.println("2) Buscar Socio (por DNI)");
        System.out.println("3) Modificar Nombre");
        System.out.println("4) Cambiar Plan");
        System.out.println("5) Dar de Baja");
        System.out.println("6) Listar Clientes (A-Z)");
        System.out.println("7) Calcular Cuota");
        System.out.println("8) Estadísticas (Altas x Mes)");
        System.out.println("9) Ver Log de Operaciones");
        System.out.println("0) Salir");
        System.out.println("==================================");
    }

    /**
     * Distribuye la lógica según la opción elegida en el menú.
     * Usamos Switch Expression para que el código quede más limpio.
     */
    private static void procesarOpcion(int op, GimnasioService servicio) {
        switch (op) {
            case 1 -> alta(servicio);
            case 2 -> buscar(servicio);
            case 3 -> modificarNombre(servicio);
            case 4 -> cambiarPlan(servicio);
            case 5 -> baja(servicio);
            case 6 -> listar(servicio);
            case 7 -> cuota(servicio);
            case 8 -> altasPorMes(servicio);
            case 9 -> verLog(servicio);
            case 0 -> System.out.println("Cerrando sistema...");
            default -> System.out.println("Opción desconocida, intente de nuevo.");
        }
    }

    /**
     * Lee un entero por consola usando recursividad.
     * <p>
     * <b>Nota técnica:</b> Cumple el requisito de "Ejemplo de recursividad simple".
     * Si el usuario escribe texto en lugar de números, capturamos el error y
     * el método se llama a sí mismo hasta obtener un dato válido.
     * </p>
     * * @param prompt Mensaje a mostrar al usuario.
     * @return Un número entero válido.
     */
    private static int leerEnteroRecursivo(String prompt) {
        System.out.print(prompt);
        try {
            String input = sc.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Error: Debe ingresar un número entero válido.");
            return leerEnteroRecursivo(prompt); // Llamada recursiva
        }
    }

    private static String leerTexto(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    private static void esperarEnter() {
        System.out.println("(Presione Enter para continuar)");
        sc.nextLine();
    }

    private static LocalDate leerFecha(String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        // Si aprieta enter sin escribir nada, asumimos hoy
        if (s.isEmpty()) return LocalDate.now();
        try {
            return LocalDate.parse(s, DF);
        } catch (Exception e) {
            System.out.println("Formato de fecha incorrecto. Se usará la fecha de hoy.");
            return LocalDate.now();
        }
    }

    private static Plan leerPlan() {
        System.out.println("Seleccione Plan: 1=BASICO ($15k)  2=FULL ($22k)  3=PREMIUM ($30k)");
        int n = leerEnteroRecursivo("> ");
        return switch (n) {
            case 1 -> Plan.BASICO;
            case 2 -> Plan.FULL;
            case 3 -> Plan.PREMIUM;
            default -> {
                System.out.println("Opción de plan no válida, asignando BASICO por defecto.");
                yield Plan.BASICO;
            }
        };
    }

    // --- Métodos auxiliares que conectan la consola con el servicio ---

    private static void alta(GimnasioService s) {
        System.out.println("-- Nuevo Socio --");
        String dni = leerTexto("DNI: ").trim();
        if (s.existeDni(dni)) {
            System.out.println("Atención: Ese DNI ya existe.");
            return;
        }
        String nombre = leerTexto("Nombre y Apellido: ");
        Plan plan = leerPlan();
        LocalDate alta = leerFecha("Fecha Alta (dd/MM/yyyy) [Enter=Hoy]: ");

        s.altaCliente(new Cliente(dni, nombre, plan, alta));
        System.out.println(">> Cliente registrado con éxito.");
    }

    private static void buscar(GimnasioService s) {
        String dni = leerTexto("Ingrese DNI a buscar: ").trim();
        Cliente c = s.buscarPorDni(dni);
        System.out.println("Datos del socio: " + c);
    }

    private static void modificarNombre(GimnasioService s) {
        String dni = leerTexto("DNI del socio: ").trim();
        String nom = leerTexto("Nuevo nombre: ");
        s.modificarNombre(dni, nom);
        System.out.println(">> Nombre actualizado.");
    }

    private static void cambiarPlan(GimnasioService s) {
        String dni = leerTexto("DNI del socio: ").trim();
        Plan p = leerPlan();
        s.cambiarPlan(dni, p);
        System.out.println(">> Plan actualizado.");
    }

    private static void baja(GimnasioService s) {
        String dni = leerTexto("DNI a dar de baja: ").trim();
        boolean ok = s.baja(dni);
        if(ok) System.out.println(">> Socio eliminado correctamente.");
        else System.out.println(">> No se encontró socio con ese DNI.");
    }

    private static void listar(GimnasioService s) {
        Cliente[] arr = s.listarOrdenadosPorNombre();
        if (arr.length == 0) System.out.println("(No hay clientes registrados)");
        for (Cliente c : arr) {
            System.out.println(c);
        }
    }

    private static void cuota(GimnasioService s) {
        String dni = leerTexto("DNI del socio: ").trim();
        double q = s.cuotaDe(dni);
        System.out.println("Valor de la cuota: $" + String.format("%.2f", q));
    }

    private static void altasPorMes(GimnasioService s) {
        int[] a = s.getAltasPorMes();
        String[] meses = {"Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"};
        System.out.println("--- Estadísticas de Altas ---");
        for (int i = 0; i < 12; i++) {
            if(a[i] > 0) System.out.println(meses[i] + ": " + a[i]);
        }
    }

    private static void verLog(GimnasioService s) {
        String[] lineas = s.logComoLineas();
        System.out.println("--- Log de Operaciones ---");
        if (lineas.length == 0) {
            System.out.println("(El log está vacío)");
        } else {
            for (String l : lineas) System.out.println(l);
        }
    }
}
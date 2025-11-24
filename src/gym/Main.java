package gym;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        GimnasioService servicio = new GimnasioService();

        // Cargamos algunos datos de prueba para no arrancar vacio
        try {
            servicio.altaCliente(new Cliente("12345678", "Ana Perez", Plan.BASICO, LocalDate.now().minusMonths(3)));
            servicio.altaCliente(new Cliente("23456789", "Juan Gomez", Plan.FULL, LocalDate.now().minusMonths(13)));
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }

        int op;
        do {
            mostrarMenu();
            op = leerEnteroRecursivo("Ingrese opción: "); // Usamos la version recursiva

            try {
                procesarOpcion(op, servicio);
            } catch (SocioNoEncontradoException e) {
                System.out.println(">> Error de logica: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(">> Error de validacion: " + e.getMessage());
            } catch (Exception e) {
                System.out.println(">> Ocurrio un error inesperado: " + e.getMessage());
            }

            // Pausa visual antes de limpiar o seguir
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

    // REQUISITO: Ejemplo de recursividad simple
    // Si el usuario ingresa texto en vez de numero, se llama a si mismo
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

    // Métodos auxiliares para cada opcion del menu

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
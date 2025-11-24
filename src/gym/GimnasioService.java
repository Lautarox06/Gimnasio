package gym;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
// Importamos Comparator aunque usaremos lambdas

public class GimnasioService {
    // Lista principal de clientes (Collection Framework)
    private final ArrayList<Cliente> clientes = new ArrayList<>();
    // Nuestro log personalizado
    private final OperacionLog log = new OperacionLog();
    // Array simple para estadística
    private final int[] altasPorMes = new int[12];

    public void altaCliente(Cliente c){
        if (existeDni(c.getDni())) {
            throw new IllegalArgumentException("El DNI ya esta registrado en el sistema");
        }
        clientes.add(c);

        // Registramos estadística del mes
        LocalDate fa = c.getFechaAlta();
        int idx = fa.getMonthValue() - 1;
        if (idx >= 0 && idx < 12) altasPorMes[idx]++;

        log.push("ALTA: " + c.getDni() + " (" + c.getNombre() + ") - Plan " + c.getPlan());
    }

    public Cliente buscarPorDni(String dni){
        // Usamos stream/filter o un for each simple.
        // El for tradicional es mas claro para debuggear a veces.
        for (Cliente c : clientes) {
            if (c.getDni().equals(dni)) {
                return c;
            }
        }
        throw new SocioNoEncontradoException("No existe cliente con DNI " + dni);
    }

    public void modificarNombre(String dni, String nuevoNombre){
        Cliente c = buscarPorDni(dni);
        String anterior = c.getNombre();
        c.setNombre(nuevoNombre);
        log.push("MODIFICACION: " + dni + " nombre '" + anterior + "' -> '" + nuevoNombre + "'");
    }

    public void cambiarPlan(String dni, Plan p){
        Cliente c = buscarPorDni(dni);
        c.setPlan(p);
        log.push("CAMBIO PLAN: " + dni + " ahora es " + p);
    }

    public boolean baja(String dni){
        // Requisito: Uso de método funcional de colección (removeIf)
        // Esto elimina al cliente si coincide el DNI y devuelve true si borró algo
        boolean borrado = clientes.removeIf(c -> c.getDni().equals(dni));

        if (borrado) {
            log.push("BAJA: " + dni);
        }
        return borrado;
    }

    public Cliente[] listarOrdenadosPorNombre(){
        ArrayList<Cliente> copia = new ArrayList<>(clientes);

        // Requisito: Uso de expresión Lambda para el Comparator
        Collections.sort(copia, (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));

        // Pasamos a array porque asi lo pedia la estructura original
        Cliente[] arr = new Cliente[copia.size()];
        return copia.toArray(arr);
    }

    public double cuotaDe(String dni){
        Cliente c = buscarPorDni(dni);
        return c.calcularCuota();
    }

    public boolean existeDni(String dni){
        // Requisito: Uso de anyMatch (Stream) o bucle.
        // Vamos con bucle for-each para mantenerlo simple pero efectivo.
        for(Cliente c : clientes){
            if(c.getDni().equals(dni)) return true;
        }
        return false;
    }

    public int[] getAltasPorMes(){
        // Devolvemos una copia para proteger el array original
        int[] copia = new int[12];
        System.arraycopy(altasPorMes, 0, copia, 0, 12);
        return copia;
    }

    public String[] logComoLineas(){
        return log.comoLineas();
    }
}
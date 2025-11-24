package gym;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Clase de servicio que contiene la lógica de negocio del gimnasio.
 * Aquí manejamos la lista de clientes, el log de operaciones y las estadísticas.
 * <p>
 * Aplica conceptos de POO, Colecciones y Lambdas.
 * </p>
 * * @author Santi
 */
public class GimnasioService {
    // Usamos ArrayList para los clientes porque necesitamos acceso rápido y recorrido frecuente
    private final ArrayList<Cliente> clientes = new ArrayList<>();

    // Instancia de nuestra estructura propia para guardar eventos
    private final OperacionLog log = new OperacionLog();

    // Array estático para contar altas por mes (índice 0 = Enero)
    private final int[] altasPorMes = new int[12];

    /**
     * Da de alta un cliente nuevo en el sistema.
     * Valida que el DNI no esté duplicado antes de agregar.
     * * @param c Objeto Cliente a registrar.
     * @throws IllegalArgumentException si el DNI ya existe.
     */
    public void altaCliente(Cliente c){
        if (existeDni(c.getDni())) {
            throw new IllegalArgumentException("El DNI ya esta registrado en el sistema");
        }
        clientes.add(c);

        // Actualizamos estadística: mes - 1 porque el array va de 0 a 11
        LocalDate fa = c.getFechaAlta();
        int idx = fa.getMonthValue() - 1;
        if (idx >= 0 && idx < 12) altasPorMes[idx]++;

        log.push("ALTA: " + c.getDni() + " (" + c.getNombre() + ") - Plan " + c.getPlan());
    }

    /**
     * Busca un cliente dentro de la colección.
     * * @param dni El documento a buscar.
     * @return El objeto Cliente si lo encuentra.
     * @throws SocioNoEncontradoException si no hay nadie con ese DNI.
     */
    public Cliente buscarPorDni(String dni){
        for (Cliente c : clientes) {
            if (c.getDni().equals(dni)) {
                return c;
            }
        }
        throw new SocioNoEncontradoException("No existe cliente con DNI " + dni);
    }

    /**
     * Actualiza el nombre de un socio existente y registra la acción en el log.
     * * @param dni DNI del socio a modificar.
     * @param nuevoNombre El nuevo nombre a guardar.
     */
    public void modificarNombre(String dni, String nuevoNombre){
        Cliente c = buscarPorDni(dni);
        String anterior = c.getNombre();
        c.setNombre(nuevoNombre);
        log.push("MODIFICACION: " + dni + " nombre '" + anterior + "' -> '" + nuevoNombre + "'");
    }

    /**
     * Cambia el plan de un socio y registra la acción.
     * @param dni DNI del socio.
     * @param p Nuevo plan seleccionado.
     */
    public void cambiarPlan(String dni, Plan p){
        Cliente c = buscarPorDni(dni);
        c.setPlan(p);
        log.push("CAMBIO PLAN: " + dni + " ahora es " + p);
    }

    /**
     * Elimina un socio del sistema.
     * <p>
     * <b>Nota técnica:</b> Usamos el método funcional <code>removeIf</code> de la colección,
     * pasando una expresión lambda como predicado.
     * </p>
     * * @param dni DNI del socio a eliminar.
     * @return true si se eliminó, false si no se encontró.
     */
    public boolean baja(String dni){
        // Predicado lambda: c -> c.getDni().equals(dni)
        boolean borrado = clientes.removeIf(c -> c.getDni().equals(dni));

        if (borrado) {
            log.push("BAJA: " + dni);
        }
        return borrado;
    }

    /**
     * Genera un listado de clientes ordenado alfabéticamente por nombre.
     * <p>
     * <b>Nota técnica:</b> Usamos <code>Collections.sort</code> con una expresión Lambda
     * para definir el criterio de comparación al vuelo.
     * </p>
     * * @return Arreglo de clientes ordenados.
     */
    public Cliente[] listarOrdenadosPorNombre(){
        ArrayList<Cliente> copia = new ArrayList<>(clientes);

        // Lambda para comparar strings ignorando mayúsculas/minúsculas
        Collections.sort(copia, (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));

        // Convertimos la lista a array para retornarlo
        Cliente[] arr = new Cliente[copia.size()];
        return copia.toArray(arr);
    }

    /**
     * Calcula cuánto debe pagar un cliente específico.
     * Delega el cálculo al propio objeto Cliente (polimorfismo/lógica interna).
     * * @param dni DNI del socio.
     * @return el monto de la cuota.
     */
    public double cuotaDe(String dni){
        Cliente c = buscarPorDni(dni);
        return c.calcularCuota();
    }

    /**
     * Verifica si un DNI ya existe en la lista.
     * Lo usamos para validaciones antes del alta.
     * * @param dni DNI a verificar.
     * @return true si ya existe.
     */
    public boolean existeDni(String dni){
        for(Cliente c : clientes){
            if(c.getDni().equals(dni)) return true;
        }
        return false;
    }

    /**
     * Devuelve una copia de la estadística de altas por mes.
     * @return arreglo de enteros con 12 posiciones.
     */
    public int[] getAltasPorMes(){
        int[] copia = new int[12];
        System.arraycopy(altasPorMes, 0, copia, 0, 12);
        return copia;
    }

    /**
     * Obtiene el historial completo de operaciones.
     * @return array de strings con los logs.
     */
    public String[] logComoLineas(){
        return log.comoLineas();
    }
}
package gym;

/**
 * Interfaz funcional que define el comportamiento de algo que se debe pagar.
 * La usamos para calcular el monto final de la cuota.
 * * @author Lautaro
 */
@FunctionalInterface
public interface Pagable {
    /**
     * Calcula el valor final a pagar.
     * @return monto del pago.
     */
    double calcularCuota();
}
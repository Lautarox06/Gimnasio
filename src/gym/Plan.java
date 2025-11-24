package gym;

/**
 * Enum que define los tipos de planes disponibles en el gimnasio.
 * Cada plan tiene asociado un precio base mensual.
 * * @author Lautaro
 */
public enum Plan {
    /** Plan básico, acceso limitado. Precio: $15000. */
    BASICO(15000),

    /** Plan completo, acceso a todas las máquinas. Precio: $22000. */
    FULL(22000),

    /** Plan premium, incluye clases y spa. Precio: $30000. */
    PREMIUM(30000);

    private final double precio;

    Plan(double p){
        this.precio = p;
    }

    /**
     * Devuelve el precio base del plan sin descuentos aplicados.
     * @return el valor monetario del plan.
     */
    public double precio(){
        return precio;
    }
}
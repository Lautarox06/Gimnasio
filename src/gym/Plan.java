package gym;

public enum Plan {
    // Precios actualizados al mes corriente
    BASICO(15000),
    FULL(22000),
    PREMIUM(30000);

    private final double precio;

    Plan(double p){
        this.precio = p;
    }

    public double precio(){
        return precio;
    }
}
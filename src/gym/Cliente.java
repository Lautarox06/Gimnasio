package gym;

import java.time.LocalDate;
import java.time.Period;

/**
 * Clase principal que representa a un socio del gimnasio.
 * Implementa las interfaces Pagable (para la cuota) e Identificable (por el DNI).
 * * @author Lautaro
 */
public class Cliente implements Pagable, Identificable {
    private final String dni;
    private String nombre;
    private Plan plan;
    private final LocalDate fechaAlta;

    /**
     * Constructor para dar de alta un nuevo cliente.
     * * @param dni Documento único. No puede estar vacío.
     * @param nombre Nombre y apellido del socio.
     * @param plan El plan elegido (BASICO, FULL, PREMIUM).
     * @param fechaAlta Fecha en la que se inscribió.
     * @throws IllegalArgumentException si el DNI es nulo o vacío.
     */
    public Cliente(String dni, String nombre, Plan plan, LocalDate fechaAlta) {
        if(dni == null || dni.isEmpty()) {
            throw new IllegalArgumentException("El DNI es obligatorio para crear un cliente.");
        }
        this.dni = dni;
        this.nombre = nombre;
        this.plan = plan;
        this.fechaAlta = fechaAlta;
    }

    public String getDni(){ return dni; }

    public String getNombre(){ return nombre; }

    public void setNombre(String n){ this.nombre = n; }

    public Plan getPlan(){ return plan; }

    public void setPlan(Plan p){ this.plan = p; }

    public LocalDate getFechaAlta(){ return fechaAlta; }

    /**
     * Implementación de Identificable.
     * Usamos el DNI como ID único del cliente.
     */
    @Override
    public String id() { return dni; }

    /**
     * Calcula la cuota mensual aplicando reglas de negocio.
     * Regla: Si el socio tiene más de 1 año (12 meses) de antigüedad,
     * se le aplica un 10% de descuento sobre el precio base.
     * * @return el precio final a pagar.
     */
    @Override
    public double calcularCuota() {
        long meses = Period.between(fechaAlta, LocalDate.now()).toTotalMonths();
        double base = plan.precio();

        // Si ya pasó un año, aplicamos descuento
        if (meses >= 12) {
            return base * 0.9;
        } else {
            return base;
        }
    }

    @Override
    public String toString(){
        return "Cliente [DNI=" + dni + ", Nombre=" + nombre + ", Plan=" + plan + ", Alta=" + fechaAlta + "]";
    }

    /**
     * Compara si dos clientes son iguales basándose únicamente en su DNI.
     * Necesario para que funcionen bien los métodos de búsqueda en las listas.
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente c = (Cliente)o;
        return dni != null && dni.equals(c.dni);
    }

    @Override
    public int hashCode(){
        return dni == null ? 0 : dni.hashCode();
    }
}
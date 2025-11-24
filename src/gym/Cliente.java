package gym;

import java.time.LocalDate;
import java.time.Period;

// Clase principal que representa al socio del gimnasio
public class Cliente implements Pagable, Identificable {
    private final String dni;
    private String nombre;
    private Plan plan;
    private final LocalDate fechaAlta;

    public Cliente(String dni, String nombre, Plan plan, LocalDate fechaAlta) {
        // Validamos que el DNI no llegue nulo
        if(dni == null || dni.isEmpty()) {
            throw new IllegalArgumentException("El DNI es obligatorio");
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

    @Override
    public String id() { return dni; }

    @Override
    public double calcularCuota() {
        // Calculamos meses de antigüedad para ver si aplica descuento
        long meses = Period.between(fechaAlta, LocalDate.now()).toTotalMonths();
        double base = plan.precio();

        // Si tiene un año o más (12 meses), le hacemos un 10% de descuento
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

    // Implementamos equals y hashCode para que las colecciones funcionen bien al buscar
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
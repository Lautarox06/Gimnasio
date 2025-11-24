package gym;

/**
 * Interfaz funcional para cualquier entidad que tenga un ID único.
 * Nos sirve para identificar objetos de forma genérica.
 * * @author Lautaro
 */
@FunctionalInterface
public interface Identificable {
    /**
     * @return el identificador único (como el DNI o ID) en formato String.
     */
    String id();
}
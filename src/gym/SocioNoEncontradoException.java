package gym;

/**
 * Excepción personalizada para cuando se intenta buscar
 * o modificar un socio que no está en la lista.
 */
public class SocioNoEncontradoException extends RuntimeException {
    public SocioNoEncontradoException(String msg){
        super(msg);
    }
}
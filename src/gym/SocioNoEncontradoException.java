package gym;

// Excepcion personalizada para cuando buscamos un DNI que no existe
public class SocioNoEncontradoException extends RuntimeException {
    public SocioNoEncontradoException(String msg){
        super(msg);
    }
}
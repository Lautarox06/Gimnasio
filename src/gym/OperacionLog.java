package gym;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Estructura de datos propia (TAD) para manejar el historial de acciones.
 * Es una lista enlazada simple implementada manualmente.
 * <p>
 * Decidimos hacerla así para cumplir con el requisito de "Implementar estructura propia"
 * y no depender solo de ArrayList o LinkedList de Java.
 * </p>
 * * @author Mateo
 */
public class OperacionLog {

    /**
     * Nodo interno para la lista enlazada.
     * Guarda la fecha/hora y el mensaje del evento.
     */
    static class Nodo {
        LocalDateTime ts;
        String msg;
        Nodo next;

        Nodo(LocalDateTime t, String m){
            ts = t;
            msg = m;
        }
    }

    private Nodo head;
    private Nodo tail;
    private int size = 0;

    /**
     * Agrega un nuevo registro al final de la lista (log).
     * Tiene complejidad O(1) porque mantenemos un puntero al final (tail).
     * * @param msg El mensaje descriptivo de la operación.
     */
    public void push(String msg){
        Nodo n = new Nodo(LocalDateTime.now(), msg);
        if (head == null) {
            head = n;
            tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    /**
     * @return Cantidad de operaciones registradas.
     */
    public int size(){ return size; }

    /**
     * Recorre la lista enlazada y convierte todos los nodos a un arreglo de Strings.
     * Útil para imprimir el historial en la consola.
     * * @return Arreglo con cada línea del log formateada con fecha y hora.
     */
    public String[] comoLineas(){
        String[] out = new String[size];
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Nodo it = head;
        int i = 0;
        // Recorremos hasta que no queden nodos
        while (it != null) {
            out[i] = "[" + it.ts.format(f) + "] " + it.msg;
            i++;
            it = it.next;
        }
        return out;
    }
}
package gym;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Estructura propia: Una lista enlazada simple para guardar el historial de acciones
// No usamos LinkedList de Java aqui porque el TP pedia una estructura propia.
public class OperacionLog {

    // Clase interna para los nodos de la lista
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

    // Agrega un evento al final de la lista
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

    public int size(){ return size; }

    // Convierte la lista enlazada a un array de Strings para mostrarlo fácil
    public String[] comoLineas(){
        String[] out = new String[size];
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Nodo it = head;
        int i = 0;
        while (it != null) {
            out[i] = "[" + it.ts.format(f) + "] " + it.msg;
            i++;
            it = it.next;
        }
        return out;
    }
}
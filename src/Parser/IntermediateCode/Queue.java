package Parser.IntermediateCode;

import java.util.ArrayList;
import java.util.List;

public class Queue implements Cloneable {

    private List<Third> stack = new ArrayList<>();

    public Queue(){
    }

    public Queue(Queue q) {
        this.stack = new ArrayList<>(q.getThirds());
    }

    public List<Third> getThirds() {
        return this.stack;
    }

    public void push (Third third){ // Agrega terceto a la pila.
        stack.add(third);
    }

    public Third head(){   // Retorna el tope de la pila.
        return stack.get(0);
    }

    public Third pop(){   // Retorna y elimina el tope de la pila.
        if (this.stack.size() == 0)
            return null;

        Third head = stack.get(0);
        stack.remove(0);
        return head;
    }

    public int size() {
        return this.stack.size();
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new Error("Something impossible just happened");
        }
    }
}
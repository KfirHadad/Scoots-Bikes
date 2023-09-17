import java.util.ArrayList;
import java.util.List;

public class Queue<T> {

    private List<T> queue;

    public Queue() {
        this.queue = new ArrayList<>();
    }

    public synchronized void add(T item) {
        queue.add(item);
        notify();
    }

    public synchronized T remove() throws InterruptedException {
        while (queue.isEmpty()) {
            wait(); //wait for the queue not to be empty 
        }
        return queue.remove(0);
    }

    public int getSize() {
        return this.queue.size();
    }

    public boolean isEmpty() {
        return this.queue.isEmpty();
    }
}
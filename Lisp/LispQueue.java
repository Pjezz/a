package Lisp;

import java.util.LinkedList;
import java.util.Queue;

public class LispQueue {
    private Queue<Object> queue = new LinkedList<>();

    public void enqueue(Object item) {
        queue.add(item);
    }

    public Object dequeue() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
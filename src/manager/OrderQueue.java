package manager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import model.MenuItem;

public class OrderQueue {

    private final Queue<MenuItem> queue = new ArrayDeque<>();

    public void enqueue(MenuItem item) {
        queue.add(item);
    }

    public MenuItem dequeue() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public List<MenuItem> peekAll() {
        return new ArrayList<>(queue);
    }

    public void clear() {
        queue.clear();
    }
}
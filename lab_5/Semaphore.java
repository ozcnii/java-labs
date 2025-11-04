package lab_5;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Задание 2: Самописный семафор с очередью
 */
public class Semaphore {
    private int permits;
    private Queue<Thread> queue;

    public Semaphore(int permits) {
        this.permits = permits;
        this.queue = new LinkedList<>();
    }

    public synchronized void acquire() throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        queue.add(currentThread);

        while (queue.peek() != currentThread || permits <= 0) {
            wait();
        }

        queue.poll();
        permits--;
    }

    public synchronized void release() {
        permits++;
        notifyAll();
    }

    public synchronized int availablePermits() {
        return permits;
    }
}


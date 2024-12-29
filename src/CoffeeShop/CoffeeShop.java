package CoffeeShop;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class CoffeeShop {
    private final Queue<String> orderQueue;
    private final Semaphore semaphore;

    public CoffeeShop(int capacity) {
        orderQueue = new LinkedList<>();
        semaphore = new Semaphore(capacity, true);
    }

    public synchronized void placeOrder(String order) throws InterruptedException {
        semaphore.acquire();
        orderQueue.add(order);
    }

    public synchronized String prepareOrder() throws InterruptedException {
        while (orderQueue.isEmpty()) {
            wait();
        }
        semaphore.release();
        return orderQueue.poll();
    }
}

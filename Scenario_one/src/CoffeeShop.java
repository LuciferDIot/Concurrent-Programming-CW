import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * Represents the coffee shop with a shared order queue.
 * This class ensures thread-safe access to the queue using `ReentrantLock` and `Condition`.
 * <p>
 * Best Practices:
 * - Use `ReentrantLock` for mutual exclusion when accessing shared resources.
 * - Use `Condition` for thread coordination (wait/notify mechanism).
 * - Keep the critical section (code inside locks) as short as possible to minimize contention.
 */
public class CoffeeShop {
    private final Queue<String> orderQueue; // Shared order queue
    private final int MAX_ORDERS; // Maximum number of orders the queue can hold

    private final Lock lock = new ReentrantLock(true); // Fair lock
    private final Condition notFull = lock.newCondition(); // Condition for waiting when the queue is full
    private final Condition notEmpty = lock.newCondition(); // Condition for waiting when the queue is empty

    /**
     * Constructor to initialize the coffee shop with a maximum number of orders.
     *
     * @param maxOrders The maximum number of orders the queue can hold.
     */
    public CoffeeShop(int maxOrders) {
        this.MAX_ORDERS = maxOrders;
        this.orderQueue = new LinkedList<>();
    }

    /**
     * Method for customers to place orders in the queue.
     * <p>
     * Steps Achieved:
     * 1. If the queue is full, customers wait until space is available (Step 1 in the document).
     * 2. Ensures mutual exclusion using `ReentrantLock` to avoid race conditions (Step 4 in the document).
     * 3. Notifies baristas when a new order is added to the queue.
     * <p>
     * Best Practices:
     * - Use `await()` and `signalAll()` for thread coordination.
     * - Always release the lock in a `finally` block to avoid deadlocks.
     *
     * @param order The order to be placed in the queue.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public void placeOrder(String order) throws InterruptedException {
        lock.lock(); // Acquire the lock
        try {
            // Step 1: If the queue is full, customers must wait until there is space available.
            while (orderQueue.size() >= MAX_ORDERS) {
                System.out.println(Thread.currentThread().getName() + " is waiting to place an order.");
                notFull.await(); // Wait for space in the queue
            }
            orderQueue.add(order); // Add order to the queue
            System.out.println(Thread.currentThread().getName() + " placed order: " + order);
            notEmpty.signalAll(); // Notify baristas that a new order is available
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    /**
     * Method for baristas to prepare orders from the queue.
     * <p>
     * Steps Achieved:
     * 1. If the queue is empty, baristas wait until orders are available (Step 1 in the document).
     * 2. Ensures mutual exclusion using `ReentrantLock` to avoid race conditions (Step 4 in the document).
     * 3. Notifies customers when an order is removed from the queue.
     * <p>
     * Best Practices:
     * - Use `await()` and `signalAll()` for thread coordination.
     * - Always release the lock in a `finally` block to avoid deadlocks.
     *
     * @return The prepared order.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public String prepareOrder() throws InterruptedException {
        lock.lock(); // Acquire the lock
        try {
            // Step 1: If the queue is empty, baristas must wait until orders are available.
            while (orderQueue.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + " is waiting for an order.");
                notEmpty.await(); // Wait for orders to be placed
            }
            String order = orderQueue.poll(); // Remove and return the next order
            System.out.println(Thread.currentThread().getName() + " is preparing: " + order);
            notFull.signalAll(); // Notify customers that space is available in the queue
            return order;
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    /**
     * Method to get the current status of the order queue.
     *
     * @return The current number of orders in the queue.
     */
    public int getQueueSize() {
        lock.lock(); // Acquire the lock
        try {
            return orderQueue.size();
        } finally {
            lock.unlock(); // Release the lock
        }
    }
}
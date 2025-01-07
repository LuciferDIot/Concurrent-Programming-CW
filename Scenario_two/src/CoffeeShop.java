
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Represents the coffee shop with a shared order queue.
 * This class ensures thread-safe access to the queue using synchronization and semaphores.
 *
 * Best Practices:
 * - Use `synchronized` blocks to ensure mutual exclusion when accessing shared resources.
 * - Use `wait()` and `notifyAll()` for thread coordination.
 * - Use a `Semaphore` to control access to the queue, ensuring only one thread modifies it at a time.
 */
public class CoffeeShop {
    private Queue<String> orderQueue; // Shared order queue
    private final int MAX_ORDERS; // Maximum number of orders the queue can hold
    private Semaphore semaphore; // Semaphore to control access to the queue

    /**
     * Constructor to initialize the coffee shop with a maximum number of orders.
     *
     * @param maxOrders The maximum number of orders the queue can hold.
     */
    public CoffeeShop(int maxOrders) {
        this.MAX_ORDERS = maxOrders;
        this.orderQueue = new LinkedList<>();
        this.semaphore = new Semaphore(1); // Initialize semaphore for mutual exclusion
    }

    /**
     * Method for customers to place orders in the queue.
     *
     * Steps Achieved:
     * 1. If the queue is full, customers wait until space is available (Step 1 in the document).
     * 2. Ensures mutual exclusion using a semaphore to avoid race conditions (Step 4 in the document).
     * 3. Notifies baristas when a new order is added to the queue.
     *
     * Best Practices:
     * - Use `synchronized` blocks to ensure thread-safe access to shared resources.
     * - Use `wait()` and `notifyAll()` for efficient thread coordination.
     *
     * @param order The order to be placed in the queue.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public void placeOrder(String order) throws InterruptedException {
        synchronized (this) {
            // Step 1: If the queue is full, customers must wait until there is space available.
            while (orderQueue.size() >= MAX_ORDERS) {
                wait(); // Wait for space in the queue
            }
            semaphore.acquire(); // Step 2: Acquire semaphore to ensure mutual exclusion
            orderQueue.add(order); // Add order to the queue
            System.out.println("Order placed: " + order);
            semaphore.release(); // Step 3: Release semaphore after adding the order
            notifyAll(); // Step 4: Notify baristas that a new order is available
        }
    }

    /**
     * Method for baristas to prepare orders from the queue.
     *
     * Steps Achieved:
     * 1. If the queue is empty, baristas wait until orders are available (Step 1 in the document).
     * 2. Ensures mutual exclusion using a semaphore to avoid race conditions (Step 4 in the document).
     * 3. Notifies customers when an order is removed from the queue.
     *
     * Best Practices:
     * - Use `synchronized` blocks to ensure thread-safe access to shared resources.
     * - Use `wait()` and `notifyAll()` for efficient thread coordination.
     *
     * @return The prepared order.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public String prepareOrder() throws InterruptedException {
        synchronized (this) {
            // Step 1: If the queue is empty, baristas must wait until orders are available.
            while (orderQueue.isEmpty()) {
                wait(); // Wait for orders to be placed
            }
            semaphore.acquire(); // Step 2: Acquire semaphore to ensure mutual exclusion
            String order = orderQueue.poll(); // Remove and return the next order
            System.out.println("Order prepared: " + order);
            semaphore.release(); // Step 3: Release semaphore after preparing the order
            notifyAll(); // Step 4: Notify customers that space is available in the queue
            return order;
        }
    }
}
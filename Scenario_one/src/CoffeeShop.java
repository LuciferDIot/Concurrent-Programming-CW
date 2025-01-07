import java.util.Queue;
import java.util.LinkedList;

/**
 * Represents the coffee shop with a shared order queue.
 * This class ensures thread-safe access to the queue using `synchronized` blocks.
 *
 * Best Practices:
 * - Use `synchronized` blocks to ensure mutual exclusion when accessing shared resources.
 * - Use `wait()` and `notifyAll()` for thread coordination.
 * - Keep the critical section (code inside `synchronized`) as short as possible to minimize contention.
 */
public class CoffeeShop {
    private Queue<String> orderQueue; // Shared order queue
    private final int MAX_ORDERS; // Maximum number of orders the queue can hold

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
     *
     * Steps Achieved:
     * 1. If the queue is full, customers wait until space is available (Step 1 in the document).
     * 2. Ensures mutual exclusion using `synchronized` to avoid race conditions (Step 4 in the document).
     * 3. Notifies baristas when a new order is added to the queue.
     *
     * Best Practices:
     * - Use `wait()` inside a loop to handle spurious wakeups.
     * - Use `notifyAll()` to wake up all waiting threads (baristas in this case).
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
            orderQueue.add(order); // Add order to the queue
            System.out.println("Order placed: " + order);
            notifyAll(); // Notify baristas that a new order is available
        }
    }

    /**
     * Method for baristas to prepare orders from the queue.
     *
     * Steps Achieved:
     * 1. If the queue is empty, baristas wait until orders are available (Step 1 in the document).
     * 2. Ensures mutual exclusion using `synchronized` to avoid race conditions (Step 4 in the document).
     * 3. Notifies customers when an order is removed from the queue.
     *
     * Best Practices:
     * - Use `wait()` inside a loop to handle spurious wakeups.
     * - Use `notifyAll()` to wake up all waiting threads (customers in this case).
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
            String order = orderQueue.poll(); // Remove and return the next order
            System.out.println("Order prepared: " + order);
            notifyAll(); // Notify customers that space is available in the queue
            return order;
        }
    }
}
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * Represents the coffee shop with a shared order queue.
 * This class ensures thread-safe access to the queue using `ReentrantLock` and `Condition`.
 */
public class CoffeeShop {
    private final Queue<DrinkType> orderQueue; // Shared order queue using DrinkType enum
    private final int MAX_ORDERS;              // Maximum number of orders the queue can hold

    private final Lock lock = new ReentrantLock(true); // Fair lock for thread synchronization
    private final Condition notFull = lock.newCondition();  // Condition for waiting when the queue is full
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
     * If the queue is full, customers wait until space is available.
     *
     * @param drink The drink type to be placed in the queue.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public void placeOrder(DrinkType drink) throws InterruptedException {
        lock.lock(); // Acquire the lock
        try {
            while (orderQueue.size() >= MAX_ORDERS) {
                System.out.println(Thread.currentThread().getName() + " is waiting to place an order.");
                notFull.await(); // Wait for space in the queue
            }
            orderQueue.add(drink); // Add the drink to the queue
            System.out.println(Thread.currentThread().getName() + " placed order: " + drink.name().toLowerCase());
            notEmpty.signalAll(); // Notify baristas that a new order is available
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    /**
     * Method for baristas to prepare orders from the queue.
     * If the queue is empty, baristas wait until orders are available.
     *
     * @return The drink type to be prepared.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public DrinkType prepareOrder() throws InterruptedException {
        lock.lock(); // Acquire the lock
        try {
            while (orderQueue.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + " is waiting for an order.");
                notEmpty.await(); // Wait for orders to be placed
            }
            DrinkType drink = orderQueue.poll(); // Remove and return the next drink
            System.out.println(Thread.currentThread().getName() + " is preparing: " + drink.name().toLowerCase());
            notFull.signalAll(); // Notify customers that space is available in the queue
            return drink;
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
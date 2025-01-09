/**
 * Represents a customer who places orders in the coffee shop.
 * <p>
 * Steps Achieved:
 * 1. Customers place orders in the queue (Step 1 in the document).
 * 2. If the queue is full, customers wait until space is available (handled in `CoffeeShop.placeOrder`).
 * <p>
 * Best Practices:
 * - Implement `Runnable` to allow the class to be executed in a separate thread.
 * - Handle `InterruptedException` to ensure threads can be interrupted gracefully.
 */
public class Customer implements Runnable {
    private final CoffeeShop coffeeShop;
    private final String order;

    /**
     * Constructor to initialize the customer with a coffee shop and an order.
     *
     * @param coffeeShop The coffee shop where the customer places orders.
     * @param order The order to be placed.
     */
    public Customer(CoffeeShop coffeeShop, String order) {
        this.coffeeShop = coffeeShop;
        this.order = order;
    }

    @Override
    public void run() {
        try {
            // Step 1: Customers place orders in the queue.
            // If the queue is full, they wait until there is space (handled in CoffeeShop.placeOrder).
            coffeeShop.placeOrder(order);
        } catch (InterruptedException e) {
            System.out.println("Customer was interrupted while placing an order.");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
    }
}
/**
 * Represents a barista who prepares orders from the queue.
 *
 * Steps Achieved:
 * 1. Baristas prepare orders from the queue (Step 1 in the document).
 * 2. If the queue is empty, baristas wait until orders are available (handled in `CoffeeShop.prepareOrder`).
 *
 * Best Practices:
 * - Implement `Runnable` to allow the class to be executed in a separate thread.
 * - Handle `InterruptedException` to ensure threads can be interrupted gracefully.
 */
public class Barista implements Runnable {
    private CoffeeShop coffeeShop;

    /**
     * Constructor to initialize the barista with a coffee shop.
     *
     * @param coffeeShop The coffee shop where the barista prepares orders.
     */
    public Barista(CoffeeShop coffeeShop) {
        this.coffeeShop = coffeeShop;
    }

    @Override
    public void run() {
        try {
            // Step 1: Baristas continuously prepare orders from the queue.
            // If the queue is empty, they wait until orders are available (handled in CoffeeShop.prepareOrder).
            while (true) {
                coffeeShop.prepareOrder();
                Thread.sleep(1000); // Simulate time taken to prepare an order
            }
        } catch (InterruptedException e) {
            System.out.println("Barista was interrupted while preparing an order.");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
    }
}
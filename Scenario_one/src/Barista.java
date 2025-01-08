import java.util.Random;

/**
 * Represents a barista who prepares orders from the queue.
 * <p>
 * Steps Achieved:
 * 1. Baristas prepare orders from the queue (Step 1 in the document).
 * 2. If the queue is empty, baristas wait until orders are available (handled in `CoffeeShop.prepareOrder`).
 * <p>
 * Best Practices:
 * - Implement `Runnable` to allow the class to be executed in a separate thread.
 * - Handle `InterruptedException` to ensure threads can be interrupted gracefully.
 */
public class Barista implements Runnable {
    private final CoffeeShop coffeeShop;
    private final Random random = new Random();

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
            while (!Thread.currentThread().isInterrupted()) {
                String order = coffeeShop.prepareOrder();
                int preparationTime = random.nextInt(3000) + 1000;
                Thread.sleep(preparationTime);
                System.out.println(Thread.currentThread().getName() + " has completed: " + order);
            }
        } catch (InterruptedException e) {
            // Handle interruption gracefully
            System.out.println(Thread.currentThread().getName() + " is stopping as the coffee shop is closing.");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        } finally {
            // Cleanup or logging when the thread stops
            System.out.println(Thread.currentThread().getName() + " has left the coffee shop.");
        }
    }
}
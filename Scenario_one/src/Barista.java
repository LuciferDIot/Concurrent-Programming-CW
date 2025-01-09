/**
 * Represents a barista who prepares orders from the queue.
 * Each barista prepares orders based on the drink type and its associated preparation time.
 */
public class Barista implements Runnable {
    private final CoffeeShop coffeeShop; // The coffee shop where orders are prepared

    /**
     * Constructor to initialize the barista with a coffee shop.
     *
     * @param coffeeShop The coffee shop where the barista prepares orders.
     */
    public Barista(CoffeeShop coffeeShop) {
        this.coffeeShop = coffeeShop;
    }

    /**
     * The run method executed by the barista thread.
     * It continuously prepares orders from the queue until interrupted.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                DrinkType drink = coffeeShop.prepareOrder(); // Retrieve the next drink from the queue
                if (drink != null) {
                    int preparationTime = drink.getPreparationTime(); // Get the preparation time for the drink
                    System.out.println(Thread.currentThread().getName() + " is preparing: " + drink.name().toLowerCase() + " (Time: " + preparationTime + "ms)");
                    Thread.sleep(preparationTime); // Simulate the preparation time
                    System.out.println(Thread.currentThread().getName() + " has completed: " + drink.name().toLowerCase());
                }
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " is stopping as the coffee shop is closing.");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        } finally {
            System.out.println(Thread.currentThread().getName() + " has left the coffee shop.");
        }
    }
}
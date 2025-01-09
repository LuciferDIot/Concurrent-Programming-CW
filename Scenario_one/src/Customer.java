/**
 * Represents a customer who places multiple orders in the coffee shop.
 * Each customer can place multiple orders of different drink types.
 */
public class Customer implements Runnable {
    private final CoffeeShop coffeeShop; // The coffee shop where orders are placed
    private final DrinkType[] orders;    // Array of drink types to be ordered by the customer

    /**
     * Constructor to initialize the customer with a coffee shop and a list of drink orders.
     *
     * @param coffeeShop The coffee shop where the customer places orders.
     * @param orders     The array of drink types to be ordered by the customer.
     */
    public Customer(CoffeeShop coffeeShop, DrinkType[] orders) {
        this.coffeeShop = coffeeShop;
        this.orders = orders;
    }

    /**
     * The run method executed by the customer thread.
     * It places each order in the queue and waits briefly between orders.
     */
    @Override
    public void run() {
        try {
            for (DrinkType drink : orders) {
                coffeeShop.placeOrder(drink); // Place each drink order in the queue
                Thread.sleep(500); // Simulate a short delay between placing orders
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted while placing orders.");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
    }
}
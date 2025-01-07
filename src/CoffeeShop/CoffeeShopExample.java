package CoffeeShop;

/**
 * Main class to simulate the coffee shop scenario.
 *
 * Steps Achieved:
 * 1. Creates a coffee shop with a limited order queue.
 * 2. Simulates multiple customers placing orders and baristas preparing them.
 *
 * Best Practices:
 * - Use a fixed-size thread pool or limit the number of threads to avoid resource exhaustion.
 * - Ensure the program can handle interruptions gracefully.
 */
public class CoffeeShopExample {
    public static void main(String[] args) {
        // Step 1: Create a coffee shop with a maximum of 5 orders in the queue.
        CoffeeShop coffeeShop = new CoffeeShop(5);

        // Step 2: Create and start multiple customer threads.
        // Each customer places an order in the queue.
        for (int i = 1; i <= 10; i++) {
            Customer customer = new Customer(coffeeShop, "Coffee Order #" + i);
            Thread customerThread = new Thread(customer);
            customerThread.start();
        }

        // Step 3: Create and start multiple barista threads.
        // Each barista prepares orders from the queue.
        for (int i = 1; i <= 3; i++) {
            Barista barista = new Barista(coffeeShop);
            Thread baristaThread = new Thread(barista);
            baristaThread.start();
        }
    }
}
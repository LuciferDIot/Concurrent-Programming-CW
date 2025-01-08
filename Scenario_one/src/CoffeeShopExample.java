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
    public static void main(String[] args) throws InterruptedException {
        // Creating coffee shop with a maximum of 3 orders in the queue.
        CoffeeShop coffeeShop = new CoffeeShop(3);

        // Creating and starting multiple customer threads.
        for (int i = 1; i <= 10; i++) {
            Customer customer = new Customer(coffeeShop, "coffee order " + i);
            Thread customerThread = new Thread(customer, "Customer" + i);
            customerThread.start();
        }

        // Creating and starting multiple barista threads.
        Barista[] baristas = new Barista[3];
        Thread[] baristaThreads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            baristas[i] = new Barista(coffeeShop);
            baristaThreads[i] = new Thread(baristas[i], "Barista" + (i + 1));
            baristaThreads[i].start();
        }

        // Simulate the coffee shop running for 10 seconds
        Thread.sleep(10000);

        // Stop all barista threads by interrupting them
        for (Thread baristaThread : baristaThreads) {
            baristaThread.interrupt(); // Interrupt the barista thread
        }

        // Wait for all barista threads to finish
        for (Thread baristaThread : baristaThreads) {
            baristaThread.join();
        }

        System.out.println("Coffee shop is now closed.");
    }
}
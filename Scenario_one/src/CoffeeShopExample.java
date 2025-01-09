/**
 * Main class to simulate the coffee shop scenario.
 * This class creates customers, baristas, and the coffee shop, and runs the simulation.
 */
public class CoffeeShopExample {
    /**
     * The main method to start the coffee shop simulation.
     *
     * @param args Command-line arguments (not used in this program).
     * @throws InterruptedException If the main thread is interrupted during execution.
     */
    public static void main(String[] args) throws InterruptedException {
        // Create a coffee shop with a maximum of 5 orders in the queue
        CoffeeShop coffeeShop = new CoffeeShop(5);

        // Define customer orders using the DrinkType enum
        DrinkType[] aliceOrders = {DrinkType.CAPPUCCINO, DrinkType.LATTE, DrinkType.ESPRESSO};
        DrinkType[] bobOrders = {DrinkType.LATTE, DrinkType.AMERICANO};

        // Create and start customer threads
        Customer alice = new Customer(coffeeShop, aliceOrders);
        Customer bob = new Customer(coffeeShop, bobOrders);
        Thread aliceThread = new Thread(alice, "Customer-Alice");
        Thread bobThread = new Thread(bob, "Customer-Bob");
        aliceThread.start();
        bobThread.start();

        // Create and start barista threads
        Barista[] baristas = new Barista[2];
        Thread[] baristaThreads = new Thread[2];
        for (int i = 0; i < 2; i++) {
            baristas[i] = new Barista(coffeeShop);
            baristaThreads[i] = new Thread(baristas[i], "Barista-" + (i + 1));
            baristaThreads[i].start();
        }

        // Simulate the coffee shop running for 30 seconds
        Thread.sleep(30000);

        // Stop all barista threads by interrupting them
        for (Thread baristaThread : baristaThreads) {
            baristaThread.interrupt();
        }

        // Wait for all barista threads to finish
        for (Thread baristaThread : baristaThreads) {
            baristaThread.join();
        }

        System.out.println("Coffee shop is now closed.");
    }
}
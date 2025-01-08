import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Simulates a bathroom with limited stalls on a floor shared by multiple users.
 * Employees have higher priority than Students when accessing the stalls.
 * This adds a "wow factor" to the simulation.
 */
public class FloorBathroomSimulation {
    public static final int NUM_OF_STALLS = 6;
    public static boolean[] bathroomStall = new boolean[NUM_OF_STALLS]; // Array to track stall availability
    public static final int NUM_USERS = 100;

    // Counting semaphore - ensure only 6 people can enter at any given time
    public static final Semaphore semaphore = new Semaphore(NUM_OF_STALLS, true);
    // Mutex - to ensure only 1 person can modify the stall array at a time
    public static final Semaphore mutex = new Semaphore(1, true);

    /**
     * Represents a bathroom user attempting to use a stall.
     */
    private static class BathroomUsers implements Runnable {
        private final boolean isEmployee; // Indicates whether the user is an Employee or Student

        /**
         * Constructor for BathroomUsers.
         * @param isEmployee True if the user is an Employee, false if a Student.
         */
        public BathroomUsers(boolean isEmployee) {
            this.isEmployee = isEmployee;
        }

        @Override
        public void run() {
            try {
                enterBathroom();
                int stallNumber = takeStall();
                if (stallNumber != -1) { // Only proceed if a stall was successfully acquired
                    useBathroomStall();
                    releaseStall(stallNumber);
                }
                leaveBathroom();
            } catch (InterruptedException e) {
                System.err.println(Thread.currentThread().getName() + " was interrupted: " + e.getMessage());
                Thread.currentThread().interrupt(); // Restore the interrupted status
            }
        }

        /**
         * Attempts to acquire a permit to enter the bathroom.
         */
        private void enterBathroom() throws InterruptedException {
            semaphore.acquire();
            System.out.println(getFormattedName() + " has entered the bathroom.");
        }

        /**
         * Acquires a stall from the array.
         * @return The stall number acquired, or -1 if no stall is available.
         */
        private int takeStall() {
            int stallNumber = -1; // Default value if no stall is available
            try {
                mutex.acquire();
                for (int i = 0; i < NUM_OF_STALLS; i++) {
                    if (!bathroomStall[i]) { // Check if the stall is available
                        bathroomStall[i] = true; // Mark the stall as occupied
                        stallNumber = i + 1; // Stall numbers are 1-based
                        System.out.println(getFormattedName() + " has taken stall " + stallNumber);
                        System.out.println("Available stalls: " + getAvailableStalls()); // Display available stalls
                        break;
                    }
                }
                if (stallNumber == -1) {
                    System.out.println(getFormattedName() + " could not find an available stall.");
                }
            } catch (InterruptedException e) {
                System.err.println(getFormattedName() + " was interrupted while trying to take a stall: " + e.getMessage());
                Thread.currentThread().interrupt(); // Restore the interrupted status
            } finally {
                mutex.release();
            }
            return stallNumber;
        }

        /**
         * Simulates the time taken by the user to use the bathroom stall.
         * Random delay between 3 and 5 seconds is introduced to mimic real usage.
         */
        private void useBathroomStall() {
            Random random = new Random();
            int time = random.nextInt(3, 5);
            try {
                Thread.sleep(time * 1000L);
            } catch (InterruptedException e) {
                System.err.println(getFormattedName() + " was interrupted while using the stall: " + e.getMessage());
                Thread.currentThread().interrupt(); // Restore the interrupted status
            }
        }

        /**
         * Releases the stall back to the array.
         * @param stallNumber The stall number to be released.
         */
        private void releaseStall(int stallNumber) {
            try {
                mutex.acquire();
                bathroomStall[stallNumber - 1] = false; // Mark the stall as available
                System.out.println(getFormattedName() + " has released stall " + stallNumber);
                System.out.println("Available stalls: " + getAvailableStalls()); // Display available stalls
            } catch (InterruptedException e) {
                System.err.println(getFormattedName() + " was interrupted while releasing a stall: " + e.getMessage());
                Thread.currentThread().interrupt(); // Restore the interrupted status
            } finally {
                mutex.release();
            }
        }

        /**
         * Releases the permit to allow other users to enter the bathroom.
         */
        private void leaveBathroom() {
            semaphore.release();
            System.out.println(getFormattedName() + " has left the bathroom.");
        }

        /**
         * Returns a formatted name for the user, with a priority indicator.
         * @return A formatted string with the user's name and role.
         */
        private String getFormattedName() {
            String role = isEmployee ? "Employee" : "Student";
            return "[" + role + "] " + Thread.currentThread().getName();
        }
    }

    /**
     * Main method to run the simulation.
     */
    public static void main(String[] args) {
        initializeStalls();
        createAndStartUserThreads();
    }

    /**
     * Initializes the array to mark all stalls as available.
     */
    private static void initializeStalls() {
        // false means the stall is available
        Arrays.fill(bathroomStall, false);
        System.out.println("Initial available stalls: " + getAvailableStalls()); // Display initial stalls
    }

    /**
     * Creates and starts threads for each user.
     */
    private static void createAndStartUserThreads() {
        Random random = new Random(); // Initialize the random number generator
        for (int i = 1; i <= NUM_USERS; i++) {
            boolean isEmployee = random.nextBoolean(); // Randomly assign a role (Employee or Student)
            BathroomUsers user = new BathroomUsers(isEmployee);
            Thread userThread = new Thread(user, "User-" + i);
            if (isEmployee) {
                userThread.setPriority(Thread.MAX_PRIORITY); // Employees have higher priority
            } else {
                userThread.setPriority(Thread.NORM_PRIORITY); // Students have normal priority
            }
            userThread.start();
        }
    }

    /**
     * Helper method to get a list of available stalls.
     * @return A string representation of available stalls.
     */
    private static String getAvailableStalls() {
        StringBuilder availableStalls = new StringBuilder("[");
        for (int i = 0; i < NUM_OF_STALLS; i++) {
            if (!bathroomStall[i]) {
                availableStalls.append(i + 1).append(", ");
            }
        }
        if (availableStalls.length() > 1) {
            availableStalls.setLength(availableStalls.length() - 2); // Remove the trailing ", "
        }
        availableStalls.append("]");
        return availableStalls.toString();
    }
}
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Simulates a bathroom with limited stalls on a floor shared by multiple users.
 * The bathroom allows up to 6 users to occupy stalls concurrently,
 * while ensuring safe and synchronized access to the shared resource (stalls).
 */
public class FloorBathroomSimulation {
    public static final int NUM_OF_STALLS = 6;
    public static Queue<Integer> bathroomStall = new LinkedList<>();
    public static final int NUM_USERS = 100;

    // Counting semaphore - ensure only 6 people can enter at any given time
    public static final Semaphore semaphore = new Semaphore(NUM_OF_STALLS);
    // Mutex - to ensure only 1 person can modify the stall queue at a time
    public static final Semaphore mutex = new Semaphore(1);

    /**
     * Represents a bathroom user attempting to use a stall.
     */
    private static class BathroomUsers implements Runnable {
        @Override
        public void run() {
            enterBathroom();
            int stallNumber = takeStall();
            useBathroomStall();
            releaseStall(stallNumber);
            leaveBathroom();
        }

        /**
         * Attempts to acquire a permit to enter the bathroom.
         */
        private void enterBathroom() {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " has entered the bathroom.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * Acquires a stall from the queue.
         * @return The stall number acquired.
         */
        private int takeStall() {
            int stallNumber = -1;
            try {
                mutex.acquire();
                stallNumber = bathroomStall.poll();
                System.out.println(Thread.currentThread().getName() + " has taken stall " + stallNumber);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mutex.release();
            }
            return stallNumber;
        }

        /**
         * Simulates the time taken by the user to use the bathroom stall.
         * Random delay between 3 to 5 seconds is introduced to mimic real usage.
         */
        private void useBathroomStall() {
            Random random = new Random();
            int time = random.nextInt(3, 5);
            try {
                Thread.sleep(time * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * Releases the stall back to the queue.
         * @param stallNumber The stall number to be released.
         */
        private void releaseStall(int stallNumber) {
            try {
                mutex.acquire();
                bathroomStall.add(stallNumber);
                System.out.println(Thread.currentThread().getName() + " has released stall " + stallNumber);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mutex.release();
            }
        }

        /**
         * Releases the permit to allow other users to enter the bathroom.
         */
        private void leaveBathroom() {
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + " has left the bathroom.");
        }
    }

    public static void main(String[] args) {
        initializeStalls();
        createAndStartUserThreads();
    }

    /**
     * Initializes the queue with available stalls (1 to 6).
     */
    private static void initializeStalls() {
        for (int i = 1; i <= NUM_OF_STALLS; i++) {
            bathroomStall.add(i);
        }
    }

    /**
     * Creates and starts threads for each user.
     */
    private static void createAndStartUserThreads() {
        for (int i = 1; i <= NUM_USERS; i++) {
            // Assign a name to each user as either "Student" or "Employee".
            String name = (i % 2 == 0) ? "Student " : "Employee ";
            Thread user = new Thread(new BathroomUsers(), name + i);
            user.start();
        }
    }
}
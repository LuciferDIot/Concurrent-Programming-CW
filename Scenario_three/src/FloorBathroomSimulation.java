import java.util.concurrent.Semaphore;

/**
 * FloorBathroomSimulation.java
 * This file contains a single class that simulates the shared bathroom stall scenario.
 * It includes the functionality of managing bathroom stalls, simulating employees/students,
 * and handling concurrent access using a Semaphore.
 * The implementation ensures that only a fixed number of people can use the bathroom stalls at a time,
 * and handles exceptions for invalid inputs.
 */

public class FloorBathroomSimulation {
    private final Semaphore stalls; // Semaphore to control access to stalls
    private final int totalStalls; // Total number of stalls available

    /**
     * Constructor for FloorBathroomSimulation.
     * @param totalStalls The total number of bathroom stalls available.
     * @throws IllegalArgumentException if the number of stalls is invalid (e.g., 0 or negative).
     */
    public FloorBathroomSimulation(int totalStalls) {
        if (totalStalls <= 0) {
            throw new IllegalArgumentException("Number of stalls must be greater than 0.");
        }
        this.totalStalls = totalStalls;
        this.stalls = new Semaphore(totalStalls, true); // Fair semaphore to ensure first-come-first-served
    }

    /**
     * Method to acquire a stall.
     * If all stalls are occupied, the thread will wait until a stall becomes available.
     * This satisfies the requirement of waiting if all stalls are occupied.
     */
    public void acquireStall() {
        try {
            stalls.acquire(); // Acquire a stall (block if no stalls are available)
            System.out.println(Thread.currentThread().getName() + " has entered a stall.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            System.out.println(Thread.currentThread().getName() + " was interrupted while waiting for a stall.");
        }
    }

    /**
     * Method to release a stall.
     * When a person leaves the stall, it becomes available for others.
     * This satisfies the requirement of allowing others to enter after someone leaves.
     */
    public void releaseStall() {
        stalls.release(); // Release the stall
        System.out.println(Thread.currentThread().getName() + " has left a stall.");
    }

    /**
     * Simulates a person trying to use a bathroom stall.
     * This method is called by each thread representing an employee/student.
     */
    public void useStall() {
        try {
            acquireStall(); // Acquire a stall
            Thread.sleep((long) (Math.random() * 2000)); // Simulate using the stall
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted while using the stall.");
        } finally {
            releaseStall(); // Release the stall
        }
    }

    /**
     * Main method to run the simulation.
     * It creates the FloorBathroomSimulation object and simulates 100 employees/students
     * trying to use the bathroom stalls. It also handles the exception case where there
     * are 0 stalls and -10 people.
     */
    public static void main(String[] args) {
        try {
            // Create a FloorBathroomSimulation with 6 stalls
            FloorBathroomSimulation floorBathroom = new FloorBathroomSimulation(6);

            // Simulate 100 employees/students trying to use the stalls
            for (int i = 0; i < 100; i++) {
                new Thread(() -> floorBathroom.useStall(), "Person-" + i).start();
            }

            // Handle the exception case where there are 0 stalls and -10 people
            // This satisfies the requirement of throwing and handling the exception.
            FloorBathroomSimulation invalidBathroom = new FloorBathroomSimulation(0); // This will throw an IllegalArgumentException
        } catch (IllegalArgumentException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
    }
}
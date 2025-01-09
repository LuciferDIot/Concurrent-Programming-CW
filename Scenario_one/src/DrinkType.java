/**
 * Represents the types of drinks available in the coffee shop.
 * Each drink type has a specific preparation time associated with it.
 */
public enum DrinkType {
    CAPPUCCINO(2000), // Takes 2 seconds to prepare
    LATTE(3000),      // Takes 3 seconds to prepare
    ESPRESSO(1000),   // Takes 1 second to prepare
    AMERICANO(1500);  // Takes 1.5 seconds to prepare

    private final int preparationTime; // Time in milliseconds

    /**
     * Constructor to initialize the drink type with its preparation time.
     *
     * @param preparationTime The time (in milliseconds) required to prepare the drink.
     */
    DrinkType(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    /**
     * Returns the preparation time for the drink.
     *
     * @return The preparation time in milliseconds.
     */
    public int getPreparationTime() {
        return preparationTime;
    }
}
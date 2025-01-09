import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a bank account with thread-safe operations.
 * Uses ReentrantLock for fine-grained locking.
 */
public class BankAccount {
    private final int id; // Unique account ID
    private double balance; // Current balance
    private final ReentrantLock lock = new ReentrantLock(true); // Fair lock

    public BankAccount(int id, double initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public int getId() {
        return id;
    }

    /**
     * Returns the current balance of the account.
     * Uses a read lock to allow concurrent reads.
     *
     * @return The current balance.
     */
    public double getBalance() {
        return balance; // No locking needed for reads
    }

    /**
     * Deposits the specified amount into the account.
     * Uses a write lock to ensure exclusive access during the update.
     *
     * @param amount The amount to deposit.
     */
    public void deposit(double amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Withdraws the specified amount from the account.
     * Uses a write lock to ensure exclusive access during the update.
     * Throws an exception if the balance is insufficient.
     *
     * @param amount The amount to withdraw.
     * @throws IllegalArgumentException If the balance is insufficient.
     */
    public void withdraw(double amount) {
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
            } else {
                throw new IllegalArgumentException("Insufficient balance");
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Locks the account for exclusive access.
     */
    public void lock() {
        lock.lock();
    }

    /**
     * Unlocks the account.
     */
    public void unlock() {
        lock.unlock();
    }
}
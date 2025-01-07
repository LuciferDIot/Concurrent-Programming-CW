
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a bank account with thread-safe operations.
 * Satisfies the following requirements from the document:
 * - Read-Write Operations: Allows concurrent reads using ReentrantReadWriteLock.
 * - Transaction Safety: Ensures exclusive access during write operations (deposit/withdraw).
 */
public class BankAccount {
    private final int id; // Unique account ID
    private double balance; // Current balance
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(); // Lock for thread safety

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
     */
    public double getBalance() {
        rwLock.readLock().lock();
        try {
            return balance;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * Deposits the specified amount into the account.
     * Uses a write lock to ensure exclusive access during the update.
     */
    public void deposit(double amount) {
        rwLock.writeLock().lock();
        try {
            balance += amount;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * Withdraws the specified amount from the account.
     * Uses a write lock to ensure exclusive access during the update.
     * Throws an exception if the balance is insufficient.
     */
    public void withdraw(double amount) {
        rwLock.writeLock().lock();
        try {
            if (balance >= amount) {
                balance -= amount;
            } else {
                throw new IllegalArgumentException("Insufficient balance");
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
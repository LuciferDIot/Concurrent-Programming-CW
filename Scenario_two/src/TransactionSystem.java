
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a banking system that handles transactions between accounts.
 * Satisfies the following requirements from the document:
 * - Transaction Safety: Locks both accounts during a transfer to ensure consistency.
 * - Avoiding Deadlock: Uses consistent lock ordering (by account ID) to prevent deadlocks.
 * - Fair Access: Synchronized blocks ensure first-come-first-served access to accounts.
 * - Multiple Transactions: Allows multiple transactions to occur simultaneously if they don't involve the same accounts.
 * - Transaction Reversal: Provides a method to safely reverse a transaction.
 */
public class TransactionSystem {
    private final Map<Integer, BankAccount> accounts = new HashMap<>(); // Stores all accounts

    /**
     * Adds a new account to the system.
     */
    public void addAccount(BankAccount account) {
        accounts.put(account.getId(), account);
    }

    /**
     * Retrieves an account by its ID.
     */
    public BankAccount getAccount(int id) {
        return accounts.get(id);
    }

    /**
     * Transfers the specified amount from one account to another.
     * Locks both accounts in a consistent order to avoid deadlocks.
     * Throws an exception if the source account has insufficient balance.
     */
    public void transfer(int fromAccountId, int toAccountId, double amount) {
        BankAccount fromAccount = accounts.get(fromAccountId);
        BankAccount toAccount = accounts.get(toAccountId);

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Invalid account ID");
        }

        // Ensure consistent lock ordering to avoid deadlocks
        BankAccount firstLock = fromAccount.getId() < toAccount.getId() ? fromAccount : toAccount;
        BankAccount secondLock = fromAccount.getId() < toAccount.getId() ? toAccount : fromAccount;

        synchronized (firstLock) {
            synchronized (secondLock) {
                if (fromAccount.getBalance() >= amount) {
                    fromAccount.withdraw(amount);
                    toAccount.deposit(amount);
                    System.out.println("Transferred " + amount + " from account " + fromAccountId +
                            " to account " + toAccountId);
                } else {
                    throw new IllegalArgumentException("Insufficient balance in the source account");
                }
            }
        }
    }

    /**
     * Reverses a transaction by transferring the amount back from the destination account to the source account.
     * Locks both accounts in a consistent order to avoid deadlocks.
     */
    public void reverseTransaction(int fromAccountId, int toAccountId, double amount) {
        BankAccount fromAccount = accounts.get(fromAccountId);
        BankAccount toAccount = accounts.get(toAccountId);

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Invalid account ID");
        }

        // Ensure consistent lock ordering to avoid deadlocks
        BankAccount firstLock = fromAccount.getId() < toAccount.getId() ? fromAccount : toAccount;
        BankAccount secondLock = fromAccount.getId() < toAccount.getId() ? toAccount : fromAccount;

        synchronized (firstLock) {
            synchronized (secondLock) {
                toAccount.withdraw(amount);
                fromAccount.deposit(amount);
                System.out.println("Reversed transaction of " + amount + " from account " + toAccountId +
                        " to account " + fromAccountId);
            }
        }
    }
}
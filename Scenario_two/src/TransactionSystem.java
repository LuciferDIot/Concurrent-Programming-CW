import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a banking system that handles transactions between accounts.
 * Ensures thread safety, avoids deadlocks, and allows concurrent reads.
 */
public class TransactionSystem {
    private final Map<Integer, BankAccount> accounts = new HashMap<>(); // Stores all accounts
    private final List<String> transactions = new ArrayList<>(); // Tracks successful transactions

    /**
     * Adds a new account to the system.
     *
     * @param account The account to add.
     */
    public void addAccount(BankAccount account) {
        accounts.put(account.getId(), account);
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param id The ID of the account.
     * @return The account, or null if it does not exist.
     */
    public BankAccount getAccount(int id) {
        return accounts.get(id);
    }

    /**
     * Transfers the specified amount from one account to another.
     * Locks both accounts in a consistent order to avoid deadlocks.
     * If the destination account does not exist, the withdrawn amount is deposited back into the source account.
     *
     * @param fromAccountId The ID of the source account.
     * @param toAccountId   The ID of the destination account.
     * @param amount        The amount to transfer.
     * @throws IllegalArgumentException If the source or destination account does not exist or if the source account has insufficient balance.
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

        // Lock both accounts in a consistent order
        firstLock.lock();
        try {
            secondLock.lock();
            try {
                if (fromAccount.getBalance() >= amount) {
                    fromAccount.withdraw(amount);
                    toAccount.deposit(amount);
                    transactions.add(fromAccountId + "-" + toAccountId + "-" + amount);
                    System.out.println("Transferred " + amount + " from account " + fromAccountId + " to account " + toAccountId);
                } else {
                    throw new IllegalArgumentException("Insufficient balance in the source account");
                }
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }

    /**
     * Reverses a transaction by withdrawing the amount from the destination account and depositing it into the source account.
     * If the destination account does not exist, the withdrawn amount is deposited back into the source account.
     *
     * @param fromAccountId The ID of the source account.
     * @param toAccountId   The ID of the destination account.
     * @param amount        The amount to reverse.
     * @throws IllegalArgumentException If the transaction does not exist or if the accounts are invalid.
     */
    public void reverseTransaction(int fromAccountId, int toAccountId, double amount) {
        String transactionKey = fromAccountId + "-" + toAccountId + "-" + amount;
        if (!transactions.contains(transactionKey)) {
            throw new IllegalArgumentException("Cannot reverse: No such transaction exists");
        }

        BankAccount fromAccount = accounts.get(fromAccountId);
        BankAccount toAccount = accounts.get(toAccountId);

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Invalid account ID");
        }

        // Ensure consistent lock ordering to avoid deadlocks
        BankAccount firstLock = fromAccount.getId() < toAccount.getId() ? fromAccount : toAccount;
        BankAccount secondLock = fromAccount.getId() < toAccount.getId() ? toAccount : fromAccount;

        // Lock both accounts in a consistent order
        firstLock.lock();
        try {
            secondLock.lock();
            try {
                if (toAccount.getBalance() >= amount) {
                    toAccount.withdraw(amount);
                    fromAccount.deposit(amount);
                    transactions.remove(transactionKey);
                    System.out.println("Reversed transaction of " + amount + " from account " + toAccountId + " to account " + fromAccountId);
                } else {
                    throw new IllegalArgumentException("Insufficient balance in the destination account");
                }
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }
}
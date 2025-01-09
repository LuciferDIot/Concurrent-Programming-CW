import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class TransactionSystem {
    private final Map<Integer, BankAccount> accounts = new HashMap<>(); // Stores all accounts
    private final List<String> transactions = new ArrayList<>(); // Tracks successful transactions

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
     * If the destination account does not exist, the withdrawn amount is deposited back into the source account.
     */
    public void transfer(int fromAccountId, int toAccountId, double amount) {
        BankAccount fromAccount = accounts.get(fromAccountId);

        if (fromAccount == null) {
            throw new IllegalArgumentException("Invalid source account ID");
        }

        // Lock the source account to ensure thread safety
        synchronized (fromAccount) {
            // Check if the source account has sufficient balance
            if (fromAccount.getBalance() < amount) {
                throw new IllegalArgumentException("Insufficient balance in the source account");
            }

            // Withdraw the amount from the source account
            fromAccount.withdraw(amount);
            System.out.println("Withdrawn " + amount + " from account " + fromAccountId);

            // Try to deposit into the destination account
            BankAccount toAccount = accounts.get(toAccountId);
            if (toAccount != null) {
                synchronized (toAccount) {
                    toAccount.deposit(amount);
                    System.out.println("Deposited " + amount + " into account " + toAccountId);

                    // Record the successful transaction
                    transactions.add(fromAccountId + "-" + toAccountId + "-" + amount);
                }
            } else {
                // If the destination account does not exist, deposit the amount back into the source account
                fromAccount.deposit(amount);
                System.out.println("Error: Destination account " + toAccountId + " does not exist. Amount deposited back into account " + fromAccountId);
            }
        }
    }

    /**
     * Reverses a transaction by withdrawing the amount from the destination account and depositing it into the source account.
     * If the destination account does not exist, the withdrawn amount is deposited back into the source account.
     */
    public void reverseTransaction(int fromAccountId, int toAccountId, double amount) {
        // Check if the transaction exists
        String transactionKey = fromAccountId + "-" + toAccountId + "-" + amount;
        if (!transactions.contains(transactionKey)) {
            throw new IllegalArgumentException("Cannot reverse: No such transaction exists");
        }

        BankAccount fromAccount = accounts.get(fromAccountId);
        BankAccount toAccount = accounts.get(toAccountId);

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Invalid account ID");
        }

        // Lock the destination account first to avoid deadlocks
        synchronized (toAccount) {
            synchronized (fromAccount) {
                // Check if the destination account has sufficient balance
                if (toAccount.getBalance() < amount) {
                    throw new IllegalArgumentException("Insufficient balance in the destination account");
                }

                // Withdraw the amount from the destination account
                toAccount.withdraw(amount);
                System.out.println("Withdrawn " + amount + " from account " + toAccountId);

                // Deposit the amount into the source account
                fromAccount.deposit(amount);
                System.out.println("Deposited " + amount + " into account " + fromAccountId);

                // Remove the transaction from the list of successful transactions
                transactions.remove(transactionKey);
            }
        }
    }
}
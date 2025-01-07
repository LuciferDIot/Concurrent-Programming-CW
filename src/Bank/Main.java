package Bank;

/**
 * The entry point of the program.
 * Demonstrates the functionality of the TransactionSystem by:
 * - Creating accounts.
 * - Performing concurrent transactions.
 * - Reversing a transaction.
 * Satisfies the following requirements from the document:
 * - Demonstrates all methods in TransactionSystem.
 * - Simulates concurrent transactions and ensures thread safety.
 */
public class Main {
    public static void main(String[] args) {
        TransactionSystem transactionSystem = new TransactionSystem();

        // Create accounts
        BankAccount account1 = new BankAccount(1, 1000);
        BankAccount account2 = new BankAccount(2, 1000);
        BankAccount account3 = new BankAccount(3, 1000);

        transactionSystem.addAccount(account1);
        transactionSystem.addAccount(account2);
        transactionSystem.addAccount(account3);

        // Perform concurrent transactions
        Thread thread1 = new Thread(() -> transactionSystem.transfer(1, 2, 100));
        Thread thread2 = new Thread(() -> transactionSystem.transfer(2, 3, 200));
        Thread thread3 = new Thread(() -> transactionSystem.transfer(3, 1, 50));

        thread1.start();
        thread2.start();
        thread3.start();

        // Wait for threads to finish
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Print final balances
        System.out.println("Final balance of account 1: " + account1.getBalance());
        System.out.println("Final balance of account 2: " + account2.getBalance());
        System.out.println("Final balance of account 3: " + account3.getBalance());

        // Reverse a transaction
        transactionSystem.reverseTransaction(1, 2, 100);
        System.out.println("Balance after reversal:");
        System.out.println("Account 1: " + account1.getBalance());
        System.out.println("Account 2: " + account2.getBalance());
    }
}
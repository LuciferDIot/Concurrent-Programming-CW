/**
 * The entry point of the program.
 * Demonstrates the functionality of the TransactionSystem by testing various scenarios with all actions in separate threads.
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

        // Print initial balances
        System.out.println("Initial balances:");
        printBalances(account1, account2, account3);
        System.out.println();

        // Scenario 1: Concurrent Transfers
        Thread thread1 = new Thread(() -> {
            transactionSystem.transfer(1, 2, 100);
            System.out.println("Thread 1: Transfer successful!");
            printBalances(account1, account2, account3);
            System.out.println();
        });

        Thread thread2 = new Thread(() -> {
            transactionSystem.transfer(2, 3, 200);
            System.out.println("Thread 2: Transfer successful!");
            printBalances(account1, account2, account3);
            System.out.println();
        });

        Thread thread3 = new Thread(() -> {
            transactionSystem.transfer(3, 1, 50);
            System.out.println("Thread 3: Transfer successful!");
            printBalances(account1, account2, account3);
            System.out.println();
        });

        // Scenario 2: Reverse Transaction
        Thread thread4 = new Thread(() -> {
            try {
                transactionSystem.reverseTransaction(1, 2, 100);
                System.out.println("Thread 4: Reversal successful!");
                printBalances(account1, account2, account3);
                System.out.println();
            } catch (IllegalArgumentException e) {
                System.out.println("Thread 4: Error - " + e.getMessage());
                printBalances(account1, account2, account3);
                System.out.println();
            }
        });

        // Start all threads together
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        // Wait for a short time to allow all threads to complete
        try {
            Thread.sleep(2000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Print final balances after all scenarios
        System.out.println("Final balances after all scenarios:");
        printBalances(account1, account2, account3);
    }

    /**
     * Prints the balances of the specified accounts.
     *
     * @param account1 The first account.
     * @param account2 The second account.
     * @param account3 The third account.
     */
    private static void printBalances(BankAccount account1, BankAccount account2, BankAccount account3) {
        System.out.println("Account 1: " + account1.getBalance());
        System.out.println("Account 2: " + account2.getBalance());
        System.out.println("Account 3: " + account3.getBalance());
    }
}
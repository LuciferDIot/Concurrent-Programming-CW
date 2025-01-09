public class Main {
    public static void main(String[] args) {
        TransactionSystem transactionSystem = new TransactionSystem();

        // Create accounts
        BankAccount account1 = new BankAccount(1, 1000);
        BankAccount account2 = new BankAccount(2, 1000);

        transactionSystem.addAccount(account1);
        transactionSystem.addAccount(account2);

        // Print initial balances
        System.out.println("Initial balances:");
        printBalances(account1, account2);

        // Scenario 1: Successful Transfer
        System.out.println("\nScenario 1: Successful Transfer");
        System.out.println("Action: Transfer 100 from Account 1 to Account 2");
        transactionSystem.transfer(1, 2, 100);
        printBalances(account1, account2);

        // Scenario 2: Insufficient Balance
        System.out.println("\nScenario 2: Insufficient Balance");
        System.out.println("Action: Attempt to transfer 1000 from Account 1 to Account 2 (insufficient balance)");
        try {
            transactionSystem.transfer(1, 2, 1000);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        printBalances(account1, account2);

        // Scenario 3: Non-Existent Destination Account
        System.out.println("\nScenario 3: Non-Existent Destination Account");
        System.out.println("Action: Attempt to transfer 100 from Account 1 to Account 10 (non-existent account)");
        transactionSystem.transfer(1, 10, 100);
        printBalances(account1, account2);

        // Scenario 4: Non-Existent Source Account
        System.out.println("\nScenario 4: Non-Existent Source Account");
        System.out.println("Action: Attempt to transfer 100 from Account 10 (non-existent account) to Account 2");
        try {
            transactionSystem.transfer(10, 2, 100);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        printBalances(account1, account2);

        // Scenario 6: Reverse Non-Existent Transaction
        System.out.println("\nScenario 5: Reverse Non-Existent Transaction");
        System.out.println("Action: Attempt to reverse a transfer of 100 from Account 2 to Account 1 (no such transaction exists)");
        try {
            transactionSystem.reverseTransaction(2, 1, 100);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        printBalances(account1, account2);

        // Scenario 5: Reverse Transaction
        System.out.println("\nScenario 6: Reverse Transaction");
        System.out.println("Action: Reverse the transfer of 100 from Account 1 to Account 2");
        transactionSystem.reverseTransaction(1, 2, 100);
        printBalances(account1, account2);

        // Scenario 7: Reverse Transaction with Non-Existent Destination Account
        System.out.println("\nScenario 7: Reverse Transaction with Non-Existent Destination Account");
        System.out.println("Action: Attempt to reverse a transaction to Account 10 (non-existent account)");
        try {
            transactionSystem.reverseTransaction(1, 10, 100);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        printBalances(account1, account2);

        // Scenario 8: Reverse Transaction with Non-Existent Source Account
        System.out.println("\nScenario 8: Reverse Transaction with Non-Existent Source Account");
        System.out.println("Action: Attempt to reverse a transaction from Account 10 (non-existent account) to Account 2");
        try {
            transactionSystem.reverseTransaction(10, 2, 100);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        printBalances(account1, account2);
    }

    private static void printBalances(BankAccount account1, BankAccount account2) {
        System.out.println("Account 1: " + account1.getBalance());
        System.out.println("Account 2: " + account2.getBalance());
    }
}
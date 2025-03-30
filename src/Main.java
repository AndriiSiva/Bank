
public class Main {
    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();

        try {
            String account1 = bank.createAccount(1000.0);
            String account2 = bank.createAccount(500.0);

            Thread t1 = new Thread(() -> {
                try {
                    bank.transfer(account1, account2, 200.0);
                    System.out.println("Transfer 200 completed");
                } catch (Exception e) {
                    System.err.println("Transfer failed: " + e.getMessage());
                }
            });

            Thread t2 = new Thread(() -> {
                try {
                    bank.getAccount(account1).deposit(300.0);
                    System.out.println("Deposit 300 completed");
                } catch (Exception e) {
                    System.err.println("Deposit failed: " + e.getMessage());
                }
            });

            Thread t3 = new Thread(() -> {
                try {
                    bank.getAccount(account2).withdraw(300.0);
                    System.out.println("Withdraw 100 completed");
                } catch (Exception e) {
                    System.err.println("Withdrawal failed: " + e.getMessage());
                }
            });

            t1.start();
            t2.start();
            t3.start();

            t1.join();
            t2.join();
            t3.join();

            System.out.println("Account 1 balance: " + bank.getAccount(account1).getBalance());
            System.out.println("Account 2 balance: " + bank.getAccount(account2).getBalance());
            System.out.println("Total balance: " + bank.getTotalBalance());
        } catch (Exception e) {
            System.err.println("Bank operation failed: " + e.getMessage());
        }
    }
}
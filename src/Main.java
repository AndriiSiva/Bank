
public class Main {
    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();

        String account1 = bank.createAccount(1000.0);
        String account2 = bank.createAccount(500.0);


        Thread t1 = new Thread(() -> {
            bank.transfer(account1, account2, 200.0);
            System.out.println("Transfer 200 completed");
        });

        Thread t2 = new Thread(() -> {
            bank.getAccount(account1).deposit(300.0);
            System.out.println("Deposit 300 completed");
        });

        Thread t3 = new Thread(() -> {
            bank.getAccount(account2).withdraw(100.0);
            System.out.println("Withdraw 100 completed");
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Account 1 balance: " + bank.getAccount(account1).getBalance());
        System.out.println("Account 2 balance: " + bank.getAccount(account2).getBalance());
        System.out.println("Total balance: " + bank.getTotalBalance());
    }
}
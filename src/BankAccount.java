
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private double balance;
    private final String accountNumber;
    final ReentrantLock lock;

    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.lock = new ReentrantLock();
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        lock.lock();
        try {
            balance += amount;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}


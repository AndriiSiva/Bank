import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentBank {
    private final Map<String, BankAccount> accounts;
    private final ReentrantLock bankLock;

    public ConcurrentBank() {
        accounts = new HashMap<>();
        bankLock = new ReentrantLock();
    }

    public String createAccount(double initialBalance) {
        bankLock.lock();
        try {
            String accountNumber = "ACC" + System.currentTimeMillis();
            BankAccount account = new BankAccount(accountNumber, initialBalance);
            accounts.put(accountNumber, account);
            return accountNumber;
        } finally {
            bankLock.unlock();
        }
    }

    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        if (amount <= 0 || fromAccountNumber.equals(toAccountNumber)) {
            return false;
        }

        BankAccount fromAccount = accounts.get(fromAccountNumber);
        BankAccount toAccount = accounts.get(toAccountNumber);

        if (fromAccount == null || toAccount == null) {
            return false;
        }


        BankAccount firstLock = fromAccountNumber.compareTo(toAccountNumber) < 0 ? fromAccount : toAccount;
        BankAccount secondLock = fromAccountNumber.compareTo(toAccountNumber) < 0 ? toAccount : fromAccount;

        firstLock.lock.lock();
        try {
            secondLock.lock.lock();
            try {
                if (fromAccount.withdraw(amount)) {
                    toAccount.deposit(amount);
                    return true;
                }
                return false;
            } finally {
                secondLock.lock.unlock();
            }
        } finally {
            firstLock.lock.unlock();
        }
    }

    public double getTotalBalance() {
        bankLock.lock();
        try {
            double total = 0;
            for (BankAccount account : accounts.values()) {
                total += account.getBalance();
            }
            return total;
        } finally {
            bankLock.unlock();
        }
    }

    public BankAccount getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
}
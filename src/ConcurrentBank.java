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

    public String createAccount(double initialBalance) throws InvalidAmountException {
        if (initialBalance < 0) {
            throw new InvalidAmountException("Initial balance cannot be negative: " + initialBalance);
        }
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

    public void transfer(String fromAccountNumber, String toAccountNumber, double amount)
            throws InvalidAmountException, InsufficientFundsException, AccountNotFoundException {
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive: " + amount);
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new InvalidAmountException("Cannot transfer to the same account");
        }

        BankAccount fromAccount = accounts.get(fromAccountNumber);
        BankAccount toAccount = accounts.get(toAccountNumber);

        if (fromAccount == null) {
            throw new AccountNotFoundException("Source account not found: " + fromAccountNumber);
        }
        if (toAccount == null) {
            throw new AccountNotFoundException("Destination account not found: " + toAccountNumber);
        }

        BankAccount firstLock = fromAccountNumber.compareTo(toAccountNumber) < 0 ? fromAccount : toAccount;
        BankAccount secondLock = fromAccountNumber.compareTo(toAccountNumber) < 0 ? toAccount : fromAccount;

        firstLock.lock.lock();
        try {
            secondLock.lock.lock();
            try {
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);
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

    public BankAccount getAccount(String accountNumber) throws AccountNotFoundException {
        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        return account;
    }
}
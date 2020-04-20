package com.abc.customer;

import com.abc.account.Account;
import com.abc.transaction.Transaction;
import com.abc.account.AccountService;

import static java.lang.Math.abs;

public class CustomerService {

    private static CustomerService instance;
    private AccountService accountService;

    public CustomerService() {
        accountService = AccountService.getInstance();
    }

    public static synchronized CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
        }

        return instance;
    }

    public Customer openAccount(Customer customer, Account account) {
        customer.getAccounts().add(account);
        return customer;
    }

    public int getNumberOfAccounts(Customer customer) {
        return customer.getAccounts().size();
    }

    public double totalInterestEarned(Customer customer) {
        double total = 0;
        for (Account account : customer.getAccounts()) {
            total += accountService.interestEarned(account);
        }
        return total;
    }

    public String getStatement(Customer customer) {
        String statement = "Statement for " + customer.getName() + "\n";
        double total = 0.0;
        for (Account account : customer.getAccounts()) {
            statement += "\n" + statementForAccount(account) + "\n";
            total += accountService.sumTransactions(account);
        }
        statement += "\nTotal In All Accounts " + toDollars(total);
        return statement;
    }

    private String statementForAccount(Account a) {
        String s = "";

        //Translate to pretty account type
        switch (a.getAccountType()) {
            case CHECKING:
                s += "Checking Account\n";
                break;
            case SAVINGS:
                s += "Savings Account\n";
                break;
            case MAXI_SAVINGS:
                s += "Maxi Savings Account\n";
                break;
        }

        //Now total up all the transactions
        double total = 0.0;
        for (Transaction t : a.getTransactions()) {
            s += "  " + (t.getAmount() < 0 ? "withdrawal" : "deposit") + " " + toDollars(t.getAmount()) + "\n";
            total += t.getAmount();
        }
        s += "Total " + toDollars(total);
        return s;
    }

    private String toDollars(double d) {
        return String.format("$%,.2f", abs(d));
    }
}

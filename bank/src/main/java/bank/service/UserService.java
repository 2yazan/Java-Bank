package main.java.bank.service;

import main.java.bank.entity.CreditAccount;
import main.java.bank.entity.PaymentAccount;
import main.java.bank.entity.User;
import main.java.bank.exception.NoPaymentAccountException;
import main.java.bank.exception.NotFoundException;
import main.java.bank.exception.UniquenessException;

import java.util.List;

public interface UserService {
    User create(User user) throws UniquenessException;
    public void printUserData(long id, boolean withAccounts);
    public User getUserById(long id);
    public List<User> getAllUsers();
    public boolean addPaymentAccount(long userId, PaymentAccount paymentAccount);
    public boolean addCreditAccount(long userId, CreditAccount creditAccount);
    public List<PaymentAccount> getAllPaymentAccountsByUserId(long userId);
    public List<CreditAccount> getAllCreditAccountsByUserId(long userId);
    public int calculateCreditRating(User user);
    public PaymentAccount getBestPaymentAccount(long id) throws NotFoundException, NoPaymentAccountException;
    public boolean transferUserToAnotherBank(User user, long newBankId);
    public boolean exportUserAccountsToTxtFile(long userId, long bankId) throws NoPaymentAccountException;
}

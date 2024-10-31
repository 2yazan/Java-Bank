package main.java.bank.service;

import main.java.bank.entity.CreditAccount;
import main.java.bank.exception.PaymentException;
import main.java.bank.exception.UniquenessException;

import java.util.List;

public interface CreditAccountService {
    CreditAccount create(CreditAccount creditAccount) throws UniquenessException;
    public CreditAccount getCreditAccountById(long id);
    public List<CreditAccount> getAllCreditAccounts();
    boolean makeMonthlyPayment(CreditAccount account) throws PaymentException;

}

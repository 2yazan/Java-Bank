package main.java.bank.service;

import main.java.bank.entity.PaymentAccount;
import main.java.bank.exception.AccountTransferException;
import main.java.bank.exception.UniquenessException;

import java.util.List;

public interface PaymentAccountService {
    PaymentAccount create(PaymentAccount paymentAccount) throws UniquenessException;
    public void printPaymentData(long id);
    public PaymentAccount getPaymentAccountById(long id);
    public List<PaymentAccount> getAllPaymentAccounts();
    boolean depositMoney(PaymentAccount account, double amount);
    boolean withdrawMoney(PaymentAccount account, double amount);
    public boolean importAccountsFromTxtAndTransferToAnotherBank(String fileName, long newBankId) throws AccountTransferException;
    public void setCreditAccountService(CreditAccountService creditAccountService);
    public void setBankService(BankService bankService);
}

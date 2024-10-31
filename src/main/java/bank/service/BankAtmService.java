package main.java.bank.service;

import main.java.bank.entity.BankAtm;
import main.java.bank.exception.UniquenessException;

import java.util.List;

public interface BankAtmService {
    BankAtm create(BankAtm bankAtm) throws UniquenessException;

    public BankAtm getBankAtmById(long id);

    public List<BankAtm> getAllBankAtms();

    boolean depositMoney(BankAtm bankAtm, double amount);

    boolean withdrawMoney(BankAtm bankAtm, double amount);

    public boolean isAtmSuitable(BankAtm bankAtm, double sum);
}
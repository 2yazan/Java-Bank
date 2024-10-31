package main.java.bank.service;

import main.java.bank.entity.Bank;
import main.java.bank.entity.BankOffice;
import main.java.bank.entity.CreditAccount;
import main.java.bank.entity.Employee;
import main.java.bank.entity.User;
import main.java.bank.exception.CreditException;
import main.java.bank.exception.NotFoundException;
import main.java.bank.exception.UniquenessException;

import java.util.List;

public interface BankService {
    public void setBankOfficeService(BankOfficeService bankOfficeService);
    public void setUserService(UserService userService);
    public Bank create(Bank bank) throws UniquenessException;
    public void printBankData(long id);
    public Bank getBankById(long id);
    public List<Bank> getAllBanks();
    public boolean addOffice(long bankId, BankOffice bankOffice);
    public List<BankOffice> getAllOfficesByBankId(long bankId);
    public boolean addEmployee(Bank bank, Employee employee);
    public boolean addClient(long bankId, User user);
    public double calculateInterestRate(Bank bank);
    public boolean depositMoney(long bankId, double amount);
    public boolean withdrawMoney(Bank bank, double amount);
    public boolean approveCredit(Bank bank, CreditAccount account, Employee employee) throws CreditException;

    public List<Bank> getBanksSuitable(double sum, int countMonth) throws NotFoundException, CreditException;

    public boolean isBankSuitable(Bank bank, double sum) throws NotFoundException;

    public List<BankOffice> getBankOfficeSuitableInBank(Bank bank, double sum) throws NotFoundException;

    public boolean transferClient(User user, long newBankId);
}

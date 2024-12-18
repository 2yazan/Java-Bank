package main.java.bank.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.bank.entity.CreditAccount;
import main.java.bank.entity.PaymentAccount;
import main.java.bank.entity.User;
import main.java.bank.exception.NoPaymentAccountException;
import main.java.bank.exception.NotFoundException;
import main.java.bank.exception.UniquenessException;
import main.java.bank.service.BankService;
import main.java.bank.service.UserService;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static main.java.bank.util.Constants.ASCII_PURPLE_COLOR;
import static main.java.bank.util.Constants.ASCII_RESET;
import static main.java.bank.util.Constants.MAX_USER_MONTHLY_INCOME;

public class UserServiceImpl implements UserService {
    private final Map<Long, User> usersTable = new HashMap<>();
    private final Map<Long, List<PaymentAccount>> paymentAccountsByUserIdTable = new HashMap<>();
    private final Map<Long, List<CreditAccount>> creditAccountsByUserIdTable = new HashMap<>();
    private final BankService bankService;

    public UserServiceImpl(BankService bankService) {
        this.bankService = bankService;
    }

    public int calculateCreditRating(User user) {
        final int rating = (int)Math.ceil(user.getMonthlyIncome() / 1000) * 100;
        user.setCreditRating(rating);
        return rating;
    }

    public User create(User user) throws UniquenessException {
        if (user == null) {
            return null;
        }

        if (user.getId() < 0) {
            System.out.println("Error: user id must be non-negative");
            return null;
        }

        if (user.getBank() == null) {
            System.out.println("Error: can not create user without bank");
            return null;
        }

        User createdUser = new User(user);

        if (usersTable.containsKey(createdUser.getId())) {
            throw new UniquenessException("User", createdUser.getId());
        }

        final Random random = new Random();

        final double monthlyIncome = random.nextDouble() * MAX_USER_MONTHLY_INCOME;
        createdUser.setMonthlyIncome(monthlyIncome);
        calculateCreditRating(createdUser);

        usersTable.put(createdUser.getId(), createdUser);
        paymentAccountsByUserIdTable.put(createdUser.getId(), new ArrayList<>());
        creditAccountsByUserIdTable.put(createdUser.getId(), new ArrayList<>());
        bankService.addClient(user.getBank().getId(), createdUser);

        return createdUser;
    }

    public void printUserData(long id, boolean withAccounts) {
        User user = usersTable.get(id);

        if (user == null) {
            System.out.println("User with id: " + id + " was not found.");
            return;
        }

        System.out.println(user);

        if (withAccounts) {
            List<PaymentAccount> paymentAccounts = paymentAccountsByUserIdTable.get(id);
            if (paymentAccounts != null) {
                System.out.println(ASCII_PURPLE_COLOR + "Payment accounts:" + ASCII_RESET);
                paymentAccounts.forEach(System.out::println);
            }

            List<CreditAccount> creditAccounts = creditAccountsByUserIdTable.get(id);
            if (creditAccounts != null) {
                System.out.println(ASCII_PURPLE_COLOR + "Credit accounts:" + ASCII_RESET);
                creditAccounts.forEach(System.out::println);
            }
        }
    }

    public User getUserById(long id) {
        User user = usersTable.get(id);

        if (user == null) {
            System.out.println("User with id: " + id + " was not found.");
        }

        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(usersTable.values());
    }

    public boolean addPaymentAccount(long userId, PaymentAccount paymentAccount) {
        User user = usersTable.get(userId);
        if (user != null) {
            List<PaymentAccount> userPaymentAccounts = paymentAccountsByUserIdTable.get(userId);
            userPaymentAccounts.add(paymentAccount);
            user.addAccount(paymentAccount);
            user.getBank().addAccount(paymentAccount);
            return true;
        }

        return false;
    }

    public boolean addCreditAccount(long userId, CreditAccount creditAccount) {
        User user = usersTable.get(userId);
        if (user != null) {
            List<CreditAccount> userCreditAccounts = creditAccountsByUserIdTable.get(userId);
            userCreditAccounts.add(creditAccount);
            user.addAccount(creditAccount);
            user.getBank().addAccount(creditAccount);
            return true;
        }

        return false;
    }

    public List<PaymentAccount> getAllPaymentAccountsByUserId(long userId) {
        return paymentAccountsByUserIdTable.get(userId);
    }

    public List<CreditAccount> getAllCreditAccountsByUserId(long userId) {
        return creditAccountsByUserIdTable.get(userId);
    }

    public PaymentAccount getBestPaymentAccount(long id) throws NotFoundException, NoPaymentAccountException {
        List<PaymentAccount> paymentAccounts = getAllPaymentAccountsByUserId(id);

        return paymentAccounts
                .stream()
                .min(Comparator.comparing(PaymentAccount::getBalance))
                .orElseThrow(NoPaymentAccountException::new);
    }

    public boolean transferUserToAnotherBank(User user, long newBankId) {
        return bankService.transferClient(user, newBankId);
    }

    public boolean exportUserAccountsToTxtFile(long userId, long bankId) throws NoPaymentAccountException {
        List<CreditAccount> userCreditAccounts = getAllCreditAccountsByUserId(userId);

        if (userCreditAccounts.isEmpty())
            throw new NoPaymentAccountException();

        try {
            // PrintWriter out = new PrintWriter("user_" + userId + "_accounts_of_bank_" + bankId + ".txt");
            PrintWriter out = new PrintWriter("users.txt");
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            out.println(gson.toJson(userCreditAccounts));
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}

package main.java.bank;

import main.java.bank.entity.Bank;
import main.java.bank.entity.BankAtm;
import main.java.bank.entity.BankOffice;
import main.java.bank.entity.CreditAccount;
import main.java.bank.entity.Employee;
import main.java.bank.entity.PaymentAccount;
import main.java.bank.entity.User;
import main.java.bank.exception.CreditException;
import main.java.bank.exception.NoPaymentAccountException;
import main.java.bank.exception.NotFoundException;
import main.java.bank.exception.UniquenessException;
import main.java.bank.service.BankAtmService;
import main.java.bank.service.BankOfficeService;
import main.java.bank.service.BankService;
import main.java.bank.service.CreditAccountService;
import main.java.bank.service.EmployeeService;
import main.java.bank.service.PaymentAccountService;
import main.java.bank.service.UserService;
import main.java.bank.service.impl.BankAtmServiceImpl;
import main.java.bank.service.impl.BankOfficeServiceImpl;
import main.java.bank.service.impl.BankServiceImpl;
import main.java.bank.service.impl.CreditAccountServiceImpl;
import main.java.bank.service.impl.EmployeeServiceImpl;
import main.java.bank.service.impl.PaymentAccountServiceImpl;
import main.java.bank.service.impl.UserServiceImpl;
import main.java.bank.util.BankAtmStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static main.java.bank.util.Constants.ASCII_GREEN_COLOR;
import static main.java.bank.util.Constants.ASCII_RED_COLOR;
import static main.java.bank.util.Constants.ASCII_RESET;
import static main.java.bank.util.Constants.BANK_ROLES;
import static main.java.bank.util.Constants.COMPANY_NAMES;
import static main.java.bank.util.Constants.PERSON_NAMES;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        // create services
        BankService bankService = new BankServiceImpl();
        BankOfficeService bankOfficeService = new BankOfficeServiceImpl(bankService);
        bankService.setBankOfficeService(bankOfficeService);
        EmployeeService employeeService = new EmployeeServiceImpl(bankOfficeService);
        bankOfficeService.setEmployeeService(employeeService);
        BankAtmService atmService = new BankAtmServiceImpl(bankOfficeService);
        bankOfficeService.setAtmService(atmService);
        UserService userService = new UserServiceImpl(bankService);
        bankService.setUserService(userService);
        PaymentAccountService paymentAccountService = new PaymentAccountServiceImpl(userService);
        CreditAccountService creditAccountService = new CreditAccountServiceImpl(userService);
        paymentAccountService.setCreditAccountService(creditAccountService);
        paymentAccountService.setBankService(bankService);

        try {
            // create banks
            bankService.create(new Bank("Alpha Bank"));
            bankService.create(new Bank("Tinkoff Bank"));
            bankService.create(new Bank("Sberbank"));
            bankService.create(new Bank("Post Office Bank"));
            bankService.create(new Bank("Otkritie Bank"));

            // create offices in each bank
            List<Bank> banks = bankService.getAllBanks();
            for (Bank bank : banks) {
                for (int i = 1; i <= 3; i++) {
                    bankOfficeService.create(new BankOffice(
                            "Office #" + String.valueOf(i) + " of " + bank.getName(),
                            "Belgorod, kastykova st., " + String.valueOf(i),
                            bank,
                            true,
                            true,
                            true,
                            true,
                            true,
                            10000,
                            100 * i
                    ));
                }
            }

            // Adding employees to each office
            List<BankOffice> offices = bankOfficeService.getAllOffices();
            for (BankOffice office : offices) {
                for (int i = 1; i <= 5; i++) {
                    employeeService.create(new Employee(
                            PERSON_NAMES.get(random.nextInt(PERSON_NAMES.size())),
                            LocalDate.of(random.nextInt(1990, 2003), random.nextInt(1, 13), random.nextInt(1, 29)),
                            BANK_ROLES.get(random.nextInt(BANK_ROLES.size())),
                            office.getBank(),
                            true,
                            office,
                            true,
                            500
                    ));
                }
            }

            // Adding ATMs to every office
            for (BankOffice office : offices) {
                for (int i = 1; i <= 3; i++) {
                    atmService.create(new BankAtm(
                            "Atm " + String.valueOf(i),
                            office.getAddress(),
                            BankAtmStatus.WORKING,
                            office.getBank(),
                            office,
                            true,
                            true,
                            random.nextDouble() * 15000,
                            random.nextDouble() * 25
                    ));
                }
            }

            // Adding clients to each bank
            for (Bank bank : banks) {
                for (int i = 1; i <= 5; i++) {
                    userService.create(
                            new User(
                                    PERSON_NAMES.get(random.nextInt(PERSON_NAMES.size())),
                                    LocalDate.of(random.nextInt(1940, 2003), random.nextInt(1, 13), random.nextInt(1, 29)),
                                    COMPANY_NAMES.get(random.nextInt(COMPANY_NAMES.size())),
                                    random.nextDouble() * 10000,
                                    bank,
                                    random.nextInt(10000)
                            ));
                }
            }

            // Adding payment accounts to each client
            List<User> users = userService.getAllUsers();
            for (User user : users) {
                for (int i = 1; i <= 2; i++) {
                    paymentAccountService.create(new PaymentAccount(
                            user,
                            user.getBank(),
                            random.nextDouble() * 10000
                    ));
                }
            }

            // Adding credit accounts to each client
            for (User user : users) {
                for (int i = 1; i <= 2; i++) {
                    List<BankOffice> bankOffices = bankService.getAllOfficesByBankId(user.getBank().getId());
                    BankOffice randomOffice = bankOffices.get(random.nextInt(bankOffices.size()));
                    List<Employee> officeEmployees = bankOfficeService.getAllEmployeesByOfficeId(randomOffice.getId());
                    Employee randomEmployee = officeEmployees.get(random.nextInt(officeEmployees.size()));

                    creditAccountService.create(new CreditAccount(
                            user,
                            user.getBank(),
                            LocalDate.of(2023, 10, 1),
                            LocalDate.of(2026, 10, 1),
                            36,
                            3600,
                            3600,
                            100,
                            user.getBank().getInterestRate(),
                            randomEmployee,
                            userService.getAllPaymentAccountsByUserId(user.getId()).get(random.nextInt(userService.getAllPaymentAccountsByUserId(user.getId()).size()))
                    ));
                }
            }

            System.out.println("\nWelcome to BankApp .");
            System.out.println("Number of banks in system: " + bankService.getAllBanks().size());
            for (Bank bank : bankService.getAllBanks()) {
                System.out.println("id: " + bank.getId() + " - " + bank.getName());
            }

            label:
            while (true) {
                try {
                    System.out.println("\nChoose an action: ");
                    System.out.println("1) check bank data by bank id");
                    System.out.println("2) check user data by user id");
                    System.out.println("3) apply for a credit");
                    System.out.println("4) export user accounts by bank id to .txt file");
                    System.out.println("5) import accounts from .txt file and move them to another bank");
                    System.out.println("6) quit program");

                    String action = scanner.nextLine();

                    switch (action) {
                        case "1":
                            System.out.println("Enter bank id:");
                            int bankIdToPrint = scanner.nextInt();
                            scanner.nextLine();
                            bankService.printBankData(bankIdToPrint);
                            break;
                        case "2":
                            System.out.println("Enter user id:");
                            int userIdToPrint = scanner.nextInt();
                            scanner.nextLine();
                            userService.printUserData(userIdToPrint, true);
                            break;
                        case "3": {
                            System.out.println("Which user wants to apply for a credit?");
                            for (User user : userService.getAllUsers()) {
                                System.out.println("id: " + user.getId() + " - " + user.getName());
                            }

                            System.out.println("Enter user id:");
                            int userId = scanner.nextInt();
                            scanner.nextLine();

                            System.out.println("Enter credit amount:");
                            double amount = scanner.nextDouble();
                            scanner.nextLine();

                            System.out.println("Enter duration in months:");
                            int months = scanner.nextInt();
                            scanner.nextLine();

                            List<Bank> suitableBanks = bankService.getBanksSuitable(amount, months);
                            System.out.println("List of suitable banks:");
                            for (Bank bank : suitableBanks) {
                                System.out.println("id: " + bank.getId() + " - " + bank.getName());
                            }

                            System.out.println("Enter chosen bank id:");
                            int bankId = scanner.nextInt();
                            scanner.nextLine();
                            Bank bank = bankService.getBankById(bankId);
                            BankOffice bankOffice = bankService.getBankOfficeSuitableInBank(bank, amount).get(0);
                            Employee employee = bankOfficeService.getSuitableEmployeeInOffice(bankOffice).get(0);

                            PaymentAccount paymentAccount;
                            // If the client does not have a payment account, it should be created
                            try {
                                paymentAccount = userService.getBestPaymentAccount(userId);
                            } catch (NoPaymentAccountException e) {
                                paymentAccount = paymentAccountService.create(new PaymentAccount(
                                        userService.getUserById(userId),
                                        userService.getUserById(userId).getBank(),
                                        0
                                ));
                            }

                            CreditAccount creditAccount = creditAccountService.create(new CreditAccount(
                                    userService.getUserById(userId),
                                    bank,
                                    LocalDate.now(),
                                    LocalDate.now().plusMonths(months),
                                    months,
                                    amount,
                                    amount,
                                    0,
                                    bank.getInterestRate(),
                                    employee,
                                    paymentAccount
                            ));
                            if (bankService.approveCredit(bank, creditAccount, employee)) {
                                System.out.println("Apply for credit was approved!");
                                System.out.println("credit account id: " + creditAccount.getId());
                            } else {
                                System.out.println("Credit was not approved");
                            }
                            break;
                        }
                        case "4": {
                            System.out.println("Available users:");
                            for (User user : userService.getAllUsers()) {
                                System.out.println("id: " + user.getId() + " - " + user.getName());
                            }

                            System.out.println("Enter user id:");
                            int userId = scanner.nextInt();
                            scanner.nextLine();

                            System.out.println("Enter bank id:");
                            int bankId = scanner.nextInt();
                            scanner.nextLine();

                            boolean success = userService.exportUserAccountsToTxtFile(userId, bankId);

                            if (success) {
                                System.out.println(ASCII_GREEN_COLOR + "Export done successfully" + ASCII_RESET);
                            } else {
                                System.out.println(ASCII_RED_COLOR + "Export operation wasn't done" + ASCII_RESET);
                            }
                            break;
                        }
                        case "5": {
                            System.out.println("Enter file name:");
                            String fileName = scanner.nextLine();

                            System.out.println("Enter new bank id:");
                            int newBankId = scanner.nextInt();
                            scanner.nextLine();

                            boolean success = paymentAccountService.importAccountsFromTxtAndTransferToAnotherBank(fileName, newBankId);

                            if (success) {
                                System.out.println(ASCII_GREEN_COLOR + "Import done successfully" + ASCII_RESET);
                            } else {
                                System.out.println(ASCII_RED_COLOR + "Import operation wasn't done" + ASCII_RESET);
                            }
                            break;
                        }
                        case "6":
                            break label;
                        default:
                            System.out.println("Error: unknown action. Please, try again");
                            break;
                    }
                } catch (CreditException | NotFoundException | NoPaymentAccountException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (UniquenessException e) {
            System.err.println(e.getMessage());
        }
    }
}

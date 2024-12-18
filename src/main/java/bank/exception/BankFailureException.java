package main.java.bank.exception;

public class BankFailureException extends RuntimeException {
    public BankFailureException(String msg) {
        super("Error: bank critical failure: " + msg);
    }
}
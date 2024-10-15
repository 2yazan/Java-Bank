package main.java.bank.exception;

public class CreditException extends RuntimeException {
    public CreditException(String msg) {
        super("Error: credit can't be approved - " + msg);
    }
}
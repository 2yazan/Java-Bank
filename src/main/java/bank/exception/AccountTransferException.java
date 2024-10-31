package main.java.bank.exception;

public class AccountTransferException extends RuntimeException {
    public AccountTransferException(String msg) {
        super("Error: account transfer is not possible: " + msg);
    }
}

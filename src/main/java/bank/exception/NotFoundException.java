package main.java.bank.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entity, long id) {
        super("Error: " + entity + " with id: " + id + " was not found.");
    }
}

package main.exception;

public class ManagerOverlappingException extends RuntimeException {

    public ManagerOverlappingException(final String message) {
        super(message);
    }
}

package main.exception;

public class RequestMethodException extends RuntimeException {
    public RequestMethodException(String message) {
        super(message);
    }
}

package main.exception;

public class TypeExistsException extends RuntimeException {
    public TypeExistsException(String message) {
        super(message);
    }
}

package main.exception;

public class TaskExistsException extends RuntimeException{
    public TaskExistsException(String message) {
        super(message);
    }
}

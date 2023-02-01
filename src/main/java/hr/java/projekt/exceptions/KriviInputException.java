package hr.java.projekt.exceptions;


public class KriviInputException extends Exception{
    public KriviInputException() {}

    public KriviInputException(String message) {
        super(message);
    }

    public KriviInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public KriviInputException(Throwable cause) {
        super(cause);
    }
}

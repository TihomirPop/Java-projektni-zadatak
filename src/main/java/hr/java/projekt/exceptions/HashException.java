package hr.java.projekt.exceptions;

public class HashException extends RuntimeException{
    public HashException() {
    }

    public HashException(String message) {
        super(message);
    }

    public HashException(String message, Throwable cause) {
        super(message, cause);
    }

    public HashException(Throwable cause) {
        super(cause);
    }
}

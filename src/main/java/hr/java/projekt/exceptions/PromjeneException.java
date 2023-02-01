package hr.java.projekt.exceptions;

public class PromjeneException extends Exception{
    public PromjeneException() {
    }

    public PromjeneException(String message) {
        super(message);
    }

    public PromjeneException(String message, Throwable cause) {
        super(message, cause);
    }

    public PromjeneException(Throwable cause) {
        super(cause);
    }
}

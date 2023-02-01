package hr.java.projekt.exceptions;

public class BazaPodatakaException extends Exception{
    public BazaPodatakaException() {}

    public BazaPodatakaException(String message) {
        super(message);
    }

    public BazaPodatakaException(String message, Throwable cause) {
        super(message, cause);
    }

    public BazaPodatakaException(Throwable cause) {
        super(cause);
    }
}

package icyllis.modernui.util;

public class BadParcelableException extends RuntimeException {

    public BadParcelableException(String msg) {
        super(msg);
    }

    public BadParcelableException(Exception cause) {
        super(cause);
    }

    public BadParcelableException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
package info.journeymap.shaded.org.eclipse.jetty.http;

public class BadMessageException extends RuntimeException {

    final int _code;

    final String _reason;

    public BadMessageException() {
        this(400, null);
    }

    public BadMessageException(int code) {
        this(code, null);
    }

    public BadMessageException(String reason) {
        this(400, reason);
    }

    public BadMessageException(String reason, Throwable cause) {
        this(400, reason, cause);
    }

    public BadMessageException(int code, String reason) {
        super(code + ": " + reason);
        this._code = code;
        this._reason = reason;
    }

    public BadMessageException(int code, String reason, Throwable cause) {
        super(code + ": " + reason, cause);
        this._code = code;
        this._reason = reason;
    }

    public int getCode() {
        return this._code;
    }

    public String getReason() {
        return this._reason;
    }
}
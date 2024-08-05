package info.journeymap.shaded.kotlin.spark;

public class HaltException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int statusCode = 200;

    private String body = null;

    HaltException() {
    }

    HaltException(int statusCode) {
        this.statusCode = statusCode;
    }

    HaltException(String body) {
        this.body = body;
    }

    HaltException(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    /**
     * @deprecated
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    public int statusCode() {
        return this.statusCode;
    }

    /**
     * @deprecated
     */
    public String getBody() {
        return this.body;
    }

    public String body() {
        return this.body;
    }
}
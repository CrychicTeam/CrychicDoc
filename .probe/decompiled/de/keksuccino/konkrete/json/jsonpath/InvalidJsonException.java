package de.keksuccino.konkrete.json.jsonpath;

public class InvalidJsonException extends JsonPathException {

    private final String json;

    public InvalidJsonException() {
        this.json = null;
    }

    public InvalidJsonException(String message) {
        super(message);
        this.json = null;
    }

    public InvalidJsonException(String message, Throwable cause) {
        super(message, cause);
        this.json = null;
    }

    public InvalidJsonException(Throwable cause) {
        super(cause);
        this.json = null;
    }

    public InvalidJsonException(Throwable cause, String json) {
        super(cause);
        this.json = json;
    }

    public String getJson() {
        return this.json;
    }
}
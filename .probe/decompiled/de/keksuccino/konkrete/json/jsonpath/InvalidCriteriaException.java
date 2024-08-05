package de.keksuccino.konkrete.json.jsonpath;

public class InvalidCriteriaException extends JsonPathException {

    public InvalidCriteriaException() {
    }

    public InvalidCriteriaException(String message) {
        super(message);
    }

    public InvalidCriteriaException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCriteriaException(Throwable cause) {
        super(cause);
    }
}
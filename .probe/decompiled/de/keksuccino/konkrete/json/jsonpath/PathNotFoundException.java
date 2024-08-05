package de.keksuccino.konkrete.json.jsonpath;

public class PathNotFoundException extends InvalidPathException {

    public PathNotFoundException() {
    }

    public PathNotFoundException(String message) {
        super(message);
    }

    public PathNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PathNotFoundException(Throwable cause) {
        super(cause);
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
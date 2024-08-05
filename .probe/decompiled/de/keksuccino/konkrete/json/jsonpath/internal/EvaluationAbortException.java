package de.keksuccino.konkrete.json.jsonpath.internal;

public class EvaluationAbortException extends RuntimeException {

    private static final long serialVersionUID = 4419305302960432348L;

    public Throwable fillInStackTrace() {
        return this;
    }
}
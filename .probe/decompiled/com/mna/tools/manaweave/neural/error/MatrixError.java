package com.mna.tools.manaweave.neural.error;

public class MatrixError extends RuntimeException {

    private static final long serialVersionUID = -8961386981267748942L;

    public MatrixError(String message) {
        super(message);
    }

    public MatrixError(Throwable t) {
        super(t);
    }
}
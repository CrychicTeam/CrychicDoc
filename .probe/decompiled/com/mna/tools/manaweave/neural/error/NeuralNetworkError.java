package com.mna.tools.manaweave.neural.error;

public class NeuralNetworkError extends RuntimeException {

    private static final long serialVersionUID = 7167228729133120101L;

    public NeuralNetworkError(String msg) {
        super(msg);
    }

    public NeuralNetworkError(Throwable t) {
        super(t);
    }
}
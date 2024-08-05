package de.keksuccino.konkrete.json.minidev.asm.ex;

public class NoSuchFieldException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoSuchFieldException() {
    }

    public NoSuchFieldException(String message) {
        super(message);
    }
}
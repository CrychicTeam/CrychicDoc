package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api;

public class DeserializationException extends Exception {

    private static final long serialVersionUID = 8425560848572561283L;

    public DeserializationException() {
    }

    public DeserializationException(String message) {
        super(message);
    }

    public DeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeserializationException(Throwable cause) {
        super(cause);
    }
}
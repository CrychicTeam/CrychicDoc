package me.lucko.spark.common.sampler.async;

public class JfrParsingException extends RuntimeException {

    public JfrParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
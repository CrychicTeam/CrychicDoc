package me.lucko.spark.lib.bytesocks.ws.exceptions;

import java.io.UnsupportedEncodingException;

public class InvalidEncodingException extends RuntimeException {

    private final UnsupportedEncodingException encodingException;

    public InvalidEncodingException(UnsupportedEncodingException encodingException) {
        if (encodingException == null) {
            throw new IllegalArgumentException();
        } else {
            this.encodingException = encodingException;
        }
    }

    public UnsupportedEncodingException getEncodingException() {
        return this.encodingException;
    }
}
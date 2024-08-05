package com.github.alexthe666.citadel.repack.jaad.aac;

import java.io.IOException;

public class AACException extends IOException {

    private final boolean eos;

    public AACException(String message) {
        this(message, false);
    }

    public AACException(String message, boolean eos) {
        super(message);
        this.eos = eos;
    }

    public AACException(Throwable cause) {
        super(cause);
        this.eos = false;
    }

    boolean isEndOfStream() {
        return this.eos;
    }
}
package me.steinborn.krypton.mod.shared.network.util;

import io.netty.handler.codec.DecoderException;

public class QuietDecoderException extends DecoderException {

    public QuietDecoderException(String message) {
        super(message);
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
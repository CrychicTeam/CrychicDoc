package me.lucko.spark.lib.adventure.key;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class InvalidKeyException extends RuntimeException {

    private static final long serialVersionUID = -5413304087321449434L;

    private final String keyNamespace;

    private final String keyValue;

    InvalidKeyException(@NotNull final String keyNamespace, @NotNull final String keyValue, @Nullable final String message) {
        super(message);
        this.keyNamespace = keyNamespace;
        this.keyValue = keyValue;
    }

    @NotNull
    public final String keyNamespace() {
        return this.keyNamespace;
    }

    @NotNull
    public final String keyValue() {
        return this.keyValue;
    }
}
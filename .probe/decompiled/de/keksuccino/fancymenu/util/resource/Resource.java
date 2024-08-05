package de.keksuccino.fancymenu.util.resource;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import org.jetbrains.annotations.Nullable;

public interface Resource extends Closeable {

    @Nullable
    InputStream open() throws IOException;

    boolean isReady();

    boolean isLoadingCompleted();

    boolean isLoadingFailed();

    default void waitForReady(long timeoutMs) {
        long start = System.currentTimeMillis();
        while (!this.isReady() && start + timeoutMs > System.currentTimeMillis()) {
        }
    }

    default void waitForLoadingCompletedOrFailed(long timeoutMs) {
        long start = System.currentTimeMillis();
        while (!this.isLoadingCompleted() && !this.isLoadingFailed() && start + timeoutMs > System.currentTimeMillis()) {
        }
    }

    boolean isClosed();
}
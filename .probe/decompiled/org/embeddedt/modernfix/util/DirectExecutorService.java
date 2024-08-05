package org.embeddedt.modernfix.util;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

public class DirectExecutorService extends AbstractExecutorService {

    private boolean isShutdown;

    public void shutdown() {
        this.isShutdown = true;
    }

    @NotNull
    public List<Runnable> shutdownNow() {
        this.isShutdown = true;
        return List.of();
    }

    public boolean isShutdown() {
        return this.isShutdown;
    }

    public boolean isTerminated() {
        return this.isShutdown;
    }

    public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return true;
    }

    public void execute(@NotNull Runnable command) {
        command.run();
    }
}
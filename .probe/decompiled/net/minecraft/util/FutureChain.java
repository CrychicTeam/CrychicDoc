package net.minecraft.util;

import com.mojang.logging.LogUtils;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import org.slf4j.Logger;

public class FutureChain implements TaskChainer, AutoCloseable {

    private static final Logger LOGGER = LogUtils.getLogger();

    private CompletableFuture<?> head = CompletableFuture.completedFuture(null);

    private final Executor checkedExecutor;

    private volatile boolean closed;

    public FutureChain(Executor executor0) {
        this.checkedExecutor = p_248283_ -> {
            if (!this.closed) {
                executor0.execute(p_248283_);
            }
        };
    }

    @Override
    public void append(TaskChainer.DelayedTask taskChainerDelayedTask0) {
        this.head = this.head.thenComposeAsync(p_248281_ -> taskChainerDelayedTask0.submit(this.checkedExecutor), this.checkedExecutor).exceptionally(p_242215_ -> {
            if (p_242215_ instanceof CompletionException $$1) {
                p_242215_ = $$1.getCause();
            }
            if (p_242215_ instanceof CancellationException $$2) {
                throw $$2;
            } else {
                LOGGER.error("Chain link failed, continuing to next one", p_242215_);
                return null;
            }
        });
    }

    public void close() {
        this.closed = true;
    }
}
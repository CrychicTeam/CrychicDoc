package net.minecraft.util;

import com.mojang.logging.LogUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.slf4j.Logger;

@FunctionalInterface
public interface TaskChainer {

    Logger LOGGER = LogUtils.getLogger();

    static TaskChainer immediate(Executor executor0) {
        return p_248285_ -> p_248285_.submit(executor0).exceptionally(p_242314_ -> {
            LOGGER.error("Task failed", p_242314_);
            return null;
        });
    }

    void append(TaskChainer.DelayedTask var1);

    public interface DelayedTask {

        CompletableFuture<?> submit(Executor var1);
    }
}
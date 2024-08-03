package org.embeddedt.modernfix.forge.load;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import net.minecraftforge.fml.ModWorkManager;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ModWorkManagerQueue extends ConcurrentLinkedDeque<Runnable> {

    private static final long PARK_TIME = TimeUnit.MILLISECONDS.toNanos(25L);

    private static final Runnable DUMMY_TASK = () -> {
    };

    private boolean shouldReturnDummyTask = false;

    public Runnable pollFirst() {
        Runnable r = (Runnable) super.pollFirst();
        if (r == null) {
            LockSupport.parkNanos(PARK_TIME);
            boolean isReturning = this.shouldReturnDummyTask;
            this.shouldReturnDummyTask = !this.shouldReturnDummyTask;
            return isReturning ? DUMMY_TASK : null;
        } else {
            return r;
        }
    }

    public static void replace() {
        try {
            Class<?> syncExecutorClass = Class.forName("net.minecraftforge.fml.ModWorkManager$SyncExecutor");
            ConcurrentLinkedDeque<Runnable> taskQueue = (ConcurrentLinkedDeque<Runnable>) ObfuscationReflectionHelper.getPrivateValue(syncExecutorClass, ModWorkManager.syncExecutor(), "tasks");
            ModWorkManagerQueue q = new ModWorkManagerQueue();
            Runnable task;
            do {
                task = (Runnable) taskQueue.pollFirst();
                if (task != null) {
                    q.push(task);
                }
            } while (task != null);
            ObfuscationReflectionHelper.setPrivateValue(syncExecutorClass, ModWorkManager.syncExecutor(), q, "tasks");
        } catch (ReflectiveOperationException var4) {
            var4.printStackTrace();
        }
    }
}
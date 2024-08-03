package me.lucko.spark.lib.bytesocks.ws.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final String threadPrefix;

    public NamedThreadFactory(String threadPrefix) {
        this.threadPrefix = threadPrefix;
    }

    public Thread newThread(Runnable runnable) {
        Thread thread = this.defaultThreadFactory.newThread(runnable);
        thread.setName(this.threadPrefix + "-" + this.threadNumber);
        return thread;
    }
}
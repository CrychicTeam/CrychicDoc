package me.lucko.spark.common.util;

import java.util.Arrays;
import java.util.stream.Stream;

public final class ThreadFinder {

    private static final ThreadGroup ROOT_THREAD_GROUP;

    private int approxActiveCount;

    public ThreadFinder() {
        this.approxActiveCount = ROOT_THREAD_GROUP.activeCount();
    }

    public Stream<Thread> getThreads() {
        Thread[] threads = new Thread[this.approxActiveCount + 10];
        int len;
        while ((len = ROOT_THREAD_GROUP.enumerate(threads, true)) == threads.length) {
            threads = new Thread[threads.length * 2];
        }
        this.approxActiveCount = len;
        return Arrays.stream(threads, 0, len);
    }

    static {
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parentGroup;
        while ((parentGroup = rootGroup.getParent()) != null) {
            rootGroup = parentGroup;
        }
        ROOT_THREAD_GROUP = rootGroup;
    }
}
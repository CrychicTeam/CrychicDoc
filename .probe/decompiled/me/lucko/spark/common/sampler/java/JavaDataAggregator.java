package me.lucko.spark.common.sampler.java;

import java.lang.Thread.State;
import java.lang.management.ThreadInfo;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import me.lucko.spark.common.sampler.ThreadGrouper;
import me.lucko.spark.common.sampler.aggregator.AbstractDataAggregator;
import me.lucko.spark.common.sampler.node.StackTraceNode;
import me.lucko.spark.common.sampler.node.ThreadNode;

public abstract class JavaDataAggregator extends AbstractDataAggregator {

    private static final StackTraceNode.Describer<StackTraceElement> STACK_TRACE_DESCRIBER = (element, parent) -> {
        int parentLineNumber = parent == null ? -1 : parent.getLineNumber();
        return new StackTraceNode.Description(element.getClassName(), element.getMethodName(), element.getLineNumber(), parentLineNumber);
    };

    protected final ExecutorService workerPool;

    protected final int interval;

    private final boolean ignoreSleeping;

    private final boolean ignoreNative;

    public JavaDataAggregator(ExecutorService workerPool, ThreadGrouper threadGrouper, int interval, boolean ignoreSleeping, boolean ignoreNative) {
        super(threadGrouper);
        this.workerPool = workerPool;
        this.interval = interval;
        this.ignoreSleeping = ignoreSleeping;
        this.ignoreNative = ignoreNative;
    }

    public abstract void insertData(ThreadInfo var1, int var2);

    protected void writeData(ThreadInfo threadInfo, int window) {
        if (!this.ignoreSleeping || !isSleeping(threadInfo)) {
            if (!this.ignoreNative || !threadInfo.isInNative()) {
                try {
                    ThreadNode node = this.getNode(this.threadGrouper.getGroup(threadInfo.getThreadId(), threadInfo.getThreadName()));
                    node.log(STACK_TRACE_DESCRIBER, threadInfo.getStackTrace(), (long) this.interval, window);
                } catch (Exception var4) {
                    var4.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<ThreadNode> exportData() {
        this.workerPool.shutdown();
        try {
            this.workerPool.awaitTermination(15L, TimeUnit.SECONDS);
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }
        return super.exportData();
    }

    private static boolean isSleeping(ThreadInfo thread) {
        if (thread.getThreadState() != State.WAITING && thread.getThreadState() != State.TIMED_WAITING) {
            StackTraceElement[] stackTrace = thread.getStackTrace();
            if (stackTrace.length == 0) {
                return false;
            } else {
                StackTraceElement call = stackTrace[0];
                String clazz = call.getClassName();
                String method = call.getMethodName();
                return clazz.equals("java.lang.Thread") && method.equals("yield") || clazz.equals("jdk.internal.misc.Unsafe") && method.equals("park") || clazz.equals("sun.misc.Unsafe") && method.equals("park");
            }
        } else {
            return true;
        }
    }
}
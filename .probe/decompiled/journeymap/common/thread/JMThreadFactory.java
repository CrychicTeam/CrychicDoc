package journeymap.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class JMThreadFactory implements ThreadFactory {

    static final AtomicInteger threadNumber = new AtomicInteger(1);

    static final String namePrefix = "JM-";

    final ThreadGroup group;

    final String name;

    public JMThreadFactory(String name) {
        this.name = "JM-" + name;
        this.group = Thread.currentThread().getThreadGroup();
    }

    public Thread newThread(Runnable runnable) {
        String fullName = this.name + "-" + threadNumber.getAndIncrement();
        Thread thread = new Thread(this.group, runnable, fullName);
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (thread.getPriority() != 5) {
            thread.setPriority(5);
        }
        return thread;
    }
}
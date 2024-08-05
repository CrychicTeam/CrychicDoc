package info.journeymap.shaded.org.eclipse.jetty.util.thread;

import info.journeymap.shaded.org.eclipse.jetty.util.component.AbstractLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Dumpable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorScheduler extends AbstractLifeCycle implements Scheduler, Dumpable {

    private final String name;

    private final boolean daemon;

    private final ClassLoader classloader;

    private final ThreadGroup threadGroup;

    private volatile ScheduledThreadPoolExecutor scheduler;

    private volatile Thread thread;

    public ScheduledExecutorScheduler() {
        this(null, false);
    }

    public ScheduledExecutorScheduler(String name, boolean daemon) {
        this(name, daemon, Thread.currentThread().getContextClassLoader());
    }

    public ScheduledExecutorScheduler(String name, boolean daemon, ClassLoader threadFactoryClassLoader) {
        this(name, daemon, threadFactoryClassLoader, null);
    }

    public ScheduledExecutorScheduler(String name, boolean daemon, ClassLoader threadFactoryClassLoader, ThreadGroup threadGroup) {
        this.name = name == null ? "Scheduler-" + this.hashCode() : name;
        this.daemon = daemon;
        this.classloader = threadFactoryClassLoader == null ? Thread.currentThread().getContextClassLoader() : threadFactoryClassLoader;
        this.threadGroup = threadGroup;
    }

    @Override
    protected void doStart() throws Exception {
        this.scheduler = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {

            public Thread newThread(Runnable r) {
                Thread thread = ScheduledExecutorScheduler.this.thread = new Thread(ScheduledExecutorScheduler.this.threadGroup, r, ScheduledExecutorScheduler.this.name);
                thread.setDaemon(ScheduledExecutorScheduler.this.daemon);
                thread.setContextClassLoader(ScheduledExecutorScheduler.this.classloader);
                return thread;
            }
        });
        this.scheduler.setRemoveOnCancelPolicy(true);
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        this.scheduler.shutdownNow();
        super.doStop();
        this.scheduler = null;
    }

    @Override
    public Scheduler.Task schedule(Runnable task, long delay, TimeUnit unit) {
        ScheduledThreadPoolExecutor s = this.scheduler;
        if (s == null) {
            return new Scheduler.Task() {

                @Override
                public boolean cancel() {
                    return false;
                }
            };
        } else {
            ScheduledFuture<?> result = s.schedule(task, delay, unit);
            return new ScheduledExecutorScheduler.ScheduledFutureTask(result);
        }
    }

    @Override
    public String dump() {
        return ContainerLifeCycle.dump(this);
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        ContainerLifeCycle.dumpObject(out, this);
        Thread thread = this.thread;
        if (thread != null) {
            List<StackTraceElement> frames = Arrays.asList(thread.getStackTrace());
            ContainerLifeCycle.dump(out, indent, frames);
        }
    }

    private static class ScheduledFutureTask implements Scheduler.Task {

        private final ScheduledFuture<?> scheduledFuture;

        ScheduledFutureTask(ScheduledFuture<?> scheduledFuture) {
            this.scheduledFuture = scheduledFuture;
        }

        @Override
        public boolean cancel() {
            return this.scheduledFuture.cancel(false);
        }
    }
}
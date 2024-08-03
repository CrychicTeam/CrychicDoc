package info.journeymap.shaded.org.eclipse.jetty.util.thread;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.Closeable;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

public interface Invocable {

    ThreadLocal<Boolean> __nonBlocking = new ThreadLocal<Boolean>() {

        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    static boolean isNonBlockingInvocation() {
        return (Boolean) __nonBlocking.get();
    }

    static void invokeNonBlocking(Runnable task) {
        Boolean was_non_blocking = (Boolean) __nonBlocking.get();
        try {
            __nonBlocking.set(Boolean.TRUE);
            task.run();
        } finally {
            __nonBlocking.set(was_non_blocking);
        }
    }

    static void invokePreferNonBlocking(Runnable task) {
        switch(getInvocationType(task)) {
            case BLOCKING:
            case NON_BLOCKING:
                task.run();
                break;
            case EITHER:
                invokeNonBlocking(task);
        }
    }

    static void invokePreferred(Runnable task, Invocable.InvocationType preferredInvocationType) {
        switch(getInvocationType(task)) {
            case BLOCKING:
            case NON_BLOCKING:
                task.run();
                break;
            case EITHER:
                if (getInvocationType(task) == Invocable.InvocationType.EITHER && preferredInvocationType == Invocable.InvocationType.NON_BLOCKING) {
                    invokeNonBlocking(task);
                } else {
                    task.run();
                }
        }
    }

    static Runnable asPreferred(Runnable task, Invocable.InvocationType preferredInvocationType) {
        switch(getInvocationType(task)) {
            case EITHER:
                if (preferredInvocationType == Invocable.InvocationType.NON_BLOCKING) {
                    return () -> invokeNonBlocking(task);
                }
            case BLOCKING:
            case NON_BLOCKING:
            default:
                return task;
        }
    }

    static Invocable.InvocationType getInvocationType(Object o) {
        return o instanceof Invocable ? ((Invocable) o).getInvocationType() : Invocable.InvocationType.BLOCKING;
    }

    default Invocable.InvocationType getInvocationType() {
        return Invocable.InvocationType.BLOCKING;
    }

    public static class InvocableExecutor implements Executor {

        private static final Logger LOG = Log.getLogger(Invocable.InvocableExecutor.class);

        private final Executor _executor;

        private final Invocable.InvocationType _preferredInvocationForExecute;

        private final Invocable.InvocationType _preferredInvocationForInvoke;

        public InvocableExecutor(Executor executor, Invocable.InvocationType preferred) {
            this(executor, preferred, preferred);
        }

        public InvocableExecutor(Executor executor, Invocable.InvocationType preferredInvocationForExecute, Invocable.InvocationType preferredInvocationForIvoke) {
            this._executor = executor;
            this._preferredInvocationForExecute = preferredInvocationForExecute;
            this._preferredInvocationForInvoke = preferredInvocationForIvoke;
        }

        public Invocable.InvocationType getPreferredInvocationType() {
            return this._preferredInvocationForInvoke;
        }

        public void invoke(Runnable task) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("{} invoke  {}", this, task);
            }
            Invocable.invokePreferred(task, this._preferredInvocationForInvoke);
            if (LOG.isDebugEnabled()) {
                LOG.debug("{} invoked {}", this, task);
            }
        }

        public void execute(Runnable task) {
            this.tryExecute(task, this._preferredInvocationForExecute);
        }

        public void execute(Runnable task, Invocable.InvocationType preferred) {
            this.tryExecute(task, preferred);
        }

        public boolean tryExecute(Runnable task) {
            return this.tryExecute(task, this._preferredInvocationForExecute);
        }

        public boolean tryExecute(Runnable task, Invocable.InvocationType preferred) {
            try {
                this._executor.execute(Invocable.asPreferred(task, preferred));
                return true;
            } catch (RejectedExecutionException var6) {
                LOG.debug(var6);
                LOG.warn("Rejected execution of {}", task);
                try {
                    if (task instanceof Closeable) {
                        ((Closeable) task).close();
                    }
                } catch (Exception var5) {
                    var6.addSuppressed(var5);
                    LOG.warn(var6);
                }
                return false;
            }
        }
    }

    public static enum InvocationType {

        BLOCKING, NON_BLOCKING, EITHER
    }
}
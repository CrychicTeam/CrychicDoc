package icyllis.modernui.mc.testforge.trash;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Deprecated
public final class PluginLoader {

    private static PluginLoader sInstance;

    @Nonnull
    private final ExecutorService mParallelThreadPool;

    private PluginLoader(@Nullable ExecutorService parallelThreadPool) {
        if (parallelThreadPool == null) {
            this.mParallelThreadPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), pool -> {
                ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
                thread.setName("mui-loading-worker-" + thread.getPoolIndex());
                thread.setContextClassLoader(Thread.currentThread().getContextClassLoader());
                return thread;
            }, null, true);
        } else {
            this.mParallelThreadPool = parallelThreadPool;
        }
    }

    @Nonnull
    public static PluginLoader create(@Nullable ExecutorService parallel) {
        if (sInstance == null) {
            synchronized (PluginLoader.class) {
                if (sInstance == null) {
                    sInstance = new PluginLoader(parallel);
                }
            }
            return sInstance;
        } else {
            throw new IllegalStateException();
        }
    }

    @Nonnull
    public static PluginLoader get() {
        return sInstance;
    }
}
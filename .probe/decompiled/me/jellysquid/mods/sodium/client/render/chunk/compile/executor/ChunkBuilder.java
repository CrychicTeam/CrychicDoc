package me.jellysquid.mods.sodium.client.render.chunk.compile.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.compat.forge.ForgeBlockRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderTask;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.embeddedt.embeddium.impl.render.chunk.compile.GlobalChunkBuildContext;

public class ChunkBuilder {

    static final Logger LOGGER = LogManager.getLogger("ChunkBuilder");

    private static final int MBS_PER_CHUNK_BUILDER = 64;

    private static final int TASK_QUEUE_LIMIT_PER_WORKER = 2;

    private final ChunkJobQueue queue = new ChunkJobQueue();

    private final List<Thread> threads = new ArrayList();

    private final AtomicInteger busyThreadCount = new AtomicInteger();

    private final ChunkBuildContext localContext;

    public ChunkBuilder(ClientLevel world, ChunkVertexType vertexType) {
        GlobalChunkBuildContext.setMainThread();
        ForgeBlockRenderer.init();
        int count = getThreadCount();
        for (int i = 0; i < count; i++) {
            ChunkBuildContext context = new ChunkBuildContext(world, vertexType);
            ChunkBuilder.WorkerRunnable worker = new ChunkBuilder.WorkerRunnable(context);
            Thread thread = new ChunkBuilder.WorkerThread(worker, "Chunk Render Task Executor #" + i, context);
            thread.setPriority(Math.max(0, 3));
            thread.start();
            this.threads.add(thread);
        }
        LOGGER.info("Started {} worker threads", this.threads.size());
        this.localContext = new ChunkBuildContext(world, vertexType);
    }

    public int getSchedulingBudget() {
        return Math.max(0, this.threads.size() * 2 - this.queue.size());
    }

    public void shutdown() {
        if (!this.queue.isRunning()) {
            throw new IllegalStateException("Worker threads are not running");
        } else {
            for (ChunkJob job : this.queue.shutdown()) {
                job.setCancelled();
            }
            this.shutdownThreads();
        }
    }

    private void shutdownThreads() {
        LOGGER.info("Stopping worker threads");
        for (Thread thread : this.threads) {
            try {
                thread.join();
            } catch (InterruptedException var4) {
            }
        }
        this.threads.clear();
    }

    public <TASK extends ChunkBuilderTask<OUTPUT>, OUTPUT> ChunkJobTyped<TASK, OUTPUT> scheduleTask(TASK task, boolean important, Consumer<ChunkJobResult<OUTPUT>> consumer) {
        Validate.notNull(task, "Task must be non-null", new Object[0]);
        if (!this.queue.isRunning()) {
            throw new IllegalStateException("Executor is stopped");
        } else {
            ChunkJobTyped<TASK, OUTPUT> job = new ChunkJobTyped<>(task, consumer);
            this.queue.add(job, important);
            return job;
        }
    }

    private static int getOptimalThreadCount() {
        return Mth.clamp(Math.max(getMaxThreadCount() / 3, getMaxThreadCount() - 6), 1, 10);
    }

    private static int getThreadCount() {
        int requested = SodiumClientMod.options().performance.chunkBuilderThreads;
        return requested == 0 ? getOptimalThreadCount() : Math.min(requested, getMaxThreadCount());
    }

    public static int getMaxThreadCount() {
        int totalCores = Runtime.getRuntime().availableProcessors();
        long memoryMb = Runtime.getRuntime().maxMemory() / 1048576L;
        int maxBuilders = Math.max(1, (int) (memoryMb / 64L));
        return Math.min(totalCores, maxBuilders);
    }

    public void tryStealTask(ChunkJob job) {
        if (this.queue.stealJob(job)) {
            ChunkBuildContext localContext = this.localContext;
            GlobalChunkBuildContext.bindMainThread(localContext);
            try {
                job.execute(localContext);
            } finally {
                GlobalChunkBuildContext.bindMainThread(null);
                localContext.cleanup();
            }
        }
    }

    public boolean isBuildQueueEmpty() {
        return this.queue.isEmpty();
    }

    public int getScheduledJobCount() {
        return this.queue.size();
    }

    public int getBusyThreadCount() {
        return this.busyThreadCount.get();
    }

    public int getTotalThreadCount() {
        return this.threads.size();
    }

    private class WorkerRunnable implements Runnable {

        private final ChunkBuildContext context;

        public WorkerRunnable(ChunkBuildContext context) {
            this.context = context;
        }

        public void run() {
            while (ChunkBuilder.this.queue.isRunning()) {
                ChunkJob job;
                try {
                    job = ChunkBuilder.this.queue.waitForNextJob();
                } catch (InterruptedException var7) {
                    continue;
                }
                if (job != null) {
                    ChunkBuilder.this.busyThreadCount.getAndIncrement();
                    try {
                        job.execute(this.context);
                    } finally {
                        this.context.cleanup();
                        ChunkBuilder.this.busyThreadCount.decrementAndGet();
                    }
                }
            }
        }
    }

    private static class WorkerThread extends Thread implements GlobalChunkBuildContext.Holder {

        private final ChunkBuildContext context;

        public WorkerThread(Runnable runnable, String name, ChunkBuildContext context) {
            super(runnable, name);
            this.context = context;
        }

        @Override
        public ChunkBuildContext embeddium$getGlobalContext() {
            return this.context;
        }
    }
}
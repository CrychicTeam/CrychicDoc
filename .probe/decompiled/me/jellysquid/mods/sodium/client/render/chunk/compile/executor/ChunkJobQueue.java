package me.jellysquid.mods.sodium.client.render.chunk.compile.executor;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

class ChunkJobQueue {

    private final ConcurrentLinkedDeque<ChunkJob> jobs = new ConcurrentLinkedDeque();

    private final Semaphore semaphore = new Semaphore(0);

    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public boolean isRunning() {
        return this.isRunning.get();
    }

    public void add(ChunkJob job, boolean important) {
        Validate.isTrue(this.isRunning(), "Queue is no longer running", new Object[0]);
        if (important) {
            this.jobs.addFirst(job);
        } else {
            this.jobs.addLast(job);
        }
        this.semaphore.release(1);
    }

    @Nullable
    public ChunkJob waitForNextJob() throws InterruptedException {
        if (!this.isRunning()) {
            return null;
        } else {
            this.semaphore.acquire();
            return this.getNextTask();
        }
    }

    public boolean stealJob(ChunkJob job) {
        if (!this.semaphore.tryAcquire()) {
            return false;
        } else {
            boolean success = this.jobs.remove(job);
            if (!success) {
                this.semaphore.release(1);
            }
            return success;
        }
    }

    @Nullable
    private ChunkJob getNextTask() {
        return (ChunkJob) this.jobs.poll();
    }

    public Collection<ChunkJob> shutdown() {
        ArrayDeque<ChunkJob> list = new ArrayDeque();
        this.isRunning.set(false);
        while (this.semaphore.tryAcquire()) {
            ChunkJob task = (ChunkJob) this.jobs.poll();
            if (task != null) {
                list.add(task);
            }
        }
        this.semaphore.release(Runtime.getRuntime().availableProcessors());
        return list;
    }

    public int size() {
        return this.semaphore.availablePermits();
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }
}
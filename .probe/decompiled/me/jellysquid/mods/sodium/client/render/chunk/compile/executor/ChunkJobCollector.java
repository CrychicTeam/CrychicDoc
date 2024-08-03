package me.jellysquid.mods.sodium.client.render.chunk.compile.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;

public class ChunkJobCollector {

    private final Semaphore semaphore = new Semaphore(0);

    private final Consumer<ChunkJobResult<ChunkBuildOutput>> collector;

    private final List<ChunkJob> submitted = new ArrayList();

    private final int budget;

    public ChunkJobCollector(int budget, Consumer<ChunkJobResult<ChunkBuildOutput>> collector) {
        this.budget = budget;
        this.collector = collector;
    }

    public void onJobFinished(ChunkJobResult<ChunkBuildOutput> result) {
        this.semaphore.release(1);
        this.collector.accept(result);
    }

    public void awaitCompletion(ChunkBuilder builder) {
        if (this.submitted.size() != 0) {
            for (ChunkJob job : this.submitted) {
                if (!job.isStarted() && !job.isCancelled()) {
                    builder.tryStealTask(job);
                }
            }
            this.semaphore.acquireUninterruptibly(this.submitted.size());
        }
    }

    public void addSubmittedJob(ChunkJob job) {
        this.submitted.add(job);
    }

    public boolean canOffer() {
        return this.budget - this.submitted.size() > 0;
    }
}
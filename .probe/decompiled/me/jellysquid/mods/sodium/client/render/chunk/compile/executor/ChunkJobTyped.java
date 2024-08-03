package me.jellysquid.mods.sodium.client.render.chunk.compile.executor;

import java.util.function.Consumer;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderTask;

public class ChunkJobTyped<TASK extends ChunkBuilderTask<OUTPUT>, OUTPUT> implements ChunkJob {

    private final TASK task;

    private final Consumer<ChunkJobResult<OUTPUT>> consumer;

    private volatile boolean cancelled;

    private volatile boolean started;

    ChunkJobTyped(TASK task, Consumer<ChunkJobResult<OUTPUT>> consumer) {
        this.task = task;
        this.consumer = consumer;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled() {
        this.cancelled = true;
    }

    @Override
    public void execute(ChunkBuildContext context) {
        if (!this.cancelled) {
            this.started = true;
            ChunkJobResult<OUTPUT> result;
            try {
                OUTPUT output = this.task.execute(context, this);
                if (output == null) {
                    return;
                }
                result = ChunkJobResult.successfully(output);
            } catch (Throwable var5) {
                result = ChunkJobResult.exceptionally(var5);
                ChunkBuilder.LOGGER.error("Chunk build failed", var5);
            }
            try {
                this.consumer.accept(result);
            } catch (Throwable var4) {
                throw new RuntimeException("Exception while consuming result", var4);
            }
        }
    }

    @Override
    public boolean isStarted() {
        return this.started;
    }
}
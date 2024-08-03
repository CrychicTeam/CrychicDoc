package net.minecraft.server.level.progress;

import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import net.minecraft.util.thread.ProcessorMailbox;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkStatus;

public class ProcessorChunkProgressListener implements ChunkProgressListener {

    private final ChunkProgressListener delegate;

    private final ProcessorMailbox<Runnable> mailbox;

    private ProcessorChunkProgressListener(ChunkProgressListener chunkProgressListener0, Executor executor1) {
        this.delegate = chunkProgressListener0;
        this.mailbox = ProcessorMailbox.create(executor1, "progressListener");
    }

    public static ProcessorChunkProgressListener createStarted(ChunkProgressListener chunkProgressListener0, Executor executor1) {
        ProcessorChunkProgressListener $$2 = new ProcessorChunkProgressListener(chunkProgressListener0, executor1);
        $$2.start();
        return $$2;
    }

    @Override
    public void updateSpawnPos(ChunkPos chunkPos0) {
        this.mailbox.tell(() -> this.delegate.updateSpawnPos(chunkPos0));
    }

    @Override
    public void onStatusChange(ChunkPos chunkPos0, @Nullable ChunkStatus chunkStatus1) {
        this.mailbox.tell(() -> this.delegate.onStatusChange(chunkPos0, chunkStatus1));
    }

    @Override
    public void start() {
        this.mailbox.tell(this.delegate::m_9662_);
    }

    @Override
    public void stop() {
        this.mailbox.tell(this.delegate::m_7646_);
    }
}
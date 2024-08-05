package journeymap.common.nbt.cache;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Unit;
import net.minecraft.util.thread.ProcessorMailbox;
import net.minecraft.util.thread.StrictQueue;
import net.minecraft.world.level.ChunkPos;

public class CacheWorker implements AutoCloseable {

    private final AtomicBoolean shutdownRequested = new AtomicBoolean();

    private final ProcessorMailbox<StrictQueue.IntRunnable> mailbox;

    private final CacheFileStorage storage;

    private final Map<ChunkPos, CacheWorker.PendingStore> pendingWrites = Maps.newLinkedHashMap();

    protected CacheWorker(Path path, boolean async) {
        this.storage = new CacheFileStorage(path, async);
        this.mailbox = new ProcessorMailbox<>(new StrictQueue.FixedPriorityQueue(CacheWorker.Priority.values().length), Util.ioPool(), "JM-Cache");
    }

    public CompletableFuture<Void> store(ChunkPos chunkPos, @Nullable CompoundTag tag) {
        return this.submitTask(() -> {
            CacheWorker.PendingStore pendingStore = (CacheWorker.PendingStore) this.pendingWrites.computeIfAbsent(chunkPos, func -> new CacheWorker.PendingStore(tag));
            pendingStore.data = tag;
            return Either.left(pendingStore.result);
        }).thenCompose(Function.identity());
    }

    @Nullable
    public CompoundTag load(ChunkPos chunkPos) throws IOException {
        CompletableFuture<CompoundTag> future = this.loadAsync(chunkPos);
        try {
            return (CompoundTag) future.join();
        } catch (CompletionException var4) {
            if (var4.getCause() instanceof IOException) {
                throw (IOException) var4.getCause();
            } else {
                throw var4;
            }
        }
    }

    protected CompletableFuture<CompoundTag> loadAsync(ChunkPos chunkPos) {
        return this.submitTask(() -> {
            CacheWorker.PendingStore store = (CacheWorker.PendingStore) this.pendingWrites.get(chunkPos);
            if (store != null) {
                return Either.left(store.data);
            } else {
                try {
                    CompoundTag compoundtag = this.storage.read(chunkPos);
                    return Either.left(compoundtag);
                } catch (Exception var4) {
                    return Either.right(var4);
                }
            }
        });
    }

    public CompletableFuture<Void> synchronize(boolean sync) {
        CompletableFuture<Void> future = this.submitTask(() -> Either.left(CompletableFuture.allOf((CompletableFuture[]) this.pendingWrites.values().stream().map(pendingStore -> pendingStore.result).toArray(CompletableFuture[]::new)))).thenCompose(Function.identity());
        return sync ? future.thenCompose(func -> this.submitTask(() -> {
            try {
                this.storage.flush();
                return Either.left(null);
            } catch (Exception var2x) {
                return Either.right(var2x);
            }
        })) : future.thenCompose(func -> this.submitTask(() -> Either.left(null)));
    }

    private <T> CompletableFuture<T> submitTask(Supplier<Either<T, Exception>> supplier) {
        return this.mailbox.m_18722_(processorHandle -> new StrictQueue.IntRunnable(CacheWorker.Priority.FOREGROUND.ordinal(), () -> {
            if (!this.shutdownRequested.get()) {
                processorHandle.tell((Either) supplier.get());
            }
            this.tellStorePending();
        }));
    }

    private void storePendingChunk() {
        if (!this.pendingWrites.isEmpty()) {
            Iterator<Entry<ChunkPos, CacheWorker.PendingStore>> iterator = this.pendingWrites.entrySet().iterator();
            Entry<ChunkPos, CacheWorker.PendingStore> entry = (Entry<ChunkPos, CacheWorker.PendingStore>) iterator.next();
            iterator.remove();
            this.runStore((ChunkPos) entry.getKey(), (CacheWorker.PendingStore) entry.getValue());
            this.tellStorePending();
        }
    }

    private void tellStorePending() {
        this.mailbox.tell(new StrictQueue.IntRunnable(CacheWorker.Priority.BACKGROUND.ordinal(), this::storePendingChunk));
    }

    private void runStore(ChunkPos pos, CacheWorker.PendingStore store) {
        try {
            this.storage.write(pos, store.data);
            store.result.complete(null);
        } catch (Exception var4) {
            store.result.completeExceptionally(var4);
        }
    }

    public void close() throws IOException {
        if (this.shutdownRequested.compareAndSet(false, true)) {
            this.mailbox.m_18720_(processorHandle -> new StrictQueue.IntRunnable(CacheWorker.Priority.SHUTDOWN.ordinal(), () -> processorHandle.tell(Unit.INSTANCE))).join();
            this.mailbox.close();
            try {
                this.storage.close();
            } catch (Exception var2) {
            }
        }
    }

    static class PendingStore {

        @Nullable
        CompoundTag data;

        final CompletableFuture<Void> result = new CompletableFuture();

        public PendingStore(@Nullable CompoundTag data) {
            this.data = data;
        }
    }

    static enum Priority {

        FOREGROUND, BACKGROUND, SHUTDOWN
    }
}
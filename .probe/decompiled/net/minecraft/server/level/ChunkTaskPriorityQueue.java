package net.minecraft.server.level;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.world.level.ChunkPos;

public class ChunkTaskPriorityQueue<T> {

    public static final int PRIORITY_LEVEL_COUNT = ChunkLevel.MAX_LEVEL + 2;

    private final List<Long2ObjectLinkedOpenHashMap<List<Optional<T>>>> taskQueue = (List<Long2ObjectLinkedOpenHashMap<List<Optional<T>>>>) IntStream.range(0, PRIORITY_LEVEL_COUNT).mapToObj(p_140520_ -> new Long2ObjectLinkedOpenHashMap()).collect(Collectors.toList());

    private volatile int firstQueue = PRIORITY_LEVEL_COUNT;

    private final String name;

    private final LongSet acquired = new LongOpenHashSet();

    private final int maxTasks;

    public ChunkTaskPriorityQueue(String string0, int int1) {
        this.name = string0;
        this.maxTasks = int1;
    }

    protected void resortChunkTasks(int int0, ChunkPos chunkPos1, int int2) {
        if (int0 < PRIORITY_LEVEL_COUNT) {
            Long2ObjectLinkedOpenHashMap<List<Optional<T>>> $$3 = (Long2ObjectLinkedOpenHashMap<List<Optional<T>>>) this.taskQueue.get(int0);
            List<Optional<T>> $$4 = (List<Optional<T>>) $$3.remove(chunkPos1.toLong());
            if (int0 == this.firstQueue) {
                while (this.hasWork() && ((Long2ObjectLinkedOpenHashMap) this.taskQueue.get(this.firstQueue)).isEmpty()) {
                    this.firstQueue++;
                }
            }
            if ($$4 != null && !$$4.isEmpty()) {
                ((List) ((Long2ObjectLinkedOpenHashMap) this.taskQueue.get(int2)).computeIfAbsent(chunkPos1.toLong(), p_140547_ -> Lists.newArrayList())).addAll($$4);
                this.firstQueue = Math.min(this.firstQueue, int2);
            }
        }
    }

    protected void submit(Optional<T> optionalT0, long long1, int int2) {
        ((List) ((Long2ObjectLinkedOpenHashMap) this.taskQueue.get(int2)).computeIfAbsent(long1, p_140545_ -> Lists.newArrayList())).add(optionalT0);
        this.firstQueue = Math.min(this.firstQueue, int2);
    }

    protected void release(long long0, boolean boolean1) {
        for (Long2ObjectLinkedOpenHashMap<List<Optional<T>>> $$2 : this.taskQueue) {
            List<Optional<T>> $$3 = (List<Optional<T>>) $$2.get(long0);
            if ($$3 != null) {
                if (boolean1) {
                    $$3.clear();
                } else {
                    $$3.removeIf(p_140534_ -> !p_140534_.isPresent());
                }
                if ($$3.isEmpty()) {
                    $$2.remove(long0);
                }
            }
        }
        while (this.hasWork() && ((Long2ObjectLinkedOpenHashMap) this.taskQueue.get(this.firstQueue)).isEmpty()) {
            this.firstQueue++;
        }
        this.acquired.remove(long0);
    }

    private Runnable acquire(long long0) {
        return () -> this.acquired.add(long0);
    }

    @Nullable
    public Stream<Either<T, Runnable>> pop() {
        if (this.acquired.size() >= this.maxTasks) {
            return null;
        } else if (!this.hasWork()) {
            return null;
        } else {
            int $$0 = this.firstQueue;
            Long2ObjectLinkedOpenHashMap<List<Optional<T>>> $$1 = (Long2ObjectLinkedOpenHashMap<List<Optional<T>>>) this.taskQueue.get($$0);
            long $$2 = $$1.firstLongKey();
            List<Optional<T>> $$3 = (List<Optional<T>>) $$1.removeFirst();
            while (this.hasWork() && ((Long2ObjectLinkedOpenHashMap) this.taskQueue.get(this.firstQueue)).isEmpty()) {
                this.firstQueue++;
            }
            return $$3.stream().map(p_140529_ -> (Either) p_140529_.map(Either::left).orElseGet(() -> Either.right(this.acquire($$2))));
        }
    }

    public boolean hasWork() {
        return this.firstQueue < PRIORITY_LEVEL_COUNT;
    }

    public String toString() {
        return this.name + " " + this.firstQueue + "...";
    }

    @VisibleForTesting
    LongSet getAcquired() {
        return new LongOpenHashSet(this.acquired);
    }
}
package net.minecraft.world.ticks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.ChunkPos;

public class LevelChunkTicks<T> implements SerializableTickContainer<T>, TickContainerAccess<T> {

    private final Queue<ScheduledTick<T>> tickQueue = new PriorityQueue(ScheduledTick.DRAIN_ORDER);

    @Nullable
    private List<SavedTick<T>> pendingTicks;

    private final Set<ScheduledTick<?>> ticksPerPosition = new ObjectOpenCustomHashSet(ScheduledTick.UNIQUE_TICK_HASH);

    @Nullable
    private BiConsumer<LevelChunkTicks<T>, ScheduledTick<T>> onTickAdded;

    public LevelChunkTicks() {
    }

    public LevelChunkTicks(List<SavedTick<T>> listSavedTickT0) {
        this.pendingTicks = listSavedTickT0;
        for (SavedTick<T> $$1 : listSavedTickT0) {
            this.ticksPerPosition.add(ScheduledTick.probe($$1.type(), $$1.pos()));
        }
    }

    public void setOnTickAdded(@Nullable BiConsumer<LevelChunkTicks<T>, ScheduledTick<T>> biConsumerLevelChunkTicksTScheduledTickT0) {
        this.onTickAdded = biConsumerLevelChunkTicksTScheduledTickT0;
    }

    @Nullable
    public ScheduledTick<T> peek() {
        return (ScheduledTick<T>) this.tickQueue.peek();
    }

    @Nullable
    public ScheduledTick<T> poll() {
        ScheduledTick<T> $$0 = (ScheduledTick<T>) this.tickQueue.poll();
        if ($$0 != null) {
            this.ticksPerPosition.remove($$0);
        }
        return $$0;
    }

    @Override
    public void schedule(ScheduledTick<T> scheduledTickT0) {
        if (this.ticksPerPosition.add(scheduledTickT0)) {
            this.scheduleUnchecked(scheduledTickT0);
        }
    }

    private void scheduleUnchecked(ScheduledTick<T> scheduledTickT0) {
        this.tickQueue.add(scheduledTickT0);
        if (this.onTickAdded != null) {
            this.onTickAdded.accept(this, scheduledTickT0);
        }
    }

    @Override
    public boolean hasScheduledTick(BlockPos blockPos0, T t1) {
        return this.ticksPerPosition.contains(ScheduledTick.probe(t1, blockPos0));
    }

    public void removeIf(Predicate<ScheduledTick<T>> predicateScheduledTickT0) {
        Iterator<ScheduledTick<T>> $$1 = this.tickQueue.iterator();
        while ($$1.hasNext()) {
            ScheduledTick<T> $$2 = (ScheduledTick<T>) $$1.next();
            if (predicateScheduledTickT0.test($$2)) {
                $$1.remove();
                this.ticksPerPosition.remove($$2);
            }
        }
    }

    public Stream<ScheduledTick<T>> getAll() {
        return this.tickQueue.stream();
    }

    @Override
    public int count() {
        return this.tickQueue.size() + (this.pendingTicks != null ? this.pendingTicks.size() : 0);
    }

    public ListTag save(long long0, Function<T, String> functionTString1) {
        ListTag $$2 = new ListTag();
        if (this.pendingTicks != null) {
            for (SavedTick<T> $$3 : this.pendingTicks) {
                $$2.add($$3.save(functionTString1));
            }
        }
        for (ScheduledTick<T> $$4 : this.tickQueue) {
            $$2.add(SavedTick.saveTick($$4, functionTString1, long0));
        }
        return $$2;
    }

    public void unpack(long long0) {
        if (this.pendingTicks != null) {
            int $$1 = -this.pendingTicks.size();
            for (SavedTick<T> $$2 : this.pendingTicks) {
                this.scheduleUnchecked($$2.unpack(long0, (long) ($$1++)));
            }
        }
        this.pendingTicks = null;
    }

    public static <T> LevelChunkTicks<T> load(ListTag listTag0, Function<String, Optional<T>> functionStringOptionalT1, ChunkPos chunkPos2) {
        Builder<SavedTick<T>> $$3 = ImmutableList.builder();
        SavedTick.loadTickList(listTag0, functionStringOptionalT1, chunkPos2, $$3::add);
        return new LevelChunkTicks<>($$3.build());
    }
}
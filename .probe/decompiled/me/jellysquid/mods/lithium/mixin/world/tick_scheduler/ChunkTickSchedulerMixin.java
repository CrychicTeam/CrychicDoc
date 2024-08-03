package me.jellysquid.mods.lithium.mixin.world.tick_scheduler;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.longs.Long2ReferenceAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import me.jellysquid.mods.lithium.common.world.scheduler.OrderedTickQueue;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.SavedTick;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LevelChunkTicks.class })
public class ChunkTickSchedulerMixin<T> {

    private static volatile Reference2IntOpenHashMap<Object> TYPE_2_INDEX = new Reference2IntOpenHashMap();

    private final Long2ReferenceAVLTreeMap<OrderedTickQueue<T>> tickQueuesByTimeAndPriority = new Long2ReferenceAVLTreeMap();

    private OrderedTickQueue<T> nextTickQueue;

    private final IntOpenHashSet allTicks = new IntOpenHashSet();

    @Shadow
    @Nullable
    private BiConsumer<LevelChunkTicks<T>, ScheduledTick<T>> onTickAdded;

    @Mutable
    @Shadow
    @Final
    private Set<ScheduledTick<?>> ticksPerPosition;

    @Shadow
    @Nullable
    private List<SavedTick<T>> pendingTicks;

    @Mutable
    @Shadow
    @Final
    private Queue<ScheduledTick<T>> tickQueue;

    @Inject(method = { "<init>()V", "<init>(Ljava/util/List;)V" }, at = { @At("RETURN") })
    private void reinit(CallbackInfo ci) {
        if (this.pendingTicks != null) {
            for (SavedTick<?> orderedTick : this.pendingTicks) {
                this.allTicks.add(tickToInt(orderedTick.pos(), orderedTick.type()));
            }
        }
        this.ticksPerPosition = null;
        this.tickQueue = null;
    }

    private static int tickToInt(BlockPos pos, Object type) {
        int typeIndex = TYPE_2_INDEX.getInt(type);
        if (typeIndex == -1) {
            typeIndex = fixMissingType2Index(type);
        }
        int ret = (pos.m_123341_() & 15) << 16 | (pos.m_123342_() & 4095) << 4 | pos.m_123343_() & 15;
        return ret | typeIndex << 20;
    }

    private static synchronized int fixMissingType2Index(Object type) {
        int typeIndex = TYPE_2_INDEX.getInt(type);
        if (typeIndex == -1) {
            Reference2IntOpenHashMap<Object> clonedType2Index = TYPE_2_INDEX.clone();
            clonedType2Index.put(type, typeIndex = clonedType2Index.size());
            TYPE_2_INDEX = clonedType2Index;
            if (typeIndex >= 4096) {
                throw new IllegalStateException("Lithium Tick Scheduler assumes at most 4096 different block types that receive scheduled ticks exist! Add mixin.world.tick_scheduler=false to the lithium properties/config to disable the optimization!");
            }
        }
        return typeIndex;
    }

    @Overwrite
    public void schedule(ScheduledTick<T> orderedTick) {
        int intTick = tickToInt(orderedTick.pos(), orderedTick.type());
        if (this.allTicks.add(intTick)) {
            this.queueTick(orderedTick);
        }
    }

    private static long getBucketKey(long time, TickPriority priority) {
        return time << 4 | (long) (priority.ordinal() & 15);
    }

    private void updateNextTickQueue(boolean checkEmpty) {
        if (checkEmpty && this.nextTickQueue != null && this.nextTickQueue.isEmpty()) {
            OrderedTickQueue<T> removed = (OrderedTickQueue<T>) this.tickQueuesByTimeAndPriority.remove(this.tickQueuesByTimeAndPriority.firstLongKey());
            if (removed != this.nextTickQueue) {
                throw new IllegalStateException("Next tick queue doesn't have the lowest key!");
            }
        }
        if (this.tickQueuesByTimeAndPriority.isEmpty()) {
            this.nextTickQueue = null;
        } else {
            long firstKey = this.tickQueuesByTimeAndPriority.firstLongKey();
            this.nextTickQueue = (OrderedTickQueue<T>) this.tickQueuesByTimeAndPriority.get(firstKey);
        }
    }

    @Overwrite
    @Nullable
    public ScheduledTick<T> peek() {
        return this.nextTickQueue == null ? null : this.nextTickQueue.peek();
    }

    @Overwrite
    @Nullable
    public ScheduledTick<T> poll() {
        ScheduledTick<T> orderedTick = this.nextTickQueue.poll();
        if (orderedTick != null) {
            if (this.nextTickQueue.isEmpty()) {
                this.updateNextTickQueue(true);
            }
            this.allTicks.remove(tickToInt(orderedTick.pos(), orderedTick.type()));
            return orderedTick;
        } else {
            return null;
        }
    }

    private void queueTick(ScheduledTick<T> orderedTick) {
        OrderedTickQueue<T> tickQueue = (OrderedTickQueue<T>) this.tickQueuesByTimeAndPriority.computeIfAbsent(getBucketKey(orderedTick.triggerTick(), orderedTick.priority()), key -> new OrderedTickQueue());
        if (tickQueue.isEmpty()) {
            this.updateNextTickQueue(false);
        }
        tickQueue.offer(orderedTick);
        if (this.onTickAdded != null) {
            this.onTickAdded.accept(this, orderedTick);
        }
    }

    @Overwrite
    public boolean hasScheduledTick(BlockPos pos, T type) {
        return this.allTicks.contains(tickToInt(pos, type));
    }

    @Overwrite
    public void removeIf(Predicate<ScheduledTick<T>> predicate) {
        ObjectIterator<OrderedTickQueue<T>> tickQueueIterator = this.tickQueuesByTimeAndPriority.values().iterator();
        while (tickQueueIterator.hasNext()) {
            OrderedTickQueue<T> nextTickQueue = (OrderedTickQueue<T>) tickQueueIterator.next();
            nextTickQueue.sort();
            boolean removed = false;
            for (int i = 0; i < nextTickQueue.size(); i++) {
                ScheduledTick<T> nextTick = nextTickQueue.getTickAtIndex(i);
                if (predicate.test(nextTick)) {
                    nextTickQueue.setTickAtIndex(i, null);
                    this.allTicks.remove(tickToInt(nextTick.pos(), nextTick.type()));
                    removed = true;
                }
            }
            if (removed) {
                nextTickQueue.removeNullsAndConsumed();
            }
            if (nextTickQueue.isEmpty()) {
                tickQueueIterator.remove();
            }
        }
        this.updateNextTickQueue(false);
    }

    @Overwrite
    public Stream<ScheduledTick<T>> getAll() {
        return this.tickQueuesByTimeAndPriority.values().stream().flatMap(Collection::stream);
    }

    @Overwrite
    public int count() {
        return this.allTicks.size();
    }

    @Overwrite
    public ListTag save(long l, Function<T, String> function) {
        ListTag nbtList = new ListTag();
        if (this.pendingTicks != null) {
            for (SavedTick<T> tick : this.pendingTicks) {
                nbtList.add(tick.save(function));
            }
        }
        ObjectIterator var9 = this.tickQueuesByTimeAndPriority.values().iterator();
        while (var9.hasNext()) {
            OrderedTickQueue<T> nextTickQueue = (OrderedTickQueue<T>) var9.next();
            for (ScheduledTick<T> orderedTick : nextTickQueue) {
                nbtList.add(SavedTick.saveTick(orderedTick, function, l));
            }
        }
        return nbtList;
    }

    @Overwrite
    public void unpack(long time) {
        if (this.pendingTicks != null) {
            int i = -this.pendingTicks.size();
            for (SavedTick<T> tick : this.pendingTicks) {
                this.queueTick(tick.unpack(time, (long) (i++)));
            }
        }
        this.pendingTicks = null;
    }

    static {
        TYPE_2_INDEX.defaultReturnValue(-1);
    }
}
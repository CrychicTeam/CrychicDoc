package net.minecraft.world.ticks;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongMaps;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class LevelTicks<T> implements LevelTickAccess<T> {

    private static final Comparator<LevelChunkTicks<?>> CONTAINER_DRAIN_ORDER = (p_193246_, p_193247_) -> ScheduledTick.INTRA_TICK_DRAIN_ORDER.compare(p_193246_.peek(), p_193247_.peek());

    private final LongPredicate tickCheck;

    private final Supplier<ProfilerFiller> profiler;

    private final Long2ObjectMap<LevelChunkTicks<T>> allContainers = new Long2ObjectOpenHashMap();

    private final Long2LongMap nextTickForContainer = Util.make(new Long2LongOpenHashMap(), p_193262_ -> p_193262_.defaultReturnValue(Long.MAX_VALUE));

    private final Queue<LevelChunkTicks<T>> containersToTick = new PriorityQueue(CONTAINER_DRAIN_ORDER);

    private final Queue<ScheduledTick<T>> toRunThisTick = new ArrayDeque();

    private final List<ScheduledTick<T>> alreadyRunThisTick = new ArrayList();

    private final Set<ScheduledTick<?>> toRunThisTickSet = new ObjectOpenCustomHashSet(ScheduledTick.UNIQUE_TICK_HASH);

    private final BiConsumer<LevelChunkTicks<T>, ScheduledTick<T>> chunkScheduleUpdater = (p_193249_, p_193250_) -> {
        if (p_193250_.equals(p_193249_.peek())) {
            this.updateContainerScheduling(p_193250_);
        }
    };

    public LevelTicks(LongPredicate longPredicate0, Supplier<ProfilerFiller> supplierProfilerFiller1) {
        this.tickCheck = longPredicate0;
        this.profiler = supplierProfilerFiller1;
    }

    public void addContainer(ChunkPos chunkPos0, LevelChunkTicks<T> levelChunkTicksT1) {
        long $$2 = chunkPos0.toLong();
        this.allContainers.put($$2, levelChunkTicksT1);
        ScheduledTick<T> $$3 = levelChunkTicksT1.peek();
        if ($$3 != null) {
            this.nextTickForContainer.put($$2, $$3.triggerTick());
        }
        levelChunkTicksT1.setOnTickAdded(this.chunkScheduleUpdater);
    }

    public void removeContainer(ChunkPos chunkPos0) {
        long $$1 = chunkPos0.toLong();
        LevelChunkTicks<T> $$2 = (LevelChunkTicks<T>) this.allContainers.remove($$1);
        this.nextTickForContainer.remove($$1);
        if ($$2 != null) {
            $$2.setOnTickAdded(null);
        }
    }

    @Override
    public void schedule(ScheduledTick<T> scheduledTickT0) {
        long $$1 = ChunkPos.asLong(scheduledTickT0.pos());
        LevelChunkTicks<T> $$2 = (LevelChunkTicks<T>) this.allContainers.get($$1);
        if ($$2 == null) {
            Util.pauseInIde((T) (new IllegalStateException("Trying to schedule tick in not loaded position " + scheduledTickT0.pos())));
        } else {
            $$2.schedule(scheduledTickT0);
        }
    }

    public void tick(long long0, int int1, BiConsumer<BlockPos, T> biConsumerBlockPosT2) {
        ProfilerFiller $$3 = (ProfilerFiller) this.profiler.get();
        $$3.push("collect");
        this.collectTicks(long0, int1, $$3);
        $$3.popPush("run");
        $$3.incrementCounter("ticksToRun", this.toRunThisTick.size());
        this.runCollectedTicks(biConsumerBlockPosT2);
        $$3.popPush("cleanup");
        this.cleanupAfterTick();
        $$3.pop();
    }

    private void collectTicks(long long0, int int1, ProfilerFiller profilerFiller2) {
        this.sortContainersToTick(long0);
        profilerFiller2.incrementCounter("containersToTick", this.containersToTick.size());
        this.drainContainers(long0, int1);
        this.rescheduleLeftoverContainers();
    }

    private void sortContainersToTick(long long0) {
        ObjectIterator<Entry> $$1 = Long2LongMaps.fastIterator(this.nextTickForContainer);
        while ($$1.hasNext()) {
            Entry $$2 = (Entry) $$1.next();
            long $$3 = $$2.getLongKey();
            long $$4 = $$2.getLongValue();
            if ($$4 <= long0) {
                LevelChunkTicks<T> $$5 = (LevelChunkTicks<T>) this.allContainers.get($$3);
                if ($$5 == null) {
                    $$1.remove();
                } else {
                    ScheduledTick<T> $$6 = $$5.peek();
                    if ($$6 == null) {
                        $$1.remove();
                    } else if ($$6.triggerTick() > long0) {
                        $$2.setValue($$6.triggerTick());
                    } else if (this.tickCheck.test($$3)) {
                        $$1.remove();
                        this.containersToTick.add($$5);
                    }
                }
            }
        }
    }

    private void drainContainers(long long0, int int1) {
        LevelChunkTicks<T> $$2;
        while (this.canScheduleMoreTicks(int1) && ($$2 = (LevelChunkTicks<T>) this.containersToTick.poll()) != null) {
            ScheduledTick<T> $$3 = $$2.poll();
            this.scheduleForThisTick($$3);
            this.drainFromCurrentContainer(this.containersToTick, $$2, long0, int1);
            ScheduledTick<T> $$4 = $$2.peek();
            if ($$4 != null) {
                if ($$4.triggerTick() <= long0 && this.canScheduleMoreTicks(int1)) {
                    this.containersToTick.add($$2);
                } else {
                    this.updateContainerScheduling($$4);
                }
            }
        }
    }

    private void rescheduleLeftoverContainers() {
        for (LevelChunkTicks<T> $$0 : this.containersToTick) {
            this.updateContainerScheduling($$0.peek());
        }
    }

    private void updateContainerScheduling(ScheduledTick<T> scheduledTickT0) {
        this.nextTickForContainer.put(ChunkPos.asLong(scheduledTickT0.pos()), scheduledTickT0.triggerTick());
    }

    private void drainFromCurrentContainer(Queue<LevelChunkTicks<T>> queueLevelChunkTicksT0, LevelChunkTicks<T> levelChunkTicksT1, long long2, int int3) {
        if (this.canScheduleMoreTicks(int3)) {
            LevelChunkTicks<T> $$4 = (LevelChunkTicks<T>) queueLevelChunkTicksT0.peek();
            ScheduledTick<T> $$5 = $$4 != null ? $$4.peek() : null;
            while (this.canScheduleMoreTicks(int3)) {
                ScheduledTick<T> $$6 = levelChunkTicksT1.peek();
                if ($$6 == null || $$6.triggerTick() > long2 || $$5 != null && ScheduledTick.INTRA_TICK_DRAIN_ORDER.compare($$6, $$5) > 0) {
                    break;
                }
                levelChunkTicksT1.poll();
                this.scheduleForThisTick($$6);
            }
        }
    }

    private void scheduleForThisTick(ScheduledTick<T> scheduledTickT0) {
        this.toRunThisTick.add(scheduledTickT0);
    }

    private boolean canScheduleMoreTicks(int int0) {
        return this.toRunThisTick.size() < int0;
    }

    private void runCollectedTicks(BiConsumer<BlockPos, T> biConsumerBlockPosT0) {
        while (!this.toRunThisTick.isEmpty()) {
            ScheduledTick<T> $$1 = (ScheduledTick<T>) this.toRunThisTick.poll();
            if (!this.toRunThisTickSet.isEmpty()) {
                this.toRunThisTickSet.remove($$1);
            }
            this.alreadyRunThisTick.add($$1);
            biConsumerBlockPosT0.accept($$1.pos(), $$1.type());
        }
    }

    private void cleanupAfterTick() {
        this.toRunThisTick.clear();
        this.containersToTick.clear();
        this.alreadyRunThisTick.clear();
        this.toRunThisTickSet.clear();
    }

    @Override
    public boolean hasScheduledTick(BlockPos blockPos0, T t1) {
        LevelChunkTicks<T> $$2 = (LevelChunkTicks<T>) this.allContainers.get(ChunkPos.asLong(blockPos0));
        return $$2 != null && $$2.hasScheduledTick(blockPos0, t1);
    }

    @Override
    public boolean willTickThisTick(BlockPos blockPos0, T t1) {
        this.calculateTickSetIfNeeded();
        return this.toRunThisTickSet.contains(ScheduledTick.probe(t1, blockPos0));
    }

    private void calculateTickSetIfNeeded() {
        if (this.toRunThisTickSet.isEmpty() && !this.toRunThisTick.isEmpty()) {
            this.toRunThisTickSet.addAll(this.toRunThisTick);
        }
    }

    private void forContainersInArea(BoundingBox boundingBox0, LevelTicks.PosAndContainerConsumer<T> levelTicksPosAndContainerConsumerT1) {
        int $$2 = SectionPos.posToSectionCoord((double) boundingBox0.minX());
        int $$3 = SectionPos.posToSectionCoord((double) boundingBox0.minZ());
        int $$4 = SectionPos.posToSectionCoord((double) boundingBox0.maxX());
        int $$5 = SectionPos.posToSectionCoord((double) boundingBox0.maxZ());
        for (int $$6 = $$2; $$6 <= $$4; $$6++) {
            for (int $$7 = $$3; $$7 <= $$5; $$7++) {
                long $$8 = ChunkPos.asLong($$6, $$7);
                LevelChunkTicks<T> $$9 = (LevelChunkTicks<T>) this.allContainers.get($$8);
                if ($$9 != null) {
                    levelTicksPosAndContainerConsumerT1.accept($$8, $$9);
                }
            }
        }
    }

    public void clearArea(BoundingBox boundingBox0) {
        Predicate<ScheduledTick<T>> $$1 = p_193241_ -> boundingBox0.isInside(p_193241_.pos());
        this.forContainersInArea(boundingBox0, (p_193276_, p_193277_) -> {
            ScheduledTick<T> $$3 = p_193277_.peek();
            p_193277_.removeIf($$1);
            ScheduledTick<T> $$4 = p_193277_.peek();
            if ($$4 != $$3) {
                if ($$4 != null) {
                    this.updateContainerScheduling($$4);
                } else {
                    this.nextTickForContainer.remove(p_193276_);
                }
            }
        });
        this.alreadyRunThisTick.removeIf($$1);
        this.toRunThisTick.removeIf($$1);
    }

    public void copyArea(BoundingBox boundingBox0, Vec3i vecI1) {
        this.copyAreaFrom(this, boundingBox0, vecI1);
    }

    public void copyAreaFrom(LevelTicks<T> levelTicksT0, BoundingBox boundingBox1, Vec3i vecI2) {
        List<ScheduledTick<T>> $$3 = new ArrayList();
        Predicate<ScheduledTick<T>> $$4 = p_200922_ -> boundingBox1.isInside(p_200922_.pos());
        levelTicksT0.alreadyRunThisTick.stream().filter($$4).forEach($$3::add);
        levelTicksT0.toRunThisTick.stream().filter($$4).forEach($$3::add);
        levelTicksT0.forContainersInArea(boundingBox1, (p_200931_, p_200932_) -> p_200932_.getAll().filter($$4).forEach($$3::add));
        LongSummaryStatistics $$5 = $$3.stream().mapToLong(ScheduledTick::f_193380_).summaryStatistics();
        long $$6 = $$5.getMin();
        long $$7 = $$5.getMax();
        $$3.forEach(p_193260_ -> this.schedule(new ScheduledTick<>((T) p_193260_.type(), p_193260_.pos().offset(vecI2), p_193260_.triggerTick(), p_193260_.priority(), p_193260_.subTickOrder() - $$6 + $$7 + 1L)));
    }

    @Override
    public int count() {
        return this.allContainers.values().stream().mapToInt(TickAccess::m_183574_).sum();
    }

    @FunctionalInterface
    interface PosAndContainerConsumer<T> {

        void accept(long var1, LevelChunkTicks<T> var3);
    }
}
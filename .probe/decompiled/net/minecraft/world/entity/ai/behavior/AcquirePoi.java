package net.minecraft.world.entity.ai.behavior;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.commons.lang3.mutable.MutableLong;

public class AcquirePoi {

    public static final int SCAN_RANGE = 48;

    public static BehaviorControl<PathfinderMob> create(Predicate<Holder<PoiType>> predicateHolderPoiType0, MemoryModuleType<GlobalPos> memoryModuleTypeGlobalPos1, boolean boolean2, Optional<Byte> optionalByte3) {
        return create(predicateHolderPoiType0, memoryModuleTypeGlobalPos1, memoryModuleTypeGlobalPos1, boolean2, optionalByte3);
    }

    public static BehaviorControl<PathfinderMob> create(Predicate<Holder<PoiType>> predicateHolderPoiType0, MemoryModuleType<GlobalPos> memoryModuleTypeGlobalPos1, MemoryModuleType<GlobalPos> memoryModuleTypeGlobalPos2, boolean boolean3, Optional<Byte> optionalByte4) {
        int $$5 = 5;
        int $$6 = 20;
        MutableLong $$7 = new MutableLong(0L);
        Long2ObjectMap<AcquirePoi.JitteredLinearRetry> $$8 = new Long2ObjectOpenHashMap();
        OneShot<PathfinderMob> $$9 = BehaviorBuilder.create(p_258276_ -> p_258276_.group(p_258276_.absent(memoryModuleTypeGlobalPos2)).apply(p_258276_, p_258300_ -> (p_258292_, p_258293_, p_258294_) -> {
            if (boolean3 && p_258293_.m_6162_()) {
                return false;
            } else if ($$7.getValue() == 0L) {
                $$7.setValue(p_258292_.m_46467_() + (long) p_258292_.f_46441_.nextInt(20));
                return false;
            } else if (p_258292_.m_46467_() < $$7.getValue()) {
                return false;
            } else {
                $$7.setValue(p_258294_ + 20L + (long) p_258292_.m_213780_().nextInt(20));
                PoiManager $$9x = p_258292_.getPoiManager();
                $$8.long2ObjectEntrySet().removeIf(p_22338_ -> !((AcquirePoi.JitteredLinearRetry) p_22338_.getValue()).isStillValid(p_258294_));
                Predicate<BlockPos> $$10 = p_258266_ -> {
                    AcquirePoi.JitteredLinearRetry $$3 = (AcquirePoi.JitteredLinearRetry) $$8.get(p_258266_.asLong());
                    if ($$3 == null) {
                        return true;
                    } else if (!$$3.shouldRetry(p_258294_)) {
                        return false;
                    } else {
                        $$3.markAttempt(p_258294_);
                        return true;
                    }
                };
                Set<Pair<Holder<PoiType>, BlockPos>> $$11 = (Set<Pair<Holder<PoiType>, BlockPos>>) $$9x.findAllClosestFirstWithType(predicateHolderPoiType0, $$10, p_258293_.m_20183_(), 48, PoiManager.Occupancy.HAS_SPACE).limit(5L).collect(Collectors.toSet());
                Path $$12 = findPathToPois(p_258293_, $$11);
                if ($$12 != null && $$12.canReach()) {
                    BlockPos $$13 = $$12.getTarget();
                    $$9x.getType($$13).ifPresent(p_288780_ -> {
                        $$9x.take(predicateHolderPoiType0, (p_217108_, p_217109_) -> p_217109_.equals($$13), $$13, 1);
                        p_258300_.set(GlobalPos.of(p_258292_.m_46472_(), $$13));
                        optionalByte4.ifPresent(p_147369_ -> p_258292_.broadcastEntityEvent(p_258293_, p_147369_));
                        $$8.clear();
                        DebugPackets.sendPoiTicketCountPacket(p_258292_, $$13);
                    });
                } else {
                    for (Pair<Holder<PoiType>, BlockPos> $$14 : $$11) {
                        $$8.computeIfAbsent(((BlockPos) $$14.getSecond()).asLong(), p_264881_ -> new AcquirePoi.JitteredLinearRetry(p_258292_.f_46441_, p_258294_));
                    }
                }
                return true;
            }
        }));
        return memoryModuleTypeGlobalPos2 == memoryModuleTypeGlobalPos1 ? $$9 : BehaviorBuilder.create(p_258269_ -> p_258269_.group(p_258269_.absent(memoryModuleTypeGlobalPos1)).apply(p_258269_, p_258302_ -> $$9));
    }

    @Nullable
    public static Path findPathToPois(Mob mob0, Set<Pair<Holder<PoiType>, BlockPos>> setPairHolderPoiTypeBlockPos1) {
        if (setPairHolderPoiTypeBlockPos1.isEmpty()) {
            return null;
        } else {
            Set<BlockPos> $$2 = new HashSet();
            int $$3 = 1;
            for (Pair<Holder<PoiType>, BlockPos> $$4 : setPairHolderPoiTypeBlockPos1) {
                $$3 = Math.max($$3, ((PoiType) ((Holder) $$4.getFirst()).value()).validRange());
                $$2.add((BlockPos) $$4.getSecond());
            }
            return mob0.getNavigation().createPath($$2, $$3);
        }
    }

    static class JitteredLinearRetry {

        private static final int MIN_INTERVAL_INCREASE = 40;

        private static final int MAX_INTERVAL_INCREASE = 80;

        private static final int MAX_RETRY_PATHFINDING_INTERVAL = 400;

        private final RandomSource random;

        private long previousAttemptTimestamp;

        private long nextScheduledAttemptTimestamp;

        private int currentDelay;

        JitteredLinearRetry(RandomSource randomSource0, long long1) {
            this.random = randomSource0;
            this.markAttempt(long1);
        }

        public void markAttempt(long long0) {
            this.previousAttemptTimestamp = long0;
            int $$1 = this.currentDelay + this.random.nextInt(40) + 40;
            this.currentDelay = Math.min($$1, 400);
            this.nextScheduledAttemptTimestamp = long0 + (long) this.currentDelay;
        }

        public boolean isStillValid(long long0) {
            return long0 - this.previousAttemptTimestamp < 400L;
        }

        public boolean shouldRetry(long long0) {
            return long0 >= this.nextScheduledAttemptTimestamp;
        }

        public String toString() {
            return "RetryMarker{, previousAttemptAt=" + this.previousAttemptTimestamp + ", nextScheduledAttemptAt=" + this.nextScheduledAttemptTimestamp + ", currentDelay=" + this.currentDelay + "}";
        }
    }
}
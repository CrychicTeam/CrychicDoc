package net.minecraft.world.entity.ai.behavior;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;

public class SetClosestHomeAsWalkTarget {

    private static final int CACHE_TIMEOUT = 40;

    private static final int BATCH_SIZE = 5;

    private static final int RATE = 20;

    private static final int OK_DISTANCE_SQR = 4;

    public static BehaviorControl<PathfinderMob> create(float float0) {
        Long2LongMap $$1 = new Long2LongOpenHashMap();
        MutableLong $$2 = new MutableLong(0L);
        return BehaviorBuilder.create(p_258633_ -> p_258633_.group(p_258633_.absent(MemoryModuleType.WALK_TARGET), p_258633_.absent(MemoryModuleType.HOME)).apply(p_258633_, (p_258626_, p_258627_) -> (p_258638_, p_258639_, p_258640_) -> {
            if (p_258638_.m_46467_() - $$2.getValue() < 20L) {
                return false;
            } else {
                PoiManager $$7 = p_258638_.getPoiManager();
                Optional<BlockPos> $$8 = $$7.findClosest(p_217376_ -> p_217376_.is(PoiTypes.HOME), p_258639_.m_20183_(), 48, PoiManager.Occupancy.ANY);
                if (!$$8.isEmpty() && !(((BlockPos) $$8.get()).m_123331_(p_258639_.m_20183_()) <= 4.0)) {
                    MutableInt $$9 = new MutableInt(0);
                    $$2.setValue(p_258638_.m_46467_() + (long) p_258638_.m_213780_().nextInt(20));
                    Predicate<BlockPos> $$10 = p_258644_ -> {
                        long $$4 = p_258644_.asLong();
                        if ($$1.containsKey($$4)) {
                            return false;
                        } else if ($$9.incrementAndGet() >= 5) {
                            return false;
                        } else {
                            $$1.put($$4, $$2.getValue() + 40L);
                            return true;
                        }
                    };
                    Set<Pair<Holder<PoiType>, BlockPos>> $$11 = (Set<Pair<Holder<PoiType>, BlockPos>>) $$7.findAllWithType(p_217372_ -> p_217372_.is(PoiTypes.HOME), $$10, p_258639_.m_20183_(), 48, PoiManager.Occupancy.ANY).collect(Collectors.toSet());
                    Path $$12 = AcquirePoi.findPathToPois(p_258639_, $$11);
                    if ($$12 != null && $$12.canReach()) {
                        BlockPos $$13 = $$12.getTarget();
                        Optional<Holder<PoiType>> $$14 = $$7.getType($$13);
                        if ($$14.isPresent()) {
                            p_258626_.set(new WalkTarget($$13, float0, 1));
                            DebugPackets.sendPoiTicketCountPacket(p_258638_, $$13);
                        }
                    } else if ($$9.getValue() < 5) {
                        $$1.long2LongEntrySet().removeIf(p_258629_ -> p_258629_.getLongValue() < $$2.getValue());
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }));
    }
}
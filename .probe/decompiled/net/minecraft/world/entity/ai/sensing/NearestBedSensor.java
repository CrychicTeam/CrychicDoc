package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.AcquirePoi;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.pathfinder.Path;

public class NearestBedSensor extends Sensor<Mob> {

    private static final int CACHE_TIMEOUT = 40;

    private static final int BATCH_SIZE = 5;

    private static final int RATE = 20;

    private final Long2LongMap batchCache = new Long2LongOpenHashMap();

    private int triedCount;

    private long lastUpdate;

    public NearestBedSensor() {
        super(20);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_BED);
    }

    protected void doTick(ServerLevel serverLevel0, Mob mob1) {
        if (mob1.m_6162_()) {
            this.triedCount = 0;
            this.lastUpdate = serverLevel0.m_46467_() + (long) serverLevel0.m_213780_().nextInt(20);
            PoiManager $$2 = serverLevel0.getPoiManager();
            Predicate<BlockPos> $$3 = p_26688_ -> {
                long $$1 = p_26688_.asLong();
                if (this.batchCache.containsKey($$1)) {
                    return false;
                } else if (++this.triedCount >= 5) {
                    return false;
                } else {
                    this.batchCache.put($$1, this.lastUpdate + 40L);
                    return true;
                }
            };
            Set<Pair<Holder<PoiType>, BlockPos>> $$4 = (Set<Pair<Holder<PoiType>, BlockPos>>) $$2.findAllWithType(p_217819_ -> p_217819_.is(PoiTypes.HOME), $$3, mob1.m_20183_(), 48, PoiManager.Occupancy.ANY).collect(Collectors.toSet());
            Path $$5 = AcquirePoi.findPathToPois(mob1, $$4);
            if ($$5 != null && $$5.canReach()) {
                BlockPos $$6 = $$5.getTarget();
                Optional<Holder<PoiType>> $$7 = $$2.getType($$6);
                if ($$7.isPresent()) {
                    mob1.m_6274_().setMemory(MemoryModuleType.NEAREST_BED, $$6);
                }
            } else if (this.triedCount < 5) {
                this.batchCache.long2LongEntrySet().removeIf(p_217821_ -> p_217821_.getLongValue() < this.lastUpdate);
            }
        }
    }
}
package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;

public class NearestItemSensor extends Sensor<Mob> {

    private static final long XZ_RANGE = 32L;

    private static final long Y_RANGE = 16L;

    public static final int MAX_DISTANCE_TO_WANTED_ITEM = 32;

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
    }

    protected void doTick(ServerLevel serverLevel0, Mob mob1) {
        Brain<?> $$2 = mob1.m_6274_();
        List<ItemEntity> $$3 = serverLevel0.m_6443_(ItemEntity.class, mob1.m_20191_().inflate(32.0, 16.0, 32.0), p_26703_ -> true);
        $$3.sort(Comparator.comparingDouble(mob1::m_20280_));
        Optional<ItemEntity> $$4 = $$3.stream().filter(p_26706_ -> mob1.wantsToPickUp(p_26706_.getItem())).filter(p_26701_ -> p_26701_.m_19950_(mob1, 32.0)).filter(mob1::m_142582_).findFirst();
        $$2.setMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, $$4);
    }
}
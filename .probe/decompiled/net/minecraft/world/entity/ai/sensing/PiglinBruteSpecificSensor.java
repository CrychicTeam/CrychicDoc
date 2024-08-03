package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;

public class PiglinBruteSpecificSensor extends Sensor<LivingEntity> {

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEARBY_ADULT_PIGLINS);
    }

    @Override
    protected void doTick(ServerLevel serverLevel0, LivingEntity livingEntity1) {
        Brain<?> $$2 = livingEntity1.getBrain();
        List<AbstractPiglin> $$3 = Lists.newArrayList();
        NearestVisibleLivingEntities $$4 = (NearestVisibleLivingEntities) $$2.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
        Optional<Mob> $$5 = $$4.findClosest(p_186155_ -> p_186155_ instanceof WitherSkeleton || p_186155_ instanceof WitherBoss).map(Mob.class::cast);
        for (LivingEntity $$7 : (List) $$2.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of())) {
            if ($$7 instanceof AbstractPiglin && ((AbstractPiglin) $$7).isAdult()) {
                $$3.add((AbstractPiglin) $$7);
            }
        }
        $$2.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, $$5);
        $$2.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, $$3);
    }
}
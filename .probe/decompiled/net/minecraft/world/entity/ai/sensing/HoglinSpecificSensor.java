package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;

public class HoglinSpecificSensor extends Sensor<Hoglin> {

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, new MemoryModuleType[0]);
    }

    protected void doTick(ServerLevel serverLevel0, Hoglin hoglin1) {
        Brain<?> $$2 = hoglin1.getBrain();
        $$2.setMemory(MemoryModuleType.NEAREST_REPELLENT, this.findNearestRepellent(serverLevel0, hoglin1));
        Optional<Piglin> $$3 = Optional.empty();
        int $$4 = 0;
        List<Hoglin> $$5 = Lists.newArrayList();
        NearestVisibleLivingEntities $$6 = (NearestVisibleLivingEntities) $$2.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
        for (LivingEntity $$7 : $$6.findAll(p_186150_ -> !p_186150_.isBaby() && (p_186150_ instanceof Piglin || p_186150_ instanceof Hoglin))) {
            if ($$7 instanceof Piglin $$8) {
                $$4++;
                if ($$3.isEmpty()) {
                    $$3 = Optional.of($$8);
                }
            }
            if ($$7 instanceof Hoglin $$9) {
                $$5.add($$9);
            }
        }
        $$2.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, $$3);
        $$2.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, $$5);
        $$2.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, $$4);
        $$2.setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, $$5.size());
    }

    private Optional<BlockPos> findNearestRepellent(ServerLevel serverLevel0, Hoglin hoglin1) {
        return BlockPos.findClosestMatch(hoglin1.m_20183_(), 8, 4, p_186148_ -> serverLevel0.m_8055_(p_186148_).m_204336_(BlockTags.HOGLIN_REPELLENTS));
    }
}
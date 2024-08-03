package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class VillagerHostilesSensor extends NearestVisibleLivingEntitySensor {

    private static final ImmutableMap<EntityType<?>, Float> ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.builder().put(EntityType.DROWNED, 8.0F).put(EntityType.EVOKER, 12.0F).put(EntityType.HUSK, 8.0F).put(EntityType.ILLUSIONER, 12.0F).put(EntityType.PILLAGER, 15.0F).put(EntityType.RAVAGER, 12.0F).put(EntityType.VEX, 8.0F).put(EntityType.VINDICATOR, 10.0F).put(EntityType.ZOGLIN, 10.0F).put(EntityType.ZOMBIE, 8.0F).put(EntityType.ZOMBIE_VILLAGER, 8.0F).build();

    @Override
    protected boolean isMatchingEntity(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        return this.isHostile(livingEntity1) && this.isClose(livingEntity0, livingEntity1);
    }

    private boolean isClose(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        float $$2 = (Float) ACCEPTABLE_DISTANCE_FROM_HOSTILES.get(livingEntity1.m_6095_());
        return livingEntity1.m_20280_(livingEntity0) <= (double) ($$2 * $$2);
    }

    @Override
    protected MemoryModuleType<LivingEntity> getMemory() {
        return MemoryModuleType.NEAREST_HOSTILE;
    }

    private boolean isHostile(LivingEntity livingEntity0) {
        return ACCEPTABLE_DISTANCE_FROM_HOSTILES.containsKey(livingEntity0.m_6095_());
    }
}
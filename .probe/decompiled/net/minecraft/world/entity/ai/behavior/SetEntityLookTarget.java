package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;

public class SetEntityLookTarget {

    public static BehaviorControl<LivingEntity> create(MobCategory mobCategory0, float float1) {
        return create(p_289375_ -> mobCategory0.equals(p_289375_.m_6095_().getCategory()), float1);
    }

    public static OneShot<LivingEntity> create(EntityType<?> entityType0, float float1) {
        return create(p_289377_ -> entityType0.equals(p_289377_.m_6095_()), float1);
    }

    public static OneShot<LivingEntity> create(float float0) {
        return create(p_23913_ -> true, float0);
    }

    public static OneShot<LivingEntity> create(Predicate<LivingEntity> predicateLivingEntity0, float float1) {
        float $$2 = float1 * float1;
        return BehaviorBuilder.create(p_258663_ -> p_258663_.group(p_258663_.absent(MemoryModuleType.LOOK_TARGET), p_258663_.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(p_258663_, (p_258656_, p_258657_) -> (p_258650_, p_258651_, p_258652_) -> {
            Optional<LivingEntity> $$8 = p_258663_.<NearestVisibleLivingEntities>get(p_258657_).findClosest(predicateLivingEntity0.and(p_264945_ -> p_264945_.m_20280_(p_258651_) <= (double) $$2 && !p_258651_.m_20363_(p_264945_)));
            if ($$8.isEmpty()) {
                return false;
            } else {
                p_258656_.set(new EntityTracker((Entity) $$8.get(), true));
                return true;
            }
        }));
    }
}
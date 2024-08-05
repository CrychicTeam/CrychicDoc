package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;

public class SetLookAndInteract {

    public static BehaviorControl<LivingEntity> create(EntityType<?> entityType0, int int1) {
        int $$2 = int1 * int1;
        return BehaviorBuilder.create(p_258685_ -> p_258685_.group(p_258685_.registered(MemoryModuleType.LOOK_TARGET), p_258685_.absent(MemoryModuleType.INTERACTION_TARGET), p_258685_.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(p_258685_, (p_258680_, p_258681_, p_258682_) -> (p_258670_, p_258671_, p_258672_) -> {
            Optional<LivingEntity> $$9 = p_258685_.<NearestVisibleLivingEntities>get(p_258682_).findClosest(p_289383_ -> p_289383_.m_20280_(p_258671_) <= (double) $$2 && entityType0.equals(p_289383_.m_6095_()));
            if ($$9.isEmpty()) {
                return false;
            } else {
                LivingEntity $$10 = (LivingEntity) $$9.get();
                p_258681_.set($$10);
                p_258680_.set(new EntityTracker($$10, true));
                return true;
            }
        }));
    }
}
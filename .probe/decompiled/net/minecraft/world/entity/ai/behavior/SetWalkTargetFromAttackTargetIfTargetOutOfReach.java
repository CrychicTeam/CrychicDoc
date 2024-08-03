package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import java.util.function.Function;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;

public class SetWalkTargetFromAttackTargetIfTargetOutOfReach {

    private static final int PROJECTILE_ATTACK_RANGE_BUFFER = 1;

    public static BehaviorControl<Mob> create(float float0) {
        return create(p_147908_ -> float0);
    }

    public static BehaviorControl<Mob> create(Function<LivingEntity, Float> functionLivingEntityFloat0) {
        return BehaviorBuilder.create(p_258687_ -> p_258687_.group(p_258687_.registered(MemoryModuleType.WALK_TARGET), p_258687_.registered(MemoryModuleType.LOOK_TARGET), p_258687_.present(MemoryModuleType.ATTACK_TARGET), p_258687_.registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(p_258687_, (p_258699_, p_258700_, p_258701_, p_258702_) -> (p_258694_, p_258695_, p_258696_) -> {
            LivingEntity $$9 = p_258687_.get(p_258701_);
            Optional<NearestVisibleLivingEntities> $$10 = p_258687_.tryGet(p_258702_);
            if ($$10.isPresent() && ((NearestVisibleLivingEntities) $$10.get()).contains($$9) && BehaviorUtils.isWithinAttackRange(p_258695_, $$9, 1)) {
                p_258699_.erase();
            } else {
                p_258700_.set(new EntityTracker($$9, true));
                p_258699_.set(new WalkTarget(new EntityTracker($$9, false), (Float) functionLivingEntityFloat0.apply(p_258695_), 0));
            }
            return true;
        }));
    }
}
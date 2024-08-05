package net.minecraft.world.entity.ai.behavior;

import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;

public class SetWalkTargetFromLookTarget {

    public static OneShot<LivingEntity> create(float float0, int int1) {
        return create(p_182369_ -> true, p_182364_ -> float0, int1);
    }

    public static OneShot<LivingEntity> create(Predicate<LivingEntity> predicateLivingEntity0, Function<LivingEntity, Float> functionLivingEntityFloat1, int int2) {
        return BehaviorBuilder.create(p_258748_ -> p_258748_.group(p_258748_.absent(MemoryModuleType.WALK_TARGET), p_258748_.present(MemoryModuleType.LOOK_TARGET)).apply(p_258748_, (p_258743_, p_258744_) -> (p_258736_, p_258737_, p_258738_) -> {
            if (!predicateLivingEntity0.test(p_258737_)) {
                return false;
            } else {
                p_258743_.set(new WalkTarget(p_258748_.get(p_258744_), (Float) functionLivingEntityFloat1.apply(p_258737_), int2));
                return true;
            }
        }));
    }
}
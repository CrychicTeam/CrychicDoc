package net.minecraft.world.entity.ai.behavior;

import java.util.function.Function;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;

public class BabyFollowAdult {

    public static OneShot<AgeableMob> create(UniformInt uniformInt0, float float1) {
        return create(uniformInt0, p_147421_ -> float1);
    }

    public static OneShot<AgeableMob> create(UniformInt uniformInt0, Function<LivingEntity, Float> functionLivingEntityFloat1) {
        return BehaviorBuilder.create(p_258331_ -> p_258331_.group(p_258331_.present(MemoryModuleType.NEAREST_VISIBLE_ADULT), p_258331_.registered(MemoryModuleType.LOOK_TARGET), p_258331_.absent(MemoryModuleType.WALK_TARGET)).apply(p_258331_, (p_258317_, p_258318_, p_258319_) -> (p_258326_, p_258327_, p_258328_) -> {
            if (!p_258327_.isBaby()) {
                return false;
            } else {
                AgeableMob $$9 = p_258331_.get(p_258317_);
                if (p_258327_.m_19950_($$9, (double) (uniformInt0.getMaxValue() + 1)) && !p_258327_.m_19950_($$9, (double) uniformInt0.getMinValue())) {
                    WalkTarget $$10 = new WalkTarget(new EntityTracker($$9, false), (Float) functionLivingEntityFloat1.apply(p_258327_), uniformInt0.getMinValue() - 1);
                    p_258318_.set(new EntityTracker($$9, true));
                    p_258319_.set($$10);
                    return true;
                } else {
                    return false;
                }
            }
        }));
    }
}
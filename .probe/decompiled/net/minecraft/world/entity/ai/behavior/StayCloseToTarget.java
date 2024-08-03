package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;

public class StayCloseToTarget {

    public static BehaviorControl<LivingEntity> create(Function<LivingEntity, Optional<PositionTracker>> functionLivingEntityOptionalPositionTracker0, Predicate<LivingEntity> predicateLivingEntity1, int int2, int int3, float float4) {
        return BehaviorBuilder.create(p_272460_ -> p_272460_.group(p_272460_.registered(MemoryModuleType.LOOK_TARGET), p_272460_.registered(MemoryModuleType.WALK_TARGET)).apply(p_272460_, (p_272466_, p_272467_) -> (p_260054_, p_260069_, p_259517_) -> {
            Optional<PositionTracker> $$10 = (Optional<PositionTracker>) functionLivingEntityOptionalPositionTracker0.apply(p_260069_);
            if (!$$10.isEmpty() && predicateLivingEntity1.test(p_260069_)) {
                PositionTracker $$11 = (PositionTracker) $$10.get();
                if (p_260069_.m_20182_().closerThan($$11.currentPosition(), (double) int3)) {
                    return false;
                } else {
                    PositionTracker $$12 = (PositionTracker) $$10.get();
                    p_272466_.set($$12);
                    p_272467_.set(new WalkTarget($$12, float4, int2));
                    return true;
                }
            } else {
                return false;
            }
        }));
    }
}
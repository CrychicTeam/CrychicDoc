package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;

public class InteractWith {

    public static <T extends LivingEntity> BehaviorControl<LivingEntity> of(EntityType<? extends T> entityTypeExtendsT0, int int1, MemoryModuleType<T> memoryModuleTypeT2, float float3, int int4) {
        return of(entityTypeExtendsT0, int1, p_23287_ -> true, p_23285_ -> true, memoryModuleTypeT2, float3, int4);
    }

    public static <E extends LivingEntity, T extends LivingEntity> BehaviorControl<E> of(EntityType<? extends T> entityTypeExtendsT0, int int1, Predicate<E> predicateE2, Predicate<T> predicateT3, MemoryModuleType<T> memoryModuleTypeT4, float float5, int int6) {
        int $$7 = int1 * int1;
        Predicate<LivingEntity> $$8 = p_289327_ -> entityTypeExtendsT0.equals(p_289327_.m_6095_()) && predicateT3.test(p_289327_);
        return BehaviorBuilder.create(p_258426_ -> p_258426_.group(p_258426_.registered(memoryModuleTypeT4), p_258426_.registered(MemoryModuleType.LOOK_TARGET), p_258426_.absent(MemoryModuleType.WALK_TARGET), p_258426_.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(p_258426_, (p_258439_, p_258440_, p_258441_, p_258442_) -> (p_258413_, p_258414_, p_258415_) -> {
            NearestVisibleLivingEntities $$13 = p_258426_.get(p_258442_);
            if (predicateE2.test(p_258414_) && $$13.contains($$8)) {
                Optional<LivingEntity> $$14 = $$13.findClosest(p_258419_ -> p_258419_.m_20280_(p_258414_) <= (double) $$7 && $$8.test(p_258419_));
                $$14.ifPresent(p_258432_ -> {
                    p_258439_.set(p_258432_);
                    p_258440_.set(new EntityTracker(p_258432_, true));
                    p_258441_.set(new WalkTarget(new EntityTracker(p_258432_, false), float5, int6));
                });
                return true;
            } else {
                return false;
            }
        }));
    }
}
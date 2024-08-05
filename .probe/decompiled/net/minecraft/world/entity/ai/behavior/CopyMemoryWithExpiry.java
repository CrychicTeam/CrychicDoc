package net.minecraft.world.entity.ai.behavior;

import java.util.function.Predicate;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class CopyMemoryWithExpiry {

    public static <E extends LivingEntity, T> BehaviorControl<E> create(Predicate<E> predicateE0, MemoryModuleType<? extends T> memoryModuleTypeExtendsT1, MemoryModuleType<T> memoryModuleTypeT2, UniformInt uniformInt3) {
        return BehaviorBuilder.create(p_260141_ -> p_260141_.group(p_260141_.present(memoryModuleTypeExtendsT1), p_260141_.absent(memoryModuleTypeT2)).apply(p_260141_, (p_259306_, p_259907_) -> (p_264887_, p_264888_, p_264889_) -> {
            if (!predicateE0.test(p_264888_)) {
                return false;
            } else {
                p_259907_.setWithExpiry(p_260141_.get(p_259306_), (long) uniformInt3.sample(p_264887_.f_46441_));
                return true;
            }
        }));
    }
}
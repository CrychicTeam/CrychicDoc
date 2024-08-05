package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class StartAttacking {

    public static <E extends Mob> BehaviorControl<E> create(Function<E, Optional<? extends LivingEntity>> functionEOptionalExtendsLivingEntity0) {
        return create(p_24212_ -> true, functionEOptionalExtendsLivingEntity0);
    }

    public static <E extends Mob> BehaviorControl<E> create(Predicate<E> predicateE0, Function<E, Optional<? extends LivingEntity>> functionEOptionalExtendsLivingEntity1) {
        return BehaviorBuilder.create(p_258782_ -> p_258782_.group(p_258782_.absent(MemoryModuleType.ATTACK_TARGET), p_258782_.registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)).apply(p_258782_, (p_258778_, p_258779_) -> (p_258773_, p_258774_, p_258775_) -> {
            if (!predicateE0.test(p_258774_)) {
                return false;
            } else {
                Optional<? extends LivingEntity> $$7 = (Optional<? extends LivingEntity>) functionEOptionalExtendsLivingEntity1.apply(p_258774_);
                if ($$7.isEmpty()) {
                    return false;
                } else {
                    LivingEntity $$8 = (LivingEntity) $$7.get();
                    if (!p_258774_.m_6779_($$8)) {
                        return false;
                    } else {
                        p_258778_.set($$8);
                        p_258779_.erase();
                        return true;
                    }
                }
            }
        }));
    }
}
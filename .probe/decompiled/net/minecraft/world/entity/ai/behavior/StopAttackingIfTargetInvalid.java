package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class StopAttackingIfTargetInvalid {

    private static final int TIMEOUT_TO_GET_WITHIN_ATTACK_RANGE = 200;

    public static <E extends Mob> BehaviorControl<E> create(BiConsumer<E, LivingEntity> biConsumerELivingEntity0) {
        return create(p_147988_ -> false, biConsumerELivingEntity0, true);
    }

    public static <E extends Mob> BehaviorControl<E> create(Predicate<LivingEntity> predicateLivingEntity0) {
        return create(predicateLivingEntity0, (p_217411_, p_217412_) -> {
        }, true);
    }

    public static <E extends Mob> BehaviorControl<E> create() {
        return create(p_147986_ -> false, (p_217408_, p_217409_) -> {
        }, true);
    }

    public static <E extends Mob> BehaviorControl<E> create(Predicate<LivingEntity> predicateLivingEntity0, BiConsumer<E, LivingEntity> biConsumerELivingEntity1, boolean boolean2) {
        return BehaviorBuilder.create(p_258801_ -> p_258801_.group(p_258801_.present(MemoryModuleType.ATTACK_TARGET), p_258801_.registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)).apply(p_258801_, (p_258787_, p_258788_) -> (p_258795_, p_258796_, p_258797_) -> {
            LivingEntity $$9 = p_258801_.get(p_258787_);
            if (p_258796_.m_6779_($$9) && (!boolean2 || !isTiredOfTryingToReachTarget(p_258796_, p_258801_.tryGet(p_258788_))) && $$9.isAlive() && $$9.m_9236_() == p_258796_.m_9236_() && !predicateLivingEntity0.test($$9)) {
                return true;
            } else {
                biConsumerELivingEntity1.accept(p_258796_, $$9);
                p_258787_.erase();
                return true;
            }
        }));
    }

    private static boolean isTiredOfTryingToReachTarget(LivingEntity livingEntity0, Optional<Long> optionalLong1) {
        return optionalLong1.isPresent() && livingEntity0.m_9236_().getGameTime() - (Long) optionalLong1.get() > 200L;
    }
}
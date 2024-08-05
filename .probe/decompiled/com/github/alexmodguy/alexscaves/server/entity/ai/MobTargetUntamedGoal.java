package com.github.alexmodguy.alexscaves.server.entity.ai;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class MobTargetUntamedGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private final TamableAnimal tamableMob;

    public MobTargetUntamedGoal(TamableAnimal tamableAnimal, Class<T> clazz, int chance, boolean seeCheck, boolean reachCheck, @Nullable Predicate<LivingEntity> entityPredicate) {
        super(tamableAnimal, clazz, chance, seeCheck, reachCheck, entityPredicate);
        this.tamableMob = tamableAnimal;
    }

    @Override
    public boolean canUse() {
        return !this.tamableMob.isTame() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.f_26051_ != null ? this.f_26051_.test(this.f_26135_, this.f_26050_) : super.m_8045_();
    }
}
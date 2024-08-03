package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class FlyingAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public FlyingAITarget(Mob creature, Class<T> classTarget, boolean checkSight) {
        super(creature, classTarget, checkSight);
    }

    public FlyingAITarget(Mob creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
        super(creature, classTarget, checkSight, onlyNearby);
    }

    public FlyingAITarget(Mob creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, @Nullable Predicate<LivingEntity> targetSelector) {
        super(creature, classTarget, chance, checkSight, onlyNearby, targetSelector);
    }

    @NotNull
    @Override
    protected AABB getTargetSearchArea(double targetDistance) {
        return this.f_26135_.m_20191_().inflate(targetDistance, targetDistance, targetDistance);
    }

    @Override
    public boolean canUse() {
        return !(this.f_26135_ instanceof EntitySeaSerpent) || !((EntitySeaSerpent) this.f_26135_).isJumpingOutOfWater() && this.f_26135_.m_20069_() ? super.canUse() : false;
    }
}
package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class DragonAITargetNonTamed<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private final EntityDragonBase dragon;

    public DragonAITargetNonTamed(EntityDragonBase entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 5, checkSight, false, targetSelector);
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
        this.dragon = entityIn;
    }

    @Override
    public boolean canUse() {
        if (this.dragon.m_21824_()) {
            return false;
        } else if (this.dragon.lookingForRoostAIFlag) {
            return false;
        } else {
            boolean canUse = super.canUse();
            boolean isSleeping = this.dragon.isSleeping();
            if (canUse) {
                return isSleeping && this.f_26050_ instanceof Player ? this.dragon.m_20280_(this.f_26050_) <= 16.0 : !isSleeping;
            } else {
                return false;
            }
        }
    }

    @NotNull
    @Override
    protected AABB getTargetSearchArea(double targetDistance) {
        return this.dragon.m_20191_().inflate(targetDistance, targetDistance, targetDistance);
    }

    @Override
    protected double getFollowDistance() {
        AttributeInstance iattributeinstance = this.f_26135_.m_21051_(Attributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 128.0 : iattributeinstance.getValue();
    }
}
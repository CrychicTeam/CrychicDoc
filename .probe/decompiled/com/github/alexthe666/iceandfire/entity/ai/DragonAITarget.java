package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
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

public class DragonAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private final EntityDragonBase dragon;

    public DragonAITarget(EntityDragonBase entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 3, checkSight, false, targetSelector);
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
        this.dragon = entityIn;
    }

    @Override
    public boolean canUse() {
        if (this.dragon.getCommand() == 1 || this.dragon.getCommand() == 2 || this.dragon.isSleeping()) {
            return false;
        } else if (!this.dragon.m_21824_() && this.dragon.lookingForRoostAIFlag) {
            return false;
        } else {
            if (this.f_26050_ != null && !this.f_26050_.getClass().equals(this.dragon.getClass())) {
                if (!super.canUse()) {
                    return false;
                }
                float dragonSize = Math.max(this.dragon.m_20205_(), this.dragon.m_20205_() * this.dragon.getRenderSize());
                if (dragonSize >= this.f_26050_.m_20205_()) {
                    if (this.f_26050_ instanceof Player && !this.dragon.m_21824_()) {
                        return true;
                    }
                    if (this.f_26050_ instanceof EntityDragonBase dragon) {
                        if (dragon.m_269323_() != null && this.dragon.m_269323_() != null && this.dragon.m_21830_(dragon.m_269323_())) {
                            return false;
                        }
                        return !dragon.isModelDead();
                    }
                    if (this.f_26050_ instanceof Player && this.dragon.m_21824_()) {
                        return false;
                    }
                    if (!this.dragon.m_21830_(this.f_26050_) && FoodUtils.getFoodPoints(this.f_26050_) > 0 && this.dragon.canMove() && (this.dragon.getHunger() < 90 || !this.dragon.m_21824_() && this.f_26050_ instanceof Player)) {
                        if (this.dragon.m_21824_()) {
                            return DragonUtils.canTameDragonAttack(this.dragon, this.f_26050_);
                        }
                        return true;
                    }
                }
            }
            return false;
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
        return iattributeinstance == null ? 64.0 : iattributeinstance.getValue();
    }
}
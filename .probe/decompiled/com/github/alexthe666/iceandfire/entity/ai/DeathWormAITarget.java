package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.google.common.base.Predicate;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class DeathWormAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private final EntityDeathWorm deathworm;

    public DeathWormAITarget(EntityDeathWorm entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetPredicate) {
        super(entityIn, classTarget, 20, checkSight, false, targetPredicate);
        this.deathworm = entityIn;
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        boolean canUse = super.canUse();
        if (canUse && this.f_26050_ != null && this.f_26050_.m_6095_() != IafEntityRegistry.DEATH_WORM.get()) {
            if (this.f_26050_ instanceof Player && !this.deathworm.m_21830_(this.f_26050_)) {
                return !this.deathworm.m_21824_();
            }
            if (this.deathworm.m_21830_(this.f_26050_)) {
                return false;
            }
            if (this.f_26050_ instanceof Monster && this.deathworm.getWormAge() > 2) {
                if (this.f_26050_ instanceof PathfinderMob) {
                    return this.deathworm.getWormAge() > 3;
                }
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    protected AABB getTargetSearchArea(double targetDistance) {
        return this.deathworm.m_20191_().inflate(targetDistance, 6.0, targetDistance);
    }
}
package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

public class HippogryphAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private final EntityHippogryph hippogryph;

    public HippogryphAITarget(EntityHippogryph entityIn, Class<T> classTarget, boolean checkSight, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(entityIn, classTarget, 20, checkSight, false, targetPredicate);
        this.hippogryph = entityIn;
    }

    public HippogryphAITarget(EntityHippogryph entityIn, Class<T> classTarget, int i, boolean checkSight, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(entityIn, classTarget, i, checkSight, false, targetPredicate);
        this.hippogryph = entityIn;
    }

    @Override
    public boolean canUse() {
        if (super.canUse() && this.f_26050_ != null && !this.f_26050_.getClass().equals(this.hippogryph.getClass()) && this.hippogryph.m_20205_() >= this.f_26050_.m_20205_()) {
            if (this.f_26050_ instanceof Player) {
                return !this.hippogryph.m_21824_();
            }
            if (!this.hippogryph.m_21830_(this.f_26050_) && this.hippogryph.canMove() && this.f_26050_ instanceof Animal) {
                if (this.hippogryph.m_21824_()) {
                    return DragonUtils.canTameDragonAttack(this.hippogryph, this.f_26050_);
                }
                return true;
            }
        }
        return false;
    }
}
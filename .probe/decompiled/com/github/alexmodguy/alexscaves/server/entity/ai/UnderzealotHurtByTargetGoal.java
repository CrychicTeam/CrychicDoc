package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.ForsakenEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.WatcherEntity;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class UnderzealotHurtByTargetGoal extends HurtByTargetGoal {

    private UnderzealotEntity underzealot;

    public UnderzealotHurtByTargetGoal(UnderzealotEntity underzealot) {
        super(underzealot, UnderzealotEntity.class, WatcherEntity.class, ForsakenEntity.class);
        this.m_26044_(new Class[0]);
        this.underzealot = underzealot;
    }

    @Override
    protected boolean canAttack(@Nullable LivingEntity target, TargetingConditions targetingConditions) {
        return !this.underzealot.isTargetingBlocked() && super.m_26150_(target, targetingConditions);
    }

    @Override
    protected void alertOther(Mob mob, LivingEntity target) {
        if (mob instanceof UnderzealotEntity otherUnderzealot && otherUnderzealot.isTargetingBlocked()) {
            return;
        }
        super.alertOther(mob, target);
    }
}
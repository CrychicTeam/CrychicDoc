package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.jetbrains.annotations.NotNull;

public class DreadAITargetNonDread extends NearestAttackableTargetGoal<LivingEntity> {

    public DreadAITargetNonDread(Mob entityIn, Class<LivingEntity> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
    }

    @Override
    protected boolean canAttack(@Nullable LivingEntity target, @NotNull TargetingConditions targetPredicate) {
        return !super.m_26150_(target, targetPredicate) ? false : !(target instanceof IDreadMob) && DragonUtils.isAlive(target);
    }
}
package com.github.alexthe666.iceandfire.entity.ai;

import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class CyclopsAITargetSheepPlayers<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public CyclopsAITargetSheepPlayers(Mob goalOwnerIn, Class<T> targetClassIn, boolean checkSight) {
        super(goalOwnerIn, targetClassIn, 0, checkSight, true, new Predicate<LivingEntity>() {

            public boolean test(LivingEntity livingEntity) {
                return false;
            }
        });
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
    }
}
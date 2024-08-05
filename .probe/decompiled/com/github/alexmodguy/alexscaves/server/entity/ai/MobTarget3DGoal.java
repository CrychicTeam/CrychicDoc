package com.github.alexmodguy.alexscaves.server.entity.ai;

import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.phys.AABB;

public class MobTarget3DGoal extends NearestAttackableTargetGoal {

    public MobTarget3DGoal(Mob mob, Class targetClass, boolean sight) {
        super(mob, targetClass, sight);
    }

    public MobTarget3DGoal(Mob mob, Class targetClass, boolean sight, int chance, Predicate<LivingEntity> predicate) {
        super(mob, targetClass, chance, sight, false, predicate);
    }

    @Override
    protected AABB getTargetSearchArea(double distance) {
        return this.f_26135_.m_20191_().inflate(distance, distance, distance);
    }
}
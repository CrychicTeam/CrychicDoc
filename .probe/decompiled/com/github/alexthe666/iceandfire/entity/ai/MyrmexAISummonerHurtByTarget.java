package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class MyrmexAISummonerHurtByTarget extends TargetGoal {

    EntityMyrmexSwarmer tameable;

    LivingEntity attacker;

    private int timestamp;

    public MyrmexAISummonerHurtByTarget(EntityMyrmexSwarmer theDefendingTameableIn) {
        super(theDefendingTameableIn, false);
        this.tameable = theDefendingTameableIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity living = this.tameable.getSummoner();
        if (living == null) {
            return false;
        } else {
            this.attacker = living.getLastHurtByMob();
            int i = living.getLastHurtByMobTimestamp();
            return i != this.timestamp && this.m_26150_(this.attacker, TargetingConditions.DEFAULT) && this.tameable.shouldAttackEntity(this.attacker, living);
        }
    }

    @Override
    public void start() {
        this.f_26135_.setTarget(this.attacker);
        LivingEntity LivingEntity = this.tameable.getSummoner();
        if (LivingEntity != null) {
            this.timestamp = LivingEntity.getLastHurtByMobTimestamp();
        }
        super.start();
    }
}
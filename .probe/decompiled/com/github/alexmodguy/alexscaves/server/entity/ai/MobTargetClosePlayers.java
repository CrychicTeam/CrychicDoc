package com.github.alexmodguy.alexscaves.server.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class MobTargetClosePlayers extends NearestAttackableTargetGoal<Player> {

    private Mob mob;

    private float range;

    public MobTargetClosePlayers(Mob mob, int chance, float range) {
        super(mob, Player.class, chance, true, true, null);
        this.mob = mob;
        this.range = range;
    }

    @Override
    public boolean canUse() {
        return !this.mob.m_6162_() && (!(this.mob instanceof TamableAnimal) || !((TamableAnimal) this.mob).isTame()) ? super.canUse() : false;
    }

    @Override
    protected double getFollowDistance() {
        return (double) this.range;
    }

    @Override
    protected AABB getTargetSearchArea(double rangeIn) {
        return this.mob.m_20191_().inflate((double) this.range, (double) this.range, (double) this.range);
    }
}
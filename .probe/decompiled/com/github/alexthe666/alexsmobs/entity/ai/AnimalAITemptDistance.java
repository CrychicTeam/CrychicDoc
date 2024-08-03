package com.github.alexthe666.alexsmobs.entity.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;

public class AnimalAITemptDistance extends Goal {

    private final TargetingConditions targetingConditions;

    protected final PathfinderMob mob;

    private final double speedModifier;

    private double px;

    private double py;

    private double pz;

    private double pRotX;

    private double pRotY;

    protected Player player;

    private int calmDown;

    private boolean isRunning;

    private final Ingredient items;

    private final boolean canScare;

    public AnimalAITemptDistance(PathfinderMob pathfinderMob0, double double1, Ingredient ingredient2, boolean boolean3, double distance) {
        this.mob = pathfinderMob0;
        this.speedModifier = double1;
        this.items = ingredient2;
        this.canScare = boolean3;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.targetingConditions = TargetingConditions.forNonCombat().range(distance).ignoreLineOfSight().copy().selector(this::shouldFollow);
    }

    @Override
    public boolean canUse() {
        if (this.calmDown > 0) {
            this.calmDown--;
            return false;
        } else {
            this.player = this.mob.m_9236_().m_45946_(this.targetingConditions, this.mob);
            return this.player != null;
        }
    }

    private boolean shouldFollow(LivingEntity livingEntity0) {
        return this.items.test(livingEntity0.getMainHandItem()) || this.items.test(livingEntity0.getOffhandItem());
    }

    @Override
    public boolean canContinueToUse() {
        if (this.canScare()) {
            if (this.mob.m_20280_(this.player) < 36.0) {
                if (this.player.m_20275_(this.px, this.py, this.pz) > 0.010000000000000002) {
                    return false;
                }
                if (Math.abs((double) this.player.m_146909_() - this.pRotX) > 5.0 || Math.abs((double) this.player.m_146908_() - this.pRotY) > 5.0) {
                    return false;
                }
            } else {
                this.px = this.player.m_20185_();
                this.py = this.player.m_20186_();
                this.pz = this.player.m_20189_();
            }
            this.pRotX = (double) this.player.m_146909_();
            this.pRotY = (double) this.player.m_146908_();
        }
        return this.canUse();
    }

    protected boolean canScare() {
        return this.canScare;
    }

    @Override
    public void start() {
        this.px = this.player.m_20185_();
        this.py = this.player.m_20186_();
        this.pz = this.player.m_20189_();
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.player = null;
        this.mob.m_21573_().stop();
        this.calmDown = 100;
        this.isRunning = false;
    }

    @Override
    public void tick() {
        this.mob.m_21563_().setLookAt(this.player, (float) (this.mob.m_8085_() + 20), (float) this.mob.m_8132_());
        if (this.mob.m_20280_(this.player) < 6.25) {
            this.mob.m_21573_().stop();
        } else {
            this.mob.m_21573_().moveTo(this.player, this.speedModifier);
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}
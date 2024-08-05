package org.violetmoon.quark.content.tweaks.ai;

import java.util.EnumSet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class NuzzleGoal extends Goal {

    private final TamableAnimal creature;

    private LivingEntity owner;

    private final double followSpeed;

    private final PathNavigation petPathfinder;

    private int timeUntilRebuildPath;

    private final float maxDist;

    private final float whineDist;

    private int whineCooldown;

    private float oldWaterCost;

    private final SoundEvent whine;

    public NuzzleGoal(TamableAnimal creature, double followSpeed, float maxDist, float whineDist, SoundEvent whine) {
        this.creature = creature;
        this.followSpeed = followSpeed;
        this.petPathfinder = creature.m_21573_();
        this.maxDist = maxDist;
        this.whineDist = whineDist;
        this.whine = whine;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET));
        if (!(creature.m_21573_() instanceof GroundPathNavigation) && !(creature.m_21573_() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for NuzzleOwnerGoal");
        }
    }

    @Override
    public boolean canUse() {
        if (!WantLoveGoal.needsPets(this.creature)) {
            return false;
        } else {
            LivingEntity living = this.creature.m_269323_();
            if (living != null && !living.m_5833_() && !this.creature.isOrderedToSit()) {
                this.owner = living;
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !WantLoveGoal.needsPets(this.creature) ? false : !this.petPathfinder.isDone() && this.creature.m_20280_(this.owner) > (double) (this.maxDist * this.maxDist) && !this.creature.isOrderedToSit();
    }

    @Override
    public void start() {
        this.timeUntilRebuildPath = 0;
        this.whineCooldown = 10;
        this.oldWaterCost = this.creature.m_21439_(BlockPathTypes.WATER);
        this.creature.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.petPathfinder.stop();
        this.creature.m_21441_(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.creature.m_21563_().setLookAt(this.owner, 10.0F, (float) this.creature.m_8132_());
        if (!this.creature.isOrderedToSit() && --this.timeUntilRebuildPath <= 0) {
            this.timeUntilRebuildPath = 10;
            this.petPathfinder.moveTo(this.owner, this.followSpeed);
        }
        if (this.creature.m_20280_(this.owner) < (double) this.whineDist && --this.whineCooldown <= 0) {
            this.whineCooldown = 80 + this.creature.m_217043_().nextInt(40);
            this.creature.m_5496_(this.whine, 1.0F, 0.5F + (float) Math.random() * 0.5F);
        }
    }
}
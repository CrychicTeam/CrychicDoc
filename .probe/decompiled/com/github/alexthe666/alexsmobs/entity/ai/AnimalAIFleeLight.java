package com.github.alexthe666.alexsmobs.entity.ai;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AnimalAIFleeLight extends Goal {

    protected final PathfinderMob creature;

    private double shelterX;

    private double shelterY;

    private double shelterZ;

    private final double movementSpeed;

    private final Level world;

    private int executeChance = 50;

    private int lightLevel = 10;

    public AnimalAIFleeLight(PathfinderMob p_i1623_1_, double p_i1623_2_) {
        this.creature = p_i1623_1_;
        this.movementSpeed = p_i1623_2_;
        this.world = p_i1623_1_.m_9236_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    public AnimalAIFleeLight(PathfinderMob p_i1623_1_, double p_i1623_2_, int chance, int level) {
        this.creature = p_i1623_1_;
        this.movementSpeed = p_i1623_2_;
        this.world = p_i1623_1_.m_9236_();
        this.executeChance = chance;
        this.lightLevel = level;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.creature.m_5448_() != null || this.creature.m_217043_().nextInt(this.executeChance) != 0) {
            return false;
        } else {
            return this.world.m_46803_(this.creature.m_20183_()) < this.lightLevel ? false : this.isPossibleShelter();
        }
    }

    protected boolean isPossibleShelter() {
        Vec3 lvt_1_1_ = this.findPossibleShelter();
        if (lvt_1_1_ == null) {
            return false;
        } else {
            this.shelterX = lvt_1_1_.x;
            this.shelterY = lvt_1_1_.y;
            this.shelterZ = lvt_1_1_.z;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.creature.m_21573_().isDone();
    }

    @Override
    public void start() {
        this.creature.m_21573_().moveTo(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    @Nullable
    protected Vec3 findPossibleShelter() {
        RandomSource lvt_1_1_ = this.creature.m_217043_();
        BlockPos lvt_2_1_ = this.creature.m_20183_();
        for (int lvt_3_1_ = 0; lvt_3_1_ < 10; lvt_3_1_++) {
            BlockPos lvt_4_1_ = lvt_2_1_.offset(lvt_1_1_.nextInt(20) - 10, lvt_1_1_.nextInt(6) - 3, lvt_1_1_.nextInt(20) - 10);
            if (this.creature.m_9236_().m_46803_(lvt_4_1_) < this.lightLevel) {
                return Vec3.atBottomCenterOf(lvt_4_1_);
            }
        }
        return null;
    }
}
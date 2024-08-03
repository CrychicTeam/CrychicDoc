package com.github.alexthe666.alexsmobs.entity.ai;

import java.util.List;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;

public class AnimalAIFollowParentRanged extends Goal {

    private final Animal childAnimal;

    private Animal parentAnimal;

    private final double moveSpeed;

    private int delayCounter;

    private float range = 8.0F;

    private float minDist = 3.0F;

    public AnimalAIFollowParentRanged(Animal p_i1626_1_, double p_i1626_2_, float range, float minDist) {
        this.childAnimal = p_i1626_1_;
        this.moveSpeed = p_i1626_2_;
        this.range = range;
        this.minDist = minDist;
    }

    @Override
    public boolean canUse() {
        if (this.childAnimal.m_146764_() >= 0) {
            return false;
        } else {
            List<? extends Animal> lvt_1_1_ = this.childAnimal.m_9236_().m_45976_(this.childAnimal.getClass(), this.childAnimal.m_20191_().inflate((double) this.range, (double) this.range * 0.5, (double) this.range));
            Animal lvt_2_1_ = null;
            double lvt_3_1_ = Double.MAX_VALUE;
            for (Animal lvt_6_1_ : lvt_1_1_) {
                if (lvt_6_1_.m_146764_() >= 0) {
                    double lvt_7_1_ = this.childAnimal.m_20280_(lvt_6_1_);
                    if (lvt_7_1_ <= lvt_3_1_) {
                        lvt_3_1_ = lvt_7_1_;
                        lvt_2_1_ = lvt_6_1_;
                    }
                }
            }
            if (lvt_2_1_ == null) {
                return false;
            } else if (lvt_3_1_ < (double) (this.minDist * this.minDist)) {
                return false;
            } else {
                this.parentAnimal = lvt_2_1_;
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.childAnimal.m_146764_() >= 0) {
            return false;
        } else if (!this.parentAnimal.m_6084_()) {
            return false;
        } else {
            double lvt_1_1_ = this.childAnimal.m_20280_(this.parentAnimal);
            return lvt_1_1_ >= (double) (this.minDist * this.minDist) && lvt_1_1_ <= (double) (this.range * this.range);
        }
    }

    @Override
    public void start() {
        this.delayCounter = 0;
    }

    @Override
    public void stop() {
        this.parentAnimal = null;
    }

    @Override
    public void tick() {
        if (--this.delayCounter <= 0) {
            this.delayCounter = 10;
            this.childAnimal.m_21573_().moveTo(this.parentAnimal, this.moveSpeed);
        }
    }
}
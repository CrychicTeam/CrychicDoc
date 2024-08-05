package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.util.PackAnimal;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class AnimalJoinPackGoal extends Goal {

    public final LivingEntity entity;

    public final PackAnimal packAnimal;

    private int distCheckCounter;

    private int rate;

    private int packSize;

    public AnimalJoinPackGoal(LivingEntity animal, int rate, int packSize) {
        this.entity = animal;
        this.packAnimal = (PackAnimal) animal;
        this.rate = rate;
        this.packSize = packSize;
    }

    @Override
    public boolean canUse() {
        long worldTime = this.entity.m_9236_().getGameTime() % 10L;
        if (worldTime != 0L && this.entity.getRandom().nextInt(m_186073_(this.rate)) != 0) {
            return false;
        } else if (!this.packAnimal.isPackFollower() && !this.packAnimal.hasPackFollower()) {
            double dist = 30.0;
            List<? extends LivingEntity> list = this.entity.m_9236_().m_45976_(this.entity.getClass(), this.entity.m_20191_().inflate(dist, dist, dist));
            LivingEntity closestTail = null;
            double d0 = Double.MAX_VALUE;
            for (LivingEntity animal : list) {
                if (!((PackAnimal) animal).hasPackFollower() && ((PackAnimal) animal).isValidLeader(((PackAnimal) animal).getPackLeader()) && !animal.m_20148_().equals(this.entity.m_20148_()) && !((PackAnimal) animal).isInPack(this.packAnimal) && ((PackAnimal) animal).getPackSize() < this.packSize) {
                    double d1 = this.entity.m_20280_(animal);
                    if (!(d1 > d0)) {
                        d0 = d1;
                        closestTail = animal;
                    }
                }
            }
            if (closestTail == null) {
                return false;
            } else if (d0 < 1.0) {
                return false;
            } else if (!this.packAnimal.isValidLeader(((PackAnimal) closestTail).getPackLeader())) {
                return false;
            } else {
                this.packAnimal.joinPackOf((PackAnimal) closestTail);
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.packAnimal.isPackFollower() && this.packAnimal.isValidLeader(this.packAnimal.getPackLeader())) {
            double d0 = this.entity.m_20280_((LivingEntity) this.packAnimal.getPriorPackMember());
            if (d0 > 676.0 && this.distCheckCounter == 0) {
                return false;
            } else {
                if (this.distCheckCounter > 0) {
                    this.distCheckCounter--;
                }
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void stop() {
        this.packAnimal.leavePack();
    }

    @Override
    public void tick() {
    }
}
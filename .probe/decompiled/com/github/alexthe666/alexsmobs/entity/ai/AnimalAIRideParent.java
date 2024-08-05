package com.github.alexthe666.alexsmobs.entity.ai;

import java.util.List;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;

public class AnimalAIRideParent extends Goal {

    private final Animal childAnimal;

    private Animal parentAnimal;

    private final double moveSpeed;

    private int delayCounter;

    public AnimalAIRideParent(Animal animal, double speed) {
        this.childAnimal = animal;
        this.moveSpeed = speed;
    }

    @Override
    public boolean canUse() {
        if (this.childAnimal.m_146764_() < 0 && !this.childAnimal.m_20159_()) {
            List<? extends Animal> list = this.childAnimal.m_9236_().m_45976_(this.childAnimal.getClass(), this.childAnimal.m_20191_().inflate(8.0, 4.0, 8.0));
            Animal animalentity = null;
            double d0 = Double.MAX_VALUE;
            for (Animal animalentity1 : list) {
                if (animalentity1.m_146764_() >= 0 && animalentity1.m_20197_().isEmpty()) {
                    double d1 = this.childAnimal.m_20280_(animalentity1);
                    if (!(d1 > d0)) {
                        d0 = d1;
                        animalentity = animalentity1;
                    }
                }
            }
            if (animalentity == null) {
                return false;
            } else if (d0 < 2.0) {
                return false;
            } else {
                this.parentAnimal = animalentity;
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.childAnimal.m_146764_() >= 0) {
            return false;
        } else if (this.parentAnimal != null && this.parentAnimal.m_6084_() && this.parentAnimal.m_20197_().isEmpty()) {
            double d0 = this.childAnimal.m_20280_(this.parentAnimal);
            return !(d0 < 2.0) && !(d0 > 256.0) && !this.childAnimal.m_20365_(this.parentAnimal);
        } else {
            return false;
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
        if ((double) this.childAnimal.m_20270_(this.parentAnimal) < 2.0) {
            this.childAnimal.m_7998_(this.parentAnimal, false);
            this.stop();
        }
    }
}
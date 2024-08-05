package com.github.alexthe666.alexsmobs.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;

public class AnimalAIHurtByTargetNotBaby extends HurtByTargetGoal {

    private final Animal animal;

    public AnimalAIHurtByTargetNotBaby(Animal creatureIn, Class<?>... excludeReinforcementTypes) {
        super(creatureIn, excludeReinforcementTypes);
        this.animal = creatureIn;
    }

    @Override
    public void start() {
        super.start();
        if (this.animal.m_6162_()) {
            this.m_26047_();
            this.m_8041_();
        }
    }

    @Override
    protected void alertOther(Mob mobIn, LivingEntity targetIn) {
        if (!mobIn.m_6162_()) {
            super.alertOther(mobIn, targetIn);
        }
    }
}
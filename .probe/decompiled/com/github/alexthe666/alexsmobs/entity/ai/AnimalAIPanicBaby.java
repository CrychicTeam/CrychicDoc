package com.github.alexthe666.alexsmobs.entity.ai;

import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.animal.Animal;

public class AnimalAIPanicBaby extends PanicGoal {

    private final Animal animal;

    public AnimalAIPanicBaby(Animal creatureIn, double speed) {
        super(creatureIn, speed);
        this.animal = creatureIn;
    }

    @Override
    public boolean canUse() {
        return this.animal.m_6162_() && super.canUse();
    }
}
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.ITargetsDroppedItems;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;

public class FlyingAITempt extends TemptGoal {

    public FlyingAITempt(PathfinderMob mob, double speed, Ingredient ingredient, boolean skittish) {
        super(mob, speed, ingredient, skittish);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_25924_ instanceof ITargetsDroppedItems hasFlyingItemAI && this.f_25924_.m_20096_()) {
            hasFlyingItemAI.setFlying(false);
        }
    }
}
package com.github.alexthe666.alexsmobs.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.crafting.Ingredient;

public class TameableAITempt extends TemptGoal {

    private static final TargetingConditions DEF = TargetingConditions.forNonCombat().range(10.0).ignoreLineOfSight();

    private final Animal tameable;

    private int calmDown;

    private final TargetingConditions targetingConditions;

    private final Ingredient items;

    public TameableAITempt(Animal tameable, double speedIn, Ingredient temptItemsIn, boolean scaredByPlayerMovementIn) {
        super(tameable, speedIn, temptItemsIn, scaredByPlayerMovementIn);
        this.tameable = tameable;
        this.items = temptItemsIn;
        this.targetingConditions = DEF.copy().selector(this::shouldFollowAM);
    }

    public boolean shouldFollowAM(LivingEntity livingEntity0) {
        return this.items.test(livingEntity0.getMainHandItem()) || this.items.test(livingEntity0.getOffhandItem());
    }

    @Override
    public boolean canUse() {
        if (this.calmDown > 0) {
            this.calmDown--;
            return false;
        } else {
            this.f_25925_ = this.f_25924_.m_9236_().m_45946_(this.targetingConditions, this.f_25924_);
            return (!(this.tameable instanceof TamableAnimal) || !((TamableAnimal) this.tameable).isTame()) && this.f_25925_ != null;
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.calmDown = m_186073_(100);
    }
}
package com.mna.api.events.construct;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.Event;

public class ConstructSprayEffectEvent extends Event {

    final LivingEntity construct;

    final LivingEntity target;

    final Fluid fluid;

    final boolean isTargetOwner;

    final boolean isTargetFriendly;

    public ConstructSprayEffectEvent(LivingEntity construct, LivingEntity target, Fluid fluid, boolean isTargetOwner, boolean isTargetFriendly) {
        this.construct = construct;
        this.target = target;
        this.fluid = fluid;
        this.isTargetOwner = isTargetOwner;
        this.isTargetFriendly = isTargetFriendly;
    }

    public LivingEntity getConstruct() {
        return this.construct;
    }

    public LivingEntity getTarget() {
        return this.target;
    }

    public Fluid getFluid() {
        return this.fluid;
    }

    public boolean isTargetOwner() {
        return this.isTargetOwner;
    }

    public boolean isTargetFriendly() {
        return this.isTargetFriendly;
    }

    public boolean isCancelable() {
        return false;
    }
}
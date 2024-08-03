package com.mna.api.events.construct;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.Event;

public class ConstructSprayTargetingEvent extends Event {

    final Entity construct;

    final Entity potentialTarget;

    final Fluid fluid;

    final boolean isTargetOwner;

    final boolean isTargetFriendly;

    public ConstructSprayTargetingEvent(Entity construct, Entity target, Fluid fluid, boolean isTargetOwner, boolean isTargetFriendly) {
        this.construct = construct;
        this.potentialTarget = target;
        this.fluid = fluid;
        this.isTargetOwner = isTargetOwner;
        this.isTargetFriendly = isTargetFriendly;
    }

    public Entity getConstruct() {
        return this.construct;
    }

    public Entity getTarget() {
        return this.potentialTarget;
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
        return true;
    }
}
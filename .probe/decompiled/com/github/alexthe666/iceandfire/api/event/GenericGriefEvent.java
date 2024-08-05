package com.github.alexthe666.iceandfire.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class GenericGriefEvent extends LivingEvent {

    private final double targetX;

    private final double targetY;

    private final double targetZ;

    public GenericGriefEvent(LivingEntity griefer, double targetX, double targetY, double targetZ) {
        super(griefer);
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    public double getTargetX() {
        return this.targetX;
    }

    public double getTargetY() {
        return this.targetY;
    }

    public double getTargetZ() {
        return this.targetZ;
    }
}
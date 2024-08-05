package com.github.alexthe666.iceandfire.api.event;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class DragonFireDamageWorldEvent extends LivingEvent {

    private EntityDragonBase dragonBase;

    private double targetX;

    private double targetY;

    private double targetZ;

    public DragonFireDamageWorldEvent(EntityDragonBase dragonBase, double targetX, double targetY, double targetZ) {
        super(dragonBase);
        this.dragonBase = dragonBase;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    public EntityDragonBase getDragon() {
        return this.dragonBase;
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
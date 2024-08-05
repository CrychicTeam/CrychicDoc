package org.violetmoon.zetaimplforge.event.play.entity;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import org.violetmoon.zeta.event.play.entity.ZEntityTeleport;

public class ForgeZEntityTeleport implements ZEntityTeleport {

    private final EntityTeleportEvent e;

    public ForgeZEntityTeleport(EntityTeleportEvent e) {
        this.e = e;
    }

    @Override
    public Entity getEntity() {
        return this.e.getEntity();
    }

    @Override
    public double getTargetX() {
        return this.e.getTargetX();
    }

    @Override
    public double getTargetY() {
        return this.e.getTargetY();
    }

    @Override
    public double getTargetZ() {
        return this.e.getTargetZ();
    }

    @Override
    public void setTargetX(double targetX) {
        this.e.setTargetX(targetX);
    }

    @Override
    public void setTargetY(double targetY) {
        this.e.setTargetY(targetY);
    }

    @Override
    public void setTargetZ(double targetZ) {
        this.e.setTargetZ(targetZ);
    }
}
package org.violetmoon.zeta.event.play.entity;

import net.minecraft.world.entity.Entity;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZEntityTeleport extends IZetaPlayEvent {

    Entity getEntity();

    double getTargetX();

    double getTargetY();

    double getTargetZ();

    void setTargetX(double var1);

    void setTargetY(double var1);

    void setTargetZ(double var1);
}
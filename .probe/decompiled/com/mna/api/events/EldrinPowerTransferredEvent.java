package com.mna.api.events;

import com.mna.api.affinity.Affinity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;

@Cancelable
@HasResult
public class EldrinPowerTransferredEvent extends Event {

    final Affinity affinity;

    final float amount;

    final Vec3 from;

    final Vec3 to;

    public EldrinPowerTransferredEvent(Affinity affinity, float amount, Vec3 from, Vec3 to) {
        this.affinity = affinity;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }
}
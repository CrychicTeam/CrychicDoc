package com.mna.api.events.construct;

import com.mna.api.entities.construct.FluidParameterRegistry;
import net.minecraftforge.eventbus.api.Event;

public class FluidParameterRegistrationEvent extends Event {

    final FluidParameterRegistry registry;

    public FluidParameterRegistrationEvent(FluidParameterRegistry registry) {
        this.registry = registry;
    }

    public FluidParameterRegistry registry() {
        return this.registry;
    }
}
package com.mna.api.events;

import com.mna.api.capabilities.resource.ICastingResourceRegistry;
import net.minecraftforge.eventbus.api.Event;

public class CastingResourceRegistrationEvent extends Event {

    private final ICastingResourceRegistry _registry;

    public CastingResourceRegistrationEvent(ICastingResourceRegistry registry) {
        this._registry = registry;
    }

    public ICastingResourceRegistry getRegistry() {
        return this._registry;
    }
}
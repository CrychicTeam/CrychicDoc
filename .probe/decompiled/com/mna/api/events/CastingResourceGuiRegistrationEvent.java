package com.mna.api.events;

import com.mna.api.capabilities.resource.ICastingResourceGuiRegistry;
import net.minecraftforge.eventbus.api.Event;

public class CastingResourceGuiRegistrationEvent extends Event {

    private final ICastingResourceGuiRegistry _registry;

    public CastingResourceGuiRegistrationEvent(ICastingResourceGuiRegistry registry) {
        this._registry = registry;
    }

    public ICastingResourceGuiRegistry getRegistry() {
        return this._registry;
    }
}
package com.mna.api.guidebook;

import net.minecraftforge.eventbus.api.Event;

public class RegisterGuidebooksEvent extends Event {

    private IGuideBookRegistry registry;

    public RegisterGuidebooksEvent(IGuideBookRegistry registry) {
        this.registry = registry;
    }

    public IGuideBookRegistry getRegistry() {
        return this.registry;
    }
}
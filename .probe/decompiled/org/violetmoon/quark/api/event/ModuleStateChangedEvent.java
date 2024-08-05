package org.violetmoon.quark.api.event;

import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class ModuleStateChangedEvent extends QuarkModuleEvent {

    public final boolean enabled;

    public ModuleStateChangedEvent(String eventName, boolean enabled) {
        super(eventName);
        this.enabled = enabled;
    }
}
package com.craisinlord.integrated_api.events.lifecycle;

import com.craisinlord.integrated_api.events.base.EventHandler;
import net.minecraft.core.RegistryAccess;

public record TagsUpdatedEvent(RegistryAccess registryAccess, boolean fromPacket) {

    public static final EventHandler<TagsUpdatedEvent> EVENT = new EventHandler<>();
}
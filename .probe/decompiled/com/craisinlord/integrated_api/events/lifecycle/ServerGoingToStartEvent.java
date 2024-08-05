package com.craisinlord.integrated_api.events.lifecycle;

import com.craisinlord.integrated_api.events.base.EventHandler;
import net.minecraft.server.MinecraftServer;

public record ServerGoingToStartEvent(MinecraftServer server) {

    public static final EventHandler<ServerGoingToStartEvent> EVENT = new EventHandler<>();

    public MinecraftServer getServer() {
        return this.server;
    }
}
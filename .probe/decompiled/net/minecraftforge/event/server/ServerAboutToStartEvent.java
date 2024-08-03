package net.minecraftforge.event.server;

import net.minecraft.server.MinecraftServer;

public class ServerAboutToStartEvent extends ServerLifecycleEvent {

    public ServerAboutToStartEvent(MinecraftServer server) {
        super(server);
    }
}
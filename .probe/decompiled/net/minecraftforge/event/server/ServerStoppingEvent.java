package net.minecraftforge.event.server;

import net.minecraft.server.MinecraftServer;

public class ServerStoppingEvent extends ServerLifecycleEvent {

    public ServerStoppingEvent(MinecraftServer server) {
        super(server);
    }
}
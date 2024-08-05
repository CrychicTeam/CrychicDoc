package net.blay09.mods.balm.api.event.server;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerResources;

public class ServerReloadedEvent extends BalmEvent {

    private final MinecraftServer server = Balm.getHooks().getServer();

    private final ReloadableServerResources resources;

    public ServerReloadedEvent(ReloadableServerResources resources) {
        this.resources = resources;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    public ReloadableServerResources getResources() {
        return this.resources;
    }
}
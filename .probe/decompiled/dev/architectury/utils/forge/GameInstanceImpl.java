package dev.architectury.utils.forge;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;

public class GameInstanceImpl {

    public static MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }
}
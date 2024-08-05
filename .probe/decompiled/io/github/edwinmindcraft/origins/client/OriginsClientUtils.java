package io.github.edwinmindcraft.origins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class OriginsClientUtils {

    public static Level getClientLevel() {
        return Minecraft.getInstance().level;
    }

    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
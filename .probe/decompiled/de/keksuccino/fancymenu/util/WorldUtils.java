package de.keksuccino.fancymenu.util;

import net.minecraft.client.Minecraft;

public class WorldUtils {

    public static boolean isSingleplayer() {
        return Minecraft.getInstance().level == null ? false : Minecraft.getInstance().hasSingleplayerServer() && Minecraft.getInstance().getSingleplayerServer() != null && !Minecraft.getInstance().getSingleplayerServer().isPublished();
    }

    public static boolean isMultiplayer() {
        return Minecraft.getInstance().level == null ? false : !isSingleplayer();
    }
}
package com.mrcrayfish.configured.client;

import com.mrcrayfish.configured.util.ConfigHelper;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;

public class SessionData {

    private static boolean developer;

    private static boolean lan;

    public static void setDeveloper(boolean enabled) {
        developer = enabled;
    }

    public static boolean isDeveloper(@Nullable Player player) {
        return developer || ConfigHelper.isServerOwnedByPlayer(player);
    }

    public static void setLan(boolean lan) {
        SessionData.lan = lan;
    }

    public static boolean isLan() {
        return lan;
    }
}
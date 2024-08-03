package dev.ftb.mods.ftblibrary.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.ftb.mods.ftblibrary.util.forge.PlayerDisplayNameUtilImpl;
import net.minecraft.world.entity.player.Player;

public class PlayerDisplayNameUtil {

    @ExpectPlatform
    @Transformed
    public static void refreshDisplayName(Player player) {
        PlayerDisplayNameUtilImpl.refreshDisplayName(player);
    }
}
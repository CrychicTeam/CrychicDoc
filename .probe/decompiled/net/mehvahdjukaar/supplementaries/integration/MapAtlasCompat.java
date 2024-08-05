package net.mehvahdjukaar.supplementaries.integration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.supplementaries.integration.forge.MapAtlasCompatImpl;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Contract;

public class MapAtlasCompat {

    @ExpectPlatform
    @Contract
    @Transformed
    public static boolean canPlayerSeeDeathMarker(Player player) {
        return MapAtlasCompatImpl.canPlayerSeeDeathMarker(player);
    }
}
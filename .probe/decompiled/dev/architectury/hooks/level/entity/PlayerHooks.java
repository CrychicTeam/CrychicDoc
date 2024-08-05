package dev.architectury.hooks.level.entity;

import dev.architectury.hooks.level.entity.forge.PlayerHooksImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.minecraft.world.entity.player.Player;

public final class PlayerHooks {

    private PlayerHooks() {
    }

    @ExpectPlatform
    @Transformed
    public static boolean isFake(Player player) {
        return PlayerHooksImpl.isFake(player);
    }
}
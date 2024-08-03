package dev.architectury.hooks.level.entity.forge;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;

public class PlayerHooksImpl {

    public static boolean isFake(Player playerEntity) {
        return playerEntity instanceof FakePlayer;
    }
}
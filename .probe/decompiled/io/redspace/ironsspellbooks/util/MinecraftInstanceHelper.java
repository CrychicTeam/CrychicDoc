package io.redspace.ironsspellbooks.util;

import java.util.function.Consumer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class MinecraftInstanceHelper implements IMinecraftInstanceHelper {

    public static IMinecraftInstanceHelper instance = () -> null;

    @Nullable
    @Override
    public Player player() {
        return instance.player();
    }

    @Nullable
    public static Player getPlayer() {
        return instance.player();
    }

    public static void ifPlayerPresent(Consumer<Player> consumer) {
        Player player = getPlayer();
        if (player != null) {
            consumer.accept(player);
        }
    }
}
package software.bernie.geckolib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class ClientUtils {

    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static Level getLevel() {
        return Minecraft.getInstance().level;
    }
}
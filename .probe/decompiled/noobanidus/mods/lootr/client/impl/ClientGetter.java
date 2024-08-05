package noobanidus.mods.lootr.client.impl;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientGetter {

    @Nullable
    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }
}
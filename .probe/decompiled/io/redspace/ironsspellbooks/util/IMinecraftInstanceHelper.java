package io.redspace.ironsspellbooks.util;

import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;

public interface IMinecraftInstanceHelper {

    @Nullable
    Player player();
}
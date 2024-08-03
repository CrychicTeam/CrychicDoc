package io.github.lightman314.lightmanscurrency.api.misc.blockentity;

import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;

public interface IOwnableBlockEntity {

    boolean canBreak(@Nullable Player var1);
}
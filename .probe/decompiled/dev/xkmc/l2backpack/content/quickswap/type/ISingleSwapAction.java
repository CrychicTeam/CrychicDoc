package dev.xkmc.l2backpack.content.quickswap.type;

import dev.xkmc.l2backpack.content.quickswap.entry.ISingleSwapHandler;
import net.minecraft.world.entity.player.Player;

public interface ISingleSwapAction {

    void swapSingle(Player var1, ISingleSwapHandler var2);
}
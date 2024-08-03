package dev.xkmc.l2backpack.content.quickswap.type;

import dev.xkmc.l2backpack.content.quickswap.entry.ISetSwapHandler;
import net.minecraft.world.entity.player.Player;

public interface ISetSwapAction {

    void swapSet(Player var1, ISetSwapHandler var2);
}
package dev.xkmc.l2backpack.content.quickswap.type;

import dev.xkmc.l2backpack.content.quickswap.entry.ISwapEntry;
import dev.xkmc.l2library.base.overlay.SelectionSideBar;
import net.minecraft.world.entity.player.Player;

public interface ISideInfoRenderer {

    void renderSide(SelectionSideBar.Context var1, int var2, int var3, Player var4, ISwapEntry<?> var5);
}
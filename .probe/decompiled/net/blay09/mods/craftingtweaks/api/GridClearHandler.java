package net.blay09.mods.craftingtweaks.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface GridClearHandler<TMenu extends AbstractContainerMenu> {

    void clearGrid(CraftingGrid var1, Player var2, TMenu var3, boolean var4);
}
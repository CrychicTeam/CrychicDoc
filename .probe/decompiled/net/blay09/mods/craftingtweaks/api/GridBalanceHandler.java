package net.blay09.mods.craftingtweaks.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface GridBalanceHandler<TMenu extends AbstractContainerMenu> {

    void balanceGrid(CraftingGrid var1, Player var2, TMenu var3);

    void spreadGrid(CraftingGrid var1, Player var2, TMenu var3);
}
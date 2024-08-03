package net.blay09.mods.craftingtweaks.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public interface GridTransferHandler<TMenu extends AbstractContainerMenu> {

    ItemStack putIntoGrid(CraftingGrid var1, Player var2, AbstractContainerMenu var3, int var4, ItemStack var5);

    boolean transferIntoGrid(CraftingGrid var1, Player var2, TMenu var3, Slot var4);

    boolean canTransferFrom(Player var1, TMenu var2, Slot var3, CraftingGrid var4);
}
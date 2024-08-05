package dev.xkmc.l2backpack.content.click;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public interface VanillaQuickInsert {

    void l2backpack$quickMove(ServerPlayer var1, AbstractContainerMenu var2, ItemStack var3, int var4);
}
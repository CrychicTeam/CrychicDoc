package noobanidus.mods.lootr.api;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

@FunctionalInterface
public interface MenuBuilder {

    AbstractContainerMenu build(int var1, Inventory var2, Container var3, int var4);
}
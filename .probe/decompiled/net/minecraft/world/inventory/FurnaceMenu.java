package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeType;

public class FurnaceMenu extends AbstractFurnaceMenu {

    public FurnaceMenu(int int0, Inventory inventory1) {
        super(MenuType.FURNACE, RecipeType.SMELTING, RecipeBookType.FURNACE, int0, inventory1);
    }

    public FurnaceMenu(int int0, Inventory inventory1, Container container2, ContainerData containerData3) {
        super(MenuType.FURNACE, RecipeType.SMELTING, RecipeBookType.FURNACE, int0, inventory1, container2, containerData3);
    }
}
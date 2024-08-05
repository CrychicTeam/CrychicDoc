package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeType;

public class BlastFurnaceMenu extends AbstractFurnaceMenu {

    public BlastFurnaceMenu(int int0, Inventory inventory1) {
        super(MenuType.BLAST_FURNACE, RecipeType.BLASTING, RecipeBookType.BLAST_FURNACE, int0, inventory1);
    }

    public BlastFurnaceMenu(int int0, Inventory inventory1, Container container2, ContainerData containerData3) {
        super(MenuType.BLAST_FURNACE, RecipeType.BLASTING, RecipeBookType.BLAST_FURNACE, int0, inventory1, container2, containerData3);
    }
}
package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeType;

public class SmokerMenu extends AbstractFurnaceMenu {

    public SmokerMenu(int int0, Inventory inventory1) {
        super(MenuType.SMOKER, RecipeType.SMOKING, RecipeBookType.SMOKER, int0, inventory1);
    }

    public SmokerMenu(int int0, Inventory inventory1, Container container2, ContainerData containerData3) {
        super(MenuType.SMOKER, RecipeType.SMOKING, RecipeBookType.SMOKER, int0, inventory1, container2, containerData3);
    }
}
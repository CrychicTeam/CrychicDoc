package net.minecraftforge.common.crafting;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;

public interface IRecipeContainer {

    ResultContainer getCraftResult();

    CraftingContainer getCraftMatrix();
}
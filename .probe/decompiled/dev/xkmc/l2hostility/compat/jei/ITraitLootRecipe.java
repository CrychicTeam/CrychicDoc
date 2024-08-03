package dev.xkmc.l2hostility.compat.jei;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public interface ITraitLootRecipe {

    List<ItemStack> getResults();

    List<ItemStack> getCurioRequired();

    List<ItemStack> getInputs();

    void addTooltip(List<Component> var1);
}
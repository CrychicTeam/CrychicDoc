package org.violetmoon.quark.integration.jei;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.tweaks.module.SlabsToBlocksModule;
import org.violetmoon.quark.content.tweaks.recipe.SlabToBlockRecipe;

public record SlabToBlockExtension(SlabToBlockRecipe recipe) implements ICraftingCategoryExtension {

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ICraftingGridHelper craftingGridHelper, @NotNull IFocusGroup focuses) {
        List<ItemStack> input1 = Lists.newArrayList();
        List<ItemStack> input2 = Lists.newArrayList();
        List<ItemStack> outputs = Lists.newArrayList();
        for (Entry<Item, Item> recipe : SlabsToBlocksModule.recipes.entrySet()) {
            input1.add(new ItemStack((ItemLike) recipe.getKey()));
            input2.add(new ItemStack((ItemLike) recipe.getKey()));
            outputs.add(new ItemStack((ItemLike) recipe.getValue()));
        }
        List<IRecipeSlotBuilder> gridSlots = craftingGridHelper.createAndSetInputs(builder, Arrays.asList(input1, input2), 0, 0);
        IRecipeSlotBuilder outSlot = craftingGridHelper.createAndSetOutputs(builder, outputs);
        builder.createFocusLink((IRecipeSlotBuilder) gridSlots.get(0), (IRecipeSlotBuilder) gridSlots.get(1), outSlot);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return this.recipe.m_6423_();
    }
}
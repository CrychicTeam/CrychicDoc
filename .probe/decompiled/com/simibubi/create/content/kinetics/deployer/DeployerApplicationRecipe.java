package com.simibubi.create.content.kinetics.deployer;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DeployerApplicationRecipe extends ItemApplicationRecipe implements IAssemblyRecipe {

    public DeployerApplicationRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(AllRecipeTypes.DEPLOYING, params);
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    public static DeployerApplicationRecipe convert(Recipe<?> sandpaperRecipe) {
        return new ProcessingRecipeBuilder<>(DeployerApplicationRecipe::new, new ResourceLocation(sandpaperRecipe.getId().getNamespace(), sandpaperRecipe.getId().getPath() + "_using_deployer")).require(sandpaperRecipe.getIngredients().get(0)).require(AllTags.AllItemTags.SANDPAPER.tag).output(sandpaperRecipe.getResultItem(Minecraft.getInstance().level.m_9598_())).build();
    }

    @Override
    public void addAssemblyIngredients(List<Ingredient> list) {
        list.add(this.ingredients.get(1));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getDescriptionForAssembly() {
        ItemStack[] matchingStacks = this.ingredients.get(1).getItems();
        return matchingStacks.length == 0 ? Components.literal("Invalid") : Lang.translateDirect("recipe.assembly.deploying_item", Components.translatable(matchingStacks[0].getDescriptionId()).getString());
    }

    @Override
    public void addRequiredMachines(Set<ItemLike> list) {
        list.add((ItemLike) AllBlocks.DEPLOYER.get());
    }

    @Override
    public Supplier<Supplier<SequencedAssemblySubCategory>> getJEISubCategory() {
        return () -> SequencedAssemblySubCategory.AssemblyDeploying::new;
    }
}
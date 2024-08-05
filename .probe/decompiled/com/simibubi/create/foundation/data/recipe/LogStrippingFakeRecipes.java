package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class LogStrippingFakeRecipes {

    public static List<ManualApplicationRecipe> createRecipes() {
        List<ManualApplicationRecipe> recipes = new ArrayList();
        if (!AllConfigs.server().recipes.displayLogStrippingRecipes.get()) {
            return recipes;
        } else {
            ItemStack axe = new ItemStack(Items.IRON_AXE);
            axe.hideTooltipPart(ItemStack.TooltipPart.MODIFIERS);
            axe.setHoverName(Lang.translateDirect("recipe.item_application.any_axe").withStyle(style -> style.withItalic(false)));
            ITagManager<Item> tags = ForgeRegistries.ITEMS.tags();
            tags.getTag(ItemTags.LOGS).forEach(stack -> process(stack, recipes, axe));
            return recipes;
        }
    }

    private static void process(Item item, List<ManualApplicationRecipe> list, ItemStack axe) {
        if (item instanceof BlockItem blockItem) {
            BlockState state = blockItem.getBlock().defaultBlockState();
            BlockState strippedState = AxeItem.getAxeStrippingState(state);
            if (strippedState != null) {
                Item resultItem = strippedState.m_60734_().asItem();
                if (resultItem != null) {
                    list.add(create(item, resultItem, axe));
                }
            }
        }
    }

    private static ManualApplicationRecipe create(Item fromItem, Item toItem, ItemStack axe) {
        ResourceLocation rn = RegisteredObjects.getKeyOrThrow(toItem);
        return new ProcessingRecipeBuilder<>(ManualApplicationRecipe::new, new ResourceLocation(rn.getNamespace(), rn.getPath() + "_via_vanilla_stripping")).require(fromItem).require(Ingredient.of(axe)).output(toItem).build();
    }
}
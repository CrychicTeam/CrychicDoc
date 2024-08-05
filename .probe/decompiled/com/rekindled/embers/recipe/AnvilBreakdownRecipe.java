package com.rekindled.embers.recipe;

import com.google.gson.JsonObject;
import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.datagen.EmbersItemTags;
import com.rekindled.embers.util.Misc;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AnvilBreakdownRecipe implements IDawnstoneAnvilRecipe, IVisuallySplitRecipe<IDawnstoneAnvilRecipe> {

    public static final AnvilBreakdownRecipe.Serializer SERIALIZER = new AnvilBreakdownRecipe.Serializer();

    public final ResourceLocation id;

    public static List<IDawnstoneAnvilRecipe> visualRecipes = new ArrayList();

    Ingredient blacklist = Ingredient.of(EmbersItemTags.BREAKDOWN_BLACKLIST);

    public AnvilBreakdownRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public boolean matches(Container context, Level pLevel) {
        ItemStack tool = context.getItem(0);
        return tool.isRepairable() && !this.blacklist.test(tool) && context.getItem(1).isEmpty() && !AugmentUtil.hasHeat(tool);
    }

    @Override
    public List<ItemStack> getOutput(Container context) {
        Ingredient repairMaterial = Misc.getRepairIngredient(context.getItem(0).getItem());
        return !repairMaterial.isEmpty() ? List.of(Misc.getPreferredItem(repairMaterial.getItems())) : List.of();
    }

    @Override
    public List<IDawnstoneAnvilRecipe> getVisualRecipes() {
        visualRecipes.clear();
        for (Holder<Item> holder : BuiltInRegistries.ITEM.m_206115_()) {
            Ingredient repairMaterial = Misc.getRepairIngredient((Item) holder.get());
            ItemStack toolStack = new ItemStack((ItemLike) holder.get());
            if (!repairMaterial.isEmpty() && toolStack.isRepairable() && !this.blacklist.test(toolStack)) {
                ItemStack brokenTool = toolStack.copy();
                brokenTool.setDamageValue(brokenTool.getMaxDamage() / 2);
                visualRecipes.add(new AnvilDisplayRecipe(this.id, List.of(Misc.getPreferredItem(repairMaterial.getItems())), List.of(brokenTool), Ingredient.EMPTY));
            }
        }
        return visualRecipes;
    }

    @Override
    public List<ItemStack> getDisplayInputBottom() {
        return List.of();
    }

    @Override
    public List<ItemStack> getDisplayInputTop() {
        return List.of();
    }

    @Override
    public List<ItemStack> getDisplayOutput() {
        return List.of();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<AnvilBreakdownRecipe> {

        public AnvilBreakdownRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            return new AnvilBreakdownRecipe(recipeId);
        }

        @Nullable
        public AnvilBreakdownRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return new AnvilBreakdownRecipe(recipeId);
        }

        public void toNetwork(FriendlyByteBuf buffer, AnvilBreakdownRecipe recipe) {
        }
    }
}
package com.rekindled.embers.recipe;

import com.google.gson.JsonObject;
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

public class AnvilRepairRecipe implements IDawnstoneAnvilRecipe, IVisuallySplitRecipe<IDawnstoneAnvilRecipe> {

    public static final AnvilRepairRecipe.Serializer SERIALIZER = new AnvilRepairRecipe.Serializer();

    public final ResourceLocation id;

    public static List<IDawnstoneAnvilRecipe> visualRecipes = new ArrayList();

    Ingredient blacklist = Ingredient.of(EmbersItemTags.REPAIR_BLACKLIST);

    public AnvilRepairRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public boolean matches(Container context, Level pLevel) {
        ItemStack tool = context.getItem(0);
        return tool.isRepairable() && tool.isDamaged() && !this.blacklist.test(tool) && tool.getItem().isValidRepairItem(tool, context.getItem(1));
    }

    @Override
    public List<ItemStack> getOutput(Container context) {
        ItemStack result = context.getItem(0).copy();
        result.setDamageValue(Math.max(0, result.getDamageValue() - result.getMaxDamage()));
        return List.of(result);
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
                visualRecipes.add(new AnvilDisplayRecipe(this.id, List.of(toolStack), List.of(brokenTool), repairMaterial));
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

    public static class Serializer implements RecipeSerializer<AnvilRepairRecipe> {

        public AnvilRepairRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            return new AnvilRepairRecipe(recipeId);
        }

        @Nullable
        public AnvilRepairRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return new AnvilRepairRecipe(recipeId);
        }

        public void toNetwork(FriendlyByteBuf buffer, AnvilRepairRecipe recipe) {
        }
    }
}
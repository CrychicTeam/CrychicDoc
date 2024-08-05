package com.rekindled.embers.recipe;

import com.google.gson.JsonObject;
import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.api.augment.IAugment;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AnvilAugmentRecipe implements IDawnstoneAnvilRecipe, IVisuallySplitRecipe<IDawnstoneAnvilRecipe> {

    public static final AnvilAugmentRecipe.Serializer SERIALIZER = new AnvilAugmentRecipe.Serializer();

    public final ResourceLocation id;

    public final Ingredient tool;

    public final Ingredient input;

    public final IAugment augment;

    public static List<IDawnstoneAnvilRecipe> visualRecipes = new ArrayList();

    public AnvilAugmentRecipe(ResourceLocation id, Ingredient tool, Ingredient input, IAugment augment) {
        this.id = id;
        this.tool = tool;
        this.input = input;
        this.augment = augment;
    }

    @Override
    public boolean matches(Container context, Level pLevel) {
        if (this.augment.countTowardsTotalLevel()) {
            ItemStack toolStack = context.getItem(0);
            return this.tool.test(toolStack) && this.input.test(context.getItem(1)) && AugmentUtil.getLevel(toolStack) > AugmentUtil.getTotalAugmentLevel(toolStack);
        } else {
            return this.tool.test(context.getItem(0)) && this.input.test(context.getItem(1));
        }
    }

    @Override
    public List<ItemStack> getOutput(Container context) {
        ItemStack result = context.getItem(0).copy();
        AugmentUtil.addAugment(result, context.getItem(1), this.augment);
        return List.of(result);
    }

    @Override
    public List<IDawnstoneAnvilRecipe> getVisualRecipes() {
        visualRecipes.clear();
        for (ItemStack stack : this.tool.getItems()) {
            ItemStack leveledTool = stack.copy();
            if (this.augment.countTowardsTotalLevel()) {
                AugmentUtil.setLevel(leveledTool, AugmentUtil.getLevel(leveledTool) + 1);
            }
            ItemStack augmentedTool = leveledTool.copy();
            AugmentUtil.addAugment(augmentedTool, ItemStack.EMPTY, this.augment);
            visualRecipes.add(new AnvilDisplayRecipe(this.id, List.of(augmentedTool), List.of(leveledTool), this.input));
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

    public static class Serializer implements RecipeSerializer<AnvilAugmentRecipe> {

        public AnvilAugmentRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient tool = Ingredient.fromJson(json.get("tool"));
            Ingredient input = Ingredient.fromJson(json.get("input"));
            IAugment augment = AugmentUtil.getAugment(new ResourceLocation(GsonHelper.getAsString(json, "augment")));
            return new AnvilAugmentRecipe(recipeId, tool, input, augment);
        }

        @Nullable
        public AnvilAugmentRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient tool = Ingredient.fromNetwork(buffer);
            Ingredient input = Ingredient.fromNetwork(buffer);
            IAugment augment = AugmentUtil.getAugment(buffer.readResourceLocation());
            return new AnvilAugmentRecipe(recipeId, tool, input, augment);
        }

        public void toNetwork(FriendlyByteBuf buffer, AnvilAugmentRecipe recipe) {
            recipe.tool.toNetwork(buffer);
            recipe.input.toNetwork(buffer);
            buffer.writeResourceLocation(recipe.augment.getName());
        }
    }
}
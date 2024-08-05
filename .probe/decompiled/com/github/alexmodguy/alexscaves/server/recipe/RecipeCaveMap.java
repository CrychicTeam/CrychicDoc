package com.github.alexmodguy.alexscaves.server.recipe;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.CaveInfoItem;
import com.github.alexmodguy.alexscaves.server.item.CaveMapItem;
import com.github.alexthe666.citadel.recipe.SpecialRecipeInGuideBook;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.biome.Biome;

public class RecipeCaveMap extends ShapedRecipe implements SpecialRecipeInGuideBook {

    public RecipeCaveMap(ResourceLocation name, CraftingBookCategory category) {
        super(name, "", category, 3, 3, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(ACItemRegistry.CAVE_CODEX.get()), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER), Ingredient.of(Items.PAPER)), new ItemStack(ACItemRegistry.CAVE_MAP.get()));
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack scroll = ItemStack.EMPTY;
        for (int i = 0; i <= container.m_6643_(); i++) {
            if (!container.m_8020_(i).isEmpty() && container.m_8020_(i).is(ACItemRegistry.CAVE_CODEX.get()) && scroll.isEmpty()) {
                scroll = container.m_8020_(i);
            }
        }
        ResourceKey<Biome> key = CaveInfoItem.getCaveBiome(scroll);
        return key != null ? CaveMapItem.createMap(key) : ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ACRecipeRegistry.CAVE_MAP.get();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getDisplayIngredients() {
        return this.m_7527_();
    }

    @Override
    public ItemStack getDisplayResultFor(NonNullList<ItemStack> nonNullList) {
        ItemStack scroll = ItemStack.EMPTY;
        for (int i = 0; i <= nonNullList.size(); i++) {
            if (!nonNullList.get(i).isEmpty() && nonNullList.get(i).is(ACItemRegistry.CAVE_CODEX.get()) && scroll.isEmpty()) {
                scroll = nonNullList.get(i);
            }
        }
        ResourceKey<Biome> key = CaveInfoItem.getCaveBiome(scroll);
        return key != null ? CaveMapItem.createMap(key) : ItemStack.EMPTY;
    }
}
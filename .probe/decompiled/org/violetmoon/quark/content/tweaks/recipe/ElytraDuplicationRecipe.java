package org.violetmoon.quark.content.tweaks.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.tweaks.module.DragonScalesModule;

public class ElytraDuplicationRecipe extends CustomRecipe {

    public static final SimpleCraftingRecipeSerializer<?> SERIALIZER = new SimpleCraftingRecipeSerializer(ElytraDuplicationRecipe::new);

    public ElytraDuplicationRecipe(ResourceLocation id, CraftingBookCategory cat) {
        super(id, cat);
    }

    public boolean matches(@NotNull CraftingContainer var1, @NotNull Level var2) {
        int sources = 0;
        boolean foundTarget = false;
        for (int i = 0; i < var1.m_6643_(); i++) {
            ItemStack stack = var1.m_8020_(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ElytraItem) {
                    if (foundTarget) {
                        return false;
                    }
                    foundTarget = true;
                } else {
                    if (stack.getItem() != DragonScalesModule.dragon_scale) {
                        return false;
                    }
                    if (sources >= 1) {
                        return false;
                    }
                    sources++;
                }
            }
        }
        return sources == 1 && foundTarget;
    }

    @NotNull
    public ItemStack assemble(@NotNull CraftingContainer var1, RegistryAccess gaming) {
        return this.getResultItem(gaming);
    }

    @NotNull
    @Override
    public ItemStack getResultItem(RegistryAccess gaming) {
        return new ItemStack(Items.ELYTRA);
    }

    @NotNull
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> ret = NonNullList.withSize(inv.m_6643_(), ItemStack.EMPTY);
        for (int i = 0; i < inv.m_6643_(); i++) {
            ItemStack stack = inv.m_8020_(i);
            if (stack.getItem() == Items.ELYTRA) {
                ret.set(i, stack.copy());
            }
        }
        return ret;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @NotNull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.withSize(2, Ingredient.EMPTY);
        list.set(0, Ingredient.of(new ItemStack(Items.ELYTRA)));
        list.set(1, Ingredient.of(new ItemStack(DragonScalesModule.dragon_scale)));
        return list;
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
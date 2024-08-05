package com.mna.items.runes;

import com.mna.api.items.TieredItem;
import com.mna.recipes.runeforging.RunescribingRecipe;
import com.mna.recipes.runeforging.RunescribingRecipeSerializer;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public class ItemRunescribingRecipe extends TieredItem {

    private static final String NBT_RECIPEID = "runescribe_recipe_id";

    public ItemRunescribingRecipe() {
        super(new Item.Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        RunescribingRecipe recipe = this.getRecipe(stack, worldIn);
        if (recipe != null && recipe.getResultItem() != null && !recipe.getResultItem().isEmpty()) {
            tooltip.add(recipe.getResultItem().getHoverName());
        }
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        if (RunescribingRecipeSerializer.ALL_RECIPES.size() > 0) {
            setRecipe(stack, (RunescribingRecipe) RunescribingRecipeSerializer.ALL_RECIPES.values().iterator().next());
        }
        return stack;
    }

    @Nullable
    public RunescribingRecipe getRecipe(ItemStack stack, Level world) {
        if (world != null && world.getRecipeManager() != null && stack.hasTag() && stack.getItem() == this) {
            CompoundTag nbt = stack.getTag();
            if (!nbt.contains("runescribe_recipe_id")) {
                return null;
            } else {
                ResourceLocation rLoc = new ResourceLocation(nbt.get("runescribe_recipe_id").getAsString());
                Optional<? extends Recipe<?>> optRecipe = world.getRecipeManager().byKey(rLoc);
                return optRecipe.isPresent() && optRecipe.get() instanceof RunescribingRecipe ? (RunescribingRecipe) optRecipe.get() : null;
            }
        } else {
            return null;
        }
    }

    public static void setRecipe(ItemStack stack, RunescribingRecipe recipe) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putString("runescribe_recipe_id", recipe.m_6423_().toString());
    }
}
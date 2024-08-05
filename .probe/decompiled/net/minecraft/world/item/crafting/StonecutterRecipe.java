package net.minecraft.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class StonecutterRecipe extends SingleItemRecipe {

    public StonecutterRecipe(ResourceLocation resourceLocation0, String string1, Ingredient ingredient2, ItemStack itemStack3) {
        super(RecipeType.STONECUTTING, RecipeSerializer.STONECUTTER, resourceLocation0, string1, ingredient2, itemStack3);
    }

    @Override
    public boolean matches(Container container0, Level level1) {
        return this.f_44409_.test(container0.getItem(0));
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.STONECUTTER);
    }
}
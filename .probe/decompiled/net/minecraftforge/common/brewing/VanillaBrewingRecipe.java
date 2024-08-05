package net.minecraftforge.common.brewing;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;

public class VanillaBrewingRecipe implements IBrewingRecipe {

    @Override
    public boolean isInput(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.POTION || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION || item == Items.GLASS_BOTTLE;
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return PotionBrewing.isIngredient(stack);
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!input.isEmpty() && !ingredient.isEmpty() && this.isIngredient(ingredient)) {
            ItemStack result = PotionBrewing.mix(ingredient, input);
            return result != input ? result : ItemStack.EMPTY;
        } else {
            return ItemStack.EMPTY;
        }
    }
}
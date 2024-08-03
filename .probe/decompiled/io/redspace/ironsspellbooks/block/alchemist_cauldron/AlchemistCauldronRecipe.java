package io.redspace.ironsspellbooks.block.alchemist_cauldron;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

public class AlchemistCauldronRecipe {

    private final ItemStack inputStack;

    private final ItemStack ingredientStack;

    private final ItemStack resultStack;

    private int requiredBaseCount = 1;

    private int resultLimitCount = 4;

    public AlchemistCauldronRecipe(ItemStack inputStack, ItemStack ingredientStack, ItemStack resultStack) {
        this.inputStack = inputStack;
        this.ingredientStack = ingredientStack;
        this.resultStack = resultStack;
    }

    public AlchemistCauldronRecipe(Item input, Item ingredient, Item result) {
        this(new ItemStack(input), new ItemStack(ingredient), new ItemStack(result));
    }

    public AlchemistCauldronRecipe(Potion input, Item ingredient, Item result) {
        this(PotionUtils.setPotion(new ItemStack(Items.POTION), input), new ItemStack(ingredient), new ItemStack(result));
    }

    public AlchemistCauldronRecipe setBaseRequirement(int i) {
        this.requiredBaseCount = i;
        return this;
    }

    public AlchemistCauldronRecipe setResultLimit(int i) {
        this.resultLimitCount = i;
        return this;
    }

    public ItemStack createOutput(ItemStack input, ItemStack ingredient, boolean consumeOnSuccess) {
        if (ItemStack.isSameItemSameTags(input, this.inputStack) && ItemStack.isSameItemSameTags(ingredient, this.ingredientStack) && input.getCount() >= this.requiredBaseCount) {
            ItemStack result = this.resultStack.copy();
            result.setCount(this.resultLimitCount);
            if (consumeOnSuccess) {
                input.shrink(this.requiredBaseCount);
                ingredient.shrink(1);
            }
            return result;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack getInput() {
        ItemStack i = this.inputStack.copy();
        i.setCount(this.requiredBaseCount);
        return i;
    }

    public ItemStack getIngredient() {
        return this.ingredientStack.copy();
    }

    public ItemStack getResult() {
        ItemStack i = this.resultStack.copy();
        i.setCount(this.resultLimitCount);
        return i;
    }
}
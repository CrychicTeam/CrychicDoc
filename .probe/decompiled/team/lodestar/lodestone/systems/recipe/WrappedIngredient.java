package team.lodestar.lodestone.systems.recipe;

import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import team.lodestar.lodestone.helpers.ItemHelper;

public class WrappedIngredient implements IRecipeComponent {

    public final Ingredient ingredient;

    public WrappedIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(this.getItem(), this.getCount(), this.ingredient.getItems()[0].getTag());
    }

    @Override
    public List<ItemStack> getStacks() {
        return ItemHelper.copyWithNewCount(List.of(this.ingredient.getItems()), this.getCount());
    }

    @Override
    public Item getItem() {
        return this.ingredient.getItems()[0].getItem();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return this.ingredient.test(stack) && stack.getCount() >= this.getCount();
    }
}
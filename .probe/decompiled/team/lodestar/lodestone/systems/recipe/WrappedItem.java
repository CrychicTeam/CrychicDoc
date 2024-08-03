package team.lodestar.lodestone.systems.recipe;

import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.helpers.ItemHelper;

public class WrappedItem implements IRecipeComponent {

    public final ItemStack stack;

    public WrappedItem(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public List<ItemStack> getStacks() {
        return ItemHelper.copyWithNewCount(List.of(this.stack), this.getCount());
    }

    @Override
    public Item getItem() {
        return this.stack.getItem();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.equals(this.stack, false);
    }
}
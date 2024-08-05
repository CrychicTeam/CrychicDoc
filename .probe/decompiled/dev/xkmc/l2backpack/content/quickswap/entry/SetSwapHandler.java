package dev.xkmc.l2backpack.content.quickswap.entry;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public record SetSwapHandler(List<ItemStack> list, Int2IntFunction mapping) implements ISetSwapHandler {

    @Override
    public ItemStack getStack(int index) {
        return (ItemStack) this.list.get(this.mapping.get(index));
    }

    @Override
    public void replace(int index, ItemStack stack) {
        this.list.set(this.mapping.get(index), stack);
    }
}
package dev.xkmc.l2backpack.content.quickswap.entry;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public record SingleSwapHandler(List<ItemStack> list, int index) implements ISingleSwapHandler {

    @Override
    public ItemStack getStack() {
        return (ItemStack) this.list.get(this.index);
    }

    @Override
    public void replace(ItemStack stack) {
        this.list.set(this.index, stack);
    }
}
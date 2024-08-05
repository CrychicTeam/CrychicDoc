package dev.xkmc.l2backpack.content.quickswap.entry;

import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public record SingleSwapEntry(IQuickSwapToken<SingleSwapEntry> token, ItemStack stack) implements ISwapEntry<SingleSwapEntry> {

    public static List<SingleSwapEntry> parse(IQuickSwapToken<SingleSwapEntry> token, List<ItemStack> list) {
        return list.stream().map(e -> new SingleSwapEntry(token, e)).toList();
    }

    @Override
    public List<ItemStack> asList() {
        return List.of(this.stack);
    }

    @Override
    public ItemStack getStack() {
        return this.stack;
    }
}
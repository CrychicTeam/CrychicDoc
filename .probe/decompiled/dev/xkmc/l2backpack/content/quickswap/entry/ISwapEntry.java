package dev.xkmc.l2backpack.content.quickswap.entry;

import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public interface ISwapEntry<T extends ISwapEntry<T>> {

    IQuickSwapToken<T> token();

    List<ItemStack> asList();

    ItemStack getStack();
}
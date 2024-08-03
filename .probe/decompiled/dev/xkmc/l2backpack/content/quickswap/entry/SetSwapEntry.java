package dev.xkmc.l2backpack.content.quickswap.entry;

import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public record SetSwapEntry(IQuickSwapToken<SetSwapEntry> token, List<ItemStack> list) implements ISwapEntry<SetSwapEntry> {

    public static List<SetSwapEntry> parse(IQuickSwapToken<SetSwapEntry> token, List<ItemStack> items, int size) {
        int row = items.size() / size;
        List<SetSwapEntry> ans = new ArrayList();
        ItemStack[] arr = new ItemStack[size];
        for (int j = 0; j < row; j++) {
            for (int i = 0; i < size; i++) {
                arr[i] = (ItemStack) items.get(i * row + j);
            }
            ans.add(new SetSwapEntry(token, List.of(arr)));
        }
        return ans;
    }

    @Override
    public List<ItemStack> asList() {
        return this.list;
    }

    @Override
    public ItemStack getStack() {
        throw new UnsupportedOperationException("not supposed to get single stack from set swap");
    }
}
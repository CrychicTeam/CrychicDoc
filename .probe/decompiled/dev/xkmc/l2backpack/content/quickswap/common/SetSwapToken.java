package dev.xkmc.l2backpack.content.quickswap.common;

import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2backpack.content.quickswap.entry.SetSwapEntry;
import dev.xkmc.l2backpack.content.quickswap.entry.SetSwapHandler;
import dev.xkmc.l2backpack.content.quickswap.type.ISetSwapAction;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record SetSwapToken(ISetSwapItem item, ItemStack stack, QuickSwapType type) implements IQuickSwapToken<SetSwapEntry> {

    @Override
    public void setSelected(int slot) {
        SingleSwapItem.setSelected(this.stack, slot);
    }

    @Override
    public List<SetSwapEntry> getList() {
        return SetSwapEntry.parse(this, BaseBagItem.getItems(this.stack), this.item.getRows());
    }

    private List<ItemStack> getRawList() {
        return BaseBagItem.getItems(this.stack);
    }

    @Override
    public int getSelected() {
        return SingleSwapItem.getSelected(this.stack);
    }

    @Override
    public void shrink(int i) {
        throw new UnsupportedOperationException("set swap does not support shrink");
    }

    @Override
    public void swap(Player player) {
        if (this.type instanceof ISetSwapAction action) {
            List<ItemStack> list = this.getRawList();
            int row = list.size() / this.item.getRows();
            int ind = this.getSelected();
            action.swapSet(player, new SetSwapHandler(list, i -> i * row + ind));
            BaseBagItem.setItems(this.stack, list);
        }
    }
}
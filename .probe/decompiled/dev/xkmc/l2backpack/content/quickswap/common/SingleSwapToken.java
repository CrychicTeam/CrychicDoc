package dev.xkmc.l2backpack.content.quickswap.common;

import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2backpack.content.quickswap.entry.SingleSwapEntry;
import dev.xkmc.l2backpack.content.quickswap.entry.SingleSwapHandler;
import dev.xkmc.l2backpack.content.quickswap.type.ISingleSwapAction;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record SingleSwapToken(IQuickSwapItem item, ItemStack stack, QuickSwapType type) implements IQuickSwapToken<SingleSwapEntry> {

    @Override
    public void setSelected(int slot) {
        SingleSwapItem.setSelected(this.stack, slot);
    }

    @Override
    public List<SingleSwapEntry> getList() {
        return SingleSwapEntry.parse(this, BaseBagItem.getItems(this.stack));
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
        List<ItemStack> list = this.getRawList();
        ((ItemStack) list.get(this.getSelected())).shrink(i);
        BaseBagItem.setItems(this.stack, list);
    }

    @Override
    public void swap(Player player) {
        if (this.type instanceof ISingleSwapAction action) {
            List var5 = this.getRawList();
            int i = this.getSelected();
            action.swapSingle(player, new SingleSwapHandler(var5, i));
            BaseBagItem.setItems(this.stack, var5);
        }
    }
}
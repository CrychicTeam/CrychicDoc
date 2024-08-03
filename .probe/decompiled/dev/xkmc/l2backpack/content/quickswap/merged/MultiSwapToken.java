package dev.xkmc.l2backpack.content.quickswap.merged;

import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapItem;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.entry.SingleSwapEntry;
import dev.xkmc.l2backpack.content.quickswap.entry.SingleSwapHandler;
import dev.xkmc.l2backpack.content.quickswap.type.ISingleSwapAction;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record MultiSwapToken(IQuickSwapItem item, ItemStack stack, QuickSwapType type) implements IQuickSwapToken<SingleSwapEntry> {

    @Override
    public void setSelected(int slot) {
        MultiSwitch.setSelected(this.stack, this.type, slot);
    }

    @Override
    public List<SingleSwapEntry> getList() {
        return SingleSwapEntry.parse(this, BaseBagItem.getItems(this.stack).subList(this.type.getIndex() * 9, this.type.getIndex() * 9 + 9));
    }

    @Override
    public int getSelected() {
        return MultiSwitch.getSelected(this.stack, this.type);
    }

    @Override
    public void shrink(int i) {
        List<ItemStack> list = BaseBagItem.getItems(this.stack);
        List<ItemStack> sublist = list.subList(this.type.getIndex() * 9, this.type.getIndex() * 9 + 9);
        ((ItemStack) sublist.get(this.getSelected())).shrink(i);
        BaseBagItem.setItems(this.stack, list);
    }

    @Override
    public void swap(Player player) {
        if (this.type instanceof ISingleSwapAction action) {
            List<ItemStack> list = BaseBagItem.getItems(this.stack);
            List sublist = list.subList(this.type.getIndex() * 9, this.type.getIndex() * 9 + 9);
            int i = this.getSelected();
            action.swapSingle(player, new SingleSwapHandler(sublist, i));
            BaseBagItem.setItems(this.stack, list);
        }
    }
}
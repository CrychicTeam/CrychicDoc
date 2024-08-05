package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.modulargolems.content.item.card.TargetFilterCard;
import dev.xkmc.modulargolems.content.menu.ghost.IGhostContainer;
import java.util.ArrayList;
import net.minecraft.world.item.ItemStack;

public record TargetFilterLine(GolemConfigEditor editor, ArrayList<ItemStack> list, int offset) implements IGhostContainer {

    public TargetFilterConfig getFilter() {
        return this.editor().entry().targetFilter;
    }

    @Override
    public int listSize() {
        return this.list.size();
    }

    @Override
    public int getContainerSize() {
        return 18;
    }

    @Override
    public void set(int slot, ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() instanceof TargetFilterCard) {
            slot -= this.offset;
            if (slot < 0) {
                slot = this.listSize();
            }
            if (slot >= this.list.size()) {
                if (!stack.isEmpty()) {
                    this.list.add(stack);
                }
            } else if (!stack.isEmpty()) {
                this.list.set(slot, stack);
            } else {
                this.list.remove(slot);
            }
        }
    }

    @Override
    public boolean internalMatch(ItemStack stack) {
        return this.getFilter().internalMatch(this.list, stack);
    }

    @Override
    public ItemStack getItem(int slot) {
        slot -= this.offset;
        return slot >= this.list.size() ? ItemStack.EMPTY : (ItemStack) this.list.get(slot);
    }
}
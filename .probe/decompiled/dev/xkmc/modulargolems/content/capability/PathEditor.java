package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.modulargolems.content.item.card.PathRecordCard;
import dev.xkmc.modulargolems.content.menu.ghost.IGhostContainer;
import net.minecraft.world.item.ItemStack;

public record PathEditor(GolemConfigEditor editor) implements IGhostContainer {

    public PathConfig getFilter() {
        return this.editor().entry().pathConfig;
    }

    @Override
    public int listSize() {
        return this.getFilter().path.size();
    }

    @Override
    public void set(int slot, ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() instanceof PathRecordCard) {
            stack = stack.copy();
            if (slot < 0) {
                slot = this.listSize();
            }
            PathConfig filter = this.getFilter();
            if (slot >= filter.path.size()) {
                if (!stack.isEmpty()) {
                    filter.path.add(stack);
                }
            } else if (!stack.isEmpty()) {
                filter.path.set(slot, stack);
            } else {
                filter.path.remove(slot);
            }
        }
    }

    @Override
    public boolean internalMatch(ItemStack stack) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public ItemStack getItem(int slot) {
        PathConfig filter = this.getFilter();
        return slot >= filter.path.size() ? ItemStack.EMPTY : (ItemStack) filter.path.get(slot);
    }
}
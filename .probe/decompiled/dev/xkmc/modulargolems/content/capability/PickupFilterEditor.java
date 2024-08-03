package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.modulargolems.content.menu.ghost.IGhostContainer;
import net.minecraft.world.item.ItemStack;

public record PickupFilterEditor(GolemConfigEditor editor) implements IGhostContainer {

    public PickupFilterConfig getFilter() {
        return this.editor().entry().pickupFilter;
    }

    @Override
    public int listSize() {
        return this.getFilter().filter.size();
    }

    @Override
    public void set(int slot, ItemStack stack) {
        if (slot < 0) {
            slot = this.listSize();
        }
        PickupFilterConfig filter = this.getFilter();
        if (slot >= filter.filter.size()) {
            if (!stack.isEmpty()) {
                filter.filter.add(stack);
            }
        } else if (!stack.isEmpty()) {
            filter.filter.set(slot, stack);
        } else {
            filter.filter.remove(slot);
        }
    }

    @Override
    public boolean internalMatch(ItemStack stack) {
        return this.getFilter().internalMatch(stack);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public ItemStack getItem(int slot) {
        PickupFilterConfig filter = this.getFilter();
        return slot >= filter.filter.size() ? ItemStack.EMPTY : (ItemStack) filter.filter.get(slot);
    }

    public boolean isBlacklist() {
        return this.getFilter().blacklist;
    }

    public boolean isTagMatch() {
        return this.getFilter().matchNBT;
    }

    public void toggleTag() {
        PickupFilterConfig filter = this.getFilter();
        filter.matchNBT = !filter.matchNBT;
        this.editor().sync();
    }

    public void toggleFilter() {
        PickupFilterConfig filter = this.getFilter();
        filter.blacklist = !filter.blacklist;
        this.editor().sync();
    }
}
package net.blay09.mods.balm.api.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SubContainer implements Container, ExtractionAwareContainer {

    private final Container container;

    private final int minSlot;

    private final int maxSlot;

    public SubContainer(Container container, int minSlot, int maxSlot) {
        this.container = container;
        this.minSlot = minSlot;
        this.maxSlot = maxSlot;
    }

    @Override
    public int getContainerSize() {
        return this.maxSlot - this.minSlot;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.containsSlot(slot) ? this.container.getItem(slot + this.minSlot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return this.containsSlot(slot) ? this.container.removeItem(slot + this.minSlot, amount) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return this.containsSlot(slot) ? this.container.removeItemNoUpdate(slot + this.minSlot) : ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        if (this.containsSlot(slot)) {
            this.container.setItem(slot + this.minSlot, itemStack);
        }
    }

    @Override
    public void startOpen(Player player) {
        this.container.startOpen(player);
    }

    @Override
    public void stopOpen(Player player) {
        this.container.stopOpen(player);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return this.containsSlot(slot) && this.container.canPlaceItem(slot + this.minSlot, itemStack);
    }

    @Override
    public boolean isEmpty() {
        for (int i = this.minSlot; i < this.maxSlot; i++) {
            if (!this.container.getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public int getMaxStackSize() {
        return this.container.getMaxStackSize();
    }

    @Override
    public void setChanged() {
        this.container.setChanged();
    }

    @Deprecated(since = "1.20")
    public boolean containsSlot(int slot) {
        return slot + this.minSlot < this.maxSlot;
    }

    public boolean containsOuterSlot(int slot) {
        return slot >= this.minSlot && slot < this.maxSlot;
    }

    @Override
    public void clearContent() {
        for (int i = this.minSlot; i < this.maxSlot; i++) {
            this.container.setItem(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canExtractItem(int slot) {
        return !(this.container instanceof ExtractionAwareContainer extractionAwareContainer) ? this.containsSlot(slot) : this.containsSlot(slot) && extractionAwareContainer.canExtractItem(slot + this.minSlot);
    }
}
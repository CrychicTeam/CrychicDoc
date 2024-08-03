package net.blay09.mods.balm.api.container;

import java.util.Set;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DelegateContainer implements Container, ExtractionAwareContainer {

    private final Container delegate;

    public DelegateContainer(Container delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getMaxStackSize() {
        return this.delegate.getMaxStackSize();
    }

    @Override
    public void startOpen(Player player) {
        this.delegate.startOpen(player);
    }

    @Override
    public void stopOpen(Player player) {
        this.delegate.stopOpen(player);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return this.delegate.canPlaceItem(slot, itemStack);
    }

    @Override
    public int countItem(Item item) {
        return this.delegate.countItem(item);
    }

    @Override
    public boolean hasAnyOf(Set<Item> items) {
        return this.delegate.hasAnyOf(items);
    }

    @Override
    public int getContainerSize() {
        return this.delegate.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.delegate.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return this.delegate.removeItem(slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return this.delegate.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.delegate.setItem(slot, itemStack);
    }

    @Override
    public void setChanged() {
        this.delegate.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.delegate.stillValid(player);
    }

    @Override
    public void clearContent() {
        this.delegate.m_6211_();
    }

    @Override
    public boolean canExtractItem(int slot) {
        return this.delegate instanceof ExtractionAwareContainer extractionAwareContainer ? extractionAwareContainer.canExtractItem(slot) : true;
    }
}
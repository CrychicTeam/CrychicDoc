package net.minecraft.world;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CompoundContainer implements Container {

    private final Container container1;

    private final Container container2;

    public CompoundContainer(Container container0, Container container1) {
        this.container1 = container0;
        this.container2 = container1;
    }

    @Override
    public int getContainerSize() {
        return this.container1.getContainerSize() + this.container2.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return this.container1.isEmpty() && this.container2.isEmpty();
    }

    public boolean contains(Container container0) {
        return this.container1 == container0 || this.container2 == container0;
    }

    @Override
    public ItemStack getItem(int int0) {
        return int0 >= this.container1.getContainerSize() ? this.container2.getItem(int0 - this.container1.getContainerSize()) : this.container1.getItem(int0);
    }

    @Override
    public ItemStack removeItem(int int0, int int1) {
        return int0 >= this.container1.getContainerSize() ? this.container2.removeItem(int0 - this.container1.getContainerSize(), int1) : this.container1.removeItem(int0, int1);
    }

    @Override
    public ItemStack removeItemNoUpdate(int int0) {
        return int0 >= this.container1.getContainerSize() ? this.container2.removeItemNoUpdate(int0 - this.container1.getContainerSize()) : this.container1.removeItemNoUpdate(int0);
    }

    @Override
    public void setItem(int int0, ItemStack itemStack1) {
        if (int0 >= this.container1.getContainerSize()) {
            this.container2.setItem(int0 - this.container1.getContainerSize(), itemStack1);
        } else {
            this.container1.setItem(int0, itemStack1);
        }
    }

    @Override
    public int getMaxStackSize() {
        return this.container1.getMaxStackSize();
    }

    @Override
    public void setChanged() {
        this.container1.setChanged();
        this.container2.setChanged();
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.container1.stillValid(player0) && this.container2.stillValid(player0);
    }

    @Override
    public void startOpen(Player player0) {
        this.container1.startOpen(player0);
        this.container2.startOpen(player0);
    }

    @Override
    public void stopOpen(Player player0) {
        this.container1.stopOpen(player0);
        this.container2.stopOpen(player0);
    }

    @Override
    public boolean canPlaceItem(int int0, ItemStack itemStack1) {
        return int0 >= this.container1.getContainerSize() ? this.container2.canPlaceItem(int0 - this.container1.getContainerSize(), itemStack1) : this.container1.canPlaceItem(int0, itemStack1);
    }

    @Override
    public void clearContent() {
        this.container1.m_6211_();
        this.container2.m_6211_();
    }
}
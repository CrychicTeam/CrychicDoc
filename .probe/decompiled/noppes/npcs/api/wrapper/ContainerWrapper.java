package noppes.npcs.api.wrapper;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.item.IItemStack;

public class ContainerWrapper implements IContainer {

    private Container inventory;

    private AbstractContainerMenu container;

    public ContainerWrapper(Container inventory) {
        this.inventory = inventory;
    }

    public ContainerWrapper(AbstractContainerMenu container) {
        this.container = container;
    }

    @Override
    public int getSize() {
        return this.inventory != null ? this.inventory.getContainerSize() : this.container.slots.size();
    }

    @Override
    public IItemStack getSlot(int slot) {
        if (slot < 0 || slot >= this.getSize()) {
            throw new CustomNPCsException("Slot is out of range " + slot);
        } else {
            return this.inventory != null ? NpcAPI.Instance().getIItemStack(this.inventory.getItem(slot)) : NpcAPI.Instance().getIItemStack(this.container.getSlot(slot).getItem());
        }
    }

    @Override
    public void setSlot(int slot, IItemStack item) {
        if (slot >= 0 && slot < this.getSize()) {
            ItemStack itemstack = item == null ? ItemStack.EMPTY : item.getMCItemStack();
            if (this.inventory != null) {
                this.inventory.setItem(slot, itemstack);
            } else {
                this.container.setItem(slot, this.container.getStateId(), itemstack);
                this.container.broadcastChanges();
            }
        } else {
            throw new CustomNPCsException("Slot is out of range " + slot);
        }
    }

    @Override
    public int count(IItemStack item, boolean ignoreDamage, boolean ignoreNBT) {
        int count = 0;
        for (int i = 0; i < this.getSize(); i++) {
            IItemStack toCompare = this.getSlot(i);
            if (NoppesUtilPlayer.compareItems(item.getMCItemStack(), toCompare.getMCItemStack(), ignoreDamage, ignoreNBT)) {
                count += toCompare.getStackSize();
            }
        }
        return count;
    }

    @Override
    public Container getMCInventory() {
        return this.inventory;
    }

    @Override
    public AbstractContainerMenu getMCContainer() {
        return this.container;
    }

    @Override
    public IItemStack[] getItems() {
        IItemStack[] items = new IItemStack[this.getSize()];
        for (int i = 0; i < this.getSize(); i++) {
            items[i] = this.getSlot(i);
        }
        return items;
    }
}
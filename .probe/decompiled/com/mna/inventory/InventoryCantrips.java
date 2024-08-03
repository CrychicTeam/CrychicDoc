package com.mna.inventory;

import com.mna.api.capabilities.IPlayerCantrip;
import com.mna.api.capabilities.IPlayerCantrips;
import com.mna.cantrips.CantripRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class InventoryCantrips implements Container {

    private List<IPlayerCantrip> cantripsWithInventory = new ArrayList();

    public InventoryCantrips(IPlayerCantrips cantripData) {
        cantripData.getCantrips().forEach(c -> CantripRegistry.INSTANCE.getCantrip(c.getCantripID()).ifPresent(rc -> {
            if (!rc.isStackLocked()) {
                this.cantripsWithInventory.add(c);
            }
        }));
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index >= 0 && index < this.cantripsWithInventory.size()) {
            ((IPlayerCantrip) this.cantripsWithInventory.get(index)).setStack(stack);
        }
    }

    @Override
    public ItemStack getItem(int index) {
        return index >= 0 && index < this.cantripsWithInventory.size() ? ((IPlayerCantrip) this.cantripsWithInventory.get(index)).getStack() : ItemStack.EMPTY;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void clearContent() {
        this.cantripsWithInventory.forEach(c -> c.setStack(ItemStack.EMPTY));
    }

    @Override
    public int getContainerSize() {
        return this.cantripsWithInventory.size();
    }

    @Override
    public boolean isEmpty() {
        return this.cantripsWithInventory.stream().allMatch(c -> c.getStack().isEmpty());
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return this.removeItemNoUpdate(index);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (index >= 0 && index < this.cantripsWithInventory.size()) {
            ItemStack stack = ((IPlayerCantrip) this.cantripsWithInventory.get(index)).getStack();
            ((IPlayerCantrip) this.cantripsWithInventory.get(index)).setStack(ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
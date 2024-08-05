package net.blay09.mods.balm.api.container;

import java.util.Arrays;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CombinedContainer implements Container, ExtractionAwareContainer {

    private final Container[] containers;

    private final int[] baseIndex;

    private final int totalSlots;

    public CombinedContainer(Container... containers) {
        this.containers = containers;
        this.baseIndex = new int[containers.length];
        int index = 0;
        for (int i = 0; i < containers.length; i++) {
            index += containers[i].getContainerSize();
            this.baseIndex[i] = index;
        }
        this.totalSlots = index;
    }

    private int getContainerIndexForSlot(int slot) {
        if (slot < 0) {
            return -1;
        } else {
            for (int i = 0; i < this.baseIndex.length; i++) {
                if (slot - this.baseIndex[i] < 0) {
                    return i;
                }
            }
            return -1;
        }
    }

    private Container getContainerFromIndex(int index) {
        return (Container) (index >= 0 && index < this.containers.length ? this.containers[index] : EmptyContainer.INSTANCE);
    }

    private int getSlotFromIndex(int slot, int index) {
        return index > 0 && index < this.baseIndex.length ? slot - this.baseIndex[index - 1] : slot;
    }

    @Override
    public int getContainerSize() {
        return this.totalSlots;
    }

    @Override
    public boolean isEmpty() {
        return Arrays.stream(this.containers).allMatch(Container::m_7983_);
    }

    @Override
    public ItemStack getItem(int slot) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        Container container = this.getContainerFromIndex(containerIndex);
        return container.getItem(this.getSlotFromIndex(slot, containerIndex));
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        Container container = this.getContainerFromIndex(containerIndex);
        return container.removeItem(this.getSlotFromIndex(slot, containerIndex), amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        Container container = this.getContainerFromIndex(containerIndex);
        return container.removeItemNoUpdate(this.getSlotFromIndex(slot, containerIndex));
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        Container container = this.getContainerFromIndex(containerIndex);
        container.setItem(this.getSlotFromIndex(slot, containerIndex), itemStack);
    }

    @Override
    public void setChanged() {
        for (Container container : this.containers) {
            container.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Arrays.stream(this.containers).allMatch(container -> container.stillValid(player));
    }

    @Override
    public void clearContent() {
        for (Container container : this.containers) {
            container.m_6211_();
        }
    }

    @Override
    public boolean canExtractItem(int slot) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        return this.getContainerFromIndex(containerIndex) instanceof ExtractionAwareContainer extractionAwareContainer ? extractionAwareContainer.canExtractItem(this.getSlotFromIndex(slot, containerIndex)) : true;
    }
}
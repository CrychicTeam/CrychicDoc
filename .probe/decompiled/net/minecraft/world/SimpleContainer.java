package net.minecraft.world;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SimpleContainer implements Container, StackedContentsCompatible {

    private final int size;

    private final NonNullList<ItemStack> items;

    @Nullable
    private List<ContainerListener> listeners;

    public SimpleContainer(int int0) {
        this.size = int0;
        this.items = NonNullList.withSize(int0, ItemStack.EMPTY);
    }

    public SimpleContainer(ItemStack... itemStack0) {
        this.size = itemStack0.length;
        this.items = NonNullList.of(ItemStack.EMPTY, itemStack0);
    }

    public void addListener(ContainerListener containerListener0) {
        if (this.listeners == null) {
            this.listeners = Lists.newArrayList();
        }
        this.listeners.add(containerListener0);
    }

    public void removeListener(ContainerListener containerListener0) {
        if (this.listeners != null) {
            this.listeners.remove(containerListener0);
        }
    }

    @Override
    public ItemStack getItem(int int0) {
        return int0 >= 0 && int0 < this.items.size() ? this.items.get(int0) : ItemStack.EMPTY;
    }

    public List<ItemStack> removeAllItems() {
        List<ItemStack> $$0 = (List<ItemStack>) this.items.stream().filter(p_19197_ -> !p_19197_.isEmpty()).collect(Collectors.toList());
        this.clearContent();
        return $$0;
    }

    @Override
    public ItemStack removeItem(int int0, int int1) {
        ItemStack $$2 = ContainerHelper.removeItem(this.items, int0, int1);
        if (!$$2.isEmpty()) {
            this.setChanged();
        }
        return $$2;
    }

    public ItemStack removeItemType(Item item0, int int1) {
        ItemStack $$2 = new ItemStack(item0, 0);
        for (int $$3 = this.size - 1; $$3 >= 0; $$3--) {
            ItemStack $$4 = this.getItem($$3);
            if ($$4.getItem().equals(item0)) {
                int $$5 = int1 - $$2.getCount();
                ItemStack $$6 = $$4.split($$5);
                $$2.grow($$6.getCount());
                if ($$2.getCount() == int1) {
                    break;
                }
            }
        }
        if (!$$2.isEmpty()) {
            this.setChanged();
        }
        return $$2;
    }

    public ItemStack addItem(ItemStack itemStack0) {
        if (itemStack0.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack $$1 = itemStack0.copy();
            this.moveItemToOccupiedSlotsWithSameType($$1);
            if ($$1.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                this.moveItemToEmptySlots($$1);
                return $$1.isEmpty() ? ItemStack.EMPTY : $$1;
            }
        }
    }

    public boolean canAddItem(ItemStack itemStack0) {
        boolean $$1 = false;
        for (ItemStack $$2 : this.items) {
            if ($$2.isEmpty() || ItemStack.isSameItemSameTags($$2, itemStack0) && $$2.getCount() < $$2.getMaxStackSize()) {
                $$1 = true;
                break;
            }
        }
        return $$1;
    }

    @Override
    public ItemStack removeItemNoUpdate(int int0) {
        ItemStack $$1 = this.items.get(int0);
        if ($$1.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.items.set(int0, ItemStack.EMPTY);
            return $$1;
        }
    }

    @Override
    public void setItem(int int0, ItemStack itemStack1) {
        this.items.set(int0, itemStack1);
        if (!itemStack1.isEmpty() && itemStack1.getCount() > this.m_6893_()) {
            itemStack1.setCount(this.m_6893_());
        }
        this.setChanged();
    }

    @Override
    public int getContainerSize() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack $$0 : this.items) {
            if (!$$0.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setChanged() {
        if (this.listeners != null) {
            for (ContainerListener $$0 : this.listeners) {
                $$0.containerChanged(this);
            }
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.setChanged();
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents0) {
        for (ItemStack $$1 : this.items) {
            stackedContents0.accountStack($$1);
        }
    }

    public String toString() {
        return ((List) this.items.stream().filter(p_19194_ -> !p_19194_.isEmpty()).collect(Collectors.toList())).toString();
    }

    private void moveItemToEmptySlots(ItemStack itemStack0) {
        for (int $$1 = 0; $$1 < this.size; $$1++) {
            ItemStack $$2 = this.getItem($$1);
            if ($$2.isEmpty()) {
                this.setItem($$1, itemStack0.copyAndClear());
                return;
            }
        }
    }

    private void moveItemToOccupiedSlotsWithSameType(ItemStack itemStack0) {
        for (int $$1 = 0; $$1 < this.size; $$1++) {
            ItemStack $$2 = this.getItem($$1);
            if (ItemStack.isSameItemSameTags($$2, itemStack0)) {
                this.moveItemsBetweenStacks(itemStack0, $$2);
                if (itemStack0.isEmpty()) {
                    return;
                }
            }
        }
    }

    private void moveItemsBetweenStacks(ItemStack itemStack0, ItemStack itemStack1) {
        int $$2 = Math.min(this.m_6893_(), itemStack1.getMaxStackSize());
        int $$3 = Math.min(itemStack0.getCount(), $$2 - itemStack1.getCount());
        if ($$3 > 0) {
            itemStack1.grow($$3);
            itemStack0.shrink($$3);
            this.setChanged();
        }
    }

    public void fromTag(ListTag listTag0) {
        this.clearContent();
        for (int $$1 = 0; $$1 < listTag0.size(); $$1++) {
            ItemStack $$2 = ItemStack.of(listTag0.getCompound($$1));
            if (!$$2.isEmpty()) {
                this.addItem($$2);
            }
        }
    }

    public ListTag createTag() {
        ListTag $$0 = new ListTag();
        for (int $$1 = 0; $$1 < this.getContainerSize(); $$1++) {
            ItemStack $$2 = this.getItem($$1);
            if (!$$2.isEmpty()) {
                $$0.add($$2.save(new CompoundTag()));
            }
        }
        return $$0;
    }
}
package noppes.npcs;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class NpcMiscInventory extends SimpleContainer {

    public final NonNullList<ItemStack> items;

    public int stackLimit = 64;

    private int size;

    public NpcMiscInventory(int size) {
        super();
        this.size = size;
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public CompoundTag getToNBT() {
        CompoundTag nbttagcompound = new CompoundTag();
        nbttagcompound.put("NpcMiscInv", NBTTags.nbtItemStackList(this.items));
        return nbttagcompound;
    }

    public void setFromNBT(CompoundTag nbttagcompound) {
        NBTTags.getItemStackList(nbttagcompound.getList("NpcMiscInv", 10), this.items);
    }

    @Override
    public int getContainerSize() {
        return this.size;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.items, index, count);
    }

    public boolean removeItem(ItemStack eating, int decrease) {
        for (int slot = 0; slot < this.items.size(); slot++) {
            ItemStack item = this.items.get(slot);
            if (!item.isEmpty() && eating == item && item.getCount() >= decrease) {
                item.split(decrease);
                if (item.getCount() <= 0) {
                    this.items.set(slot, ItemStack.EMPTY);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack removeItemNoUpdate(int var1) {
        return this.items.set(var1, ItemStack.EMPTY);
    }

    @Override
    public void setItem(int var1, ItemStack var2) {
        if (var1 < this.getContainerSize()) {
            this.items.set(var1, var2);
        }
    }

    @Override
    public int getMaxStackSize() {
        return this.stackLimit;
    }

    @Override
    public boolean stillValid(Player var1) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public void setChanged() {
    }

    public boolean addItemStack(ItemStack item) {
        boolean merged = false;
        ItemStack mergable;
        while (!(mergable = this.getMergableItem(item)).isEmpty() && mergable.getCount() > 0) {
            int size = mergable.getMaxStackSize() - mergable.getCount();
            if (size > item.getCount()) {
                mergable.setCount(mergable.getMaxStackSize());
                item.setCount(item.getCount() - size);
                merged = true;
            } else {
                mergable.setCount(mergable.getCount() + item.getCount());
                item.setCount(0);
            }
        }
        if (item.getCount() <= 0) {
            return true;
        } else {
            int slot = this.firstFreeSlot();
            if (slot >= 0) {
                this.items.set(slot, item.copy());
                item.setCount(0);
                return true;
            } else {
                return merged;
            }
        }
    }

    public ItemStack getMergableItem(ItemStack item) {
        for (ItemStack is : this.items) {
            if (NoppesUtilPlayer.compareItems(item, is, false, false) && is.getCount() < is.getMaxStackSize()) {
                return is;
            }
        }
        return ItemStack.EMPTY;
    }

    public int firstFreeSlot() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (this.items.get(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public void setSize(int i) {
        this.size = i;
    }

    @Override
    public void startOpen(Player player) {
    }

    @Override
    public void stopOpen(Player player) {
    }

    @Override
    public void clearContent() {
    }

    @Override
    public boolean isEmpty() {
        for (int slot = 0; slot < this.getContainerSize(); slot++) {
            ItemStack item = this.getItem(slot);
            if (!NoppesUtilServer.IsItemStackNull(item) && !item.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
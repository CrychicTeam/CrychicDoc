package me.jellysquid.mods.lithium.common.hopper;

import me.jellysquid.mods.lithium.api.inventory.LithiumDefaultedList;
import me.jellysquid.mods.lithium.common.block.entity.inventory_change_tracking.InventoryChangeTracker;
import me.jellysquid.mods.lithium.mixin.block.hopper.DefaultedListAccessor;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class LithiumStackList extends NonNullList<ItemStack> implements LithiumDefaultedList {

    final int maxCountPerStack;

    protected int cachedSignalStrength;

    private ComparatorUpdatePattern cachedComparatorUpdatePattern;

    private boolean signalStrengthOverride;

    private long modCount;

    private int occupiedSlots;

    private int fullSlots;

    LithiumDoubleStackList parent;

    InventoryChangeTracker inventoryModificationCallback;

    public LithiumStackList(NonNullList<ItemStack> original, int maxCountPerStack) {
        super(((DefaultedListAccessor) original).getDelegate(), ItemStack.EMPTY);
        this.maxCountPerStack = maxCountPerStack;
        this.cachedSignalStrength = -1;
        this.cachedComparatorUpdatePattern = null;
        this.modCount = 0L;
        this.signalStrengthOverride = false;
        this.occupiedSlots = 0;
        this.fullSlots = 0;
        int size = this.size();
        for (int i = 0; i < size; i++) {
            ItemStack stack = this.get(i);
            if (!stack.isEmpty()) {
                this.occupiedSlots++;
                if (stack.getMaxStackSize() <= stack.getCount()) {
                    this.fullSlots++;
                }
                ((StorableItemStack) stack).registerToInventory(this, i);
            }
        }
        this.inventoryModificationCallback = null;
    }

    public LithiumStackList(int maxCountPerStack) {
        super(null, ItemStack.EMPTY);
        this.maxCountPerStack = maxCountPerStack;
        this.cachedSignalStrength = -1;
        this.inventoryModificationCallback = null;
    }

    public long getModCount() {
        return this.modCount;
    }

    public void changedALot() {
        this.changed();
        this.occupiedSlots = 0;
        this.fullSlots = 0;
        int size = this.size();
        for (int i = 0; i < size; i++) {
            ItemStack stack = this.get(i);
            if (!stack.isEmpty()) {
                this.occupiedSlots++;
                if (stack.getMaxStackSize() <= stack.getCount()) {
                    this.fullSlots++;
                }
                ((StorableItemStack) stack).unregisterFromInventory(this);
            }
        }
        for (int ix = 0; ix < size; ix++) {
            ItemStack stack = this.get(ix);
            if (!stack.isEmpty()) {
                ((StorableItemStack) stack).registerToInventory(this, ix);
            }
        }
    }

    public void beforeSlotCountChange(int slot, int newCount) {
        ItemStack stack = this.get(slot);
        int count = stack.getCount();
        if (newCount <= 0) {
            ((StorableItemStack) stack).unregisterFromInventory(this, slot);
        }
        int maxCount = stack.getMaxStackSize();
        this.occupiedSlots -= newCount <= 0 ? 1 : 0;
        this.fullSlots += (newCount >= maxCount ? 1 : 0) - (count >= maxCount ? 1 : 0);
        this.changed();
    }

    public void changed() {
        this.cachedSignalStrength = -1;
        this.cachedComparatorUpdatePattern = null;
        this.modCount++;
        InventoryChangeTracker inventoryModificationCallback = this.inventoryModificationCallback;
        if (inventoryModificationCallback != null) {
            this.inventoryModificationCallback = null;
            inventoryModificationCallback.emitContentModified();
        }
    }

    public ItemStack set(int index, ItemStack element) {
        ItemStack previous = super.set(index, element);
        if (previous != element) {
            ((StorableItemStack) previous).unregisterFromInventory(this, index);
            if (!element.isEmpty()) {
                ((StorableItemStack) element).registerToInventory(this, index);
            }
            this.occupiedSlots = this.occupiedSlots + ((previous.isEmpty() ? 1 : 0) - (element.isEmpty() ? 1 : 0));
            this.fullSlots = this.fullSlots + ((element.getCount() >= element.getMaxStackSize() ? 1 : 0) - (previous.getCount() >= previous.getMaxStackSize() ? 1 : 0));
            this.changed();
        }
        return previous;
    }

    public void add(int slot, ItemStack element) {
        super.add(slot, element);
        if (!element.isEmpty()) {
            ((StorableItemStack) element).registerToInventory(this, this.indexOf(element));
        }
        this.changedALot();
    }

    public ItemStack remove(int index) {
        ItemStack previous = (ItemStack) super.remove(index);
        ((StorableItemStack) previous).unregisterFromInventory(this, index);
        this.changedALot();
        return previous;
    }

    @Override
    public void clear() {
        int size = this.size();
        for (int i = 0; i < size; i++) {
            ItemStack stack = this.get(i);
            if (!stack.isEmpty()) {
                ((StorableItemStack) stack).unregisterFromInventory(this, i);
            }
        }
        super.clear();
        this.changedALot();
    }

    public boolean hasSignalStrengthOverride() {
        return this.signalStrengthOverride;
    }

    public int getSignalStrength(Container inventory) {
        if (this.signalStrengthOverride) {
            return 0;
        } else {
            int signalStrength = this.cachedSignalStrength;
            return signalStrength == -1 ? (this.cachedSignalStrength = this.calculateSignalStrength(inventory.getContainerSize())) : signalStrength;
        }
    }

    int calculateSignalStrength(int inventorySize) {
        int i = 0;
        float f = 0.0F;
        inventorySize = Math.min(inventorySize, this.size());
        for (int j = 0; j < inventorySize; j++) {
            ItemStack itemStack = this.get(j);
            if (!itemStack.isEmpty()) {
                f += (float) itemStack.getCount() / (float) Math.min(this.maxCountPerStack, itemStack.getMaxStackSize());
                i++;
            }
        }
        f /= (float) inventorySize;
        return Mth.floor(f * 14.0F) + (i > 0 ? 1 : 0);
    }

    public void setReducedSignalStrengthOverride() {
        this.signalStrengthOverride = true;
    }

    public void clearSignalStrengthOverride() {
        this.signalStrengthOverride = false;
    }

    public void runComparatorUpdatePatternOnFailedExtract(LithiumStackList masterStackList, Container inventory) {
        if (inventory instanceof BlockEntity) {
            if (this.cachedComparatorUpdatePattern == null) {
                this.cachedComparatorUpdatePattern = HopperHelper.determineComparatorUpdatePattern(inventory, masterStackList);
            }
            this.cachedComparatorUpdatePattern.apply((BlockEntity) inventory, masterStackList);
        }
    }

    public boolean maybeSendsComparatorUpdatesOnFailedExtract() {
        return this.cachedComparatorUpdatePattern == null || this.cachedComparatorUpdatePattern != ComparatorUpdatePattern.NO_UPDATE;
    }

    public int getOccupiedSlots() {
        return this.occupiedSlots;
    }

    public int getFullSlots() {
        return this.fullSlots;
    }

    @Override
    public void changedInteractionConditions() {
        this.changed();
    }

    public void setInventoryModificationCallback(@NotNull InventoryChangeTracker inventoryModificationCallback) {
        if (this.inventoryModificationCallback != null && this.inventoryModificationCallback != inventoryModificationCallback) {
            this.inventoryModificationCallback.emitCallbackReplaced();
        }
        this.inventoryModificationCallback = inventoryModificationCallback;
    }

    public void removeInventoryModificationCallback(@NotNull InventoryChangeTracker inventoryModificationCallback) {
        if (this.inventoryModificationCallback != null && this.inventoryModificationCallback == inventoryModificationCallback) {
            this.inventoryModificationCallback = null;
        }
    }
}
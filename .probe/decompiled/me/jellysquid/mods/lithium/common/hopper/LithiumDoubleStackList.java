package me.jellysquid.mods.lithium.common.hopper;

import me.jellysquid.mods.lithium.common.block.entity.inventory_change_tracking.InventoryChangeTracker;
import me.jellysquid.mods.lithium.mixin.block.hopper.DoubleInventoryAccessor;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LithiumDoubleStackList extends LithiumStackList {

    private final LithiumStackList first;

    private final LithiumStackList second;

    final LithiumDoubleInventory doubleInventory;

    private long signalStrengthChangeCount;

    public LithiumDoubleStackList(LithiumDoubleInventory doubleInventory, LithiumStackList first, LithiumStackList second, int maxCountPerStack) {
        super(maxCountPerStack);
        this.first = first;
        this.second = second;
        this.doubleInventory = doubleInventory;
    }

    public static LithiumDoubleStackList getOrCreate(LithiumDoubleInventory doubleInventory, LithiumStackList first, LithiumStackList second, int maxCountPerStack) {
        LithiumDoubleStackList parentStackList = first.parent;
        if (parentStackList == null || parentStackList != second.parent || parentStackList.first != first || parentStackList.second != second) {
            if (parentStackList != null) {
                parentStackList.doubleInventory.emitRemoved();
            }
            parentStackList = new LithiumDoubleStackList(doubleInventory, first, second, maxCountPerStack);
            first.parent = parentStackList;
            second.parent = parentStackList;
        }
        return parentStackList;
    }

    @Override
    public long getModCount() {
        return this.first.getModCount() + this.second.getModCount();
    }

    @Override
    public void changedALot() {
        throw new UnsupportedOperationException("Call changed() on the inventory half only!");
    }

    @Override
    public void changed() {
        throw new UnsupportedOperationException("Call changed() on the inventory half only!");
    }

    @Override
    public ItemStack set(int index, ItemStack element) {
        return index >= this.first.size() ? this.second.set(index - this.first.size(), element) : this.first.set(index, element);
    }

    @Override
    public void add(int slot, ItemStack element) {
        throw new UnsupportedOperationException("Call add(int value, ItemStack element) on the inventory half only!");
    }

    @Override
    public ItemStack remove(int index) {
        throw new UnsupportedOperationException("Call remove(int value, ItemStack element) on the inventory half only!");
    }

    @Override
    public void clear() {
        this.first.clear();
        this.second.clear();
    }

    @Override
    public int getSignalStrength(Container inventory) {
        boolean signalStrengthOverride = this.first.hasSignalStrengthOverride() || this.second.hasSignalStrengthOverride();
        if (signalStrengthOverride) {
            return 0;
        } else {
            int cachedSignalStrength = this.cachedSignalStrength;
            if (cachedSignalStrength != -1 && this.getModCount() == this.signalStrengthChangeCount) {
                return cachedSignalStrength;
            } else {
                cachedSignalStrength = this.calculateSignalStrength(Integer.MAX_VALUE);
                this.signalStrengthChangeCount = this.getModCount();
                this.cachedSignalStrength = cachedSignalStrength;
                return cachedSignalStrength;
            }
        }
    }

    @Override
    public void setReducedSignalStrengthOverride() {
        this.first.setReducedSignalStrengthOverride();
        this.second.setReducedSignalStrengthOverride();
    }

    @Override
    public void clearSignalStrengthOverride() {
        this.first.clearSignalStrengthOverride();
        this.second.clearSignalStrengthOverride();
    }

    @Override
    public void runComparatorUpdatePatternOnFailedExtract(LithiumStackList masterStackList, Container inventory) {
        if (inventory instanceof CompoundContainer) {
            this.first.runComparatorUpdatePatternOnFailedExtract(this, ((DoubleInventoryAccessor) inventory).getFirst());
            this.second.runComparatorUpdatePatternOnFailedExtract(this, ((DoubleInventoryAccessor) inventory).getSecond());
        }
    }

    @NotNull
    public ItemStack get(int index) {
        return index >= this.first.size() ? this.second.get(index - this.first.size()) : this.first.get(index);
    }

    @Override
    public int size() {
        return this.first.size() + this.second.size();
    }

    @Override
    public void setInventoryModificationCallback(@NotNull InventoryChangeTracker inventoryModificationCallback) {
        this.first.setInventoryModificationCallback(inventoryModificationCallback);
        this.second.setInventoryModificationCallback(inventoryModificationCallback);
    }

    @Override
    public void removeInventoryModificationCallback(@NotNull InventoryChangeTracker inventoryModificationCallback) {
        this.first.removeInventoryModificationCallback(inventoryModificationCallback);
        this.second.removeInventoryModificationCallback(inventoryModificationCallback);
    }
}
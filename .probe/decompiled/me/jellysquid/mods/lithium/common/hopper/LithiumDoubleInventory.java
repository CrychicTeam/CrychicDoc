package me.jellysquid.mods.lithium.common.hopper;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import me.jellysquid.mods.lithium.api.inventory.LithiumInventory;
import me.jellysquid.mods.lithium.common.block.entity.inventory_change_tracking.InventoryChangeEmitter;
import me.jellysquid.mods.lithium.common.block.entity.inventory_change_tracking.InventoryChangeListener;
import me.jellysquid.mods.lithium.common.block.entity.inventory_change_tracking.InventoryChangeTracker;
import me.jellysquid.mods.lithium.common.block.entity.inventory_comparator_tracking.ComparatorTracker;
import me.jellysquid.mods.lithium.mixin.block.hopper.DoubleInventoryAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class LithiumDoubleInventory extends CompoundContainer implements LithiumInventory, InventoryChangeTracker, InventoryChangeEmitter, InventoryChangeListener, ComparatorTracker {

    private final LithiumInventory first;

    private final LithiumInventory second;

    private LithiumStackList doubleStackList;

    ReferenceOpenHashSet<InventoryChangeListener> inventoryChangeListeners = null;

    ReferenceOpenHashSet<InventoryChangeListener> inventoryHandlingTypeListeners = null;

    public static LithiumDoubleInventory getLithiumInventory(CompoundContainer doubleInventory) {
        Container vanillaFirst = ((DoubleInventoryAccessor) doubleInventory).getFirst();
        Container vanillaSecond = ((DoubleInventoryAccessor) doubleInventory).getSecond();
        if (vanillaFirst != vanillaSecond && vanillaFirst instanceof LithiumInventory first && vanillaSecond instanceof LithiumInventory second) {
            LithiumDoubleInventory newDoubleInventory = new LithiumDoubleInventory(first, second);
            LithiumDoubleStackList doubleStackList = LithiumDoubleStackList.getOrCreate(newDoubleInventory, InventoryHelper.getLithiumStackList(first), InventoryHelper.getLithiumStackList(second), newDoubleInventory.m_6893_());
            newDoubleInventory.doubleStackList = doubleStackList;
            return doubleStackList.doubleInventory;
        }
        return null;
    }

    private LithiumDoubleInventory(LithiumInventory first, LithiumInventory second) {
        super(first, second);
        this.first = first;
        this.second = second;
    }

    @Override
    public void emitContentModified() {
        ReferenceOpenHashSet<InventoryChangeListener> inventoryChangeListeners = this.inventoryChangeListeners;
        if (inventoryChangeListeners != null) {
            ObjectIterator var2 = inventoryChangeListeners.iterator();
            while (var2.hasNext()) {
                InventoryChangeListener inventoryChangeListener = (InventoryChangeListener) var2.next();
                inventoryChangeListener.handleInventoryContentModified(this);
            }
            inventoryChangeListeners.clear();
        }
    }

    @Override
    public void emitStackListReplaced() {
        ReferenceOpenHashSet<InventoryChangeListener> listeners = this.inventoryHandlingTypeListeners;
        if (listeners != null && !listeners.isEmpty()) {
            listeners.forEach(inventoryChangeListener -> inventoryChangeListener.handleStackListReplaced(this));
        }
        this.invalidateChangeListening();
    }

    @Override
    public void emitRemoved() {
        ReferenceOpenHashSet<InventoryChangeListener> listeners = this.inventoryHandlingTypeListeners;
        if (listeners != null && !listeners.isEmpty()) {
            listeners.forEach(listener -> listener.handleInventoryRemoved(this));
        }
        this.invalidateChangeListening();
    }

    private void invalidateChangeListening() {
        if (this.inventoryChangeListeners != null) {
            this.inventoryChangeListeners.clear();
        }
        LithiumStackList lithiumStackList = InventoryHelper.getLithiumStackListOrNull(this);
        if (lithiumStackList != null) {
            lithiumStackList.removeInventoryModificationCallback(this);
        }
    }

    @Override
    public void emitFirstComparatorAdded() {
        ReferenceOpenHashSet<InventoryChangeListener> inventoryChangeListeners = this.inventoryChangeListeners;
        if (inventoryChangeListeners != null && !inventoryChangeListeners.isEmpty()) {
            inventoryChangeListeners.removeIf(inventoryChangeListener -> inventoryChangeListener.handleComparatorAdded(this));
        }
    }

    @Override
    public void forwardContentChangeOnce(InventoryChangeListener inventoryChangeListener, LithiumStackList stackList, InventoryChangeTracker thisTracker) {
        if (this.inventoryChangeListeners == null) {
            this.inventoryChangeListeners = new ReferenceOpenHashSet(1);
        }
        stackList.setInventoryModificationCallback(thisTracker);
        this.inventoryChangeListeners.add(inventoryChangeListener);
    }

    @Override
    public void forwardMajorInventoryChanges(InventoryChangeListener inventoryChangeListener) {
        if (this.inventoryHandlingTypeListeners == null) {
            this.inventoryHandlingTypeListeners = new ReferenceOpenHashSet(1);
            ((InventoryChangeTracker) this.first).listenForMajorInventoryChanges(this);
            ((InventoryChangeTracker) this.second).listenForMajorInventoryChanges(this);
        }
        this.inventoryHandlingTypeListeners.add(inventoryChangeListener);
    }

    @Override
    public void stopForwardingMajorInventoryChanges(InventoryChangeListener inventoryChangeListener) {
        if (this.inventoryHandlingTypeListeners != null) {
            this.inventoryHandlingTypeListeners.remove(inventoryChangeListener);
            if (this.inventoryHandlingTypeListeners.isEmpty()) {
                ((InventoryChangeTracker) this.first).stopListenForMajorInventoryChanges(this);
                ((InventoryChangeTracker) this.second).stopListenForMajorInventoryChanges(this);
            }
        }
    }

    @Override
    public NonNullList<ItemStack> getInventoryLithium() {
        return this.doubleStackList;
    }

    @Override
    public void setInventoryLithium(NonNullList<ItemStack> inventory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleInventoryContentModified(Container inventory) {
        this.emitContentModified();
    }

    @Override
    public void handleInventoryRemoved(Container inventory) {
        this.emitRemoved();
    }

    @Override
    public boolean handleComparatorAdded(Container inventory) {
        this.emitFirstComparatorAdded();
        return this.inventoryChangeListeners.isEmpty();
    }

    @Override
    public void onComparatorAdded(Direction direction, int offset) {
        throw new UnsupportedOperationException("Call onComparatorAdded(Direction direction, int offset) on the inventory half only!");
    }

    @Override
    public boolean hasAnyComparatorNearby() {
        return ((ComparatorTracker) this.first).hasAnyComparatorNearby() || ((ComparatorTracker) this.second).hasAnyComparatorNearby();
    }
}
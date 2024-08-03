package me.jellysquid.mods.lithium.mixin.block.hopper;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import me.jellysquid.mods.lithium.common.hopper.LithiumStackList;
import me.jellysquid.mods.lithium.common.hopper.StorableItemStack;
import me.jellysquid.mods.lithium.common.util.tuples.RefIntPair;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({ ItemStack.class })
public abstract class ItemStackMixin implements StorableItemStack {

    @Shadow
    private int count;

    private int mySlot;

    @Nullable
    private Object myLocation;

    @Override
    public void registerToInventory(LithiumStackList itemStacks, int mySlot) {
        if (this.myLocation != null) {
            this.lithiumRegisterMultipleInventories(itemStacks, mySlot);
        } else {
            this.myLocation = itemStacks;
            this.mySlot = mySlot;
        }
    }

    @Override
    public void unregisterFromInventory(LithiumStackList stackList) {
        this.unregisterFromInventory(stackList, -1);
    }

    @Override
    public void unregisterFromInventory(LithiumStackList myInventoryList, int index) {
        if (this.myLocation == myInventoryList) {
            this.myLocation = null;
            this.mySlot = -1;
        } else if (this.myLocation instanceof Set) {
            this.lithiumUnregisterMultipleInventories(myInventoryList, index);
        } else {
            this.myLocation = null;
        }
    }

    @ModifyVariable(method = { "setCount(I)V" }, at = @At("HEAD"))
    public int updateInventory(int count) {
        if (this.myLocation != null && this.count != count) {
            if (this.myLocation instanceof LithiumStackList stackList) {
                stackList.beforeSlotCountChange(this.mySlot, count);
            } else {
                this.lithiumUpdateMultipleInventories();
            }
        }
        return count;
    }

    private void lithiumRegisterMultipleInventories(LithiumStackList itemStacks, int mySlot) {
        if (!(this.myLocation instanceof Set<RefIntPair<LithiumStackList>> stackLists)) {
            stackLists = new ObjectOpenHashSet();
            if (this.myLocation != null) {
                RefIntPair<LithiumStackList> pair = new RefIntPair<>((LithiumStackList) this.myLocation, this.mySlot);
                stackLists.add(pair);
                this.myLocation = stackLists;
                this.mySlot = -1;
            }
        }
        RefIntPair<LithiumStackList> pair = new RefIntPair<>(itemStacks, mySlot);
        stackLists.add(pair);
    }

    private void lithiumUnregisterMultipleInventories(LithiumStackList itemStacks, int mySlot) {
        if (this.myLocation instanceof Set<?> set) {
            if (mySlot >= 0) {
                set.remove(new RefIntPair<>(itemStacks, mySlot));
            } else {
                set.removeIf(stackListSlotPair -> stackListSlotPair.left() == itemStacks);
            }
        }
    }

    private void lithiumUpdateMultipleInventories() {
        Object stackLists = this.myLocation;
        if (stackLists instanceof Set) {
            for (RefIntPair<LithiumStackList> stackListLocationPair : (Set) stackLists) {
                stackListLocationPair.left().beforeSlotCountChange(stackListLocationPair.right(), this.count);
            }
        }
    }
}
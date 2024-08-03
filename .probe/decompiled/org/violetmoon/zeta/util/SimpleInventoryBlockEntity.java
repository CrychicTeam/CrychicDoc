package org.violetmoon.zeta.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.block.be.ZetaBlockEntity;

public abstract class SimpleInventoryBlockEntity extends ZetaBlockEntity implements WorldlyContainer {

    protected NonNullList<ItemStack> inventorySlots = NonNullList.withSize(this.m_6643_(), ItemStack.EMPTY);

    public SimpleInventoryBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Override
    public void readSharedNBT(CompoundTag par1NBTTagCompound) {
        if (this.needsToSyncInventory()) {
            ListTag var2 = par1NBTTagCompound.getList("Items", 10);
            this.clearContent();
            for (int var3 = 0; var3 < var2.size(); var3++) {
                CompoundTag var4 = var2.getCompound(var3);
                byte var5 = var4.getByte("Slot");
                if (var5 >= 0 && var5 < this.inventorySlots.size()) {
                    this.inventorySlots.set(var5, ItemStack.of(var4));
                }
            }
        }
    }

    @Override
    public void writeSharedNBT(CompoundTag par1NBTTagCompound) {
        if (this.needsToSyncInventory()) {
            ListTag var2 = new ListTag();
            for (int var3 = 0; var3 < this.inventorySlots.size(); var3++) {
                if (!this.inventorySlots.get(var3).isEmpty()) {
                    CompoundTag var4 = new CompoundTag();
                    var4.putByte("Slot", (byte) var3);
                    this.inventorySlots.get(var3).save(var4);
                    var2.add(var4);
                }
            }
            par1NBTTagCompound.put("Items", var2);
        }
    }

    protected boolean needsToSyncInventory() {
        return true;
    }

    @NotNull
    @Override
    public ItemStack getItem(int i) {
        return this.inventorySlots.get(i);
    }

    @NotNull
    @Override
    public ItemStack removeItem(int i, int j) {
        if (!this.inventorySlots.get(i).isEmpty()) {
            ItemStack stackAt;
            if (this.inventorySlots.get(i).getCount() <= j) {
                stackAt = this.inventorySlots.get(i);
                this.inventorySlots.set(i, ItemStack.EMPTY);
            } else {
                stackAt = this.inventorySlots.get(i).split(j);
                if (this.inventorySlots.get(i).getCount() == 0) {
                    this.inventorySlots.set(i, ItemStack.EMPTY);
                }
            }
            this.inventoryChanged(i);
            return stackAt;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @NotNull
    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack stack = this.getItem(i);
        this.setItem(i, ItemStack.EMPTY);
        this.inventoryChanged(i);
        return stack;
    }

    @Override
    public void setItem(int i, @NotNull ItemStack itemstack) {
        this.inventorySlots.set(i, itemstack);
        this.inventoryChanged(i);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.m_6643_(); i++) {
            ItemStack stack = this.getItem(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean stillValid(@NotNull Player entityplayer) {
        return this.m_58904_().getBlockEntity(this.m_58899_()) == this && entityplayer.m_20275_((double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.5, (double) this.f_58858_.m_123343_() + 0.5) <= 64.0;
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, Direction facing) {
        return capability == ForgeCapabilities.ITEM_HANDLER ? LazyOptional.of(() -> (T) (new SidedInvWrapper(this, facing))) : LazyOptional.empty();
    }

    @Override
    public boolean canPlaceItem(int i, @NotNull ItemStack itemstack) {
        return true;
    }

    @Override
    public void startOpen(@NotNull Player player) {
    }

    @Override
    public void stopOpen(@NotNull Player player) {
    }

    @Override
    public void clearContent() {
        this.inventorySlots = NonNullList.withSize(this.m_6643_(), ItemStack.EMPTY);
    }

    public void inventoryChanged(int i) {
    }

    public boolean isAutomationEnabled() {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction) {
        return this.isAutomationEnabled();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack itemStackIn, Direction direction) {
        return this.isAutomationEnabled();
    }

    @NotNull
    @Override
    public int[] getSlotsForFace(@NotNull Direction side) {
        if (!this.isAutomationEnabled()) {
            return new int[0];
        } else {
            int[] slots = new int[this.m_6643_()];
            int i = 0;
            while (i < slots.length) {
                slots[i] = i++;
            }
            return slots;
        }
    }
}
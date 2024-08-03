package com.mna.api.blocks.tile;

import java.util.HashMap;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileEntityWithInventory extends BlockEntity implements Container, WorldlyContainer {

    protected NonNullList<ItemStack> inventoryItems;

    protected boolean ignoreQtyLimit = false;

    protected HashMap<Direction, LazyOptional<?>> sidedHandlers;

    public TileEntityWithInventory(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state, int inventorySize) {
        super(tileEntityTypeIn, pos, state);
        this.inventoryItems = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        this.sidedHandlers = new HashMap();
    }

    protected LazyOptional<?> getOrCreateSidedHandler(Direction side) {
        if (!this.sidedHandlers.containsKey(side)) {
            this.sidedHandlers.put(side, LazyOptional.of(() -> new SidedInvWrapper(this, side)));
        }
        return (LazyOptional<?>) this.sidedHandlers.get(side);
    }

    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return !this.f_58859_ && cap == ForgeCapabilities.ITEM_HANDLER ? this.getOrCreateSidedHandler(side).cast() : super.getCapability(cap, side);
    }

    @Override
    public void clearContent() {
        this.m_6596_();
        this.inventoryItems.clear();
    }

    @Override
    public int getContainerSize() {
        return this.inventoryItems.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.inventoryItems) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasStack(int index) {
        return !this.getItem(index).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.inventoryItems.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack output = ContainerHelper.removeItem(this.inventoryItems, index, count);
        this.m_6596_();
        return output;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack itemstack = this.inventoryItems.get(index);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.inventoryItems.set(index, ItemStack.EMPTY);
            this.m_6596_();
            return itemstack;
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.inventoryItems.set(index, stack);
        if (!this.ignoreQtyLimit && !stack.isEmpty() && stack.getCount() > this.m_6893_()) {
            stack.setCount(this.m_6893_());
        }
        this.m_6596_();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.f_58857_.getBlockEntity(this.f_58858_) != this ? false : this.m_58899_().m_203195_(player.m_20182_(), 8.0);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        ContainerHelper.saveAllItems(compound, this.inventoryItems);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        ContainerHelper.loadAllItems(compound, this.inventoryItems);
    }
}
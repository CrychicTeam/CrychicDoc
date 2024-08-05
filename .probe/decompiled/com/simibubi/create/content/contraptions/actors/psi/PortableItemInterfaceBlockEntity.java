package com.simibubi.create.content.contraptions.actors.psi;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.foundation.item.ItemHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class PortableItemInterfaceBlockEntity extends PortableStorageInterfaceBlockEntity {

    protected LazyOptional<IItemHandlerModifiable> capability = this.createEmptyHandler();

    public PortableItemInterfaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void startTransferringTo(Contraption contraption, float distance) {
        LazyOptional<IItemHandlerModifiable> oldCap = this.capability;
        this.capability = LazyOptional.of(() -> new PortableItemInterfaceBlockEntity.InterfaceItemHandler(contraption.getSharedInventory()));
        oldCap.invalidate();
        super.startTransferringTo(contraption, distance);
    }

    @Override
    protected void stopTransferring() {
        LazyOptional<IItemHandlerModifiable> oldCap = this.capability;
        this.capability = this.createEmptyHandler();
        oldCap.invalidate();
        super.stopTransferring();
    }

    private LazyOptional<IItemHandlerModifiable> createEmptyHandler() {
        return LazyOptional.of(() -> new PortableItemInterfaceBlockEntity.InterfaceItemHandler(new ItemStackHandler(0)));
    }

    @Override
    protected void invalidateCapability() {
        this.capability.invalidate();
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return this.isItemHandlerCap(cap) ? this.capability.cast() : super.getCapability(cap, side);
    }

    class InterfaceItemHandler extends ItemHandlerWrapper {

        public InterfaceItemHandler(IItemHandlerModifiable wrapped) {
            super(wrapped);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (!PortableItemInterfaceBlockEntity.this.canTransfer()) {
                return ItemStack.EMPTY;
            } else {
                ItemStack extractItem = super.extractItem(slot, amount, simulate);
                if (!simulate && !extractItem.isEmpty()) {
                    PortableItemInterfaceBlockEntity.this.onContentTransferred();
                }
                return extractItem;
            }
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (!PortableItemInterfaceBlockEntity.this.canTransfer()) {
                return stack;
            } else {
                ItemStack insertItem = super.insertItem(slot, stack, simulate);
                if (!simulate && !insertItem.equals(stack, false)) {
                    PortableItemInterfaceBlockEntity.this.onContentTransferred();
                }
                return insertItem;
            }
        }
    }
}
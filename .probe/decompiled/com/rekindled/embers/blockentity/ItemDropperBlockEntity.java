package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemDropperBlockEntity extends BlockEntity implements IItemPipePriority {

    public ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            ItemDropperBlockEntity.this.m_6596_();
        }
    };

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    public ItemDropperBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.ITEM_DROPPER_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", this.inventory.serializeNBT());
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ItemDropperBlockEntity blockEntity) {
        if (!blockEntity.inventory.getStackInSlot(0).isEmpty()) {
            level.m_7967_(new ItemEntity(level, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5, blockEntity.inventory.extractItem(0, 1, false), 0.0, -0.1, 0.0));
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && side == Direction.UP && cap == ForgeCapabilities.ITEM_HANDLER ? ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder) : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.holder.invalidate();
    }

    @Override
    public int getPriority(Direction facing) {
        return 50;
    }
}
package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.tile.IBin;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class BinBlockEntity extends BlockEntity implements IBin {

    int ticksExisted = 0;

    public ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            BinBlockEntity.this.setChanged();
        }
    };

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    public BinBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.BIN_ENTITY.get(), pPos, pBlockState);
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

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.put("inventory", this.inventory.serializeNBT());
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BinBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        if (blockEntity.ticksExisted % 10 == 0) {
            List<ItemEntity> items = level.m_45976_(ItemEntity.class, new AABB((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), (double) (pos.m_123341_() + 1), (double) pos.m_123342_() + 1.25, (double) (pos.m_123343_() + 1)));
            for (int i = 0; i < items.size(); i++) {
                ItemStack stack = blockEntity.inventory.insertItem(0, ((ItemEntity) items.get(i)).getItem(), false);
                if (!stack.isEmpty()) {
                    ((ItemEntity) items.get(i)).setItem(stack);
                } else {
                    ((ItemEntity) items.get(i)).m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == ForgeCapabilities.ITEM_HANDLER ? ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder) : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.holder.invalidate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }

    @Override
    public IItemHandler getInventory() {
        return this.inventory;
    }
}
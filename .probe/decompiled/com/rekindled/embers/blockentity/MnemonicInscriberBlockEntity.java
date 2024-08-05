package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.datagen.EmbersItemTags;
import com.rekindled.embers.upgrade.MnemonicInscriberUpgrade;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class MnemonicInscriberBlockEntity extends BlockEntity implements IExtraCapabilityInformation {

    public ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.is(EmbersItemTags.INSCRIBABLE_PAPER);
        }

        @Override
        protected void onContentsChanged(int slot) {
            MnemonicInscriberBlockEntity.this.setChanged();
        }
    };

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    public MnemonicInscriberUpgrade upgrade = new MnemonicInscriberUpgrade(this);

    public MnemonicInscriberBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.MNEMONIC_INSCRIBER_ENTITY.get(), pPos, pBlockState);
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

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.f_58859_ && (this.f_58857_.getBlockState(this.f_58858_).m_61138_(BlockStateProperties.FACING) || side == null)) {
            Direction facing = side == null ? null : (Direction) this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.FACING);
            if (cap == ForgeCapabilities.ITEM_HANDLER && (side == null || side.getOpposite() != facing)) {
                return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder);
            }
            if (cap == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY && (side == null || side.getOpposite() == facing)) {
                return this.upgrade.getCapability(cap, side);
            }
        }
        return super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.holder.invalidate();
        this.upgrade.invalidate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.ITEM_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.INPUT, "embers.tooltip.goggles.item", Component.translatable("embers.tooltip.goggles.item.paper")));
        }
    }
}
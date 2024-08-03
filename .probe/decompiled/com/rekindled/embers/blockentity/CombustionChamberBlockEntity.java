package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.recipe.CatalysisCombustionContext;
import com.rekindled.embers.recipe.ICatalysisCombustionRecipe;
import com.rekindled.embers.util.Misc;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CombustionChamberBlockEntity extends BlockEntity implements IExtraCapabilityInformation {

    public static ItemStack machine = new ItemStack(RegistryManager.COMBUSTION_CHAMBER_ITEM.get());

    int progress = 0;

    double multiplier = 0.0;

    public ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            CombustionChamberBlockEntity.this.setChanged();
        }
    };

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    public ICatalysisCombustionRecipe cachedRecipe = null;

    public CombustionChamberBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.COMBUSTION_CHAMBER_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("inventory")) {
            this.inventory.deserializeNBT(nbt.getCompound("inventory"));
        }
        if (nbt.contains("progress")) {
            this.progress = nbt.getInt("progress");
        }
        this.multiplier = nbt.getDouble("multiplier");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", this.inventory.serializeNBT());
        nbt.putInt("progress", this.progress);
        nbt.putDouble("multiplier", this.multiplier);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putDouble("multiplier", this.multiplier);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CombustionChamberBlockEntity blockEntity) {
        if (blockEntity.progress > 0) {
            blockEntity.progress--;
            if (blockEntity.progress == 0) {
                blockEntity.multiplier = 0.0;
                ((ServerLevel) level).getChunkSource().blockChanged(pos);
            }
            blockEntity.setChanged();
        }
        if (blockEntity.progress == 0 && !blockEntity.inventory.getStackInSlot(0).isEmpty()) {
            CatalysisCombustionContext wrapper = new CatalysisCombustionContext(blockEntity.inventory, machine);
            blockEntity.cachedRecipe = Misc.getRecipe(blockEntity.cachedRecipe, RegistryManager.CATALYSIS_COMBUSTION.get(), wrapper, level);
            if (blockEntity.cachedRecipe != null) {
                blockEntity.multiplier = blockEntity.cachedRecipe.getmultiplier(wrapper);
                blockEntity.progress = blockEntity.cachedRecipe.getBurnTIme(wrapper);
                blockEntity.cachedRecipe.process(wrapper);
                ((ServerLevel) level).getChunkSource().blockChanged(pos);
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
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.ITEM_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.INPUT, "embers.tooltip.goggles.item", Component.translatable("embers.tooltip.goggles.item.combustion")));
        }
    }
}
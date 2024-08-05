package com.rekindled.embers.blockentity;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.particle.VaporParticleOptions;
import com.rekindled.embers.util.Misc;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.joml.Vector3f;

public class StampBaseBlockEntity extends OpenTankBlockEntity implements IExtraCapabilityInformation {

    int ticksExisted = 0;

    public float renderOffset;

    int previousFluid;

    public ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            StampBaseBlockEntity.this.m_6596_();
        }
    };

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    public StampBaseBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.STAMP_BASE_ENTITY.get(), pPos, pBlockState);
        this.tank = new FluidTank(ConfigManager.STAMP_BASE_CAPACITY.get()) {

            @Override
            public void onContentsChanged() {
                StampBaseBlockEntity.this.m_6596_();
            }

            @Override
            public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
                if (Misc.isGaseousFluid(resource)) {
                    StampBaseBlockEntity.this.setEscapedFluid(resource);
                    return resource.getAmount();
                } else {
                    return super.fill(resource, action);
                }
            }
        };
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

    public int getCapacity() {
        return this.tank.getCapacity();
    }

    public FluidStack getFluidStack() {
        return this.tank.getFluid();
    }

    public FluidTank getTank() {
        return this.tank;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, StampBaseBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        if (blockEntity.ticksExisted == 1) {
            blockEntity.previousFluid = blockEntity.tank.getFluidAmount();
        }
        if (blockEntity.tank.getFluidAmount() != blockEntity.previousFluid) {
            blockEntity.renderOffset = blockEntity.renderOffset + (float) blockEntity.tank.getFluidAmount() - (float) blockEntity.previousFluid;
            blockEntity.previousFluid = blockEntity.tank.getFluidAmount();
        }
        if (blockEntity.shouldEmitParticles()) {
            blockEntity.updateEscapeParticles();
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == ForgeCapabilities.ITEM_HANDLER ? ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder) : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.holder.invalidate();
    }

    @Override
    protected void updateEscapeParticles() {
        Vector3f color = IClientFluidTypeExtensions.of(this.lastEscaped.getFluid().getFluidType()).modifyFogColor(Minecraft.getInstance().gameRenderer.getMainCamera(), 0.0F, (ClientLevel) this.f_58857_, 6, 0.0F, new Vector3f(1.0F, 1.0F, 1.0F));
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            float xOffset = 0.5F + (random.nextFloat() - 0.5F) * 2.0F * 0.2F;
            float yOffset = 0.9F;
            float zOffset = 0.5F + (random.nextFloat() - 0.5F) * 2.0F * 0.2F;
            this.f_58857_.addParticle(new VaporParticleOptions(color, 2.0F), (double) ((float) this.f_58858_.m_123341_() + xOffset), (double) ((float) this.f_58858_.m_123342_() + yOffset), (double) ((float) this.f_58858_.m_123343_() + zOffset), 0.0, 0.2F, 0.0);
        }
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.FLUID_HANDLER || capability == ForgeCapabilities.ITEM_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.INPUT, "embers.tooltip.goggles.fluid", Component.translatable("embers.tooltip.goggles.fluid.metal")));
        }
    }
}
package com.mna.blocks.tileentities;

import com.mna.blocks.artifice.FluidJugBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidJugTile extends BlockEntity implements IFluidHandler {

    private FluidStack infiniteFluid = null;

    private FluidStack containedFluid = FluidStack.EMPTY;

    private int capacity = 16000;

    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> this);

    public FluidJugTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public FluidJugTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.FLUID_JUG.get(), pos, state);
    }

    private void spawnFluidFillParticles() {
        int numParticles = this.containedFluid.getAmount() == this.capacity ? 50 : 1;
        for (int i = 0; i < numParticles; i++) {
            this.m_58904_().addParticle(ParticleTypes.SPLASH, (double) this.m_58899_().m_123341_() + 0.5, (double) (this.m_58899_().m_123342_() + 1), (double) this.m_58899_().m_123343_() + 0.5, ((double) this.m_58904_().getRandom().nextFloat() - 0.5) * 2.0, (double) this.m_58904_().getRandom().nextFloat(), ((double) this.m_58904_().getRandom().nextFloat() - 0.5) * 2.0);
        }
    }

    public boolean isInfinite() {
        Block block = this.m_58900_().m_60734_();
        return block instanceof FluidJugBlock ? ((FluidJugBlock) block).is_infinite() : false;
    }

    private FluidStack infiniteFluidType() {
        if (this.infiniteFluid == null) {
            this.infiniteFluid = FluidStack.EMPTY;
            Block block = this.m_58900_().m_60734_();
            if (block instanceof FluidJugBlock) {
                ResourceLocation flType = ((FluidJugBlock) block).getFluidType();
                if (ForgeRegistries.FLUIDS.containsKey(flType)) {
                    this.infiniteFluid = new FluidStack(ForgeRegistries.FLUIDS.getValue(flType), this.capacity);
                }
            }
        }
        return this.infiniteFluid;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.isInfinite() ? this.infiniteFluidType() : this.containedFluid;
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.capacity;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return this.isInfinite() ? stack.getFluid().equals(this.infiniteFluidType().getFluid()) : true;
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if (this.isInfinite()) {
            return resource.getFluid().equals(this.infiniteFluidType().getFluid()) ? resource.getAmount() : 0;
        } else if (resource.isEmpty()) {
            return 0;
        } else if (!this.containedFluid.isEmpty() && !this.containedFluid.isFluidEqual(resource)) {
            return 0;
        } else {
            int amount = Math.min(resource.getAmount(), this.capacity - this.containedFluid.getAmount());
            if (action != IFluidHandler.FluidAction.SIMULATE) {
                if (this.containedFluid.isEmpty()) {
                    this.containedFluid = new FluidStack(resource.getFluid(), 0);
                }
                this.containedFluid.setAmount(this.containedFluid.getAmount() + amount);
                if (!this.m_58904_().isClientSide()) {
                    this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
                }
            }
            return amount;
        }
    }

    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        return this.isInfinite() || !this.containedFluid.isEmpty() && this.containedFluid.isFluidEqual(resource) ? this.drain(resource.getAmount(), action) : FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        if (this.isInfinite()) {
            FluidStack flOutput = this.infiniteFluidType().copy();
            flOutput.setAmount(maxDrain);
            return flOutput;
        } else if (this.containedFluid.isEmpty()) {
            return FluidStack.EMPTY;
        } else {
            int amount = Math.min(maxDrain, this.containedFluid.getAmount());
            FluidStack output = new FluidStack(this.containedFluid.getFluid(), amount);
            if (action != IFluidHandler.FluidAction.SIMULATE) {
                this.containedFluid.shrink(amount);
                if (this.containedFluid.getAmount() <= 0) {
                    this.containedFluid = FluidStack.EMPTY;
                }
                if (!this.m_58904_().isClientSide()) {
                    this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
                }
            }
            return output;
        }
    }

    public float getFillPct() {
        return this.isInfinite() ? 1.0F : (float) this.containedFluid.getAmount() / (float) this.capacity;
    }

    public FluidStack getContainedFluid() {
        return this.containedFluid;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.containedFluid = FluidStack.loadFluidStackFromNBT(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.containedFluid.writeToNBT(tag);
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == ForgeCapabilities.FLUID_HANDLER ? this.holder.cast() : super.getCapability(capability, facing);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag base = super.getUpdateTag();
        this.saveAdditional(base);
        return base;
    }

    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        int prevAmt = this.containedFluid.getAmount();
        this.load(tag);
        if (this.containedFluid.getAmount() != prevAmt && this.m_58904_().isClientSide()) {
            this.spawnFluidFillParticles();
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        int prevAmt = this.containedFluid.getAmount();
        this.load(tag);
        if (this.containedFluid.getAmount() > prevAmt && this.m_58904_().isClientSide()) {
            this.spawnFluidFillParticles();
        }
    }
}
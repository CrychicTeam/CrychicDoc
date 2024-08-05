package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.block.MechEdgeBlockBase;
import com.rekindled.embers.datagen.EmbersBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CaminiteValveBlockEntity extends BlockEntity {

    int ticksExisted = 0;

    ReservoirBlockEntity reservoir;

    IFluidHandler fluidHandler;

    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> this.fluidHandler);

    public CaminiteValveBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.CAMINITE_VALVE_ENTITY.get(), pPos, pBlockState);
        this.fluidHandler = new IFluidHandler() {

            @Override
            public int getTanks() {
                return CaminiteValveBlockEntity.this.reservoir != null ? CaminiteValveBlockEntity.this.reservoir.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null).getTanks() : 0;
            }

            @NotNull
            @Override
            public FluidStack getFluidInTank(int tank) {
                return CaminiteValveBlockEntity.this.reservoir != null ? CaminiteValveBlockEntity.this.reservoir.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null).getFluidInTank(tank) : FluidStack.EMPTY;
            }

            @Override
            public int getTankCapacity(int tank) {
                return CaminiteValveBlockEntity.this.reservoir != null ? CaminiteValveBlockEntity.this.reservoir.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null).getTankCapacity(tank) : 0;
            }

            @Override
            public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
                return CaminiteValveBlockEntity.this.reservoir != null ? CaminiteValveBlockEntity.this.reservoir.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null).isFluidValid(tank, stack) : false;
            }

            @Override
            public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
                return CaminiteValveBlockEntity.this.reservoir != null ? CaminiteValveBlockEntity.this.reservoir.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null).fill(resource, action) : 0;
            }

            @NotNull
            @Override
            public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
                return CaminiteValveBlockEntity.this.reservoir != null ? CaminiteValveBlockEntity.this.reservoir.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null).drain(resource, action) : FluidStack.EMPTY;
            }

            @NotNull
            @Override
            public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
                return CaminiteValveBlockEntity.this.reservoir != null ? CaminiteValveBlockEntity.this.reservoir.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null).drain(maxDrain, action) : FluidStack.EMPTY;
            }
        };
    }

    public ReservoirBlockEntity getReservoir() {
        return this.reservoir;
    }

    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return this.f_58859_ || capability != ForgeCapabilities.FLUID_HANDLER || facing != null && facing.getAxis() == Direction.Axis.Y ? super.getCapability(capability, facing) : this.holder.cast();
    }

    public void updateTank() {
        if (!this.m_58901_() && this.f_58857_.getBlockState(this.f_58858_).m_61138_(MechEdgeBlockBase.EDGE)) {
            this.reservoir = null;
            BlockPos basePos = this.f_58858_.offset(((MechEdgeBlockBase.MechEdge) this.f_58857_.getBlockState(this.f_58858_).m_61143_(MechEdgeBlockBase.EDGE)).centerPos);
            for (int i = 1; i < 64; i++) {
                BlockPos pos = basePos.below(i);
                if (!this.f_58857_.getBlockState(pos).m_204336_(EmbersBlockTags.RESERVOIR_EXPANSION)) {
                    BlockEntity tile = this.f_58857_.getBlockEntity(pos);
                    if (tile instanceof ReservoirBlockEntity) {
                        this.reservoir = (ReservoirBlockEntity) tile;
                    }
                    break;
                }
            }
        }
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, CaminiteValveBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        if (blockEntity.reservoir != null && blockEntity.reservoir.m_58901_()) {
            blockEntity.reservoir = null;
        }
        if (blockEntity.ticksExisted % 20 == 0) {
            blockEntity.updateTank();
        }
    }
}
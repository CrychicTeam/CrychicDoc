package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.particle.VaporParticleOptions;
import com.rekindled.embers.util.Misc;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.joml.Vector3f;

public class FluidTransferBlockEntity extends FluidPipeBlockEntityBase {

    public static final int PRIORITY_TRANSFER = -10;

    public FluidStack filterFluid = FluidStack.EMPTY;

    Random random = new Random();

    public boolean syncFilter = true;

    IFluidHandler outputSide;

    public LazyOptional<IFluidHandler> outputHolder = LazyOptional.of(() -> this.outputSide);

    public FluidTransferBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.FLUID_TRANSFER_ENTITY.get(), pPos, pBlockState);
        this.syncConnections = false;
        this.saveConnections = false;
    }

    @Override
    protected void initFluidTank() {
        this.tank = new FluidTank(this.getCapacity()) {

            @Override
            protected void onContentsChanged() {
                FluidTransferBlockEntity.this.m_6596_();
            }

            @Override
            public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
                if (FluidTransferBlockEntity.this.filterFluid.isEmpty()) {
                    return super.fill(resource, action);
                } else {
                    return resource == null || (FluidTransferBlockEntity.this.filterFluid.getTag() != null ? !resource.isFluidEqual(FluidTransferBlockEntity.this.filterFluid) : resource.getFluid() != FluidTransferBlockEntity.this.filterFluid.getFluid()) ? 0 : super.fill(resource, action);
                }
            }
        };
        this.outputSide = Misc.makeRestrictedFluidHandler(this.tank, false, true);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("filter")) {
            this.filterFluid = FluidStack.loadFluidStackFromNBT(nbt.getCompound("filter"));
        }
    }

    @Override
    protected boolean requiresSync() {
        return this.syncFilter || super.requiresSync();
    }

    @Override
    protected void resetSync() {
        super.resetSync();
        this.syncFilter = false;
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.writeFilter(nbt);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.m_5995_();
        if (this.syncFilter) {
            this.writeFilter(nbt);
        }
        return nbt;
    }

    private void writeFilter(CompoundTag nbt) {
        nbt.put("filter", this.filterFluid.writeToNBT(new CompoundTag()));
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FluidTransferBlockEntity blockEntity) {
        if (level instanceof ServerLevel && blockEntity.clogged && blockEntity.isAnySideUnclogged()) {
            Random posRand = new Random(pos.asLong());
            double angleA = posRand.nextDouble() * Math.PI * 2.0;
            double angleB = posRand.nextDouble() * Math.PI * 2.0;
            float xOffset = (float) (Math.cos(angleA) * Math.cos(angleB));
            float yOffset = (float) (Math.sin(angleA) * Math.cos(angleB));
            float zOffset = (float) Math.sin(angleB);
            float speed = 0.1F;
            float vx = xOffset * speed + posRand.nextFloat() * speed * 0.3F;
            float vy = yOffset * speed + posRand.nextFloat() * speed * 0.3F;
            float vz = zOffset * speed + posRand.nextFloat() * speed * 0.3F;
            ((ServerLevel) level).sendParticles(new VaporParticleOptions(new Vector3f(0.2509804F, 0.2509804F, 0.2509804F), new Vec3((double) vx, (double) vy, (double) vz), 1.0F), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, 4, 0.0, 0.0, 0.0, 1.0);
        }
        FluidPipeBlockEntityBase.serverTick(level, pos, state, blockEntity);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.f_58859_ && cap == ForgeCapabilities.FLUID_HANDLER) {
            if (side == null) {
                return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, this.holder);
            }
            if (this.f_58857_.getBlockState(this.m_58899_()).m_61138_(BlockStateProperties.FACING)) {
                Direction facing = (Direction) this.f_58857_.getBlockState(this.m_58899_()).m_61143_(BlockStateProperties.FACING);
                if (side.getOpposite() == facing) {
                    return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, this.outputHolder);
                }
                if (side.getAxis() == facing.getAxis()) {
                    return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, this.holder);
                }
            }
        }
        return LazyOptional.empty();
    }

    @Override
    public int getCapacity() {
        return 240;
    }

    @Override
    public int getPriority(Direction facing) {
        return -10;
    }

    @Override
    public PipeBlockEntityBase.PipeConnection getConnection(Direction facing) {
        BlockState state = this.f_58857_.getBlockState(this.m_58899_());
        return state.m_61138_(BlockStateProperties.FACING) && ((Direction) state.m_61143_(BlockStateProperties.FACING)).getAxis() == facing.getAxis() ? PipeBlockEntityBase.PipeConnection.PIPE : PipeBlockEntityBase.PipeConnection.NONE;
    }

    @Override
    protected boolean isFrom(Direction facing) {
        return this.f_58857_.getBlockState(this.m_58899_()).m_61143_(BlockStateProperties.FACING) == facing;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.outputHolder.invalidate();
    }
}
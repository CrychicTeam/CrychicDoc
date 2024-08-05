package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.particle.VaporParticleOptions;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class FluidPipeBlockEntity extends FluidPipeBlockEntityBase {

    IFluidHandler[] sideHandlers;

    public FluidPipeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.FLUID_PIPE_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void initFluidTank() {
        super.initFluidTank();
        this.sideHandlers = new IFluidHandler[Direction.values().length];
        for (final Direction facing : Direction.values()) {
            this.sideHandlers[facing.get3DDataValue()] = new IFluidHandler() {

                @Override
                public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
                    if (action.execute()) {
                        FluidPipeBlockEntity.this.setFrom(facing, true);
                    }
                    return FluidPipeBlockEntity.this.tank.fill(resource, action);
                }

                @Nullable
                @Override
                public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
                    return FluidPipeBlockEntity.this.tank.drain(resource, action);
                }

                @Nullable
                @Override
                public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
                    return FluidPipeBlockEntity.this.tank.drain(maxDrain, action);
                }

                @Override
                public int getTanks() {
                    return FluidPipeBlockEntity.this.tank.getTanks();
                }

                @NotNull
                @Override
                public FluidStack getFluidInTank(int tankNum) {
                    return FluidPipeBlockEntity.this.tank.getFluidInTank(tankNum);
                }

                @Override
                public int getTankCapacity(int tankNum) {
                    return FluidPipeBlockEntity.this.tank.getTankCapacity(tankNum);
                }

                @Override
                public boolean isFluidValid(int tankNum, @NotNull FluidStack stack) {
                    return FluidPipeBlockEntity.this.tank.isFluidValid(tankNum, stack);
                }
            };
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FluidPipeBlockEntity blockEntity) {
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
            if (this.getConnection(side).transfer) {
                return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, LazyOptional.of(() -> this.sideHandlers[side.get3DDataValue()]));
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public int getCapacity() {
        return 240;
    }
}
package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.particle.VaporParticleOptions;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class FluidExtractorBlockEntity extends FluidPipeBlockEntityBase {

    Random random = new Random();

    IFluidHandler[] sideHandlers;

    boolean active;

    public static final int MAX_DRAIN = 120;

    public FluidExtractorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.FLUID_EXTRACTOR_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void initFluidTank() {
        super.initFluidTank();
        this.sideHandlers = new IFluidHandler[Direction.values().length];
        for (final Direction facing : Direction.values()) {
            this.sideHandlers[facing.get3DDataValue()] = new IFluidHandler() {

                @Override
                public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
                    if (FluidExtractorBlockEntity.this.active) {
                        return 0;
                    } else {
                        if (action.execute()) {
                            FluidExtractorBlockEntity.this.setFrom(facing, true);
                        }
                        return FluidExtractorBlockEntity.this.tank.fill(resource, action);
                    }
                }

                @Nullable
                @Override
                public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
                    return FluidExtractorBlockEntity.this.tank.drain(resource, action);
                }

                @Nullable
                @Override
                public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
                    return FluidExtractorBlockEntity.this.tank.drain(maxDrain, action);
                }

                @Override
                public int getTanks() {
                    return FluidExtractorBlockEntity.this.tank.getTanks();
                }

                @NotNull
                @Override
                public FluidStack getFluidInTank(int tankNum) {
                    return FluidExtractorBlockEntity.this.tank.getFluidInTank(tankNum);
                }

                @Override
                public int getTankCapacity(int tankNum) {
                    return FluidExtractorBlockEntity.this.tank.getTankCapacity(tankNum);
                }

                @Override
                public boolean isFluidValid(int tankNum, @NotNull FluidStack stack) {
                    return FluidExtractorBlockEntity.this.tank.isFluidValid(tankNum, stack);
                }
            };
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FluidExtractorBlockEntity blockEntity) {
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
        blockEntity.active = level.m_276867_(pos);
        for (Direction facing : Direction.values()) {
            if (blockEntity.getConnection(facing).transfer) {
                BlockEntity tile = level.getBlockEntity(pos.relative(facing));
                if (tile != null && !(tile instanceof FluidPipeBlockEntityBase)) {
                    if (blockEntity.active) {
                        IFluidHandler handler = (IFluidHandler) tile.getCapability(ForgeCapabilities.FLUID_HANDLER, facing.getOpposite()).orElse(null);
                        if (handler != null && handler.drain(120, IFluidHandler.FluidAction.SIMULATE) != null) {
                            FluidStack extracted = handler.drain(120, IFluidHandler.FluidAction.SIMULATE);
                            int filled = blockEntity.tank.fill(extracted, IFluidHandler.FluidAction.SIMULATE);
                            if (filled > 0) {
                                blockEntity.tank.fill(extracted, IFluidHandler.FluidAction.EXECUTE);
                                handler.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                            }
                        }
                        blockEntity.setFrom(facing, true);
                    } else {
                        blockEntity.setFrom(facing, false);
                    }
                }
            }
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
package com.rekindled.embers.blockentity;

import com.rekindled.embers.api.tile.IFluidPipePriority;
import com.rekindled.embers.util.PipePriorityMap;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public abstract class FluidPipeBlockEntityBase extends PipeBlockEntityBase implements IFluidPipePriority {

    public static final int MAX_PUSH = 120;

    public FluidTank tank;

    public LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> this.tank);

    public FluidPipeBlockEntityBase(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.initFluidTank();
    }

    protected void initFluidTank() {
        this.tank = new FluidTank(this.getCapacity()) {

            @Override
            protected void onContentsChanged() {
                FluidPipeBlockEntityBase.this.m_6596_();
            }
        };
    }

    public abstract int getCapacity();

    @Override
    public int getPriority(Direction facing) {
        return 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FluidPipeBlockEntityBase blockEntity) {
        if (!blockEntity.loaded) {
            blockEntity.initConnections();
        }
        blockEntity.ticksExisted++;
        boolean fluidMoved = false;
        FluidStack passStack = blockEntity.tank.drain(120, IFluidHandler.FluidAction.SIMULATE);
        if (!passStack.isEmpty()) {
            PipePriorityMap<Integer, Direction> possibleDirections = new PipePriorityMap<>();
            IFluidHandler[] fluidHandlers = new IFluidHandler[Direction.values().length];
            for (Direction facing : Direction.values()) {
                if (blockEntity.getConnection(facing).transfer && !blockEntity.isFrom(facing)) {
                    BlockEntity tile = level.getBlockEntity(pos.relative(facing));
                    if (tile != null) {
                        IFluidHandler handler = (IFluidHandler) tile.getCapability(ForgeCapabilities.FLUID_HANDLER, facing.getOpposite()).orElse(null);
                        if (handler != null) {
                            int priority = 0;
                            if (tile instanceof IFluidPipePriority) {
                                priority = ((IFluidPipePriority) tile).getPriority(facing.getOpposite());
                            }
                            if (blockEntity.isFrom(facing.getOpposite())) {
                                priority -= 5;
                            }
                            possibleDirections.put(priority, facing);
                            fluidHandlers[facing.get3DDataValue()] = handler;
                        }
                    }
                }
            }
            for (int key : possibleDirections.keySet()) {
                ArrayList<Direction> list = possibleDirections.get(key);
                for (int i = 0; i < list.size(); i++) {
                    Direction facingx = (Direction) list.get((i + blockEntity.lastRobin) % list.size());
                    IFluidHandler handler = fluidHandlers[facingx.get3DDataValue()];
                    fluidMoved = blockEntity.pushStack(passStack, facingx, handler);
                    if (blockEntity.lastTransfer != facingx) {
                        blockEntity.lastTransfer = facingx;
                        blockEntity.syncTransfer = true;
                        blockEntity.m_6596_();
                    }
                    if (fluidMoved) {
                        blockEntity.lastRobin++;
                        break;
                    }
                }
                if (fluidMoved) {
                    break;
                }
            }
        }
        if (blockEntity.tank.getFluidAmount() <= 0) {
            if (blockEntity.lastTransfer != null && !fluidMoved) {
                blockEntity.lastTransfer = null;
                blockEntity.syncTransfer = true;
                blockEntity.m_6596_();
            }
            fluidMoved = true;
            blockEntity.resetFrom();
        }
        if (blockEntity.clogged == fluidMoved) {
            blockEntity.clogged = !fluidMoved;
            blockEntity.syncCloggedFlag = true;
            blockEntity.m_6596_();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientTick(Level level, BlockPos pos, BlockState state, FluidPipeBlockEntityBase blockEntity) {
        PipeBlockEntityBase.clientTick(level, pos, state, blockEntity);
    }

    private boolean pushStack(FluidStack passStack, Direction facing, IFluidHandler handler) {
        int added = handler.fill(passStack, IFluidHandler.FluidAction.SIMULATE);
        if (added > 0) {
            handler.fill(passStack, IFluidHandler.FluidAction.EXECUTE);
            this.tank.drain(added, IFluidHandler.FluidAction.EXECUTE);
            passStack.setAmount(passStack.getAmount() - added);
            return passStack.getAmount() <= 0;
        } else {
            if (this.isFrom(facing)) {
                this.setFrom(facing, false);
            }
            return false;
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("tank")) {
            this.tank.readFromNBT(nbt.getCompound("tank"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.writeTank(nbt);
    }

    public void writeTank(CompoundTag nbt) {
        nbt.put("tank", this.tank.writeToNBT(new CompoundTag()));
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == ForgeCapabilities.FLUID_HANDLER ? this.holder.cast() : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.holder.invalidate();
    }
}
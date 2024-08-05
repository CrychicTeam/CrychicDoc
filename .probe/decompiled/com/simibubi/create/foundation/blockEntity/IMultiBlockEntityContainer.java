package com.simibubi.create.foundation.blockEntity;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public interface IMultiBlockEntityContainer {

    BlockPos getController();

    <T extends BlockEntity & IMultiBlockEntityContainer> T getControllerBE();

    boolean isController();

    void setController(BlockPos var1);

    void removeController(boolean var1);

    BlockPos getLastKnownPos();

    void preventConnectivityUpdate();

    void notifyMultiUpdated();

    default void setExtraData(@Nullable Object data) {
    }

    @Nullable
    default Object getExtraData() {
        return null;
    }

    default Object modifyExtraData(Object data) {
        return data;
    }

    Direction.Axis getMainConnectionAxis();

    default Direction.Axis getMainAxisOf(BlockEntity be) {
        BlockState state = be.getBlockState();
        Direction.Axis axis;
        if (state.m_61138_(BlockStateProperties.HORIZONTAL_AXIS)) {
            axis = (Direction.Axis) state.m_61143_(BlockStateProperties.HORIZONTAL_AXIS);
        } else if (state.m_61138_(BlockStateProperties.FACING)) {
            axis = ((Direction) state.m_61143_(BlockStateProperties.FACING)).getAxis();
        } else if (state.m_61138_(BlockStateProperties.HORIZONTAL_FACING)) {
            axis = ((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING)).getAxis();
        } else {
            axis = Direction.Axis.Y;
        }
        return axis;
    }

    int getMaxLength(Direction.Axis var1, int var2);

    int getMaxWidth();

    int getHeight();

    void setHeight(int var1);

    int getWidth();

    void setWidth(int var1);

    public interface Fluid extends IMultiBlockEntityContainer {

        default boolean hasTank() {
            return false;
        }

        default int getTankSize(int tank) {
            return 0;
        }

        default void setTankSize(int tank, int blocks) {
        }

        default IFluidTank getTank(int tank) {
            return null;
        }

        default FluidStack getFluid(int tank) {
            return FluidStack.EMPTY;
        }
    }

    public interface Inventory extends IMultiBlockEntityContainer {

        default boolean hasInventory() {
            return false;
        }
    }
}
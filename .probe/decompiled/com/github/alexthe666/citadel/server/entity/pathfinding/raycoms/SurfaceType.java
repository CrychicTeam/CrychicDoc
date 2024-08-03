package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BambooSaplingBlock;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

public enum SurfaceType {

    WALKABLE, DROPABLE, NOT_PASSABLE, FLYABLE;

    public static SurfaceType getSurfaceType(BlockGetter world, BlockState blockState, BlockPos pos) {
        Block block = blockState.m_60734_();
        if (block instanceof FenceBlock || block instanceof FenceGateBlock || block instanceof WallBlock || block instanceof FireBlock || block instanceof CampfireBlock || block instanceof BambooStalkBlock || block instanceof BambooSaplingBlock || block instanceof DoorBlock || block instanceof MagmaBlock || block instanceof PowderSnowBlock) {
            return NOT_PASSABLE;
        } else if (block instanceof TrapDoorBlock && !(Boolean) blockState.m_61143_(TrapDoorBlock.OPEN)) {
            return WALKABLE;
        } else {
            VoxelShape shape = blockState.m_60808_(world, pos);
            if (shape.max(Direction.Axis.Y) > 1.0) {
                return NOT_PASSABLE;
            } else {
                FluidState fluid = world.getFluidState(pos);
                if (blockState.m_60734_() != Blocks.LAVA && (fluid.isEmpty() || fluid.getType() != Fluids.LAVA && fluid.getType() != Fluids.FLOWING_LAVA)) {
                    if (isWater(world, pos, blockState, fluid)) {
                        return WALKABLE;
                    } else if (block instanceof SignBlock || block instanceof VineBlock) {
                        return DROPABLE;
                    } else {
                        return (!blockState.m_280296_() || !(shape.max(Direction.Axis.X) - shape.min(Direction.Axis.X) > 0.75) || !(shape.max(Direction.Axis.Z) - shape.min(Direction.Axis.Z) > 0.75)) && (blockState.m_60734_() != Blocks.SNOW || blockState.m_61143_(SnowLayerBlock.LAYERS) <= 1) && !(block instanceof CarpetBlock) ? DROPABLE : WALKABLE;
                    }
                } else {
                    return NOT_PASSABLE;
                }
            }
        }
    }

    public static boolean isWater(LevelReader world, BlockPos pos) {
        return isWater(world, pos, null, null);
    }

    public static boolean isWater(BlockGetter world, BlockPos pos, @Nullable BlockState pState, @Nullable FluidState pFluidState) {
        BlockState state = pState;
        if (pState == null) {
            state = world.getBlockState(pos);
        }
        if (state.m_60815_()) {
            return false;
        } else if (state.m_60734_() == Blocks.WATER) {
            return true;
        } else {
            FluidState fluidState = pFluidState;
            if (pFluidState == null) {
                fluidState = world.getFluidState(pos);
            }
            if (fluidState.isEmpty()) {
                return false;
            } else if ((state.m_60734_() instanceof TrapDoorBlock || state.m_60734_() instanceof HorizontalDirectionalBlock) && state.m_61138_(TrapDoorBlock.OPEN) && !(Boolean) state.m_61143_(TrapDoorBlock.OPEN) && state.m_61138_(TrapDoorBlock.HALF) && state.m_61143_(TrapDoorBlock.HALF) == Half.TOP) {
                return false;
            } else {
                Fluid fluid = fluidState.getType();
                return fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER;
            }
        }
    }
}
package com.github.alexmodguy.alexscaves.server.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;

public class GalenaPillarBlock extends RotatedPillarBlock {

    public static final IntegerProperty SHAPE = IntegerProperty.create("shape", 0, 3);

    public GalenaPillarBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.5F, 10.0F).sound(SoundType.DEEPSLATE));
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(f_55923_, Direction.Axis.Y)).m_61124_(SHAPE, 3));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        Direction.Axis axis = context.m_43719_().getAxis();
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(f_55923_, axis)).m_61124_(SHAPE, this.getShapeInt(levelaccessor, context.getClickedPos(), axis));
    }

    public int getShapeInt(LevelAccessor levelAccessor, BlockPos pos, Direction.Axis axis) {
        Direction belowDir = Direction.UP;
        Direction aboveDir = Direction.UP;
        switch(axis) {
            case X:
                belowDir = Direction.WEST;
                aboveDir = Direction.EAST;
                break;
            case Y:
                belowDir = Direction.DOWN;
                aboveDir = Direction.UP;
                break;
            case Z:
                belowDir = Direction.SOUTH;
                aboveDir = Direction.NORTH;
        }
        BlockState aboveState = levelAccessor.m_8055_(pos.relative(aboveDir));
        BlockState belowState = levelAccessor.m_8055_(pos.relative(belowDir));
        boolean above = aboveState.m_60713_(this) && aboveState.m_61143_(f_55923_) == axis;
        boolean below = belowState.m_60713_(this) && belowState.m_61143_(f_55923_) == axis;
        if (above && below) {
            return 3;
        } else if (!above && !below) {
            return 0;
        } else {
            return above ? 1 : 2;
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        return (BlockState) state.m_61124_(SHAPE, this.getShapeInt(levelAccessor, blockPos, (Direction.Axis) state.m_61143_(f_55923_)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(SHAPE, f_55923_);
    }
}
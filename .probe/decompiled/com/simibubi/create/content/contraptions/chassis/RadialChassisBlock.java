package com.simibubi.create.content.contraptions.chassis;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class RadialChassisBlock extends AbstractChassisBlock {

    public static final BooleanProperty STICKY_NORTH = BooleanProperty.create("sticky_north");

    public static final BooleanProperty STICKY_SOUTH = BooleanProperty.create("sticky_south");

    public static final BooleanProperty STICKY_EAST = BooleanProperty.create("sticky_east");

    public static final BooleanProperty STICKY_WEST = BooleanProperty.create("sticky_west");

    public RadialChassisBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(STICKY_EAST, false)).m_61124_(STICKY_SOUTH, false)).m_61124_(STICKY_NORTH, false)).m_61124_(STICKY_WEST, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STICKY_NORTH, STICKY_EAST, STICKY_SOUTH, STICKY_WEST);
        super.m_7926_(builder);
    }

    @Override
    public BooleanProperty getGlueableSide(BlockState state, Direction face) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(f_55923_);
        if (axis == Direction.Axis.X) {
            if (face == Direction.NORTH) {
                return STICKY_WEST;
            }
            if (face == Direction.SOUTH) {
                return STICKY_EAST;
            }
            if (face == Direction.UP) {
                return STICKY_NORTH;
            }
            if (face == Direction.DOWN) {
                return STICKY_SOUTH;
            }
        }
        if (axis == Direction.Axis.Y) {
            if (face == Direction.NORTH) {
                return STICKY_NORTH;
            }
            if (face == Direction.SOUTH) {
                return STICKY_SOUTH;
            }
            if (face == Direction.EAST) {
                return STICKY_EAST;
            }
            if (face == Direction.WEST) {
                return STICKY_WEST;
            }
        }
        if (axis == Direction.Axis.Z) {
            if (face == Direction.UP) {
                return STICKY_NORTH;
            }
            if (face == Direction.DOWN) {
                return STICKY_SOUTH;
            }
            if (face == Direction.EAST) {
                return STICKY_EAST;
            }
            if (face == Direction.WEST) {
                return STICKY_WEST;
            }
        }
        return null;
    }
}
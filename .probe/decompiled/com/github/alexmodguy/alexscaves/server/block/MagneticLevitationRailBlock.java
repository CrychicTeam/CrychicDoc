package com.github.alexmodguy.alexscaves.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;

public class MagneticLevitationRailBlock extends BaseRailBlock {

    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

    public MagneticLevitationRailBlock() {
        super(true, BlockBehaviour.Properties.of().noCollission().strength(0.7F).sound(SoundType.METAL).lightLevel(i -> 3).emissiveRendering((state, level, pos) -> true));
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(SHAPE, RailShape.NORTH_SOUTH)).m_61124_(f_152149_, false));
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(f_152149_, SHAPE);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        switch(rotation) {
            case CLOCKWISE_180:
                switch((RailShape) state.m_61143_(SHAPE)) {
                    case ASCENDING_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.NORTH_WEST);
                    case SOUTH_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                }
            case COUNTERCLOCKWISE_90:
                switch((RailShape) state.m_61143_(SHAPE)) {
                    case ASCENDING_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_SOUTH:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.NORTH_SOUTH);
                }
            case CLOCKWISE_90:
                switch((RailShape) state.m_61143_(SHAPE)) {
                    case ASCENDING_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_SOUTH:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.NORTH_SOUTH);
                }
            default:
                return state;
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        RailShape railshape = (RailShape) state.m_61143_(SHAPE);
        switch(mirror) {
            case LEFT_RIGHT:
                switch(railshape) {
                    case ASCENDING_NORTH:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    default:
                        return super.m_6943_(state, mirror);
                }
            case FRONT_BACK:
                switch(railshape) {
                    case ASCENDING_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return (BlockState) state.m_61124_(SHAPE, RailShape.NORTH_WEST);
                }
            default:
                return super.m_6943_(state, mirror);
        }
    }

    public float getRailMaxSpeed(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        return super.getRailMaxSpeed(state, level, pos, cart) + 0.3F;
    }
}
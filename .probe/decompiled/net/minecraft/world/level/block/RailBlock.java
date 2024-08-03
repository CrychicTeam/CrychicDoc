package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;

public class RailBlock extends BaseRailBlock {

    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE;

    protected RailBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(false, blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(SHAPE, RailShape.NORTH_SOUTH)).m_61124_(f_152149_, false));
    }

    @Override
    protected void updateState(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3) {
        if (block3.defaultBlockState().m_60803_() && new RailState(level1, blockPos2, blockState0).countPotentialConnections() == 3) {
            this.m_49367_(level1, blockPos2, blockState0, false);
        }
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        switch(rotation1) {
            case CLOCKWISE_180:
                switch((RailShape) blockState0.m_61143_(SHAPE)) {
                    case ASCENDING_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_WEST);
                    case SOUTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                }
            case COUNTERCLOCKWISE_90:
                switch((RailShape) blockState0.m_61143_(SHAPE)) {
                    case ASCENDING_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_SOUTH);
                }
            case CLOCKWISE_90:
                switch((RailShape) blockState0.m_61143_(SHAPE)) {
                    case ASCENDING_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_SOUTH);
                }
            default:
                return blockState0;
        }
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        RailShape $$2 = (RailShape) blockState0.m_61143_(SHAPE);
        switch(mirror1) {
            case LEFT_RIGHT:
                switch($$2) {
                    case ASCENDING_NORTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    default:
                        return super.m_6943_(blockState0, mirror1);
                }
            case FRONT_BACK:
                switch($$2) {
                    case ASCENDING_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_WEST);
                }
        }
        return super.m_6943_(blockState0, mirror1);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(SHAPE, f_152149_);
    }
}
package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;

public class PoweredRailBlock extends BaseRailBlock {

    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    protected PoweredRailBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(true, blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(SHAPE, RailShape.NORTH_SOUTH)).m_61124_(POWERED, false)).m_61124_(f_152149_, false));
    }

    protected boolean findPoweredRailSignal(Level level0, BlockPos blockPos1, BlockState blockState2, boolean boolean3, int int4) {
        if (int4 >= 8) {
            return false;
        } else {
            int $$5 = blockPos1.m_123341_();
            int $$6 = blockPos1.m_123342_();
            int $$7 = blockPos1.m_123343_();
            boolean $$8 = true;
            RailShape $$9 = (RailShape) blockState2.m_61143_(SHAPE);
            switch($$9) {
                case NORTH_SOUTH:
                    if (boolean3) {
                        $$7++;
                    } else {
                        $$7--;
                    }
                    break;
                case EAST_WEST:
                    if (boolean3) {
                        $$5--;
                    } else {
                        $$5++;
                    }
                    break;
                case ASCENDING_EAST:
                    if (boolean3) {
                        $$5--;
                    } else {
                        $$5++;
                        $$6++;
                        $$8 = false;
                    }
                    $$9 = RailShape.EAST_WEST;
                    break;
                case ASCENDING_WEST:
                    if (boolean3) {
                        $$5--;
                        $$6++;
                        $$8 = false;
                    } else {
                        $$5++;
                    }
                    $$9 = RailShape.EAST_WEST;
                    break;
                case ASCENDING_NORTH:
                    if (boolean3) {
                        $$7++;
                    } else {
                        $$7--;
                        $$6++;
                        $$8 = false;
                    }
                    $$9 = RailShape.NORTH_SOUTH;
                    break;
                case ASCENDING_SOUTH:
                    if (boolean3) {
                        $$7++;
                        $$6++;
                        $$8 = false;
                    } else {
                        $$7--;
                    }
                    $$9 = RailShape.NORTH_SOUTH;
            }
            return this.isSameRailWithPower(level0, new BlockPos($$5, $$6, $$7), boolean3, int4, $$9) ? true : $$8 && this.isSameRailWithPower(level0, new BlockPos($$5, $$6 - 1, $$7), boolean3, int4, $$9);
        }
    }

    protected boolean isSameRailWithPower(Level level0, BlockPos blockPos1, boolean boolean2, int int3, RailShape railShape4) {
        BlockState $$5 = level0.getBlockState(blockPos1);
        if (!$$5.m_60713_(this)) {
            return false;
        } else {
            RailShape $$6 = (RailShape) $$5.m_61143_(SHAPE);
            if (railShape4 != RailShape.EAST_WEST || $$6 != RailShape.NORTH_SOUTH && $$6 != RailShape.ASCENDING_NORTH && $$6 != RailShape.ASCENDING_SOUTH) {
                if (railShape4 != RailShape.NORTH_SOUTH || $$6 != RailShape.EAST_WEST && $$6 != RailShape.ASCENDING_EAST && $$6 != RailShape.ASCENDING_WEST) {
                    if (!(Boolean) $$5.m_61143_(POWERED)) {
                        return false;
                    } else {
                        return level0.m_276867_(blockPos1) ? true : this.findPoweredRailSignal(level0, blockPos1, $$5, boolean2, int3 + 1);
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    protected void updateState(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3) {
        boolean $$4 = (Boolean) blockState0.m_61143_(POWERED);
        boolean $$5 = level1.m_276867_(blockPos2) || this.findPoweredRailSignal(level1, blockPos2, blockState0, true, 0) || this.findPoweredRailSignal(level1, blockPos2, blockState0, false, 0);
        if ($$5 != $$4) {
            level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(POWERED, $$5), 3);
            level1.updateNeighborsAt(blockPos2.below(), this);
            if (((RailShape) blockState0.m_61143_(SHAPE)).isAscending()) {
                level1.updateNeighborsAt(blockPos2.above(), this);
            }
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
                    case NORTH_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_SOUTH);
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
                }
            case CLOCKWISE_90:
                switch((RailShape) blockState0.m_61143_(SHAPE)) {
                    case NORTH_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_SOUTH);
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
        stateDefinitionBuilderBlockBlockState0.add(SHAPE, POWERED, f_152149_);
    }
}
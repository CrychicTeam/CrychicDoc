package com.simibubi.create.content.decoration.palettes;

import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.ticks.LevelTickAccess;

public class ConnectedPillarBlock extends LayeredBlock {

    public static final BooleanProperty NORTH = BooleanProperty.create("north");

    public static final BooleanProperty SOUTH = BooleanProperty.create("south");

    public static final BooleanProperty EAST = BooleanProperty.create("east");

    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public ConnectedPillarBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(NORTH, false)).m_61124_(WEST, false)).m_61124_(EAST, false)).m_61124_(SOUTH, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.m_7926_(pBuilder.add(NORTH, SOUTH, EAST, WEST));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = super.getStateForPlacement(pContext);
        return this.updateColumn(pContext.m_43725_(), pContext.getClickedPos(), state, true);
    }

    private BlockState updateColumn(Level level, BlockPos pos, BlockState state, boolean present) {
        BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos();
        Direction.Axis axis = (Direction.Axis) state.m_61143_(f_55923_);
        for (Direction connection : Iterate.directions) {
            if (connection.getAxis() != axis) {
                boolean connect = true;
                label54: for (Direction movement : Iterate.directionsInAxis(axis)) {
                    currentPos.set(pos);
                    for (int i = 0; i < 1000 && level.isLoaded(currentPos); i++) {
                        BlockState other1 = currentPos.equals(pos) ? state : level.getBlockState(currentPos);
                        BlockState other2 = level.getBlockState(currentPos.m_121945_(connection));
                        boolean col1 = this.canConnect(state, other1);
                        boolean col2 = this.canConnect(state, other2);
                        currentPos.move(movement);
                        if (!col1 && !col2) {
                            break;
                        }
                        if (!col1 || !col2) {
                            connect = false;
                            break label54;
                        }
                    }
                }
                state = setConnection(state, connection, connect);
            }
        }
        return state;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (pOldState.m_60734_() != this) {
            LevelTickAccess<Block> blockTicks = pLevel.m_183326_();
            if (!blockTicks.m_183582_(pPos, this)) {
                pLevel.m_186460_(pPos, this, 1);
            }
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.m_60734_() == this) {
            BlockPos belowPos = pPos.relative(Direction.fromAxisAndDirection((Direction.Axis) pState.m_61143_(f_55923_), Direction.AxisDirection.NEGATIVE));
            BlockState belowState = pLevel.m_8055_(belowPos);
            if (!this.canConnect(pState, belowState)) {
                pLevel.m_7731_(pPos, this.updateColumn(pLevel, pPos, pState, true), 3);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (!this.canConnect(state, pNeighborState)) {
            return setConnection(state, pDirection, false);
        } else {
            return pDirection.getAxis() == state.m_61143_(f_55923_) ? this.m_152465_(pNeighborState) : setConnection(state, pDirection, getConnection(pNeighborState, pDirection.getOpposite()));
        }
    }

    protected boolean canConnect(BlockState state, BlockState other) {
        return other.m_60734_() == this && state.m_61143_(f_55923_) == other.m_61143_(f_55923_);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pIsMoving && pNewState.m_60734_() != this) {
            for (Direction d : Iterate.directionsInAxis((Direction.Axis) pState.m_61143_(f_55923_))) {
                BlockPos relative = pPos.relative(d);
                BlockState adjacent = pLevel.getBlockState(relative);
                if (this.canConnect(pState, adjacent)) {
                    pLevel.setBlock(relative, this.updateColumn(pLevel, relative, adjacent, false), 3);
                }
            }
        }
    }

    public static boolean getConnection(BlockState state, Direction side) {
        BooleanProperty property = connection((Direction.Axis) state.m_61143_(f_55923_), side);
        return property != null && (Boolean) state.m_61143_(property);
    }

    public static BlockState setConnection(BlockState state, Direction side, boolean connect) {
        BooleanProperty property = connection((Direction.Axis) state.m_61143_(f_55923_), side);
        if (property != null) {
            state = (BlockState) state.m_61124_(property, connect);
        }
        return state;
    }

    public static BooleanProperty connection(Direction.Axis axis, Direction side) {
        if (side.getAxis() == axis) {
            return null;
        } else if (axis == Direction.Axis.X) {
            switch(side) {
                case UP:
                    return EAST;
                case NORTH:
                    return NORTH;
                case SOUTH:
                    return SOUTH;
                case DOWN:
                    return WEST;
                default:
                    return null;
            }
        } else if (axis == Direction.Axis.Y) {
            switch(side) {
                case NORTH:
                    return NORTH;
                case SOUTH:
                    return SOUTH;
                case DOWN:
                default:
                    return null;
                case EAST:
                    return EAST;
                case WEST:
                    return WEST;
            }
        } else if (axis == Direction.Axis.Z) {
            switch(side) {
                case UP:
                    return WEST;
                case NORTH:
                case SOUTH:
                default:
                    return null;
                case DOWN:
                    return EAST;
                case EAST:
                    return NORTH;
                case WEST:
                    return SOUTH;
            }
        } else {
            return null;
        }
    }
}
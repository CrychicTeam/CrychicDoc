package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.EmberEmitterBlockEntity;
import com.rekindled.embers.datagen.EmbersBlockTags;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EmberEmitterBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    protected static final VoxelShape UP_AABB = Shapes.box(0.25, 0.0, 0.25, 0.75, 0.9375, 0.75);

    protected static final VoxelShape DOWN_AABB = Shapes.box(0.25, 0.0625, 0.25, 0.75, 1.0, 0.75);

    protected static final VoxelShape NORTH_AABB = Shapes.box(0.25, 0.25, 0.0625, 0.75, 0.75, 1.0);

    protected static final VoxelShape SOUTH_AABB = Shapes.box(0.25, 0.25, 0.0, 0.75, 0.75, 0.9375);

    protected static final VoxelShape WEST_AABB = Shapes.box(0.0625, 0.25, 0.25, 1.0, 0.75, 0.75);

    protected static final VoxelShape EAST_AABB = Shapes.box(0.0, 0.25, 0.25, 0.9375, 0.75, 0.75);

    protected static final VoxelShape SUPPORT_X = Shapes.or(Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.1), Shapes.box(0.0, 0.0, 0.9, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.0, 0.0, 1.0, 0.1, 1.0), Shapes.box(0.0, 0.9, 0.0, 1.0, 1.0, 1.0));

    protected static final VoxelShape SUPPORT_Y = Shapes.or(Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.1), Shapes.box(0.0, 0.0, 0.9, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.0, 0.0, 0.1, 1.0, 1.0), Shapes.box(0.9, 0.0, 0.0, 1.0, 1.0, 1.0));

    protected static final VoxelShape SUPPORT_Z = Shapes.or(Shapes.box(0.0, 0.0, 0.0, 1.0, 0.1, 1.0), Shapes.box(0.0, 0.9, 0.0, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.0, 0.0, 0.1, 1.0, 1.0), Shapes.box(0.9, 0.0, 0.0, 1.0, 1.0, 1.0));

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final Direction[] X_DIRECTIONS = new Direction[] { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH };

    public static final Direction[] Y_DIRECTIONS = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };

    public static final Direction[] Z_DIRECTIONS = new Direction[] { Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST };

    public static final BooleanProperty[] DIRECTIONS = new BooleanProperty[] { BooleanProperty.create("front"), BooleanProperty.create("right"), BooleanProperty.create("back"), BooleanProperty.create("left") };

    public static final BooleanProperty[] ALL_DIRECTIONS = new BooleanProperty[] { BlockStateProperties.DOWN, BlockStateProperties.UP, BlockStateProperties.NORTH, BlockStateProperties.SOUTH, BlockStateProperties.WEST, BlockStateProperties.EAST };

    public EmberEmitterBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.UP)).m_61124_(BlockStateProperties.WATERLOGGED, false)).m_61124_(DIRECTIONS[0], false)).m_61124_(DIRECTIONS[1], false)).m_61124_(DIRECTIONS[2], false)).m_61124_(DIRECTIONS[3], false));
    }

    public static int getIndexForDirection(Direction.Axis axis, Direction direction) {
        int index = direction.get3DDataValue();
        switch(axis) {
            case Z:
                return index > 1 ? index - 2 : index;
            case Y:
                return index - 2;
            case X:
            default:
                return index;
        }
    }

    public static Direction getDirectionForIndex(Direction.Axis axis, int index) {
        switch(axis) {
            case Z:
                return Z_DIRECTIONS[index];
            case Y:
            default:
                return Y_DIRECTIONS[index];
            case X:
                return X_DIRECTIONS[index];
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch((Direction) pState.m_61143_(FACING)) {
            case UP:
                return UP_AABB;
            case DOWN:
                return DOWN_AABB;
            case EAST:
                return EAST_AABB;
            case WEST:
                return WEST_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case NORTH:
            default:
                return NORTH_AABB;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return canAttach(pLevel, pPos, ((Direction) pState.m_61143_(FACING)).getOpposite());
    }

    public static boolean canAttach(LevelReader pReader, BlockPos pPos, Direction pDirection) {
        return !pReader.m_8055_(pPos.relative(pDirection)).m_60795_();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        for (Direction direction : pContext.getNearestLookingDirections()) {
            BlockState blockstate = (BlockState) this.m_49966_().m_61124_(FACING, direction.getOpposite());
            if (blockstate.m_60710_(pContext.m_43725_(), pContext.getClickedPos())) {
                for (int i = 0; i < DIRECTIONS.length; i++) {
                    Direction facing = getDirectionForIndex(direction.getAxis(), i);
                    blockstate = (BlockState) blockstate.m_61124_(DIRECTIONS[i], connected(facing, pContext.m_43725_().getBlockState(pContext.getClickedPos().relative(facing))));
                }
                return (BlockState) blockstate.m_61124_(BlockStateProperties.WATERLOGGED, pContext.m_43725_().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER);
            }
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if ((Boolean) pState.m_61143_(BlockStateProperties.WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.m_6718_(pLevel));
        }
        if (((Direction) pState.m_61143_(FACING)).getAxis() != pFacing.getAxis()) {
            pState = (BlockState) pState.m_61124_(DIRECTIONS[getIndexForDirection(((Direction) pState.m_61143_(FACING)).getAxis(), pFacing)], connected(pFacing, pFacingState));
        }
        return ((Direction) pState.m_61143_(FACING)).getOpposite() == pFacing && !pState.m_60710_(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    private static boolean facingConnected(Direction facing, BlockState state, DirectionProperty property) {
        return !state.m_61138_(property) || state.m_61143_(property) == facing;
    }

    private static boolean connected(Direction direction, BlockState state) {
        if (!state.m_204336_(EmbersBlockTags.EMITTER_CONNECTION)) {
            return false;
        } else if (!state.m_204336_(EmbersBlockTags.EMITTER_CONNECTION_FLOOR) && !state.m_204336_(EmbersBlockTags.EMITTER_CONNECTION_CEILING)) {
            BooleanProperty sideProp = ALL_DIRECTIONS[direction.getOpposite().get3DDataValue()];
            if (state.m_61138_(sideProp) && (Boolean) state.m_61143_(sideProp)) {
                return true;
            } else if (state.m_61138_(BlockStateProperties.BELL_ATTACHMENT) && state.m_61143_(BlockStateProperties.BELL_ATTACHMENT) == BellAttachType.CEILING && direction == Direction.DOWN) {
                return true;
            } else if (state.m_61138_(BlockStateProperties.HANGING)) {
                return direction == Direction.DOWN && state.m_61143_(BlockStateProperties.HANGING) ? true : direction == Direction.UP && !(Boolean) state.m_61143_(BlockStateProperties.HANGING);
            } else if (state.m_61138_(BlockStateProperties.AXIS) && !state.m_61138_(BlockStateProperties.FACING)) {
                return state.m_61143_(BlockStateProperties.AXIS) == direction.getAxis();
            } else if (!state.m_61138_(BlockStateProperties.ATTACH_FACE) || state.m_61143_(BlockStateProperties.ATTACH_FACE) == AttachFace.WALL) {
                return facingConnected(direction, state, BlockStateProperties.HORIZONTAL_FACING) && facingConnected(direction, state, BlockStateProperties.FACING) && facingConnected(direction, state, BlockStateProperties.FACING_HOPPER);
            } else {
                return direction == Direction.DOWN && state.m_61143_(BlockStateProperties.ATTACH_FACE) == AttachFace.CEILING ? true : direction == Direction.UP && state.m_61143_(BlockStateProperties.ATTACH_FACE) == AttachFace.FLOOR;
            }
        } else {
            return direction == Direction.DOWN && state.m_204336_(EmbersBlockTags.EMITTER_CONNECTION_CEILING) ? true : direction == Direction.UP;
        }
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return (BlockState) pState.m_61124_(FACING, pRot.rotate((Direction) pState.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.m_60717_(pMirror.getRotation((Direction) pState.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING).add(DIRECTIONS).add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.EMBER_EMITTER_ENTITY.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : m_152132_(pBlockEntityType, RegistryManager.EMBER_EMITTER_ENTITY.get(), EmberEmitterBlockEntity::serverTick);
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        switch(((Direction) pState.m_61143_(FACING)).getAxis()) {
            case Z:
            default:
                return SUPPORT_Z;
            case Y:
                return SUPPORT_Y;
            case X:
                return SUPPORT_X;
        }
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(pState);
    }
}
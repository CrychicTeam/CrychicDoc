package snownee.kiwi.customization.block.component;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.block.loader.KBlockComponents;

public record MouldingComponent() implements KBlockComponent {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

    private static final MouldingComponent INSTANCE = new MouldingComponent();

    public static MouldingComponent getInstance() {
        return INSTANCE;
    }

    @Override
    public KBlockComponent.Type<?> type() {
        return KBlockComponents.MOULDING.getOrCreate();
    }

    @Override
    public void injectProperties(Block block, StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE);
    }

    @Override
    public BlockState registerDefaultState(BlockState state) {
        return (BlockState) ((BlockState) state.m_61124_(FACING, Direction.NORTH)).m_61124_(SHAPE, StairsShape.STRAIGHT);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }

    private StairsShape getShapeAt(BlockState ourState, BlockGetter pLevel, BlockPos pPos) {
        Direction ourFacing = (Direction) ourState.m_61143_(FACING);
        BlockState theirState = pLevel.getBlockState(pPos.relative(ourFacing));
        if (this.canBeConnected(ourState, theirState)) {
            Direction theirFacing = (Direction) theirState.m_61143_(FACING);
            if (theirFacing.getAxis() != ourFacing.getAxis() && this.canTakeShape(ourState, pLevel, pPos, theirFacing.getOpposite())) {
                if (theirFacing == ourFacing.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }
                return StairsShape.OUTER_RIGHT;
            }
        }
        theirState = pLevel.getBlockState(pPos.relative(ourFacing.getOpposite()));
        if (this.canBeConnected(ourState, theirState)) {
            Direction theirFacing = (Direction) theirState.m_61143_(FACING);
            if (theirFacing.getAxis() != ourFacing.getAxis() && this.canTakeShape(ourState, pLevel, pPos, theirFacing)) {
                if (theirFacing == ourFacing.getCounterClockWise()) {
                    return StairsShape.INNER_LEFT;
                }
                return StairsShape.INNER_RIGHT;
            }
        }
        return StairsShape.STRAIGHT;
    }

    private boolean canTakeShape(BlockState ourState, BlockGetter pLevel, BlockPos pPos, Direction pFace) {
        BlockState blockState = pLevel.getBlockState(pPos.relative(pFace));
        return !this.canBeConnected(ourState, blockState) || blockState.m_61143_(FACING) != ourState.m_61143_(FACING);
    }

    private boolean canBeConnected(BlockState ourState, BlockState theirState) {
        return ourState.m_60713_(theirState.m_60734_());
    }

    @Override
    public BlockState getStateForPlacement(KBlockSettings settings, BlockState state, BlockPlaceContext context) {
        if (settings.customPlacement) {
            return state;
        } else {
            BlockPos blockpos = context.getClickedPos();
            BlockState blockstate = (BlockState) state.m_61124_(FACING, context.m_8125_());
            blockstate = (BlockState) blockstate.m_61124_(SHAPE, this.getShapeAt(blockstate, context.m_43725_(), blockpos));
            return blockstate.m_60710_(context.m_43725_(), context.getClickedPos()) ? blockstate : null;
        }
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (pDirection.getAxis().isHorizontal()) {
            pState = (BlockState) pState.m_61124_(SHAPE, this.getShapeAt(pState, pLevel, pPos));
        }
        return pState;
    }

    @Nullable
    @Override
    public Direction getHorizontalFacing(BlockState blockState) {
        return blockState.m_61143_(SHAPE) == StairsShape.STRAIGHT ? ((Direction) blockState.m_61143_(FACING)).getOpposite() : null;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return (BlockState) pState.m_61124_(FACING, pRotation.rotate((Direction) pState.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        Direction direction = (Direction) pState.m_61143_(FACING);
        StairsShape stairsshape = (StairsShape) pState.m_61143_(SHAPE);
        switch(pMirror) {
            case LEFT_RIGHT:
                if (direction.getAxis() == Direction.Axis.Z) {
                    return switch(stairsshape) {
                        case INNER_LEFT ->
                            (BlockState) pState.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.INNER_RIGHT);
                        case INNER_RIGHT ->
                            (BlockState) pState.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.INNER_LEFT);
                        case OUTER_LEFT ->
                            (BlockState) pState.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT ->
                            (BlockState) pState.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.OUTER_LEFT);
                        case STRAIGHT ->
                            pState.m_60717_(Rotation.CLOCKWISE_180);
                    };
                }
                break;
            case FRONT_BACK:
                if (direction.getAxis() == Direction.Axis.X) {
                    return switch(stairsshape) {
                        case INNER_LEFT ->
                            (BlockState) pState.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_RIGHT ->
                            (BlockState) pState.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_LEFT ->
                            (BlockState) pState.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT ->
                            (BlockState) pState.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.OUTER_LEFT);
                        case STRAIGHT ->
                            pState.m_60717_(Rotation.CLOCKWISE_180);
                    };
                }
        }
        return pState;
    }
}
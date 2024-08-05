package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.block.blockentity.ACBlockEntityRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.CopperValveBlockEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CopperValveBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final BooleanProperty TURNED = BooleanProperty.create("turned");

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE_UP = ACMath.buildShape(Block.box(7.0, 0.0, 7.0, 9.0, 11.0, 9.0), Block.box(1.0, 9.0, 1.0, 15.0, 11.0, 15.0));

    private static final VoxelShape SHAPE_DOWN = ACMath.buildShape(Block.box(7.0, 4.0, 7.0, 9.0, 16.0, 9.0), Block.box(1.0, 5.0, 1.0, 15.0, 7.0, 15.0));

    private static final VoxelShape SHAPE_NORTH = ACMath.buildShape(Block.box(7.0, 7.0, 4.0, 9.0, 9.0, 16.0), Block.box(1.0, 1.0, 5.0, 15.0, 15.0, 7.0));

    private static final VoxelShape SHAPE_SOUTH = ACMath.buildShape(Block.box(7.0, 7.0, 0.0, 9.0, 9.0, 11.0), Block.box(1.0, 1.0, 9.0, 15.0, 15.0, 11.0));

    private static final VoxelShape SHAPE_EAST = ACMath.buildShape(Block.box(0.0, 7.0, 7.0, 11.0, 9.0, 9.0), Block.box(9.0, 1.0, 1.0, 11.0, 15.0, 15.0));

    private static final VoxelShape SHAPE_WEST = ACMath.buildShape(Block.box(4.0, 7.0, 7.0, 16.0, 9.0, 9.0), Block.box(5.0, 1.0, 1.0, 7.0, 15.0, 15.0));

    protected CopperValveBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(3.0F, 12.0F).sound(SoundType.COPPER));
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, Direction.UP)).m_61124_(TURNED, false));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = (Direction) state.m_61143_(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        return level.m_8055_(blockpos).m_60783_(level, blockpos, direction);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(FACING, rotation.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING, TURNED, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_43719_())).m_61124_(TURNED, context.m_43725_().m_276867_(context.getClickedPos()))).m_61124_(WATERLOGGED, context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        return super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos blockPos, BlockState blockState, boolean b) {
        if (!b && !state.m_60713_(blockState.m_60734_())) {
            level.updateNeighborsAt(blockPos, state.m_60734_());
            level.updateNeighborsAt(blockPos.relative(((Direction) state.m_61143_(FACING)).getOpposite()), state.m_60734_());
            super.m_6810_(state, level, blockPos, blockState, b);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        switch((Direction) state.m_61143_(FACING)) {
            case DOWN:
                return SHAPE_DOWN;
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            case EAST:
                return SHAPE_EAST;
            default:
                return SHAPE_UP;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, ACBlockEntityRegistry.COPPER_VALVE.get(), CopperValveBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof CopperValveBlockEntity copperValve && !player.m_6144_()) {
            if ((Boolean) state.m_61143_(TURNED)) {
                copperValve.moveDown(false);
            } else {
                copperValve.moveDown(!copperValve.isMovingDown());
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CopperValveBlockEntity(pos, state);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return state.m_61143_(TURNED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
        return state.m_61143_(TURNED) && state.m_61143_(FACING) == direction ? 15 : 0;
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }
}
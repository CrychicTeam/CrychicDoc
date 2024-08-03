package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AmethystClusterBlock extends AmethystBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    protected final VoxelShape northAabb;

    protected final VoxelShape southAabb;

    protected final VoxelShape eastAabb;

    protected final VoxelShape westAabb;

    protected final VoxelShape upAabb;

    protected final VoxelShape downAabb;

    public AmethystClusterBlock(int int0, int int1, BlockBehaviour.Properties blockBehaviourProperties2) {
        super(blockBehaviourProperties2);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(FACING, Direction.UP));
        this.upAabb = Block.box((double) int1, 0.0, (double) int1, (double) (16 - int1), (double) int0, (double) (16 - int1));
        this.downAabb = Block.box((double) int1, (double) (16 - int0), (double) int1, (double) (16 - int1), 16.0, (double) (16 - int1));
        this.northAabb = Block.box((double) int1, (double) int1, (double) (16 - int0), (double) (16 - int1), (double) (16 - int1), 16.0);
        this.southAabb = Block.box((double) int1, (double) int1, 0.0, (double) (16 - int1), (double) (16 - int1), (double) int0);
        this.eastAabb = Block.box(0.0, (double) int1, (double) int1, (double) int0, (double) (16 - int1), (double) (16 - int1));
        this.westAabb = Block.box((double) (16 - int0), (double) int1, (double) int1, 16.0, (double) (16 - int1), (double) (16 - int1));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        Direction $$4 = (Direction) blockState0.m_61143_(FACING);
        switch($$4) {
            case NORTH:
                return this.northAabb;
            case SOUTH:
                return this.southAabb;
            case EAST:
                return this.eastAabb;
            case WEST:
                return this.westAabb;
            case DOWN:
                return this.downAabb;
            case UP:
            default:
                return this.upAabb;
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        Direction $$3 = (Direction) blockState0.m_61143_(FACING);
        BlockPos $$4 = blockPos2.relative($$3.getOpposite());
        return levelReader1.m_8055_($$4).m_60783_(levelReader1, $$4, $$3);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return direction1 == ((Direction) blockState0.m_61143_(FACING)).getOpposite() && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        LevelAccessor $$1 = blockPlaceContext0.m_43725_();
        BlockPos $$2 = blockPlaceContext0.getClickedPos();
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, $$1.m_6425_($$2).getType() == Fluids.WATER)).m_61124_(FACING, blockPlaceContext0.m_43719_());
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(WATERLOGGED, FACING);
    }
}
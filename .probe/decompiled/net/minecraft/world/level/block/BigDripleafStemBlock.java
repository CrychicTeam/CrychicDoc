package net.minecraft.world.level.block;

import java.util.Optional;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BigDripleafStemBlock extends HorizontalDirectionalBlock implements BonemealableBlock, SimpleWaterloggedBlock {

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final int STEM_WIDTH = 6;

    protected static final VoxelShape NORTH_SHAPE = Block.box(5.0, 0.0, 9.0, 11.0, 16.0, 15.0);

    protected static final VoxelShape SOUTH_SHAPE = Block.box(5.0, 0.0, 1.0, 11.0, 16.0, 7.0);

    protected static final VoxelShape EAST_SHAPE = Block.box(1.0, 0.0, 5.0, 7.0, 16.0, 11.0);

    protected static final VoxelShape WEST_SHAPE = Block.box(9.0, 0.0, 5.0, 15.0, 16.0, 11.0);

    protected BigDripleafStemBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(f_54117_, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        switch((Direction) blockState0.m_61143_(f_54117_)) {
            case SOUTH:
                return SOUTH_SHAPE;
            case NORTH:
            default:
                return NORTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
            case EAST:
                return EAST_SHAPE;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(WATERLOGGED, f_54117_);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockPos $$3 = blockPos2.below();
        BlockState $$4 = levelReader1.m_8055_($$3);
        BlockState $$5 = levelReader1.m_8055_(blockPos2.above());
        return ($$4.m_60713_(this) || $$4.m_204336_(BlockTags.BIG_DRIPLEAF_PLACEABLE)) && ($$5.m_60713_(this) || $$5.m_60713_(Blocks.BIG_DRIPLEAF));
    }

    protected static boolean place(LevelAccessor levelAccessor0, BlockPos blockPos1, FluidState fluidState2, Direction direction3) {
        BlockState $$4 = (BlockState) ((BlockState) Blocks.BIG_DRIPLEAF_STEM.defaultBlockState().m_61124_(WATERLOGGED, fluidState2.isSourceOfType(Fluids.WATER))).m_61124_(f_54117_, direction3);
        return levelAccessor0.m_7731_(blockPos1, $$4, 3);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((direction1 == Direction.DOWN || direction1 == Direction.UP) && !blockState0.m_60710_(levelAccessor3, blockPos4)) {
            levelAccessor3.scheduleTick(blockPos4, this, 1);
        }
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!blockState0.m_60710_(serverLevel1, blockPos2)) {
            serverLevel1.m_46961_(blockPos2, true);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        Optional<BlockPos> $$4 = BlockUtil.getTopConnectedBlock(levelReader0, blockPos1, blockState2.m_60734_(), Direction.UP, Blocks.BIG_DRIPLEAF);
        if (!$$4.isPresent()) {
            return false;
        } else {
            BlockPos $$5 = ((BlockPos) $$4.get()).above();
            BlockState $$6 = levelReader0.m_8055_($$5);
            return BigDripleafBlock.canPlaceAt(levelReader0, $$5, $$6);
        }
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        Optional<BlockPos> $$4 = BlockUtil.getTopConnectedBlock(serverLevel0, blockPos2, blockState3.m_60734_(), Direction.UP, Blocks.BIG_DRIPLEAF);
        if ($$4.isPresent()) {
            BlockPos $$5 = (BlockPos) $$4.get();
            BlockPos $$6 = $$5.above();
            Direction $$7 = (Direction) blockState3.m_61143_(f_54117_);
            place(serverLevel0, $$5, serverLevel0.m_6425_($$5), $$7);
            BigDripleafBlock.place(serverLevel0, $$6, serverLevel0.m_6425_($$6), $$7);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return new ItemStack(Blocks.BIG_DRIPLEAF);
    }
}
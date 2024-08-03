package vectorwing.farmersdelight.common.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class WildRiceBlock extends DoublePlantBlock implements SimpleWaterloggedBlock, BonemealableBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public WildRiceBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, true)).m_61124_(f_52858_, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_52858_, WATERLOGGED);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        FluidState fluid = level.m_6425_(pos);
        BlockPos floorPos = pos.below();
        return state.m_61143_(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER ? super.canSurvive(state, level, pos) && this.mayPlaceOn(level.m_8055_(floorPos), level, floorPos) && fluid.is(FluidTags.WATER) && fluid.getAmount() == 8 : super.canSurvive(state, level, pos) && level.m_8055_(pos.below()).m_60734_() == ModBlocks.WILD_RICE.get();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        return state.m_204336_(BlockTags.DIRT) || state.m_60713_(Blocks.SAND);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return false;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), (BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(f_52858_, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        BlockState currentState = super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
        DoubleBlockHalf half = (DoubleBlockHalf) stateIn.m_61143_(f_52858_);
        if (!currentState.m_60795_()) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(level));
        }
        if (facing.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (facing == Direction.UP) || facingState.m_60734_() == this && facingState.m_61143_(f_52858_) != half) {
            return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.m_60710_(level, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        FluidState fluid = context.m_43725_().getFluidState(context.getClickedPos());
        return pos.m_123342_() < context.m_43725_().m_151558_() - 1 && fluid.is(FluidTags.WATER) && fluid.getAmount() == 8 && context.m_43725_().getBlockState(pos.above()).m_60795_() ? super.getStateForPlacement(context) : null;
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluidIn) {
        return state.m_61143_(f_52858_) == DoubleBlockHalf.LOWER;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(f_52858_) == DoubleBlockHalf.LOWER ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
        return (double) rand.nextFloat() < 0.3F;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        m_49840_(level, pos, new ItemStack(this));
    }
}
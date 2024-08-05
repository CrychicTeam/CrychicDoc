package vectorwing.farmersdelight.common.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

public class RiceBlock extends BushBlock implements BonemealableBlock, LiquidBlockContainer {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public static final BooleanProperty SUPPORTING = BooleanProperty.create("supporting");

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0), Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0), Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0), Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0) };

    public RiceBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(AGE, 0)).m_61124_(SUPPORTING, false));
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.m_213897_(state, level, pos, random);
        if (level.isAreaLoaded(pos, 1)) {
            if (level.m_45524_(pos.above(), 0) >= 6) {
                int age = this.getAge(state);
                if (age <= this.getMaxAge()) {
                    float chance = 10.0F;
                    if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / chance) + 1) == 0)) {
                        if (age == this.getMaxAge()) {
                            RicePaniclesBlock riceUpper = (RicePaniclesBlock) ModBlocks.RICE_CROP_PANICLES.get();
                            if (riceUpper.m_49966_().m_60710_(level, pos.above()) && level.m_46859_(pos.above())) {
                                level.m_46597_(pos.above(), riceUpper.m_49966_());
                                ForgeHooks.onCropsGrowPost(level, pos, state);
                            }
                        } else {
                            level.m_7731_(pos, this.withAge(age + 1), 2);
                            ForgeHooks.onCropsGrowPost(level, pos, state);
                        }
                    }
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.m_61143_(this.getAgeProperty())];
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        FluidState fluid = level.m_6425_(pos);
        return super.canSurvive(state, level, pos) && fluid.is(FluidTags.WATER) && fluid.getAmount() == 8;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.m_204336_(BlockTags.DIRT);
    }

    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    protected int getAge(BlockState state) {
        return (Integer) state.m_61143_(this.getAgeProperty());
    }

    public int getMaxAge() {
        return 3;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.RICE.get());
    }

    public BlockState withAge(int age) {
        return (BlockState) this.m_49966_().m_61124_(this.getAgeProperty(), age);
    }

    public boolean isMaxAge(BlockState state) {
        return (Integer) state.m_61143_(this.getAgeProperty()) >= this.getMaxAge();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, SUPPORTING);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        BlockState state = super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
        if (!state.m_60795_()) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(level));
            if (facing == Direction.UP) {
                return (BlockState) state.m_61124_(SUPPORTING, this.isSupportingRiceUpper(facingState));
            }
        }
        return state;
    }

    public boolean isSupportingRiceUpper(BlockState topState) {
        return topState.m_60734_() == ModBlocks.RICE_CROP_PANICLES.get();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.m_43725_().getFluidState(context.getClickedPos());
        return fluid.is(FluidTags.WATER) && fluid.getAmount() == 8 ? super.m_5573_(context) : null;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        BlockState upperState = level.m_8055_(pos.above());
        return upperState.m_60734_() instanceof RicePaniclesBlock ? !((RicePaniclesBlock) upperState.m_60734_()).m_52307_(upperState) : true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
        return true;
    }

    protected int getBonemealAgeIncrease(Level level) {
        return Mth.nextInt(level.random, 1, 4);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
        int ageGrowth = Math.min(this.getAge(state) + this.getBonemealAgeIncrease(level), 7);
        if (ageGrowth <= this.getMaxAge()) {
            level.m_46597_(pos, (BlockState) state.m_61124_(AGE, ageGrowth));
        } else {
            BlockState top = level.m_8055_(pos.above());
            if (top.m_60734_() == ModBlocks.RICE_CROP_PANICLES.get()) {
                BonemealableBlock growable = (BonemealableBlock) level.m_8055_(pos.above()).m_60734_();
                if (growable.isValidBonemealTarget(level, pos.above(), top, false)) {
                    growable.performBonemeal(level, level.f_46441_, pos.above(), top);
                }
            } else {
                RicePaniclesBlock riceUpper = (RicePaniclesBlock) ModBlocks.RICE_CROP_PANICLES.get();
                int remainingGrowth = ageGrowth - this.getMaxAge() - 1;
                if (riceUpper.m_49966_().m_60710_(level, pos.above()) && level.m_46859_(pos.above())) {
                    level.m_46597_(pos, (BlockState) state.m_61124_(AGE, this.getMaxAge()));
                    level.m_7731_(pos.above(), (BlockState) riceUpper.m_49966_().m_61124_(RicePaniclesBlock.RICE_AGE, remainingGrowth), 2);
                }
            }
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluidIn) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return false;
    }
}
package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class BuddingTomatoBlock extends BuddingBushBlock implements BonemealableBlock {

    public BuddingTomatoBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        return ModBlocks.BUDDING_TOMATO_CROP.get().defaultBlockState();
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.m_60713_(ModBlocks.RICH_SOIL_FARMLAND.get()) || pState.m_60713_(Blocks.FARMLAND);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if ((Integer) state.m_61143_(BuddingBushBlock.AGE) == 4) {
            level.m_7731_(currentPos, ModBlocks.TOMATO_CROP.get().defaultBlockState(), 3);
        }
        return super.m_7417_(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean canGrowPastMaxAge() {
        return true;
    }

    @Override
    public void growPastMaxAge(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.m_46597_(pos, ModBlocks.TOMATO_CROP.get().defaultBlockState());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    protected int getBonemealAgeIncrease(Level level) {
        return Mth.nextInt(level.random, 1, 4);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int maxAge = this.getMaxAge();
        int ageGrowth = Math.min(this.getAge(state) + this.getBonemealAgeIncrease(level), 7);
        if (ageGrowth <= maxAge) {
            level.m_46597_(pos, (BlockState) state.m_61124_(AGE, ageGrowth));
        } else {
            int remainingGrowth = ageGrowth - maxAge - 1;
            level.m_46597_(pos, (BlockState) ModBlocks.TOMATO_CROP.get().defaultBlockState().m_61124_(TomatoVineBlock.VINE_AGE, remainingGrowth));
        }
    }
}
package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.FarmlandWaterManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;

public class RichSoilFarmlandBlock extends FarmBlock {

    public RichSoilFarmlandBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    private static boolean hasWater(LevelReader level, BlockPos pos) {
        for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
            if (level.m_6425_(nearbyPos).is(FluidTags.WATER)) {
                return true;
            }
        }
        return FarmlandWaterManager.hasBlockWaterTicket(level, pos);
    }

    public static void turnToRichSoil(BlockState state, Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, m_49897_(state, ModBlocks.RICH_SOIL.get().defaultBlockState(), level, pos));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState aboveState = level.m_8055_(pos.above());
        return super.canSurvive(state, level, pos) || aboveState.m_60734_() instanceof StemGrownBlock;
    }

    public boolean isFertile(BlockState state, BlockGetter world, BlockPos pos) {
        return state.m_60713_(ModBlocks.RICH_SOIL_FARMLAND.get()) ? (Integer) state.m_61143_(f_53243_) > 0 : false;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (!state.m_60710_(level, pos)) {
            turnToRichSoil(state, level, pos);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int moisture = (Integer) state.m_61143_(f_53243_);
        if (!hasWater(level, pos) && !level.m_46758_(pos.above())) {
            if (moisture > 0) {
                level.m_7731_(pos, (BlockState) state.m_61124_(f_53243_, moisture - 1), 2);
            }
        } else if (moisture < 7) {
            level.m_7731_(pos, (BlockState) state.m_61124_(f_53243_, 7), 2);
        } else if (moisture == 7) {
            if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0) {
                return;
            }
            BlockState aboveState = level.m_8055_(pos.above());
            Block aboveBlock = aboveState.m_60734_();
            if (aboveState.m_204336_(ModTags.UNAFFECTED_BY_RICH_SOIL) || aboveBlock instanceof TallFlowerBlock) {
                return;
            }
            if (aboveBlock instanceof BonemealableBlock growable && (double) MathUtils.RAND.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get() && growable.isValidBonemealTarget(level, pos.above(), aboveState, false) && ForgeHooks.onCropsGrowPre(level, pos.above(), aboveState, true)) {
                growable.performBonemeal(level, level.f_46441_, pos.above(), aboveState);
                if (!level.f_46443_) {
                    level.m_46796_(2005, pos.above(), 0);
                }
                ForgeHooks.onCropsGrowPost(level, pos.above(), aboveState);
            }
        }
    }

    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
        return plantType == PlantType.CROP || plantType == PlantType.PLAINS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return !this.m_49966_().m_60710_(context.m_43725_(), context.getClickedPos()) ? ModBlocks.RICH_SOIL.get().defaultBlockState() : super.getStateForPlacement(context);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
    }
}
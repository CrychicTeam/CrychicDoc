package vectorwing.farmersdelight.common.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;

public class RichSoilBlock extends Block {

    public RichSoilBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (!level.f_46443_) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.m_8055_(abovePos);
            Block aboveBlock = aboveState.m_60734_();
            if (aboveState.m_204336_(ModTags.UNAFFECTED_BY_RICH_SOIL) || aboveBlock instanceof TallFlowerBlock) {
                return;
            }
            if (aboveBlock == Blocks.BROWN_MUSHROOM) {
                level.m_46597_(pos.above(), ModBlocks.BROWN_MUSHROOM_COLONY.get().defaultBlockState());
                return;
            }
            if (aboveBlock == Blocks.RED_MUSHROOM) {
                level.m_46597_(pos.above(), ModBlocks.RED_MUSHROOM_COLONY.get().defaultBlockState());
                return;
            }
            if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0) {
                return;
            }
            if (aboveBlock instanceof BonemealableBlock growable && (double) MathUtils.RAND.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get() && growable.isValidBonemealTarget(level, pos.above(), aboveState, false) && ForgeHooks.onCropsGrowPre(level, pos.above(), aboveState, true)) {
                growable.performBonemeal(level, level.f_46441_, pos.above(), aboveState);
                level.m_46796_(2005, pos.above(), 0);
                ForgeHooks.onCropsGrowPost(level, pos.above(), aboveState);
            }
        }
    }

    @Nullable
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        return toolAction.equals(ToolActions.HOE_TILL) && context.getLevel().getBlockState(context.getClickedPos().above()).m_60795_() ? ModBlocks.RICH_SOIL_FARMLAND.get().defaultBlockState() : null;
    }

    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
        return plantType != PlantType.CROP && plantType != PlantType.NETHER && plantType != PlantType.WATER;
    }
}
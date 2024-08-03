package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class UnknownCropsBlock extends AbstractCrockPotCropBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0) };

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 1;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.m_61143_(this.getAgeProperty())];
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isAreaLoaded(pos, 1)) {
            List<Block> unknownCropsBlocks = ForgeRegistries.BLOCKS.tags().getTag(CrockPotBlockTags.UNKNOWN_CROPS).stream().toList();
            if (!unknownCropsBlocks.isEmpty()) {
                if (level.m_45524_(pos, 0) >= 9) {
                    float growthChance = m_52272_(this, level, pos);
                    if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
                        level.m_7731_(pos, ((Block) unknownCropsBlocks.get(level.f_46441_.nextInt(unknownCropsBlocks.size()))).defaultBlockState(), 2);
                        ForgeHooks.onCropsGrowPost(level, pos, state);
                    }
                }
            }
        }
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        List<Block> unknownCropsBlocks = ForgeRegistries.BLOCKS.tags().getTag(CrockPotBlockTags.UNKNOWN_CROPS).stream().toList();
        if (!unknownCropsBlocks.isEmpty()) {
            Block block = (Block) unknownCropsBlocks.get(level.random.nextInt(unknownCropsBlocks.size()));
            int age = this.m_7125_(level) - 1;
            if (block instanceof AbstractCrockPotDoubleCropBlock cropBlock) {
                int maxAge = cropBlock.getMaxGrowthAge(cropBlock.m_49966_());
                if (age > maxAge) {
                    level.setBlock(pos, cropBlock.m_52289_(maxAge), 2);
                    if (level.m_46859_(pos.above())) {
                        level.setBlock(pos.above(), cropBlock.m_52289_(age), 2);
                    }
                } else {
                    level.setBlock(pos, cropBlock.m_52289_(age), 2);
                }
            } else if (block instanceof CropBlock cropBlockx) {
                level.setBlock(pos, cropBlockx.getStateForAge(Math.min(age, cropBlockx.getMaxAge())), 2);
            } else {
                level.setBlock(pos, block.defaultBlockState(), 2);
            }
        }
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return CrockPotItems.UNKNOWN_SEEDS.get();
    }
}
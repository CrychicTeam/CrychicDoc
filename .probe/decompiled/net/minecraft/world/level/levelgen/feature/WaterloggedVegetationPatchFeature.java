package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;

public class WaterloggedVegetationPatchFeature extends VegetationPatchFeature {

    public WaterloggedVegetationPatchFeature(Codec<VegetationPatchConfiguration> codecVegetationPatchConfiguration0) {
        super(codecVegetationPatchConfiguration0);
    }

    @Override
    protected Set<BlockPos> placeGroundPatch(WorldGenLevel worldGenLevel0, VegetationPatchConfiguration vegetationPatchConfiguration1, RandomSource randomSource2, BlockPos blockPos3, Predicate<BlockState> predicateBlockState4, int int5, int int6) {
        Set<BlockPos> $$7 = super.placeGroundPatch(worldGenLevel0, vegetationPatchConfiguration1, randomSource2, blockPos3, predicateBlockState4, int5, int6);
        Set<BlockPos> $$8 = new HashSet();
        BlockPos.MutableBlockPos $$9 = new BlockPos.MutableBlockPos();
        for (BlockPos $$10 : $$7) {
            if (!isExposed(worldGenLevel0, $$7, $$10, $$9)) {
                $$8.add($$10);
            }
        }
        for (BlockPos $$11 : $$8) {
            worldGenLevel0.m_7731_($$11, Blocks.WATER.defaultBlockState(), 2);
        }
        return $$8;
    }

    private static boolean isExposed(WorldGenLevel worldGenLevel0, Set<BlockPos> setBlockPos1, BlockPos blockPos2, BlockPos.MutableBlockPos blockPosMutableBlockPos3) {
        return isExposedDirection(worldGenLevel0, blockPos2, blockPosMutableBlockPos3, Direction.NORTH) || isExposedDirection(worldGenLevel0, blockPos2, blockPosMutableBlockPos3, Direction.EAST) || isExposedDirection(worldGenLevel0, blockPos2, blockPosMutableBlockPos3, Direction.SOUTH) || isExposedDirection(worldGenLevel0, blockPos2, blockPosMutableBlockPos3, Direction.WEST) || isExposedDirection(worldGenLevel0, blockPos2, blockPosMutableBlockPos3, Direction.DOWN);
    }

    private static boolean isExposedDirection(WorldGenLevel worldGenLevel0, BlockPos blockPos1, BlockPos.MutableBlockPos blockPosMutableBlockPos2, Direction direction3) {
        blockPosMutableBlockPos2.setWithOffset(blockPos1, direction3);
        return !worldGenLevel0.m_8055_(blockPosMutableBlockPos2).m_60783_(worldGenLevel0, blockPosMutableBlockPos2, direction3.getOpposite());
    }

    @Override
    protected boolean placeVegetation(WorldGenLevel worldGenLevel0, VegetationPatchConfiguration vegetationPatchConfiguration1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BlockPos blockPos4) {
        if (super.placeVegetation(worldGenLevel0, vegetationPatchConfiguration1, chunkGenerator2, randomSource3, blockPos4.below())) {
            BlockState $$5 = worldGenLevel0.m_8055_(blockPos4);
            if ($$5.m_61138_(BlockStateProperties.WATERLOGGED) && !(Boolean) $$5.m_61143_(BlockStateProperties.WATERLOGGED)) {
                worldGenLevel0.m_7731_(blockPos4, (BlockState) $$5.m_61124_(BlockStateProperties.WATERLOGGED, true), 2);
            }
            return true;
        } else {
            return false;
        }
    }
}
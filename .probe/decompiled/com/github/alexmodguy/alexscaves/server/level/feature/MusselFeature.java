package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.MusselBlock;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MusselFeature extends Feature<NoneFeatureConfiguration> {

    public MusselFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel level = context.level();
        RandomSource randomSource = context.random();
        if (!this.tryPlaceMussel(level, pos, randomSource)) {
            return false;
        } else {
            for (int i = 0; i < randomSource.nextInt(3); i++) {
                this.tryPlaceMussel(level, pos.offset(randomSource.nextInt(4) - 2, randomSource.nextInt(4) - 2, randomSource.nextInt(4) - 2), randomSource);
            }
            return true;
        }
    }

    private boolean tryPlaceMussel(WorldGenLevel level, BlockPos pos, RandomSource randomSource) {
        if (level.m_8055_(pos).m_60713_(Blocks.WATER)) {
            List<Direction> possiblities = new ArrayList();
            for (Direction possible : Direction.values()) {
                BlockPos check = pos.relative(possible);
                if (this.isSameChunk(pos, check) && level.m_8055_(check).m_60783_(level, check, possible.getOpposite())) {
                    possiblities.add(possible.getOpposite());
                }
            }
            Direction direction = selectDirection(possiblities, randomSource);
            if (direction != null) {
                level.m_7731_(pos, (BlockState) ((BlockState) ((BlockState) ACBlockRegistry.MUSSEL.get().defaultBlockState().m_61124_(MusselBlock.FACING, direction)).m_61124_(MusselBlock.WATERLOGGED, true)).m_61124_(MusselBlock.MUSSELS, 1 + randomSource.nextInt(4)), 3);
                return true;
            }
        }
        return false;
    }

    private boolean isSameChunk(BlockPos pos, BlockPos check) {
        return SectionPos.blockToSectionCoord(pos.m_123341_()) == SectionPos.blockToSectionCoord(check.m_123341_()) && SectionPos.blockToSectionCoord(pos.m_123343_()) == SectionPos.blockToSectionCoord(check.m_123343_());
    }

    private static Direction selectDirection(List<Direction> directionList, RandomSource randomSource) {
        if (directionList.size() <= 0) {
            return null;
        } else {
            return directionList.size() <= 1 ? (Direction) directionList.get(0) : (Direction) directionList.get(randomSource.nextInt(directionList.size() - 1));
        }
    }
}
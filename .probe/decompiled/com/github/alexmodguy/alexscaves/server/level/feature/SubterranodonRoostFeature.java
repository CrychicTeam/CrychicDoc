package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.DinosaurEggBlock;
import com.github.alexmodguy.alexscaves.server.block.MultipleDinosaurEggsBlock;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.SubterranodonEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
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
import net.minecraft.world.phys.Vec3;

public class SubterranodonRoostFeature extends Feature<NoneFeatureConfiguration> {

    public SubterranodonRoostFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel level = context.level();
        RandomSource randomSource = context.random();
        Direction direction = this.getCliffDirection(level, pos, randomSource);
        if (direction == null) {
            return false;
        } else {
            int centerLength = 2 + randomSource.nextInt(1);
            int maxLeft = 2 + randomSource.nextInt(5);
            int maxRight = 2 + randomSource.nextInt(5);
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos mutableCopy = new BlockPos.MutableBlockPos();
            mutableBlockPos.set(pos);
            mutableBlockPos.move(direction.getCounterClockWise(), maxRight);
            float radius = (float) (maxLeft + maxRight) / 2.0F;
            for (int i = -maxRight; i < maxLeft; i++) {
                float dist = (float) Math.abs(i) / radius;
                mutableBlockPos.move(direction.getClockWise());
                mutableCopy.set(mutableBlockPos);
                int length = (int) Math.round((double) centerLength * Math.sqrt((double) (1.0F - dist)));
                for (int j = 0; j < length + 1; j++) {
                    this.fillCliff(level, mutableBlockPos, direction, (float) length, j == 0, randomSource);
                    mutableBlockPos.set(mutableCopy);
                    mutableBlockPos.move(0, -j, 0);
                    mutableBlockPos.move(direction.getOpposite(), j);
                }
                mutableBlockPos.set(mutableCopy);
            }
            return true;
        }
    }

    private void fillCliff(WorldGenLevel level, BlockPos.MutableBlockPos cliff, Direction direction, float length, boolean decorate, RandomSource randomSource) {
        int distBack = 0;
        int maxBack = (int) (Math.ceil((double) length) + 1.0);
        while (level.m_8055_(cliff).m_60795_() && distBack < maxBack) {
            distBack++;
            cliff.move(direction.getOpposite());
        }
        BlockState behind = level.m_8055_(cliff);
        BlockState set = behind.m_60713_(Blocks.SANDSTONE) ? Blocks.SANDSTONE.defaultBlockState() : ACBlockRegistry.LIMESTONE.get().defaultBlockState();
        cliff.move(direction);
        for (int i = 0; (double) i < Math.ceil((double) length); i++) {
            if (level.m_8055_(cliff).m_60795_()) {
                if (decorate && randomSource.nextInt(3) == 0 && i == 0) {
                    level.m_7731_(cliff, set, 3);
                    level.m_7731_(cliff.m_7494_(), ACBlockRegistry.FERN_THATCH.get().defaultBlockState(), 3);
                } else if (decorate && randomSource.nextInt(7) == 0) {
                    BlockPos immutable = cliff.immutable();
                    level.m_7731_(immutable.below(), set, 3);
                    for (Direction direction1 : ACMath.HORIZONTAL_DIRECTIONS) {
                        BlockPos corner = immutable.relative(direction1);
                        level.m_7731_(corner, ACBlockRegistry.FERN_THATCH.get().defaultBlockState(), 3);
                        level.m_7731_(corner.below(), set, 3);
                    }
                    level.m_7731_(immutable, ACBlockRegistry.FERN_THATCH.get().defaultBlockState(), 3);
                    level.m_7731_(immutable.above(), (BlockState) ((BlockState) ACBlockRegistry.SUBTERRANODON_EGG.get().defaultBlockState().m_61124_(MultipleDinosaurEggsBlock.EGGS, 1 + randomSource.nextInt(3))).m_61124_(DinosaurEggBlock.NEEDS_PLAYER, true), 3);
                    Vec3 spawnMobAt = Vec3.atCenterOf(immutable.relative(direction).above());
                    SubterranodonEntity subterranodon = ACEntityRegistry.SUBTERRANODON.get().create(level.m_6018_());
                    subterranodon.m_146884_(spawnMobAt);
                    subterranodon.m_21446_(immutable.above(), 20 + randomSource.nextInt(20));
                    if (!level.m_186437_(subterranodon, subterranodon.m_20191_())) {
                        level.m_7967_(subterranodon);
                    }
                } else {
                    level.m_7731_(cliff, set, 3);
                    if (decorate && randomSource.nextInt(5) == 0) {
                        level.m_7731_(cliff.m_7494_(), Blocks.MOSS_CARPET.defaultBlockState(), 3);
                    }
                }
            }
            cliff.move(direction);
        }
    }

    @Nullable
    private Direction getCliffDirection(WorldGenLevel level, BlockPos pos, RandomSource randomSource) {
        if (level.m_8055_(pos.below()).m_60795_() && level.m_8055_(pos.above()).m_60795_()) {
            List<Direction> possiblities = new ArrayList();
            for (Direction possible : ACMath.HORIZONTAL_DIRECTIONS) {
                BlockPos check = pos.relative(possible);
                if (this.isSameChunk(pos, check)) {
                    BlockState cliffState = level.m_8055_(check);
                    if (cliffState.m_60783_(level, check, possible.getOpposite()) && !cliffState.m_204336_(ACTagRegistry.VOLCANO_BLOCKS)) {
                        possiblities.add(possible.getOpposite());
                    }
                }
            }
            return selectDirection(possiblities, randomSource);
        } else {
            return null;
        }
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
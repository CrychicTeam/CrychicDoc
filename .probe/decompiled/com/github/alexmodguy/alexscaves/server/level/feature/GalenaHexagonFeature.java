package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.level.feature.config.GalenaHexagonFeatureConfiguration;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class GalenaHexagonFeature extends Feature<GalenaHexagonFeatureConfiguration> {

    public GalenaHexagonFeature(Codec<GalenaHexagonFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<GalenaHexagonFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel level = context.level();
        RandomSource randomSource = context.random();
        List<BlockPos> genPos = new ArrayList();
        BlockPos chunkCenter = new BlockPos(context.origin().m_123341_(), level.m_141937_() + 3, context.origin().m_123343_());
        int surface = level.m_6924_(Heightmap.Types.OCEAN_FLOOR_WG, chunkCenter.m_123341_(), chunkCenter.m_123343_());
        while (chunkCenter.m_123342_() < surface) {
            BlockPos next = chunkCenter.above();
            BlockState currentState = level.m_8055_(chunkCenter);
            BlockState nextState = level.m_8055_(next);
            if (context.config().ceiling) {
                if (nextState.m_60713_(ACBlockRegistry.GALENA.get()) && canReplace(currentState)) {
                    genPos.add(chunkCenter);
                }
            } else if (currentState.m_60713_(ACBlockRegistry.GALENA.get()) && canReplace(nextState)) {
                genPos.add(chunkCenter);
            }
            chunkCenter = next;
        }
        for (BlockPos floor : genPos) {
            drawHexagon(level, floor, randomSource, context.config().hexBlock, 3 + randomSource.nextInt(6), 1 + randomSource.nextInt(3), !context.config().ceiling);
        }
        return true;
    }

    private static boolean canReplace(BlockState state) {
        return state.m_60795_() || state.m_247087_();
    }

    private static void drawHexagon(WorldGenLevel level, BlockPos center, RandomSource random, BlockStateProvider blockState, int height, int radius, boolean goingUp) {
        int startY = -4 - random.nextInt(4);
        int endY = height + 1;
        for (int y = startY; y < endY; y++) {
            int setY = y * (goingUp ? 1 : -1);
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos fill = center.offset(x, setY, z);
                    if (fill.m_203202_((double) center.m_123341_(), (double) fill.m_123342_(), (double) center.m_123343_()) <= (double) (radius * radius) && canReplace(level.m_8055_(fill))) {
                        level.m_7731_(fill, blockState.getState(random, fill), 3);
                    }
                }
            }
        }
    }
}
package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class VoidStartPlatformFeature extends Feature<NoneFeatureConfiguration> {

    private static final BlockPos PLATFORM_OFFSET = new BlockPos(8, 3, 8);

    private static final ChunkPos PLATFORM_ORIGIN_CHUNK = new ChunkPos(PLATFORM_OFFSET);

    private static final int PLATFORM_RADIUS = 16;

    private static final int PLATFORM_RADIUS_CHUNKS = 1;

    public VoidStartPlatformFeature(Codec<NoneFeatureConfiguration> codecNoneFeatureConfiguration0) {
        super(codecNoneFeatureConfiguration0);
    }

    private static int checkerboardDistance(int int0, int int1, int int2, int int3) {
        return Math.max(Math.abs(int0 - int2), Math.abs(int1 - int3));
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContextNoneFeatureConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextNoneFeatureConfiguration0.level();
        ChunkPos $$2 = new ChunkPos(featurePlaceContextNoneFeatureConfiguration0.origin());
        if (checkerboardDistance($$2.x, $$2.z, PLATFORM_ORIGIN_CHUNK.x, PLATFORM_ORIGIN_CHUNK.z) > 1) {
            return true;
        } else {
            BlockPos $$3 = PLATFORM_OFFSET.atY(featurePlaceContextNoneFeatureConfiguration0.origin().m_123342_() + PLATFORM_OFFSET.m_123342_());
            BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
            for (int $$5 = $$2.getMinBlockZ(); $$5 <= $$2.getMaxBlockZ(); $$5++) {
                for (int $$6 = $$2.getMinBlockX(); $$6 <= $$2.getMaxBlockX(); $$6++) {
                    if (checkerboardDistance($$3.m_123341_(), $$3.m_123343_(), $$6, $$5) <= 16) {
                        $$4.set($$6, $$3.m_123342_(), $$5);
                        if ($$4.equals($$3)) {
                            $$1.m_7731_($$4, Blocks.COBBLESTONE.defaultBlockState(), 2);
                        } else {
                            $$1.m_7731_($$4, Blocks.STONE.defaultBlockState(), 2);
                        }
                    }
                }
            }
            return true;
        }
    }
}
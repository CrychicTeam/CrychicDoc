package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.level.feature.config.FillBiomeAboveConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class FillBiomeAboveFeature extends Feature<FillBiomeAboveConfiguration> {

    public FillBiomeAboveFeature(Codec<FillBiomeAboveConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<FillBiomeAboveConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int startY = level.m_5736_() + context.config().yAboveSeaLevel;
        pos.set(context.origin().m_123341_(), startY, context.origin().m_123343_());
        ChunkAccess chunkAccess = level.m_46865_(pos);
        if (chunkAccess != null) {
            int lastSectionIndex = -1;
            while (pos.m_123342_() < level.m_151558_()) {
                pos.move(0, 8, 0);
                if (pos.m_123342_() >> 4 != lastSectionIndex) {
                    lastSectionIndex = pos.m_123342_() >> 4;
                    int sectionIndex = chunkAccess.m_151564_(pos.m_123342_());
                    if (sectionIndex >= 0 && sectionIndex < chunkAccess.getSections().length) {
                        LevelChunkSection section = chunkAccess.getSection(sectionIndex);
                        PalettedContainer<Holder<Biome>> container = section.getBiomes().recreate();
                        for (int biomeX = 0; biomeX < 4; biomeX++) {
                            for (int biomeY = 0; biomeY < 4; biomeY++) {
                                for (int biomeZ = 0; biomeZ < 4; biomeZ++) {
                                    container.getAndSetUnchecked(biomeX, biomeY, biomeZ, context.config().newBiome);
                                }
                            }
                        }
                        section.biomes = container;
                    }
                }
            }
        }
        return true;
    }
}
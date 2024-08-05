package com.github.alexmodguy.alexscaves.server.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

class FillInBubblesWithWaterFeature extends Feature<NoneFeatureConfiguration> {

    public FillInBubblesWithWaterFeature(Codec<NoneFeatureConfiguration> codecNoneFeatureConfiguration0) {
        super(codecNoneFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                int k = context.origin().m_123341_() + i;
                int l = context.origin().m_123343_() + j;
                int i1 = worldgenlevel.m_5736_() - 2;
                pos.set(k, i1, l);
            }
        }
        return true;
    }
}
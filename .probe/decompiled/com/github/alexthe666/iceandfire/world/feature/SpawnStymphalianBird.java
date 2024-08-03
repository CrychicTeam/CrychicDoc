package com.github.alexthe666.iceandfire.world.feature;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SpawnStymphalianBird extends Feature<NoneFeatureConfiguration> {

    public SpawnStymphalianBird(Codec<NoneFeatureConfiguration> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();
        position = worldIn.m_5452_(Heightmap.Types.WORLD_SURFACE_WG, position.offset(8, 0, 8));
        if (IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position) && rand.nextInt(IafConfig.stymphalianBirdSpawnChance + 1) == 0) {
            for (int i = 0; i < 4 + rand.nextInt(4); i++) {
                BlockPos pos = position.offset(rand.nextInt(10) - 5, 0, rand.nextInt(10) - 5);
                pos = worldIn.m_5452_(Heightmap.Types.WORLD_SURFACE_WG, pos);
                if (worldIn.m_8055_(pos.below()).m_60815_()) {
                    EntityStymphalianBird bird = IafEntityRegistry.STYMPHALIAN_BIRD.get().create(worldIn.m_6018_());
                    bird.m_7678_((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 1.5F), (double) ((float) pos.m_123343_() + 0.5F), 0.0F, 0.0F);
                    worldIn.m_7967_(bird);
                }
            }
        }
        return true;
    }
}
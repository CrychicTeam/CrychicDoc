package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.level.feature.config.UndergroundRuinsFeatureConfiguration;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public class DeepOnesRuinsFeature extends UndergroundRuinsFeature {

    public DeepOnesRuinsFeature(Codec<UndergroundRuinsFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean canGenerateAt(WorldGenLevel level, BlockPos blockpos) {
        return level.m_8055_(blockpos).m_60713_(ACBlockRegistry.MUCK.get());
    }

    @Override
    public void processMarker(String marker, WorldGenLevel level, BlockPos pos, RandomSource randomsource) {
        if (marker.equals("submarine")) {
            level.m_7731_(pos, Blocks.CAVE_AIR.defaultBlockState(), 3);
        }
        if (marker.equals("submarine_damaged")) {
            level.m_7731_(pos, Blocks.WATER.defaultBlockState(), 3);
        }
    }

    @Override
    public void processBoundingBox(WorldGenLevel level, BoundingBox boundingBox, RandomSource randomsource) {
        BoundingBox box = new BoundingBox(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.minY() + 1, boundingBox.maxZ());
        Set<BlockPos> supportsNeededBelow = Sets.newHashSet();
        BlockPos.betweenClosedStream(box).forEach(posx -> {
            if (!level.m_8055_(posx).m_60713_(ACBlockRegistry.MUCK.get()) && !level.m_8055_(posx).m_60713_(Blocks.WATER) && (level.m_8055_(posx.below()).m_60713_(Blocks.WATER) || !level.m_8055_(posx.below()).m_60838_(level, posx.below()))) {
                supportsNeededBelow.add(posx.immutable());
            }
        });
        BlockPos.MutableBlockPos grounded = new BlockPos.MutableBlockPos();
        for (BlockPos pos : supportsNeededBelow) {
            grounded.set(pos.m_123341_(), pos.m_123342_() - 1, pos.m_123343_());
            while ((level.m_8055_(grounded).m_60713_(Blocks.WATER) || !level.m_8055_(grounded).m_60838_(level, grounded)) && grounded.m_123342_() > level.m_141937_()) {
                level.m_7731_(grounded, ACBlockRegistry.ABYSSMARINE_BRICKS.get().defaultBlockState(), 3);
                grounded.move(0, -1, 0);
            }
        }
    }

    @Override
    public StructurePlaceSettings modifyPlacementSettings(StructurePlaceSettings structureplacesettings) {
        return structureplacesettings.setKeepLiquids(false);
    }
}
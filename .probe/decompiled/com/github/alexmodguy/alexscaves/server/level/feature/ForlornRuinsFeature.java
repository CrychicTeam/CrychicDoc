package com.github.alexmodguy.alexscaves.server.level.feature;

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

public class ForlornRuinsFeature extends UndergroundRuinsFeature {

    public ForlornRuinsFeature(Codec<UndergroundRuinsFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean canGenerateAt(WorldGenLevel level, BlockPos blockpos) {
        return level.m_8055_(blockpos).m_60713_(Blocks.PACKED_MUD);
    }

    @Override
    public void processBoundingBox(WorldGenLevel level, BoundingBox boundingBox, RandomSource randomsource) {
        BoundingBox box = new BoundingBox(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.minY() + 1, boundingBox.maxZ());
        Set<BlockPos> supportsNeededBelow = Sets.newHashSet();
        BlockPos.betweenClosedStream(box).forEach(posx -> {
            if (!level.m_8055_(posx).m_60795_() && (!level.m_6425_(posx.below()).isEmpty() || !level.m_8055_(posx.below()).m_60838_(level, posx.below()))) {
                supportsNeededBelow.add(posx.immutable());
            }
        });
        BlockPos.MutableBlockPos grounded = new BlockPos.MutableBlockPos();
        for (BlockPos pos : supportsNeededBelow) {
            grounded.set(pos.m_123341_(), pos.m_123342_() - 1, pos.m_123343_());
            while ((!level.m_8055_(grounded).m_60819_().isEmpty() || !level.m_8055_(grounded).m_60838_(level, grounded)) && grounded.m_123342_() > level.m_141937_()) {
                level.m_7731_(grounded, Blocks.PACKED_MUD.defaultBlockState(), 3);
                grounded.move(0, -1, 0);
            }
        }
    }

    @Override
    public StructurePlaceSettings modifyPlacementSettings(StructurePlaceSettings structureplacesettings) {
        return structureplacesettings.setKeepLiquids(false);
    }
}
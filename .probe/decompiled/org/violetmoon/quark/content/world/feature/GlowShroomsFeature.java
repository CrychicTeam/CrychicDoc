package org.violetmoon.quark.content.world.feature;

import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import org.violetmoon.quark.content.world.block.HugeGlowShroomBlock;
import org.violetmoon.quark.content.world.module.GlimmeringWealdModule;

public class GlowShroomsFeature extends Feature<NoneFeatureConfiguration> {

    public GlowShroomsFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static List<PlacementModifier> placed() {
        return Arrays.asList(CountPlacement.of(125), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> config) {
        WorldGenLevel worldgenlevel = config.level();
        BlockPos blockpos = config.origin();
        RandomSource rng = config.random();
        BlockPos.MutableBlockPos setPos = new BlockPos.MutableBlockPos(blockpos.m_123341_(), blockpos.m_123342_(), blockpos.m_123343_());
        for (int i = -6; i < 7; i++) {
            for (int j = -6; j < 7; j++) {
                for (int k = -2; k < 3; k++) {
                    setPos.set(blockpos.m_123341_() + i, blockpos.m_123342_() + k, blockpos.m_123343_() + j);
                    double dist = blockpos.m_123331_(setPos);
                    if (dist > 10.0) {
                        double chance = 1.0 - (dist - 10.0) / 10.0;
                        if (chance < 0.0 || rng.nextDouble() < chance) {
                            continue;
                        }
                    }
                    if (worldgenlevel.m_7433_(setPos, s -> s.m_60734_() == Blocks.DEEPSLATE) && worldgenlevel.m_7433_(setPos.m_7494_(), BlockBehaviour.BlockStateBase::m_60795_) && rng.nextDouble() < 0.08) {
                        boolean placeSmall = !HugeGlowShroomBlock.place(worldgenlevel, rng, setPos.m_7494_());
                        if (placeSmall) {
                            worldgenlevel.m_7731_(setPos.m_7494_(), GlimmeringWealdModule.glow_shroom.defaultBlockState(), 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}
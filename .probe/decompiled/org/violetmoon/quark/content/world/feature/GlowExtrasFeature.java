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
import net.minecraft.world.level.block.GlowLichenBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import org.violetmoon.quark.content.world.module.GlimmeringWealdModule;

public class GlowExtrasFeature extends Feature<NoneFeatureConfiguration> {

    public GlowExtrasFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static List<PlacementModifier> placed() {
        return Arrays.asList(CountPlacement.of(200), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> config) {
        WorldGenLevel worldgenlevel = config.level();
        BlockPos blockpos = config.origin();
        RandomSource rng = config.random();
        BlockPos.MutableBlockPos setPos = new BlockPos.MutableBlockPos(blockpos.m_123341_(), blockpos.m_123342_(), blockpos.m_123343_());
        int horiz = 2;
        int vert = 6;
        float chance = 0.5F;
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                for (int k = -6; k < 7; k++) {
                    setPos.set(blockpos.m_123341_() + i, blockpos.m_123342_() + k, blockpos.m_123343_() + j);
                    if (rng.nextFloat() < 0.5F && worldgenlevel.m_7433_(setPos, BlockBehaviour.BlockStateBase::m_60795_)) {
                        double res = rng.nextDouble();
                        if (res > 0.85) {
                            if (worldgenlevel.m_7433_(setPos.m_7495_(), s -> s.m_60734_() == Blocks.DEEPSLATE)) {
                                worldgenlevel.m_7731_(setPos, GlimmeringWealdModule.glow_lichen_growth.defaultBlockState(), 2);
                            }
                        } else if (res > 0.35) {
                            for (Direction dir : Direction.values()) {
                                if (worldgenlevel.m_7433_(setPos.m_121945_(dir), s -> s.m_60734_() == Blocks.DEEPSLATE)) {
                                    BlockState place = Blocks.GLOW_LICHEN.defaultBlockState();
                                    for (Direction dir2 : Direction.values()) {
                                        BooleanProperty prop = GlowLichenBlock.m_153933_(dir2);
                                        place = (BlockState) place.m_61124_(prop, dir == dir2);
                                    }
                                    worldgenlevel.m_7731_(setPos, place, 2);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;

public class GeodeConfiguration implements FeatureConfiguration {

    public static final Codec<Double> CHANCE_RANGE = Codec.doubleRange(0.0, 1.0);

    public static final Codec<GeodeConfiguration> CODEC = RecordCodecBuilder.create(p_160842_ -> p_160842_.group(GeodeBlockSettings.CODEC.fieldOf("blocks").forGetter(p_160868_ -> p_160868_.geodeBlockSettings), GeodeLayerSettings.CODEC.fieldOf("layers").forGetter(p_160866_ -> p_160866_.geodeLayerSettings), GeodeCrackSettings.CODEC.fieldOf("crack").forGetter(p_160864_ -> p_160864_.geodeCrackSettings), CHANCE_RANGE.fieldOf("use_potential_placements_chance").orElse(0.35).forGetter(p_160862_ -> p_160862_.usePotentialPlacementsChance), CHANCE_RANGE.fieldOf("use_alternate_layer0_chance").orElse(0.0).forGetter(p_160860_ -> p_160860_.useAlternateLayer0Chance), Codec.BOOL.fieldOf("placements_require_layer0_alternate").orElse(true).forGetter(p_160858_ -> p_160858_.placementsRequireLayer0Alternate), IntProvider.codec(1, 20).fieldOf("outer_wall_distance").orElse(UniformInt.of(4, 5)).forGetter(p_160856_ -> p_160856_.outerWallDistance), IntProvider.codec(1, 20).fieldOf("distribution_points").orElse(UniformInt.of(3, 4)).forGetter(p_160854_ -> p_160854_.distributionPoints), IntProvider.codec(0, 10).fieldOf("point_offset").orElse(UniformInt.of(1, 2)).forGetter(p_160852_ -> p_160852_.pointOffset), Codec.INT.fieldOf("min_gen_offset").orElse(-16).forGetter(p_160850_ -> p_160850_.minGenOffset), Codec.INT.fieldOf("max_gen_offset").orElse(16).forGetter(p_160848_ -> p_160848_.maxGenOffset), CHANCE_RANGE.fieldOf("noise_multiplier").orElse(0.05).forGetter(p_160846_ -> p_160846_.noiseMultiplier), Codec.INT.fieldOf("invalid_blocks_threshold").forGetter(p_160844_ -> p_160844_.invalidBlocksThreshold)).apply(p_160842_, GeodeConfiguration::new));

    public final GeodeBlockSettings geodeBlockSettings;

    public final GeodeLayerSettings geodeLayerSettings;

    public final GeodeCrackSettings geodeCrackSettings;

    public final double usePotentialPlacementsChance;

    public final double useAlternateLayer0Chance;

    public final boolean placementsRequireLayer0Alternate;

    public final IntProvider outerWallDistance;

    public final IntProvider distributionPoints;

    public final IntProvider pointOffset;

    public final int minGenOffset;

    public final int maxGenOffset;

    public final double noiseMultiplier;

    public final int invalidBlocksThreshold;

    public GeodeConfiguration(GeodeBlockSettings geodeBlockSettings0, GeodeLayerSettings geodeLayerSettings1, GeodeCrackSettings geodeCrackSettings2, double double3, double double4, boolean boolean5, IntProvider intProvider6, IntProvider intProvider7, IntProvider intProvider8, int int9, int int10, double double11, int int12) {
        this.geodeBlockSettings = geodeBlockSettings0;
        this.geodeLayerSettings = geodeLayerSettings1;
        this.geodeCrackSettings = geodeCrackSettings2;
        this.usePotentialPlacementsChance = double3;
        this.useAlternateLayer0Chance = double4;
        this.placementsRequireLayer0Alternate = boolean5;
        this.outerWallDistance = intProvider6;
        this.distributionPoints = intProvider7;
        this.pointOffset = intProvider8;
        this.minGenOffset = int9;
        this.maxGenOffset = int10;
        this.noiseMultiplier = double11;
        this.invalidBlocksThreshold = int12;
    }
}
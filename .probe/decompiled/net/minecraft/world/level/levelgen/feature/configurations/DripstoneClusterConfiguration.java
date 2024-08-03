package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;

public class DripstoneClusterConfiguration implements FeatureConfiguration {

    public static final Codec<DripstoneClusterConfiguration> CODEC = RecordCodecBuilder.create(p_160784_ -> p_160784_.group(Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").forGetter(p_160806_ -> p_160806_.floorToCeilingSearchRange), IntProvider.codec(1, 128).fieldOf("height").forGetter(p_160804_ -> p_160804_.height), IntProvider.codec(1, 128).fieldOf("radius").forGetter(p_160802_ -> p_160802_.radius), Codec.intRange(0, 64).fieldOf("max_stalagmite_stalactite_height_diff").forGetter(p_160800_ -> p_160800_.maxStalagmiteStalactiteHeightDiff), Codec.intRange(1, 64).fieldOf("height_deviation").forGetter(p_160798_ -> p_160798_.heightDeviation), IntProvider.codec(0, 128).fieldOf("dripstone_block_layer_thickness").forGetter(p_160796_ -> p_160796_.dripstoneBlockLayerThickness), FloatProvider.codec(0.0F, 2.0F).fieldOf("density").forGetter(p_160794_ -> p_160794_.density), FloatProvider.codec(0.0F, 2.0F).fieldOf("wetness").forGetter(p_160792_ -> p_160792_.wetness), Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_dripstone_column_at_max_distance_from_center").forGetter(p_160790_ -> p_160790_.chanceOfDripstoneColumnAtMaxDistanceFromCenter), Codec.intRange(1, 64).fieldOf("max_distance_from_edge_affecting_chance_of_dripstone_column").forGetter(p_160788_ -> p_160788_.maxDistanceFromEdgeAffectingChanceOfDripstoneColumn), Codec.intRange(1, 64).fieldOf("max_distance_from_center_affecting_height_bias").forGetter(p_160786_ -> p_160786_.maxDistanceFromCenterAffectingHeightBias)).apply(p_160784_, DripstoneClusterConfiguration::new));

    public final int floorToCeilingSearchRange;

    public final IntProvider height;

    public final IntProvider radius;

    public final int maxStalagmiteStalactiteHeightDiff;

    public final int heightDeviation;

    public final IntProvider dripstoneBlockLayerThickness;

    public final FloatProvider density;

    public final FloatProvider wetness;

    public final float chanceOfDripstoneColumnAtMaxDistanceFromCenter;

    public final int maxDistanceFromEdgeAffectingChanceOfDripstoneColumn;

    public final int maxDistanceFromCenterAffectingHeightBias;

    public DripstoneClusterConfiguration(int int0, IntProvider intProvider1, IntProvider intProvider2, int int3, int int4, IntProvider intProvider5, FloatProvider floatProvider6, FloatProvider floatProvider7, float float8, int int9, int int10) {
        this.floorToCeilingSearchRange = int0;
        this.height = intProvider1;
        this.radius = intProvider2;
        this.maxStalagmiteStalactiteHeightDiff = int3;
        this.heightDeviation = int4;
        this.dripstoneBlockLayerThickness = intProvider5;
        this.density = floatProvider6;
        this.wetness = floatProvider7;
        this.chanceOfDripstoneColumnAtMaxDistanceFromCenter = float8;
        this.maxDistanceFromEdgeAffectingChanceOfDripstoneColumn = int9;
        this.maxDistanceFromCenterAffectingHeightBias = int10;
    }
}
package net.minecraft.world.level.levelgen.carver;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;

public class CanyonCarverConfiguration extends CarverConfiguration {

    public static final Codec<CanyonCarverConfiguration> CODEC = RecordCodecBuilder.create(p_158984_ -> p_158984_.group(CarverConfiguration.CODEC.forGetter(p_158990_ -> p_158990_), FloatProvider.CODEC.fieldOf("vertical_rotation").forGetter(p_158988_ -> p_158988_.verticalRotation), CanyonCarverConfiguration.CanyonShapeConfiguration.CODEC.fieldOf("shape").forGetter(p_158986_ -> p_158986_.shape)).apply(p_158984_, CanyonCarverConfiguration::new));

    public final FloatProvider verticalRotation;

    public final CanyonCarverConfiguration.CanyonShapeConfiguration shape;

    public CanyonCarverConfiguration(float float0, HeightProvider heightProvider1, FloatProvider floatProvider2, VerticalAnchor verticalAnchor3, CarverDebugSettings carverDebugSettings4, HolderSet<Block> holderSetBlock5, FloatProvider floatProvider6, CanyonCarverConfiguration.CanyonShapeConfiguration canyonCarverConfigurationCanyonShapeConfiguration7) {
        super(float0, heightProvider1, floatProvider2, verticalAnchor3, carverDebugSettings4, holderSetBlock5);
        this.verticalRotation = floatProvider6;
        this.shape = canyonCarverConfigurationCanyonShapeConfiguration7;
    }

    public CanyonCarverConfiguration(CarverConfiguration carverConfiguration0, FloatProvider floatProvider1, CanyonCarverConfiguration.CanyonShapeConfiguration canyonCarverConfigurationCanyonShapeConfiguration2) {
        this(carverConfiguration0.f_67859_, carverConfiguration0.y, carverConfiguration0.yScale, carverConfiguration0.lavaLevel, carverConfiguration0.debugSettings, carverConfiguration0.replaceable, floatProvider1, canyonCarverConfigurationCanyonShapeConfiguration2);
    }

    public static class CanyonShapeConfiguration {

        public static final Codec<CanyonCarverConfiguration.CanyonShapeConfiguration> CODEC = RecordCodecBuilder.create(p_159007_ -> p_159007_.group(FloatProvider.CODEC.fieldOf("distance_factor").forGetter(p_159019_ -> p_159019_.distanceFactor), FloatProvider.CODEC.fieldOf("thickness").forGetter(p_159017_ -> p_159017_.thickness), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("width_smoothness").forGetter(p_159015_ -> p_159015_.widthSmoothness), FloatProvider.CODEC.fieldOf("horizontal_radius_factor").forGetter(p_159013_ -> p_159013_.horizontalRadiusFactor), Codec.FLOAT.fieldOf("vertical_radius_default_factor").forGetter(p_159011_ -> p_159011_.verticalRadiusDefaultFactor), Codec.FLOAT.fieldOf("vertical_radius_center_factor").forGetter(p_159009_ -> p_159009_.verticalRadiusCenterFactor)).apply(p_159007_, CanyonCarverConfiguration.CanyonShapeConfiguration::new));

        public final FloatProvider distanceFactor;

        public final FloatProvider thickness;

        public final int widthSmoothness;

        public final FloatProvider horizontalRadiusFactor;

        public final float verticalRadiusDefaultFactor;

        public final float verticalRadiusCenterFactor;

        public CanyonShapeConfiguration(FloatProvider floatProvider0, FloatProvider floatProvider1, int int2, FloatProvider floatProvider3, float float4, float float5) {
            this.widthSmoothness = int2;
            this.horizontalRadiusFactor = floatProvider3;
            this.verticalRadiusDefaultFactor = float4;
            this.verticalRadiusCenterFactor = float5;
            this.distanceFactor = floatProvider0;
            this.thickness = floatProvider1;
        }
    }
}
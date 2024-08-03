package net.minecraft.world.level.levelgen.carver;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;

public class CaveCarverConfiguration extends CarverConfiguration {

    public static final Codec<CaveCarverConfiguration> CODEC = RecordCodecBuilder.create(p_159184_ -> p_159184_.group(CarverConfiguration.CODEC.forGetter(p_159192_ -> p_159192_), FloatProvider.CODEC.fieldOf("horizontal_radius_multiplier").forGetter(p_159190_ -> p_159190_.horizontalRadiusMultiplier), FloatProvider.CODEC.fieldOf("vertical_radius_multiplier").forGetter(p_159188_ -> p_159188_.verticalRadiusMultiplier), FloatProvider.codec(-1.0F, 1.0F).fieldOf("floor_level").forGetter(p_159186_ -> p_159186_.floorLevel)).apply(p_159184_, CaveCarverConfiguration::new));

    public final FloatProvider horizontalRadiusMultiplier;

    public final FloatProvider verticalRadiusMultiplier;

    final FloatProvider floorLevel;

    public CaveCarverConfiguration(float float0, HeightProvider heightProvider1, FloatProvider floatProvider2, VerticalAnchor verticalAnchor3, CarverDebugSettings carverDebugSettings4, HolderSet<Block> holderSetBlock5, FloatProvider floatProvider6, FloatProvider floatProvider7, FloatProvider floatProvider8) {
        super(float0, heightProvider1, floatProvider2, verticalAnchor3, carverDebugSettings4, holderSetBlock5);
        this.horizontalRadiusMultiplier = floatProvider6;
        this.verticalRadiusMultiplier = floatProvider7;
        this.floorLevel = floatProvider8;
    }

    public CaveCarverConfiguration(float float0, HeightProvider heightProvider1, FloatProvider floatProvider2, VerticalAnchor verticalAnchor3, HolderSet<Block> holderSetBlock4, FloatProvider floatProvider5, FloatProvider floatProvider6, FloatProvider floatProvider7) {
        this(float0, heightProvider1, floatProvider2, verticalAnchor3, CarverDebugSettings.DEFAULT, holderSetBlock4, floatProvider5, floatProvider6, floatProvider7);
    }

    public CaveCarverConfiguration(CarverConfiguration carverConfiguration0, FloatProvider floatProvider1, FloatProvider floatProvider2, FloatProvider floatProvider3) {
        this(carverConfiguration0.f_67859_, carverConfiguration0.y, carverConfiguration0.yScale, carverConfiguration0.lavaLevel, carverConfiguration0.debugSettings, carverConfiguration0.replaceable, floatProvider1, floatProvider2, floatProvider3);
    }
}
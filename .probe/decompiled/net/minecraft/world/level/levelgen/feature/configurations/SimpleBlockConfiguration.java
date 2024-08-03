package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record SimpleBlockConfiguration(BlockStateProvider f_68069_) implements FeatureConfiguration {

    private final BlockStateProvider toPlace;

    public static final Codec<SimpleBlockConfiguration> CODEC = RecordCodecBuilder.create(p_191331_ -> p_191331_.group(BlockStateProvider.CODEC.fieldOf("to_place").forGetter(p_161168_ -> p_161168_.toPlace)).apply(p_191331_, SimpleBlockConfiguration::new));

    public SimpleBlockConfiguration(BlockStateProvider f_68069_) {
        this.toPlace = f_68069_;
    }
}
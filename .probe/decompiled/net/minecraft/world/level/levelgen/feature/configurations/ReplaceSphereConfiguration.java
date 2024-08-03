package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;

public class ReplaceSphereConfiguration implements FeatureConfiguration {

    public static final Codec<ReplaceSphereConfiguration> CODEC = RecordCodecBuilder.create(p_68048_ -> p_68048_.group(BlockState.CODEC.fieldOf("target").forGetter(p_161100_ -> p_161100_.targetState), BlockState.CODEC.fieldOf("state").forGetter(p_161098_ -> p_161098_.replaceState), IntProvider.codec(0, 12).fieldOf("radius").forGetter(p_161095_ -> p_161095_.radius)).apply(p_68048_, ReplaceSphereConfiguration::new));

    public final BlockState targetState;

    public final BlockState replaceState;

    private final IntProvider radius;

    public ReplaceSphereConfiguration(BlockState blockState0, BlockState blockState1, IntProvider intProvider2) {
        this.targetState = blockState0;
        this.replaceState = blockState1;
        this.radius = intProvider2;
    }

    public IntProvider radius() {
        return this.radius;
    }
}
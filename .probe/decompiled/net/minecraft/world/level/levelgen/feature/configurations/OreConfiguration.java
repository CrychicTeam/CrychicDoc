package net.minecraft.world.level.levelgen.feature.configurations;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class OreConfiguration implements FeatureConfiguration {

    public static final Codec<OreConfiguration> CODEC = RecordCodecBuilder.create(p_67849_ -> p_67849_.group(Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf("targets").forGetter(p_161027_ -> p_161027_.targetStates), Codec.intRange(0, 64).fieldOf("size").forGetter(p_161025_ -> p_161025_.size), Codec.floatRange(0.0F, 1.0F).fieldOf("discard_chance_on_air_exposure").forGetter(p_161020_ -> p_161020_.discardChanceOnAirExposure)).apply(p_67849_, OreConfiguration::new));

    public final List<OreConfiguration.TargetBlockState> targetStates;

    public final int size;

    public final float discardChanceOnAirExposure;

    public OreConfiguration(List<OreConfiguration.TargetBlockState> listOreConfigurationTargetBlockState0, int int1, float float2) {
        this.size = int1;
        this.targetStates = listOreConfigurationTargetBlockState0;
        this.discardChanceOnAirExposure = float2;
    }

    public OreConfiguration(List<OreConfiguration.TargetBlockState> listOreConfigurationTargetBlockState0, int int1) {
        this(listOreConfigurationTargetBlockState0, int1, 0.0F);
    }

    public OreConfiguration(RuleTest ruleTest0, BlockState blockState1, int int2, float float3) {
        this(ImmutableList.of(new OreConfiguration.TargetBlockState(ruleTest0, blockState1)), int2, float3);
    }

    public OreConfiguration(RuleTest ruleTest0, BlockState blockState1, int int2) {
        this(ImmutableList.of(new OreConfiguration.TargetBlockState(ruleTest0, blockState1)), int2, 0.0F);
    }

    public static OreConfiguration.TargetBlockState target(RuleTest ruleTest0, BlockState blockState1) {
        return new OreConfiguration.TargetBlockState(ruleTest0, blockState1);
    }

    public static class TargetBlockState {

        public static final Codec<OreConfiguration.TargetBlockState> CODEC = RecordCodecBuilder.create(p_161039_ -> p_161039_.group(RuleTest.CODEC.fieldOf("target").forGetter(p_161043_ -> p_161043_.target), BlockState.CODEC.fieldOf("state").forGetter(p_161041_ -> p_161041_.state)).apply(p_161039_, OreConfiguration.TargetBlockState::new));

        public final RuleTest target;

        public final BlockState state;

        TargetBlockState(RuleTest ruleTest0, BlockState blockState1) {
            this.target = ruleTest0;
            this.state = blockState1;
        }
    }
}
package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;

public class SpikeConfiguration implements FeatureConfiguration {

    public static final Codec<SpikeConfiguration> CODEC = RecordCodecBuilder.create(p_68115_ -> p_68115_.group(Codec.BOOL.fieldOf("crystal_invulnerable").orElse(false).forGetter(p_161195_ -> p_161195_.crystalInvulnerable), SpikeFeature.EndSpike.CODEC.listOf().fieldOf("spikes").forGetter(p_161193_ -> p_161193_.spikes), BlockPos.CODEC.optionalFieldOf("crystal_beam_target").forGetter(p_161191_ -> Optional.ofNullable(p_161191_.crystalBeamTarget))).apply(p_68115_, SpikeConfiguration::new));

    private final boolean crystalInvulnerable;

    private final List<SpikeFeature.EndSpike> spikes;

    @Nullable
    private final BlockPos crystalBeamTarget;

    public SpikeConfiguration(boolean boolean0, List<SpikeFeature.EndSpike> listSpikeFeatureEndSpike1, @Nullable BlockPos blockPos2) {
        this(boolean0, listSpikeFeatureEndSpike1, Optional.ofNullable(blockPos2));
    }

    private SpikeConfiguration(boolean boolean0, List<SpikeFeature.EndSpike> listSpikeFeatureEndSpike1, Optional<BlockPos> optionalBlockPos2) {
        this.crystalInvulnerable = boolean0;
        this.spikes = listSpikeFeatureEndSpike1;
        this.crystalBeamTarget = (BlockPos) optionalBlockPos2.orElse(null);
    }

    public boolean isCrystalInvulnerable() {
        return this.crystalInvulnerable;
    }

    public List<SpikeFeature.EndSpike> getSpikes() {
        return this.spikes;
    }

    @Nullable
    public BlockPos getCrystalBeamTarget() {
        return this.crystalBeamTarget;
    }
}
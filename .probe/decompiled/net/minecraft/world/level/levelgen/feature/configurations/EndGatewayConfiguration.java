package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;

public class EndGatewayConfiguration implements FeatureConfiguration {

    public static final Codec<EndGatewayConfiguration> CODEC = RecordCodecBuilder.create(p_67649_ -> p_67649_.group(BlockPos.CODEC.optionalFieldOf("exit").forGetter(p_160810_ -> p_160810_.exit), Codec.BOOL.fieldOf("exact").forGetter(p_160808_ -> p_160808_.exact)).apply(p_67649_, EndGatewayConfiguration::new));

    private final Optional<BlockPos> exit;

    private final boolean exact;

    private EndGatewayConfiguration(Optional<BlockPos> optionalBlockPos0, boolean boolean1) {
        this.exit = optionalBlockPos0;
        this.exact = boolean1;
    }

    public static EndGatewayConfiguration knownExit(BlockPos blockPos0, boolean boolean1) {
        return new EndGatewayConfiguration(Optional.of(blockPos0), boolean1);
    }

    public static EndGatewayConfiguration delayedExitSearch() {
        return new EndGatewayConfiguration(Optional.empty(), false);
    }

    public Optional<BlockPos> getExit() {
        return this.exit;
    }

    public boolean isExitExact() {
        return this.exact;
    }
}
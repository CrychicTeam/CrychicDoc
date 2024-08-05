package dev.worldgen.tectonic.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.worldgen.tectonic.config.ConfigHandler;
import net.minecraft.core.Holder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import org.jetbrains.annotations.NotNull;

public record ConfigDensityFunction(String option, Holder<DensityFunction> trueArgument, Holder<DensityFunction> falseArgument) implements DensityFunction {

    public static MapCodec<ConfigDensityFunction> DATA_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("option").forGetter(ConfigDensityFunction::option), DensityFunction.CODEC.fieldOf("true_argument").orElse(Holder.direct(DensityFunctions.constant(1.0))).forGetter(ConfigDensityFunction::trueArgument), DensityFunction.CODEC.fieldOf("false_argument").orElse(Holder.direct(DensityFunctions.zero())).forGetter(ConfigDensityFunction::falseArgument)).apply(instance, ConfigDensityFunction::new));

    public static KeyDispatchDataCodec<ConfigDensityFunction> CODEC_HOLDER = KeyDispatchDataCodec.of(DATA_CODEC);

    @Override
    public double compute(@NotNull DensityFunction.FunctionContext context) {
        if (this.option().equals("terrain_scale")) {
            return ConfigHandler.getConfig().getValue("terrain_scale");
        } else {
            boolean enabled = ConfigHandler.getConfig().getValue(this.option()) == 1.0;
            Holder<DensityFunction> toCompute = enabled ? this.trueArgument() : this.falseArgument();
            return toCompute.value().compute(context);
        }
    }

    @Override
    public void fillArray(@NotNull double[] doubles, DensityFunction.ContextProvider contextProvider) {
        contextProvider.fillAllDirectly(doubles, this);
    }

    @NotNull
    @Override
    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
        return visitor.apply(this);
    }

    @Override
    public double minValue() {
        return -1.0E7;
    }

    @Override
    public double maxValue() {
        return 1.0E7;
    }

    @NotNull
    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC_HOLDER;
    }
}
package dev.worldgen.tectonic.worldgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.worldgen.tectonic.Tectonic;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

public record DynamicReferenceDensityFunction(HolderSet<DensityFunction> arguments, Holder<DensityFunction> fallback) implements DensityFunction {

    public static MapCodec<DynamicReferenceDensityFunction> DATA_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Tectonic.getHolderSetCodec().fieldOf("arguments").forGetter(DynamicReferenceDensityFunction::arguments), DensityFunction.CODEC.fieldOf("fallback").forGetter(DynamicReferenceDensityFunction::fallback)).apply(instance, DynamicReferenceDensityFunction::new));

    public static KeyDispatchDataCodec<DynamicReferenceDensityFunction> CODEC_HOLDER = KeyDispatchDataCodec.of(DATA_CODEC);

    @Override
    public double compute(@NotNull DensityFunction.FunctionContext context) {
        Holder<DensityFunction> toCompute;
        if (this.arguments().size() == 1) {
            toCompute = this.arguments().get(0);
        } else {
            toCompute = this.fallback();
        }
        return toCompute.value().compute(context);
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
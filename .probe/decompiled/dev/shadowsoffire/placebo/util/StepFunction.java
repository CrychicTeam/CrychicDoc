package dev.shadowsoffire.placebo.util;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;

public record StepFunction(float min, int steps, float step) implements Float2FloatFunction {

    public static final Codec<StepFunction> STRICT_CODEC = RecordCodecBuilder.create(inst -> inst.group(Codec.FLOAT.fieldOf("min").forGetter(StepFunction::min), Codec.intRange(1, Integer.MAX_VALUE).fieldOf("steps").forGetter(StepFunction::steps), Codec.FLOAT.fieldOf("step").forGetter(StepFunction::step)).apply(inst, StepFunction::new));

    public static final Codec<StepFunction> CONSTANT_CODEC = Codec.FLOAT.xmap(StepFunction::constant, StepFunction::min);

    public static final Codec<StepFunction> CODEC = Codec.either(CONSTANT_CODEC, STRICT_CODEC).xmap(e -> (StepFunction) e.map(Function.identity(), Function.identity()), Either::right);

    public StepFunction(float min, int steps, float step) {
        this.min = min;
        this.steps = steps;
        this.step = step;
        Preconditions.checkArgument(steps > 0);
    }

    public float get(float level) {
        return this.min + (float) ((int) ((float) this.steps * (level + 0.5F / (float) this.steps))) * this.step;
    }

    public int getInt(float level) {
        return (int) this.get(level);
    }

    public float max() {
        return this.min + (float) this.steps * this.step;
    }

    public int getStep(float level) {
        return (int) ((float) this.steps * (level + 0.5F / (float) this.steps));
    }

    public float getForStep(int step) {
        return this.min + this.step * (float) step;
    }

    public float getIntForStep(int step) {
        return (float) ((int) this.getForStep(step));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeFloat(this.min);
        buf.writeInt(this.steps);
        buf.writeFloat(this.step);
    }

    public static StepFunction read(FriendlyByteBuf buf) {
        return new StepFunction(buf.readFloat(), buf.readInt(), buf.readFloat());
    }

    public static StepFunction constant(float val) {
        return new StepFunction(val, 1, 0.0F);
    }
}
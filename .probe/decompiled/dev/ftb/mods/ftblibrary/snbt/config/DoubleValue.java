package dev.ftb.mods.ftblibrary.snbt.config;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DoubleValue extends NumberValue<Double> {

    DoubleValue(SNBTConfig c, String n, double def) {
        super(c, n, def);
    }

    public NumberValue<Double> range(double max) {
        return this.range(Double.valueOf(0.0), Double.valueOf(max));
    }

    public void set(Double v) {
        super.set(Double.valueOf(Mth.clamp(v, this.minValue == null ? Double.NEGATIVE_INFINITY : this.minValue, this.maxValue == null ? Double.POSITIVE_INFINITY : this.maxValue)));
    }

    @Override
    public void write(SNBTCompoundTag tag) {
        super.write(tag);
        tag.m_128347_(this.key, this.get());
    }

    @Override
    public void read(SNBTCompoundTag tag) {
        this.set(tag.m_128459_(this.key));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void createClientConfig(ConfigGroup group) {
        group.addDouble(this.key, this.get(), this::set, this.defaultValue, this.minValue == null ? Double.NEGATIVE_INFINITY : this.minValue, this.maxValue == null ? Double.POSITIVE_INFINITY : this.maxValue).fader(this.fader).setCanEdit(this.enabled.getAsBoolean());
    }
}
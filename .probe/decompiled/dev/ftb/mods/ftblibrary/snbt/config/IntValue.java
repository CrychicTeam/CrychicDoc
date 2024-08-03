package dev.ftb.mods.ftblibrary.snbt.config;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IntValue extends NumberValue<Integer> {

    IntValue(SNBTConfig c, String n, int def) {
        super(c, n, def);
    }

    public NumberValue<Integer> range(int max) {
        return this.range(Integer.valueOf(0), Integer.valueOf(max));
    }

    public void set(Integer v) {
        super.set(Integer.valueOf(Mth.clamp(v, this.minValue == null ? Integer.MIN_VALUE : this.minValue, this.maxValue == null ? Integer.MAX_VALUE : this.maxValue)));
    }

    @Override
    public void write(SNBTCompoundTag tag) {
        super.write(tag);
        tag.m_128405_(this.key, this.get());
    }

    @Override
    public void read(SNBTCompoundTag tag) {
        this.set(tag.m_128451_(this.key));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void createClientConfig(ConfigGroup group) {
        group.addInt(this.key, this.get(), this::set, this.defaultValue, this.minValue == null ? Integer.MIN_VALUE : this.minValue, this.maxValue == null ? Integer.MAX_VALUE : this.maxValue).fader(this.fader).setCanEdit(this.enabled.getAsBoolean());
    }
}
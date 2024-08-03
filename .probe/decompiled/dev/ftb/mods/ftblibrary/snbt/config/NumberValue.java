package dev.ftb.mods.ftblibrary.snbt.config;

import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import java.util.ArrayList;
import java.util.List;

public abstract class NumberValue<T extends Number> extends BaseValue<T> {

    protected T minValue = (T) null;

    protected T maxValue = (T) null;

    protected boolean fader;

    NumberValue(SNBTConfig c, String n, T def) {
        super(c, n, def);
    }

    public <E extends BaseValue<T>> E range(T min, T max) {
        this.minValue = min;
        this.maxValue = max;
        return (E) this;
    }

    public <E extends BaseValue<T>> E fader() {
        this.fader = true;
        return (E) this;
    }

    @Override
    public void write(SNBTCompoundTag tag) {
        List<String> c = new ArrayList(this.comment);
        c.add("Default: " + this.defaultValue);
        c.add("Range: " + (this.minValue == null ? "-∞" : this.minValue) + " ~ " + (this.maxValue == null ? "+∞" : this.maxValue));
        tag.comment(this.key, String.join("\n", c));
    }
}
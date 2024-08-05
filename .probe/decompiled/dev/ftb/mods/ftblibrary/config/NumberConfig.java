package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.util.StringUtils;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public abstract class NumberConfig<T extends Number> extends ConfigFromString<T> {

    public static final Color4I COLOR = Color4I.rgb(11164392);

    public final T min;

    public final T max;

    public boolean fader;

    protected T scrollIncrement;

    public NumberConfig(T mn, T mx) {
        this.min = mn;
        this.max = mx;
    }

    public Color4I getColor(@Nullable T v) {
        return COLOR;
    }

    public NumberConfig<T> fader(boolean v) {
        this.fader = v;
        return this;
    }

    @Override
    public boolean canScroll() {
        return true;
    }

    public Component getStringForGUI(@Nullable T v) {
        return (Component) (v == null ? NULL_TEXT : Component.literal(this.formatValue(v)));
    }

    protected String formatValue(T v) {
        return StringUtils.formatDouble(v.doubleValue(), true);
    }

    public NumberConfig<T> withScrollIncrement(T increment) {
        this.scrollIncrement = increment;
        return this;
    }
}
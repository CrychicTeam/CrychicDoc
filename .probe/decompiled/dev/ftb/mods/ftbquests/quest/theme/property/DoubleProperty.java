package dev.ftb.mods.ftbquests.quest.theme.property;

import net.minecraft.util.Mth;

public class DoubleProperty extends ThemeProperty<Double> {

    public final double min;

    public final double max;

    public DoubleProperty(String n, double mn, double mx) {
        super(n, 0.0);
        this.min = mn;
        this.max = mx;
    }

    public DoubleProperty(String n) {
        this(n, 0.0, 1.0);
    }

    public Double parse(String string) {
        try {
            double i = Double.parseDouble(string);
            return Mth.clamp(i, this.min, this.max);
        } catch (Exception var4) {
            return null;
        }
    }
}
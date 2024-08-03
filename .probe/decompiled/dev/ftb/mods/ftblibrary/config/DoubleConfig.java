package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class DoubleConfig extends NumberConfig<Double> {

    public DoubleConfig(double mn, double mx) {
        super(mn, mx);
        this.scrollIncrement = 1.0;
    }

    @Override
    public void addInfo(TooltipList list) {
        super.addInfo(list);
        if (this.min != Double.NEGATIVE_INFINITY) {
            list.add(info("Min", this.formatValue(this.min)));
        }
        if (this.max != Double.POSITIVE_INFINITY) {
            list.add(info("Max", this.formatValue(this.max)));
        }
    }

    public String getStringFromValue(@Nullable Double v) {
        if (v == null) {
            return "null";
        } else if (v == Double.POSITIVE_INFINITY) {
            return "+Inf";
        } else {
            return v == Double.NEGATIVE_INFINITY ? "-Inf" : super.getStringFromValue(v);
        }
    }

    @Override
    public boolean parse(@Nullable Consumer<Double> callback, String string) {
        if (!string.equals("-") && !string.equals("+") && !string.isEmpty()) {
            switch(string) {
                case "+Inf":
                    return this.max == Double.POSITIVE_INFINITY && this.okValue(callback, Double.valueOf(Double.POSITIVE_INFINITY));
                case "-Inf":
                    return this.min == Double.NEGATIVE_INFINITY && this.okValue(callback, Double.valueOf(Double.NEGATIVE_INFINITY));
                case "-":
                    return this.min <= 0.0 && this.max >= 0.0 && this.okValue(callback, Double.valueOf(0.0));
                default:
                    try {
                        double multiplier = 1.0;
                        if (string.endsWith("K")) {
                            multiplier = 1000.0;
                            string = string.substring(0, string.length() - 1);
                        } else if (string.endsWith("M")) {
                            multiplier = 1000000.0;
                            string = string.substring(0, string.length() - 1);
                        } else if (string.endsWith("B")) {
                            multiplier = 1.0E9;
                            string = string.substring(0, string.length() - 1);
                        }
                        double v = Double.parseDouble(string.trim()) * multiplier;
                        if (v >= this.min && v <= this.max) {
                            return this.okValue(callback, Double.valueOf(v));
                        }
                    } catch (Exception var7) {
                    }
                    return false;
            }
        } else {
            return this.okValue(callback, Double.valueOf(0.0));
        }
    }

    public Optional<Double> scrollValue(Double currentValue, boolean forward) {
        double newVal = Mth.clamp(currentValue + (forward ? this.scrollIncrement : -this.scrollIncrement), this.min, this.max);
        return newVal != currentValue ? Optional.of(newVal) : Optional.empty();
    }
}
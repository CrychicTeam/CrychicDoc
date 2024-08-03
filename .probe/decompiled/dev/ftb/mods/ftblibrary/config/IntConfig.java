package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class IntConfig extends NumberConfig<Integer> {

    public IntConfig(int mn, int mx) {
        super(mn, mx);
        this.scrollIncrement = 1;
    }

    @Override
    public void addInfo(TooltipList list) {
        super.addInfo(list);
        if (this.min != Integer.MIN_VALUE) {
            list.add(info("Min", this.formatValue(this.min)));
        }
        if (this.max != Integer.MAX_VALUE) {
            list.add(info("Max", this.formatValue(this.max)));
        }
    }

    @Override
    public boolean parse(@Nullable Consumer<Integer> callback, String string) {
        if (!string.equals("-") && !string.equals("+") && !string.isEmpty()) {
            try {
                Integer v = Integer.decode(string);
                if (v >= this.min && v <= this.max) {
                    return this.okValue(callback, v);
                }
            } catch (Exception var4) {
            }
            return false;
        } else {
            return this.okValue(callback, Integer.valueOf(0));
        }
    }

    protected String formatValue(Integer v) {
        return String.format("%,d", v);
    }

    public Optional<Integer> scrollValue(Integer currentValue, boolean forward) {
        int newVal = Mth.clamp(currentValue + (forward ? this.scrollIncrement : -this.scrollIncrement), this.min, this.max);
        return newVal != currentValue ? Optional.of(newVal) : Optional.empty();
    }
}
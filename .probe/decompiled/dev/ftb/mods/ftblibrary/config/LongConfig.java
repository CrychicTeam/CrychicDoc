package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.math.MathUtils;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.Optional;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public class LongConfig extends NumberConfig<Long> {

    public LongConfig(long mn, long mx) {
        super(mn, mx);
    }

    @Override
    public void addInfo(TooltipList list) {
        super.addInfo(list);
        if (this.min != Long.MIN_VALUE) {
            list.add(info("Min", this.formatValue(this.min)));
        }
        if (this.max != Long.MAX_VALUE) {
            list.add(info("Max", this.formatValue(this.max)));
        }
    }

    @Override
    public boolean parse(@Nullable Consumer<Long> callback, String string) {
        if (!string.equals("-") && !string.equals("+") && !string.isEmpty()) {
            try {
                long v = Long.decode(string);
                if (v >= this.min && v <= this.max) {
                    return this.okValue(callback, Long.valueOf(v));
                }
            } catch (Exception var5) {
            }
            return false;
        } else {
            return this.okValue(callback, Long.valueOf(0L));
        }
    }

    protected String formatValue(Long v) {
        return String.format("%,d", v);
    }

    public Optional<Long> scrollValue(Long currentValue, boolean forward) {
        long newVal = MathUtils.clamp(currentValue + (forward ? 1L : -1L), this.min, this.max);
        return newVal != currentValue ? Optional.of(newVal) : Optional.empty();
    }
}
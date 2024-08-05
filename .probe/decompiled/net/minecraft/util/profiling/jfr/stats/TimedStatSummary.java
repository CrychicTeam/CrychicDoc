package net.minecraft.util.profiling.jfr.stats;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.profiling.jfr.Percentiles;

public record TimedStatSummary<T extends TimedStat>(T f_185833_, T f_185834_, @Nullable T f_185835_, int f_185836_, Map<Integer, Double> f_185837_, Duration f_185838_) {

    private final T fastest;

    private final T slowest;

    @Nullable
    private final T secondSlowest;

    private final int count;

    private final Map<Integer, Double> percentilesNanos;

    private final Duration totalDuration;

    public TimedStatSummary(T f_185833_, T f_185834_, @Nullable T f_185835_, int f_185836_, Map<Integer, Double> f_185837_, Duration f_185838_) {
        this.fastest = f_185833_;
        this.slowest = f_185834_;
        this.secondSlowest = f_185835_;
        this.count = f_185836_;
        this.percentilesNanos = f_185837_;
        this.totalDuration = f_185838_;
    }

    public static <T extends TimedStat> TimedStatSummary<T> summary(List<T> p_185850_) {
        if (p_185850_.isEmpty()) {
            throw new IllegalArgumentException("No values");
        } else {
            List<T> $$1 = p_185850_.stream().sorted(Comparator.comparing(TimedStat::m_183571_)).toList();
            Duration $$2 = (Duration) $$1.stream().map(TimedStat::m_183571_).reduce(Duration::plus).orElse(Duration.ZERO);
            T $$3 = (T) $$1.get(0);
            T $$4 = (T) $$1.get($$1.size() - 1);
            T $$5 = (T) ($$1.size() > 1 ? $$1.get($$1.size() - 2) : null);
            int $$6 = $$1.size();
            Map<Integer, Double> $$7 = Percentiles.evaluate($$1.stream().mapToLong(p_185848_ -> p_185848_.duration().toNanos()).toArray());
            return new TimedStatSummary<>($$3, $$4, $$5, $$6, $$7, $$2);
        }
    }
}
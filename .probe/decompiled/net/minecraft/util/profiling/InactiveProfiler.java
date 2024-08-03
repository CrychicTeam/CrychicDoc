package net.minecraft.util.profiling;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.util.profiling.metrics.MetricCategory;
import org.apache.commons.lang3.tuple.Pair;

public class InactiveProfiler implements ProfileCollector {

    public static final InactiveProfiler INSTANCE = new InactiveProfiler();

    private InactiveProfiler() {
    }

    @Override
    public void startTick() {
    }

    @Override
    public void endTick() {
    }

    @Override
    public void push(String string0) {
    }

    @Override
    public void push(Supplier<String> supplierString0) {
    }

    @Override
    public void markForCharting(MetricCategory metricCategory0) {
    }

    @Override
    public void pop() {
    }

    @Override
    public void popPush(String string0) {
    }

    @Override
    public void popPush(Supplier<String> supplierString0) {
    }

    @Override
    public void incrementCounter(String string0, int int1) {
    }

    @Override
    public void incrementCounter(Supplier<String> supplierString0, int int1) {
    }

    @Override
    public ProfileResults getResults() {
        return EmptyProfileResults.EMPTY;
    }

    @Nullable
    @Override
    public ActiveProfiler.PathEntry getEntry(String string0) {
        return null;
    }

    @Override
    public Set<Pair<String, MetricCategory>> getChartedPaths() {
        return ImmutableSet.of();
    }
}
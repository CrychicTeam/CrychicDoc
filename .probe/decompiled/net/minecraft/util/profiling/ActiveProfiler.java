package net.minecraft.util.profiling;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.util.profiling.metrics.MetricCategory;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

public class ActiveProfiler implements ProfileCollector {

    private static final long WARNING_TIME_NANOS = Duration.ofMillis(100L).toNanos();

    private static final Logger LOGGER = LogUtils.getLogger();

    private final List<String> paths = Lists.newArrayList();

    private final LongList startTimes = new LongArrayList();

    private final Map<String, ActiveProfiler.PathEntry> entries = Maps.newHashMap();

    private final IntSupplier getTickTime;

    private final LongSupplier getRealTime;

    private final long startTimeNano;

    private final int startTimeTicks;

    private String path = "";

    private boolean started;

    @Nullable
    private ActiveProfiler.PathEntry currentEntry;

    private final boolean warn;

    private final Set<Pair<String, MetricCategory>> chartedPaths = new ObjectArraySet();

    public ActiveProfiler(LongSupplier longSupplier0, IntSupplier intSupplier1, boolean boolean2) {
        this.startTimeNano = longSupplier0.getAsLong();
        this.getRealTime = longSupplier0;
        this.startTimeTicks = intSupplier1.getAsInt();
        this.getTickTime = intSupplier1;
        this.warn = boolean2;
    }

    @Override
    public void startTick() {
        if (this.started) {
            LOGGER.error("Profiler tick already started - missing endTick()?");
        } else {
            this.started = true;
            this.path = "";
            this.paths.clear();
            this.push("root");
        }
    }

    @Override
    public void endTick() {
        if (!this.started) {
            LOGGER.error("Profiler tick already ended - missing startTick()?");
        } else {
            this.pop();
            this.started = false;
            if (!this.path.isEmpty()) {
                LOGGER.error("Profiler tick ended before path was fully popped (remainder: '{}'). Mismatched push/pop?", LogUtils.defer(() -> ProfileResults.demanglePath(this.path)));
            }
        }
    }

    @Override
    public void push(String string0) {
        if (!this.started) {
            LOGGER.error("Cannot push '{}' to profiler if profiler tick hasn't started - missing startTick()?", string0);
        } else {
            if (!this.path.isEmpty()) {
                this.path = this.path + "\u001e";
            }
            this.path = this.path + string0;
            this.paths.add(this.path);
            this.startTimes.add(Util.getNanos());
            this.currentEntry = null;
        }
    }

    @Override
    public void push(Supplier<String> supplierString0) {
        this.push((String) supplierString0.get());
    }

    @Override
    public void markForCharting(MetricCategory metricCategory0) {
        this.chartedPaths.add(Pair.of(this.path, metricCategory0));
    }

    @Override
    public void pop() {
        if (!this.started) {
            LOGGER.error("Cannot pop from profiler if profiler tick hasn't started - missing startTick()?");
        } else if (this.startTimes.isEmpty()) {
            LOGGER.error("Tried to pop one too many times! Mismatched push() and pop()?");
        } else {
            long $$0 = Util.getNanos();
            long $$1 = this.startTimes.removeLong(this.startTimes.size() - 1);
            this.paths.remove(this.paths.size() - 1);
            long $$2 = $$0 - $$1;
            ActiveProfiler.PathEntry $$3 = this.getCurrentEntry();
            $$3.accumulatedDuration += $$2;
            $$3.count++;
            $$3.maxDuration = Math.max($$3.maxDuration, $$2);
            $$3.minDuration = Math.min($$3.minDuration, $$2);
            if (this.warn && $$2 > WARNING_TIME_NANOS) {
                LOGGER.warn("Something's taking too long! '{}' took aprox {} ms", LogUtils.defer(() -> ProfileResults.demanglePath(this.path)), LogUtils.defer(() -> (double) $$2 / 1000000.0));
            }
            this.path = this.paths.isEmpty() ? "" : (String) this.paths.get(this.paths.size() - 1);
            this.currentEntry = null;
        }
    }

    @Override
    public void popPush(String string0) {
        this.pop();
        this.push(string0);
    }

    @Override
    public void popPush(Supplier<String> supplierString0) {
        this.pop();
        this.push(supplierString0);
    }

    private ActiveProfiler.PathEntry getCurrentEntry() {
        if (this.currentEntry == null) {
            this.currentEntry = (ActiveProfiler.PathEntry) this.entries.computeIfAbsent(this.path, p_18405_ -> new ActiveProfiler.PathEntry());
        }
        return this.currentEntry;
    }

    @Override
    public void incrementCounter(String string0, int int1) {
        this.getCurrentEntry().counters.addTo(string0, (long) int1);
    }

    @Override
    public void incrementCounter(Supplier<String> supplierString0, int int1) {
        this.getCurrentEntry().counters.addTo((String) supplierString0.get(), (long) int1);
    }

    @Override
    public ProfileResults getResults() {
        return new FilledProfileResults(this.entries, this.startTimeNano, this.startTimeTicks, this.getRealTime.getAsLong(), this.getTickTime.getAsInt());
    }

    @Nullable
    @Override
    public ActiveProfiler.PathEntry getEntry(String string0) {
        return (ActiveProfiler.PathEntry) this.entries.get(string0);
    }

    @Override
    public Set<Pair<String, MetricCategory>> getChartedPaths() {
        return this.chartedPaths;
    }

    public static class PathEntry implements ProfilerPathEntry {

        long maxDuration = Long.MIN_VALUE;

        long minDuration = Long.MAX_VALUE;

        long accumulatedDuration;

        long count;

        final Object2LongOpenHashMap<String> counters = new Object2LongOpenHashMap();

        @Override
        public long getDuration() {
            return this.accumulatedDuration;
        }

        @Override
        public long getMaxDuration() {
            return this.maxDuration;
        }

        @Override
        public long getCount() {
            return this.count;
        }

        @Override
        public Object2LongMap<String> getCounters() {
            return Object2LongMaps.unmodifiable(this.counters);
        }
    }
}
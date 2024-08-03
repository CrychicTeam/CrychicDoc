package net.minecraft.util.profiling.metrics.profiling;

import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.IntStream;
import net.minecraft.util.profiling.ProfileCollector;
import net.minecraft.util.profiling.metrics.MetricCategory;
import net.minecraft.util.profiling.metrics.MetricSampler;
import net.minecraft.util.profiling.metrics.MetricsRegistry;
import net.minecraft.util.profiling.metrics.MetricsSamplerProvider;
import org.slf4j.Logger;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

public class ServerMetricsSamplersProvider implements MetricsSamplerProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Set<MetricSampler> samplers = new ObjectOpenHashSet();

    private final ProfilerSamplerAdapter samplerFactory = new ProfilerSamplerAdapter();

    public ServerMetricsSamplersProvider(LongSupplier longSupplier0, boolean boolean1) {
        this.samplers.add(tickTimeSampler(longSupplier0));
        if (boolean1) {
            this.samplers.addAll(runtimeIndependentSamplers());
        }
    }

    public static Set<MetricSampler> runtimeIndependentSamplers() {
        Builder<MetricSampler> $$0 = ImmutableSet.builder();
        try {
            ServerMetricsSamplersProvider.CpuStats $$1 = new ServerMetricsSamplersProvider.CpuStats();
            IntStream.range(0, $$1.nrOfCpus).mapToObj(p_146185_ -> MetricSampler.create("cpu#" + p_146185_, MetricCategory.CPU, () -> $$1.loadForCpu(p_146185_))).forEach($$0::add);
        } catch (Throwable var2) {
            LOGGER.warn("Failed to query cpu, no cpu stats will be recorded", var2);
        }
        $$0.add(MetricSampler.create("heap MiB", MetricCategory.JVM, () -> (double) ((float) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576.0F)));
        $$0.addAll(MetricsRegistry.INSTANCE.getRegisteredSamplers());
        return $$0.build();
    }

    @Override
    public Set<MetricSampler> samplers(Supplier<ProfileCollector> supplierProfileCollector0) {
        this.samplers.addAll(this.samplerFactory.newSamplersFoundInProfiler(supplierProfileCollector0));
        return this.samplers;
    }

    public static MetricSampler tickTimeSampler(final LongSupplier longSupplier0) {
        Stopwatch $$1 = Stopwatch.createUnstarted(new Ticker() {

            public long read() {
                return longSupplier0.getAsLong();
            }
        });
        ToDoubleFunction<Stopwatch> $$2 = p_146187_ -> {
            if (p_146187_.isRunning()) {
                p_146187_.stop();
            }
            long $$1x = p_146187_.elapsed(TimeUnit.NANOSECONDS);
            p_146187_.reset();
            return (double) $$1x;
        };
        MetricSampler.ValueIncreasedByPercentage $$3 = new MetricSampler.ValueIncreasedByPercentage(2.0F);
        return MetricSampler.builder("ticktime", MetricCategory.TICK_LOOP, $$2, $$1).withBeforeTick(Stopwatch::start).withThresholdAlert($$3).build();
    }

    static class CpuStats {

        private final SystemInfo systemInfo = new SystemInfo();

        private final CentralProcessor processor = this.systemInfo.getHardware().getProcessor();

        public final int nrOfCpus = this.processor.getLogicalProcessorCount();

        private long[][] previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();

        private double[] currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);

        private long lastPollMs;

        public double loadForCpu(int int0) {
            long $$1 = System.currentTimeMillis();
            if (this.lastPollMs == 0L || this.lastPollMs + 501L < $$1) {
                this.currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
                this.previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
                this.lastPollMs = $$1;
            }
            return this.currentLoad[int0] * 100.0;
        }
    }
}
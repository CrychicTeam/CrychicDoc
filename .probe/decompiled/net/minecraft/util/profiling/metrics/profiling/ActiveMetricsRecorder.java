package net.minecraft.util.profiling.metrics.profiling;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.LongSupplier;
import javax.annotation.Nullable;
import net.minecraft.util.profiling.ActiveProfiler;
import net.minecraft.util.profiling.ContinuousProfiler;
import net.minecraft.util.profiling.EmptyProfileResults;
import net.minecraft.util.profiling.InactiveProfiler;
import net.minecraft.util.profiling.ProfileCollector;
import net.minecraft.util.profiling.ProfileResults;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.profiling.metrics.MetricSampler;
import net.minecraft.util.profiling.metrics.MetricsSamplerProvider;
import net.minecraft.util.profiling.metrics.storage.MetricsPersister;
import net.minecraft.util.profiling.metrics.storage.RecordedDeviation;

public class ActiveMetricsRecorder implements MetricsRecorder {

    public static final int PROFILING_MAX_DURATION_SECONDS = 10;

    @Nullable
    private static Consumer<Path> globalOnReportFinished = null;

    private final Map<MetricSampler, List<RecordedDeviation>> deviationsBySampler = new Object2ObjectOpenHashMap();

    private final ContinuousProfiler taskProfiler;

    private final Executor ioExecutor;

    private final MetricsPersister metricsPersister;

    private final Consumer<ProfileResults> onProfilingEnd;

    private final Consumer<Path> onReportFinished;

    private final MetricsSamplerProvider metricsSamplerProvider;

    private final LongSupplier wallTimeSource;

    private final long deadlineNano;

    private int currentTick;

    private ProfileCollector singleTickProfiler;

    private volatile boolean killSwitch;

    private Set<MetricSampler> thisTickSamplers = ImmutableSet.of();

    private ActiveMetricsRecorder(MetricsSamplerProvider metricsSamplerProvider0, LongSupplier longSupplier1, Executor executor2, MetricsPersister metricsPersister3, Consumer<ProfileResults> consumerProfileResults4, Consumer<Path> consumerPath5) {
        this.metricsSamplerProvider = metricsSamplerProvider0;
        this.wallTimeSource = longSupplier1;
        this.taskProfiler = new ContinuousProfiler(longSupplier1, () -> this.currentTick);
        this.ioExecutor = executor2;
        this.metricsPersister = metricsPersister3;
        this.onProfilingEnd = consumerProfileResults4;
        this.onReportFinished = globalOnReportFinished == null ? consumerPath5 : consumerPath5.andThen(globalOnReportFinished);
        this.deadlineNano = longSupplier1.getAsLong() + TimeUnit.NANOSECONDS.convert(10L, TimeUnit.SECONDS);
        this.singleTickProfiler = new ActiveProfiler(this.wallTimeSource, () -> this.currentTick, false);
        this.taskProfiler.enable();
    }

    public static ActiveMetricsRecorder createStarted(MetricsSamplerProvider metricsSamplerProvider0, LongSupplier longSupplier1, Executor executor2, MetricsPersister metricsPersister3, Consumer<ProfileResults> consumerProfileResults4, Consumer<Path> consumerPath5) {
        return new ActiveMetricsRecorder(metricsSamplerProvider0, longSupplier1, executor2, metricsPersister3, consumerProfileResults4, consumerPath5);
    }

    @Override
    public synchronized void end() {
        if (this.isRecording()) {
            this.killSwitch = true;
        }
    }

    @Override
    public synchronized void cancel() {
        if (this.isRecording()) {
            this.singleTickProfiler = InactiveProfiler.INSTANCE;
            this.onProfilingEnd.accept(EmptyProfileResults.EMPTY);
            this.cleanup(this.thisTickSamplers);
        }
    }

    @Override
    public void startTick() {
        this.verifyStarted();
        this.thisTickSamplers = this.metricsSamplerProvider.samplers(() -> this.singleTickProfiler);
        for (MetricSampler $$0 : this.thisTickSamplers) {
            $$0.onStartTick();
        }
        this.currentTick++;
    }

    @Override
    public void endTick() {
        this.verifyStarted();
        if (this.currentTick != 0) {
            for (MetricSampler $$0 : this.thisTickSamplers) {
                $$0.onEndTick(this.currentTick);
                if ($$0.triggersThreshold()) {
                    RecordedDeviation $$1 = new RecordedDeviation(Instant.now(), this.currentTick, this.singleTickProfiler.getResults());
                    ((List) this.deviationsBySampler.computeIfAbsent($$0, p_146131_ -> Lists.newArrayList())).add($$1);
                }
            }
            if (!this.killSwitch && this.wallTimeSource.getAsLong() <= this.deadlineNano) {
                this.singleTickProfiler = new ActiveProfiler(this.wallTimeSource, () -> this.currentTick, false);
            } else {
                this.killSwitch = false;
                ProfileResults $$2 = this.taskProfiler.getResults();
                this.singleTickProfiler = InactiveProfiler.INSTANCE;
                this.onProfilingEnd.accept($$2);
                this.scheduleSaveResults($$2);
            }
        }
    }

    @Override
    public boolean isRecording() {
        return this.taskProfiler.isEnabled();
    }

    @Override
    public ProfilerFiller getProfiler() {
        return ProfilerFiller.tee(this.taskProfiler.getFiller(), this.singleTickProfiler);
    }

    private void verifyStarted() {
        if (!this.isRecording()) {
            throw new IllegalStateException("Not started!");
        }
    }

    private void scheduleSaveResults(ProfileResults profileResults0) {
        HashSet<MetricSampler> $$1 = new HashSet(this.thisTickSamplers);
        this.ioExecutor.execute(() -> {
            Path $$2 = this.metricsPersister.saveReports($$1, this.deviationsBySampler, profileResults0);
            this.cleanup($$1);
            this.onReportFinished.accept($$2);
        });
    }

    private void cleanup(Collection<MetricSampler> collectionMetricSampler0) {
        for (MetricSampler $$1 : collectionMetricSampler0) {
            $$1.onFinished();
        }
        this.deviationsBySampler.clear();
        this.taskProfiler.disable();
    }

    public static void registerGlobalCompletionCallback(Consumer<Path> consumerPath0) {
        globalOnReportFinished = consumerPath0;
    }
}
package me.lucko.spark.common.sampler.async;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.IntPredicate;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.sampler.AbstractSampler;
import me.lucko.spark.common.sampler.Sampler;
import me.lucko.spark.common.sampler.SamplerMode;
import me.lucko.spark.common.sampler.SamplerSettings;
import me.lucko.spark.common.sampler.node.MergeMode;
import me.lucko.spark.common.sampler.source.ClassSourceLookup;
import me.lucko.spark.common.sampler.window.ProfilingWindowUtils;
import me.lucko.spark.common.tick.TickHook;
import me.lucko.spark.common.util.SparkThreadFactory;
import me.lucko.spark.common.ws.ViewerSocket;
import me.lucko.spark.proto.SparkSamplerProtos;

public class AsyncSampler extends AbstractSampler {

    private final SampleCollector<?> sampleCollector;

    private final AsyncProfilerAccess profilerAccess;

    private final AsyncDataAggregator dataAggregator;

    private final Object[] currentJobMutex = new Object[0];

    private AsyncProfilerJob currentJob;

    private ScheduledExecutorService scheduler;

    private ScheduledFuture<?> socketStatisticsTask;

    public AsyncSampler(SparkPlatform platform, SamplerSettings settings, SampleCollector<?> collector) {
        super(platform, settings);
        this.sampleCollector = collector;
        this.profilerAccess = AsyncProfilerAccess.getInstance(platform);
        this.dataAggregator = new AsyncDataAggregator(settings.threadGrouper());
        this.scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("spark-async-sampler-worker-thread").setUncaughtExceptionHandler(SparkThreadFactory.EXCEPTION_HANDLER).build());
    }

    @Override
    public void start() {
        super.start();
        TickHook tickHook = this.platform.getTickHook();
        if (tickHook != null) {
            this.windowStatisticsCollector.startCountingTicks(tickHook);
        }
        int window = ProfilingWindowUtils.windowNow();
        AsyncProfilerJob job = this.profilerAccess.startNewProfilerJob();
        job.init(this.platform, this.sampleCollector, this.threadDumper, window, this.background);
        job.start();
        this.windowStatisticsCollector.recordWindowStartTime(window);
        this.currentJob = job;
        boolean shouldNotRotate = this.sampleCollector instanceof SampleCollector.Allocation && ((SampleCollector.Allocation) this.sampleCollector).isLiveOnly();
        if (!shouldNotRotate) {
            this.scheduler.scheduleAtFixedRate(this::rotateProfilerJob, 60L, 60L, TimeUnit.SECONDS);
        }
        this.recordInitialGcStats();
        this.scheduleTimeout();
    }

    private void rotateProfilerJob() {
        try {
            synchronized (this.currentJobMutex) {
                AsyncProfilerJob previousJob = this.currentJob;
                if (previousJob == null) {
                    return;
                }
                try {
                    previousJob.stop();
                } catch (Exception var8) {
                    var8.printStackTrace();
                }
                int window = previousJob.getWindow() + 1;
                AsyncProfilerJob newJob = this.profilerAccess.startNewProfilerJob();
                newJob.init(this.platform, this.sampleCollector, this.threadDumper, window, this.background);
                newJob.start();
                this.windowStatisticsCollector.recordWindowStartTime(window);
                this.currentJob = newJob;
                try {
                    this.windowStatisticsCollector.measureNow(previousJob.getWindow());
                } catch (Exception var7) {
                    var7.printStackTrace();
                }
                previousJob.aggregate(this.dataAggregator);
                IntPredicate predicate = ProfilingWindowUtils.keepHistoryBefore(window);
                this.dataAggregator.pruneData(predicate);
                this.windowStatisticsCollector.pruneStatistics(predicate);
                this.scheduler.execute(() -> this.processWindowRotate());
            }
        } catch (Throwable var10) {
            var10.printStackTrace();
        }
    }

    private void scheduleTimeout() {
        if (this.autoEndTime != -1L) {
            long delay = this.autoEndTime - System.currentTimeMillis();
            if (delay > 0L) {
                this.scheduler.schedule(() -> {
                    this.stop(false);
                    this.future.complete(this);
                }, delay, TimeUnit.MILLISECONDS);
            }
        }
    }

    @Override
    public void stop(boolean cancelled) {
        super.stop(cancelled);
        synchronized (this.currentJobMutex) {
            this.currentJob.stop();
            if (!cancelled) {
                this.windowStatisticsCollector.measureNow(this.currentJob.getWindow());
                this.currentJob.aggregate(this.dataAggregator);
            } else {
                this.currentJob.deleteOutputFile();
            }
            this.currentJob = null;
        }
        if (this.socketStatisticsTask != null) {
            this.socketStatisticsTask.cancel(false);
        }
        if (this.scheduler != null) {
            this.scheduler.shutdown();
            this.scheduler = null;
        }
    }

    @Override
    public void attachSocket(ViewerSocket socket) {
        super.attachSocket(socket);
        if (this.socketStatisticsTask == null) {
            this.socketStatisticsTask = this.scheduler.scheduleAtFixedRate(() -> this.sendStatisticsToSocket(), 10L, 10L, TimeUnit.SECONDS);
        }
    }

    @Override
    public SamplerMode getMode() {
        return this.sampleCollector.getMode();
    }

    @Override
    public SparkSamplerProtos.SamplerData toProto(SparkPlatform platform, Sampler.ExportProps exportProps) {
        SparkSamplerProtos.SamplerData.Builder proto = SparkSamplerProtos.SamplerData.newBuilder();
        if (exportProps.channelInfo() != null) {
            proto.setChannelInfo(exportProps.channelInfo());
        }
        this.writeMetadataToProto(proto, platform, exportProps.creator(), exportProps.comment(), this.dataAggregator);
        this.writeDataToProto(proto, this.dataAggregator, (MergeMode) exportProps.mergeMode().get(), (ClassSourceLookup) exportProps.classSourceLookup().get());
        return proto.build();
    }
}
package me.lucko.spark.common.sampler.java;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.sampler.AbstractSampler;
import me.lucko.spark.common.sampler.Sampler;
import me.lucko.spark.common.sampler.SamplerMode;
import me.lucko.spark.common.sampler.SamplerSettings;
import me.lucko.spark.common.sampler.node.MergeMode;
import me.lucko.spark.common.sampler.source.ClassSourceLookup;
import me.lucko.spark.common.sampler.window.ProfilingWindowUtils;
import me.lucko.spark.common.sampler.window.WindowStatisticsCollector;
import me.lucko.spark.common.tick.TickHook;
import me.lucko.spark.common.util.SparkThreadFactory;
import me.lucko.spark.common.ws.ViewerSocket;
import me.lucko.spark.proto.SparkSamplerProtos;

public class JavaSampler extends AbstractSampler implements Runnable {

    private static final AtomicInteger THREAD_ID = new AtomicInteger(0);

    private final ScheduledExecutorService workerPool = Executors.newScheduledThreadPool(6, new ThreadFactoryBuilder().setNameFormat("spark-java-sampler-" + THREAD_ID.getAndIncrement() + "-%d").setUncaughtExceptionHandler(SparkThreadFactory.EXCEPTION_HANDLER).build());

    private ScheduledFuture<?> task;

    private ScheduledFuture<?> socketStatisticsTask;

    private final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

    private final JavaDataAggregator dataAggregator;

    private final AtomicInteger lastWindow = new AtomicInteger();

    public JavaSampler(SparkPlatform platform, SamplerSettings settings, boolean ignoreSleeping, boolean ignoreNative) {
        super(platform, settings);
        this.dataAggregator = new SimpleDataAggregator(this.workerPool, settings.threadGrouper(), settings.interval(), ignoreSleeping, ignoreNative);
    }

    public JavaSampler(SparkPlatform platform, SamplerSettings settings, boolean ignoreSleeping, boolean ignoreNative, TickHook tickHook, int tickLengthThreshold) {
        super(platform, settings);
        this.dataAggregator = new TickedDataAggregator(this.workerPool, settings.threadGrouper(), settings.interval(), ignoreSleeping, ignoreNative, tickHook, tickLengthThreshold);
    }

    @Override
    public void start() {
        super.start();
        TickHook tickHook = this.platform.getTickHook();
        if (tickHook != null) {
            if (this.dataAggregator instanceof TickedDataAggregator) {
                WindowStatisticsCollector.ExplicitTickCounter counter = this.windowStatisticsCollector.startCountingTicksExplicit(tickHook);
                ((TickedDataAggregator) this.dataAggregator).setTickCounter(counter);
            } else {
                this.windowStatisticsCollector.startCountingTicks(tickHook);
            }
        }
        this.windowStatisticsCollector.recordWindowStartTime(ProfilingWindowUtils.unixMillisToWindow(this.startTime));
        this.task = this.workerPool.scheduleAtFixedRate(this, 0L, (long) this.interval, TimeUnit.MICROSECONDS);
    }

    @Override
    public void stop(boolean cancelled) {
        super.stop(cancelled);
        this.task.cancel(false);
        if (this.socketStatisticsTask != null) {
            this.socketStatisticsTask.cancel(false);
        }
        if (!cancelled) {
            this.windowStatisticsCollector.measureNow(this.lastWindow.get());
        }
        this.workerPool.shutdown();
    }

    public void run() {
        try {
            long time = System.currentTimeMillis();
            if (this.autoEndTime != -1L && this.autoEndTime <= time) {
                this.stop(false);
                this.future.complete(this);
                return;
            }
            int window = ProfilingWindowUtils.unixMillisToWindow(time);
            ThreadInfo[] threadDumps = this.threadDumper.dumpThreads(this.threadBean);
            this.workerPool.execute(new JavaSampler.InsertDataTask(threadDumps, window));
        } catch (Throwable var5) {
            this.stop(false);
            this.future.completeExceptionally(var5);
        }
    }

    @Override
    public void attachSocket(ViewerSocket socket) {
        super.attachSocket(socket);
        if (this.socketStatisticsTask == null) {
            this.socketStatisticsTask = this.workerPool.scheduleAtFixedRate(() -> this.sendStatisticsToSocket(), 10L, 10L, TimeUnit.SECONDS);
        }
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

    @Override
    public SamplerMode getMode() {
        return SamplerMode.EXECUTION;
    }

    private final class InsertDataTask implements Runnable {

        private final ThreadInfo[] threadDumps;

        private final int window;

        InsertDataTask(ThreadInfo[] threadDumps, int window) {
            this.threadDumps = threadDumps;
            this.window = window;
        }

        public void run() {
            for (ThreadInfo threadInfo : this.threadDumps) {
                if (threadInfo.getThreadName() != null && threadInfo.getStackTrace() != null) {
                    JavaSampler.this.dataAggregator.insertData(threadInfo, this.window);
                }
            }
            int previousWindow = JavaSampler.this.lastWindow.getAndUpdate(previous -> Math.max(this.window, previous));
            if (previousWindow != 0 && previousWindow != this.window) {
                JavaSampler.this.windowStatisticsCollector.recordWindowStartTime(this.window);
                JavaSampler.this.windowStatisticsCollector.measureNow(previousWindow);
                IntPredicate predicate = ProfilingWindowUtils.keepHistoryBefore(this.window);
                JavaSampler.this.dataAggregator.pruneData(predicate);
                JavaSampler.this.windowStatisticsCollector.pruneStatistics(predicate);
                JavaSampler.this.workerPool.execute(() -> JavaSampler.this.processWindowRotate());
            }
        }
    }
}
package me.lucko.spark.common.sampler.window;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;
import java.util.logging.Level;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.monitor.cpu.CpuMonitor;
import me.lucko.spark.common.monitor.tick.TickStatistics;
import me.lucko.spark.common.platform.world.AsyncWorldInfoProvider;
import me.lucko.spark.common.platform.world.WorldInfoProvider;
import me.lucko.spark.common.tick.TickHook;
import me.lucko.spark.proto.SparkProtos;

public class WindowStatisticsCollector {

    private static final SparkProtos.WindowStatistics ZERO = SparkProtos.WindowStatistics.newBuilder().setDuration(60000).build();

    private final SparkPlatform platform;

    private final Map<Integer, Long> windowStartTimes = new HashMap();

    private final Map<Integer, SparkProtos.WindowStatistics> stats;

    private WindowStatisticsCollector.TickCounter tickCounter;

    public WindowStatisticsCollector(SparkPlatform platform) {
        this.platform = platform;
        this.stats = new ConcurrentHashMap();
    }

    public void startCountingTicks(TickHook hook) {
        this.tickCounter = new WindowStatisticsCollector.NormalTickCounter(this.platform, hook);
    }

    public WindowStatisticsCollector.ExplicitTickCounter startCountingTicksExplicit(TickHook hook) {
        WindowStatisticsCollector.ExplicitTickCounter counter = new WindowStatisticsCollector.ExplicitTickCounter(this.platform, hook);
        this.tickCounter = counter;
        return counter;
    }

    public void stop() {
        if (this.tickCounter != null) {
            this.tickCounter.stop();
        }
    }

    public int getTotalTicks() {
        return this.tickCounter == null ? -1 : this.tickCounter.getTotalTicks();
    }

    public void recordWindowStartTime(int window) {
        this.windowStartTimes.put(window, System.currentTimeMillis());
    }

    public void measureNow(int window) {
        this.stats.computeIfAbsent(window, this::measure);
    }

    public void ensureHasStatisticsForAllWindows(int[] windows) {
        for (int window : windows) {
            this.stats.computeIfAbsent(window, w -> ZERO);
        }
    }

    public void pruneStatistics(IntPredicate predicate) {
        this.stats.keySet().removeIf(predicate::test);
    }

    public Map<Integer, SparkProtos.WindowStatistics> export() {
        return this.stats;
    }

    private SparkProtos.WindowStatistics measure(int window) {
        SparkProtos.WindowStatistics.Builder builder = SparkProtos.WindowStatistics.newBuilder();
        long endTime = System.currentTimeMillis();
        Long startTime = (Long) this.windowStartTimes.get(window);
        if (startTime == null) {
            this.platform.getPlugin().log(Level.WARNING, "Unknown start time for window " + window);
            startTime = endTime - 60000L;
        }
        builder.setStartTime(startTime);
        builder.setEndTime(endTime);
        builder.setDuration((int) (endTime - startTime));
        TickStatistics tickStatistics = this.platform.getTickStatistics();
        if (tickStatistics != null) {
            builder.setTps(tickStatistics.tps1Min());
            DoubleAverageInfo mspt = tickStatistics.duration1Min();
            if (mspt != null) {
                builder.setMsptMedian(mspt.median());
                builder.setMsptMax(mspt.max());
            }
        }
        if (this.tickCounter != null) {
            int ticks = this.tickCounter.getCountedTicksThisWindowAndReset();
            builder.setTicks(ticks);
        }
        builder.setCpuProcess(CpuMonitor.processLoad1MinAvg());
        builder.setCpuSystem(CpuMonitor.systemLoad1MinAvg());
        try {
            AsyncWorldInfoProvider worldInfoProvider = new AsyncWorldInfoProvider(this.platform, this.platform.getPlugin().createWorldInfoProvider());
            WorldInfoProvider.CountsResult counts = worldInfoProvider.getCounts();
            if (counts != null) {
                builder.setPlayers(counts.players());
                builder.setEntities(counts.entities());
                builder.setTileEntities(counts.tileEntities());
                builder.setChunks(counts.chunks());
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }
        return builder.build();
    }

    private abstract static class BaseTickCounter implements WindowStatisticsCollector.TickCounter {

        protected final SparkPlatform platform;

        protected final TickHook tickHook;

        private final int startTick;

        private int stopTick = -1;

        BaseTickCounter(SparkPlatform platform, TickHook tickHook) {
            this.platform = platform;
            this.tickHook = tickHook;
            this.startTick = this.tickHook.getCurrentTick();
        }

        @Override
        public void stop() {
            this.stopTick = this.tickHook.getCurrentTick();
        }

        @Override
        public int getTotalTicks() {
            if (this.startTick == -1) {
                throw new IllegalStateException("start tick not recorded");
            } else {
                int stopTick = this.stopTick;
                if (stopTick == -1) {
                    stopTick = this.tickHook.getCurrentTick();
                }
                return stopTick - this.startTick;
            }
        }
    }

    public static final class ExplicitTickCounter extends WindowStatisticsCollector.BaseTickCounter {

        private final AtomicInteger counted = new AtomicInteger();

        private final AtomicInteger total = new AtomicInteger();

        ExplicitTickCounter(SparkPlatform platform, TickHook tickHook) {
            super(platform, tickHook);
        }

        public void increment() {
            this.counted.incrementAndGet();
            this.total.incrementAndGet();
        }

        public int getTotalCountedTicks() {
            return this.total.get();
        }

        @Override
        public int getCountedTicksThisWindowAndReset() {
            return this.counted.getAndSet(0);
        }
    }

    public static final class NormalTickCounter extends WindowStatisticsCollector.BaseTickCounter {

        private int last = this.tickHook.getCurrentTick();

        NormalTickCounter(SparkPlatform platform, TickHook tickHook) {
            super(platform, tickHook);
        }

        @Override
        public int getCountedTicksThisWindowAndReset() {
            synchronized (this) {
                int now = this.tickHook.getCurrentTick();
                int ticks = now - this.last;
                this.last = now;
                return ticks;
            }
        }
    }

    public interface TickCounter {

        void stop();

        int getTotalTicks();

        int getCountedTicksThisWindowAndReset();
    }
}
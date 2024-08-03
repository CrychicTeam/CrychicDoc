package me.lucko.spark.common.monitor.ping;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import me.lucko.spark.common.monitor.MonitoringExecutor;
import me.lucko.spark.common.util.RollingAverage;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class PingStatistics implements Runnable, AutoCloseable {

    private static final int QUERY_RATE_SECONDS = 10;

    private static final int WINDOW_SIZE_SECONDS = (int) TimeUnit.MINUTES.toSeconds(15L);

    private static final int WINDOW_SIZE = WINDOW_SIZE_SECONDS / 10;

    private final PlayerPingProvider provider;

    private final RollingAverage rollingAverage = new RollingAverage(WINDOW_SIZE);

    private ScheduledFuture<?> future;

    public PingStatistics(PlayerPingProvider provider) {
        this.provider = provider;
    }

    public void start() {
        if (this.future != null) {
            throw new IllegalStateException();
        } else {
            this.future = MonitoringExecutor.INSTANCE.scheduleAtFixedRate(this, 10L, 10L, TimeUnit.SECONDS);
        }
    }

    public void close() {
        if (this.future != null) {
            this.future.cancel(false);
            this.future = null;
        }
    }

    public void run() {
        PingSummary summary = this.currentSummary();
        if (summary.total() != 0) {
            this.rollingAverage.add(BigDecimal.valueOf(summary.median()));
        }
    }

    public RollingAverage getPingAverage() {
        return this.rollingAverage;
    }

    public PingSummary currentSummary() {
        Map<String, Integer> results = this.provider.poll();
        int[] values = results.values().stream().filter(ping -> ping > 0).mapToInt(i -> i).toArray();
        return values.length == 0 ? new PingSummary(new int[] { 0 }) : new PingSummary(values);
    }

    @Nullable
    public PingStatistics.PlayerPing query(String playerName) {
        Map<String, Integer> results = this.provider.poll();
        Integer result = (Integer) results.get(playerName);
        if (result != null) {
            return new PingStatistics.PlayerPing(playerName, result);
        } else {
            for (Entry<String, Integer> entry : results.entrySet()) {
                if (((String) entry.getKey()).equalsIgnoreCase(playerName)) {
                    return new PingStatistics.PlayerPing((String) entry.getKey(), (Integer) entry.getValue());
                }
            }
            return null;
        }
    }

    public static final class PlayerPing {

        private final String name;

        private final int ping;

        PlayerPing(String name, int ping) {
            this.name = name;
            this.ping = ping;
        }

        public String name() {
            return this.name;
        }

        public int ping() {
            return this.ping;
        }
    }
}
package net.minecraft.util.profiling.jfr.stats;

import com.google.common.base.MoreObjects;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedThread;

public record ThreadAllocationStat(Instant f_185786_, String f_185787_, long f_185788_) {

    private final Instant timestamp;

    private final String threadName;

    private final long totalBytes;

    private static final String UNKNOWN_THREAD = "unknown";

    public ThreadAllocationStat(Instant f_185786_, String f_185787_, long f_185788_) {
        this.timestamp = f_185786_;
        this.threadName = f_185787_;
        this.totalBytes = f_185788_;
    }

    public static ThreadAllocationStat from(RecordedEvent p_185804_) {
        RecordedThread $$1 = p_185804_.getThread("thread");
        String $$2 = $$1 == null ? "unknown" : (String) MoreObjects.firstNonNull($$1.getJavaName(), "unknown");
        return new ThreadAllocationStat(p_185804_.getStartTime(), $$2, p_185804_.getLong("allocated"));
    }

    public static ThreadAllocationStat.Summary summary(List<ThreadAllocationStat> p_185798_) {
        Map<String, Double> $$1 = new TreeMap();
        Map<String, List<ThreadAllocationStat>> $$2 = (Map<String, List<ThreadAllocationStat>>) p_185798_.stream().collect(Collectors.groupingBy(p_185796_ -> p_185796_.threadName));
        $$2.forEach((p_185801_, p_185802_) -> {
            if (p_185802_.size() >= 2) {
                ThreadAllocationStat $$3 = (ThreadAllocationStat) p_185802_.get(0);
                ThreadAllocationStat $$4 = (ThreadAllocationStat) p_185802_.get(p_185802_.size() - 1);
                long $$5 = Duration.between($$3.timestamp, $$4.timestamp).getSeconds();
                long $$6 = $$4.totalBytes - $$3.totalBytes;
                $$1.put(p_185801_, (double) $$6 / (double) $$5);
            }
        });
        return new ThreadAllocationStat.Summary($$1);
    }

    public static record Summary(Map<String, Double> f_185811_) {

        private final Map<String, Double> allocationsPerSecondByThread;

        public Summary(Map<String, Double> f_185811_) {
            this.allocationsPerSecondByThread = f_185811_;
        }
    }
}
package journeymap.client.log;

import com.google.common.util.concurrent.AtomicDouble;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.ChatFormatting;
import org.apache.logging.log4j.Logger;

public class StatTimer {

    public static final double NS = 1000000.0;

    private static final int WARMUP_COUNT_DEFAULT = 10;

    private static final int MAX_COUNT = 1000000;

    private static final int MAX_ELAPSED_LIMIT_WARNINGS = 10;

    private static final int ELAPSED_LIMIT_DEFAULT = 1000;

    private static final Logger logger = Journeymap.getLogger();

    private static Map<String, StatTimer> timers = Collections.synchronizedMap(new HashMap());

    private final int warmupCount;

    private final int elapsedLimit;

    private final AtomicLong counter = new AtomicLong();

    private final AtomicLong cancelCounter = new AtomicLong();

    private final AtomicDouble totalTime = new AtomicDouble();

    private final String name;

    private final boolean disposable;

    private final boolean doWarmup;

    private int elapsedLimitWarnings = 10;

    private boolean warmup = true;

    private boolean maxed = false;

    private boolean ranTooLong = true;

    private int ranTooLongCount;

    private Long started;

    private double max = 0.0;

    private double min = Double.MAX_VALUE;

    private StatTimer(String name, int warmupCount, int elapsedLimit, boolean disposable) {
        this.name = name;
        this.warmupCount = warmupCount;
        this.elapsedLimit = elapsedLimit;
        this.disposable = disposable;
        this.doWarmup = warmupCount > 0;
        this.warmup = warmupCount > 0;
    }

    public static synchronized StatTimer get(String name) {
        return get(name, 10);
    }

    public static synchronized StatTimer get(String name, int warmupCount) {
        if (name == null) {
            throw new IllegalArgumentException("StatTimer name required");
        } else {
            StatTimer timer = (StatTimer) timers.get(name);
            if (timer == null) {
                timer = new StatTimer(name, warmupCount, 1000, false);
                timers.put(name, timer);
            }
            return timer;
        }
    }

    public static synchronized StatTimer get(String name, int warmupCount, int elapsedLimit) {
        if (name == null) {
            throw new IllegalArgumentException("StatTimer name required");
        } else {
            StatTimer timer = (StatTimer) timers.get(name);
            if (timer == null) {
                timer = new StatTimer(name, warmupCount, elapsedLimit, false);
                timers.put(name, timer);
            }
            return timer;
        }
    }

    public static StatTimer getDisposable(String name) {
        return new StatTimer(name, 0, 1000, true);
    }

    public static StatTimer getDisposable(String name, int elapsedLimit) {
        return new StatTimer(name, 0, elapsedLimit, true);
    }

    public static synchronized void resetAll() {
        for (StatTimer timer : timers.values()) {
            timer.reset();
        }
    }

    public static synchronized String getReport() {
        List<StatTimer> list = new ArrayList(timers.values());
        Collections.sort(list, new Comparator<StatTimer>() {

            public int compare(StatTimer o1, StatTimer o2) {
                return o1.name.compareTo(o2.name);
            }
        });
        StringBuffer sb = new StringBuffer();
        for (StatTimer timer : list) {
            if (timer.counter.get() > 0L) {
                sb.append(LogFormatter.LINEBREAK).append(timer.getReportString());
            }
        }
        return sb.toString();
    }

    public static synchronized List<String> getReportByTotalTime(String prefix, String suffix) {
        List<StatTimer> list = new ArrayList(timers.values());
        Collections.sort(list, new Comparator<StatTimer>() {

            public int compare(StatTimer o1, StatTimer o2) {
                return Double.compare(o2.totalTime.get(), o1.totalTime.get());
            }
        });
        ArrayList<String> strings = new ArrayList();
        for (StatTimer timer : list) {
            if (timer.counter.get() > 0L) {
                strings.add(prefix + timer.getSimpleReportString() + suffix);
            }
            if (strings.size() >= 30) {
                break;
            }
        }
        return strings;
    }

    private static String pad(Object s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public StatTimer start() {
        synchronized (this.counter) {
            if (this.maxed) {
                return this;
            } else {
                if (this.started != null) {
                    logger.warn(this.name + " is already running, cancelling first");
                    this.cancel();
                }
                this.ranTooLong = false;
                if (this.counter.get() == 1000000L) {
                    this.maxed = true;
                    logger.info(this.name + " hit max count, 1000000");
                    return this;
                } else {
                    if (this.warmup && this.counter.get() > (long) this.warmupCount) {
                        this.warmup = false;
                        this.max = 0.0;
                        this.min = Double.MAX_VALUE;
                        this.counter.set(0L);
                        this.cancelCounter.set(0L);
                        this.totalTime.set(0.0);
                        if (logger.isTraceEnabled()) {
                            logger.debug(this.name + " warmup done, " + this.warmupCount);
                        }
                    }
                    this.started = System.nanoTime();
                    return this;
                }
            }
        }
    }

    public double stop() {
        synchronized (this.counter) {
            if (this.maxed) {
                return 0.0;
            } else if (this.started == null) {
                if (this.counter.get() > 0L) {
                    logger.warn(this.name + " is not running.");
                }
                return 0.0;
            } else {
                double var10000;
                try {
                    double elapsedMs = (double) (System.nanoTime() - this.started) / 1000000.0;
                    this.totalTime.getAndAdd(elapsedMs);
                    this.counter.getAndIncrement();
                    if (elapsedMs < this.min) {
                        this.min = elapsedMs;
                    }
                    if (elapsedMs > this.max) {
                        this.max = elapsedMs;
                    }
                    this.started = null;
                    if (!this.warmup && elapsedMs >= (double) this.elapsedLimit) {
                        this.ranTooLong = true;
                        this.ranTooLongCount++;
                        if (this.elapsedLimitWarnings > 0) {
                            String msg = this.getName() + " was slow: " + elapsedMs;
                            if (--this.elapsedLimitWarnings == 0) {
                                msg = msg + " (Warning limit reached)";
                                logger.warn(msg);
                                logger.warn(this.getReportString().replaceAll("<b>", "").replaceAll("</b>", "").trim());
                            } else {
                                logger.debug(msg);
                            }
                        }
                    }
                    var10000 = elapsedMs;
                } catch (Throwable var6) {
                    logger.error("Timer error: " + LogFormatter.toString(var6));
                    this.reset();
                    return 0.0;
                }
                return var10000;
            }
        }
    }

    public double elapsed() {
        synchronized (this.counter) {
            return !this.maxed && this.started != null ? (double) (System.nanoTime() - this.started) / 1000000.0 : 0.0;
        }
    }

    public boolean hasReachedElapsedLimit() {
        return this.ranTooLong;
    }

    public int getElapsedLimitReachedCount() {
        return this.ranTooLongCount;
    }

    public int getElapsedLimitWarningsRemaining() {
        return this.elapsedLimitWarnings;
    }

    public String stopAndReport() {
        this.stop();
        return this.getSimpleReportString();
    }

    public void cancel() {
        synchronized (this.counter) {
            this.started = null;
            this.cancelCounter.incrementAndGet();
        }
    }

    public void reset() {
        synchronized (this.counter) {
            this.warmup = this.doWarmup;
            this.maxed = false;
            this.started = null;
            this.counter.set(0L);
            this.cancelCounter.set(0L);
            this.totalTime.set(0.0);
            this.elapsedLimitWarnings = 10;
            this.ranTooLong = false;
            this.ranTooLongCount = 0;
        }
    }

    public void report() {
        logger.info(this.getReportString());
    }

    public String getReportString() {
        DecimalFormat df = new DecimalFormat("###.##");
        synchronized (this.counter) {
            long count = this.counter.get();
            double total = this.totalTime.get();
            double avg = total / (double) count;
            long cancels = this.cancelCounter.get();
            String report = String.format("<b>%40s:</b> Avg: %8sms, Min: %8sms, Max: %10sms, Total: %10s sec, Count: %8s, Canceled: %8s, Slow: %8s", this.name, df.format(avg), df.format(this.min), df.format(this.max), TimeUnit.MILLISECONDS.toSeconds((long) total), count, cancels, this.ranTooLongCount);
            if (this.warmup) {
                report = report + String.format("* Warmup of %s not met", this.warmupCount);
            }
            if (this.maxed) {
                report = report + "(MAXED)";
            }
            return report;
        }
    }

    public String getLogReportString() {
        this.stop();
        return ChatFormatting.stripFormatting(this.getSimpleReportString());
    }

    public String getSimpleReportString() {
        try {
            DecimalFormat df = new DecimalFormat("###.##");
            synchronized (this.counter) {
                long count = this.counter.get();
                double total = this.totalTime.get();
                double avg = total / (double) count;
                StringBuilder sb = new StringBuilder(this.name);
                sb.append(ChatFormatting.DARK_GRAY);
                sb.append(" count ").append(ChatFormatting.RESET);
                sb.append(count);
                sb.append(ChatFormatting.DARK_GRAY);
                sb.append(" avg ").append(ChatFormatting.RESET);
                if (this.ranTooLongCount > 0) {
                    sb.append(ChatFormatting.RESET);
                }
                sb.append(df.format(avg));
                sb.append(ChatFormatting.DARK_GRAY);
                sb.append("ms");
                sb.append(ChatFormatting.RESET);
                if (this.maxed) {
                    sb.append("(MAXED)");
                }
                return sb.toString();
            }
        } catch (Throwable var12) {
            return String.format("StatTimer '%s' encountered an error getting its simple report: %s", this.name, var12);
        }
    }

    public String getName() {
        return this.name;
    }
}
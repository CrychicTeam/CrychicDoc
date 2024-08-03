package mezz.jei.library.load;

import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PluginCallerTimerRunnable {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final long startReportDurationMs = 10L;

    private static final long longReportDurationInterval = Duration.ofSeconds(5L).toMillis();

    private final String title;

    private final ResourceLocation pluginUid;

    private final long startTime;

    private long nextLongReportDurationMs = longReportDurationInterval;

    public PluginCallerTimerRunnable(String title, ResourceLocation pluginUid) {
        this.title = title;
        this.pluginUid = pluginUid;
        this.startTime = System.nanoTime();
        LOGGER.debug("{}: {}...", title, pluginUid);
    }

    public void check() {
        Duration elapsed = Duration.ofNanos(System.nanoTime() - this.startTime);
        long elapsedMs = elapsed.toMillis();
        if (elapsedMs > this.nextLongReportDurationMs) {
            LOGGER.error("{}: {} is running and has taken {} so far", this.title, this.pluginUid, toHumanString(elapsed));
            this.nextLongReportDurationMs = this.nextLongReportDurationMs + longReportDurationInterval;
        }
    }

    public void stop() {
        Duration elapsed = Duration.ofNanos(System.nanoTime() - this.startTime);
        if (elapsed.toMillis() > 10L) {
            LOGGER.info("{}: {} took {}", this.title, this.pluginUid, toHumanString(elapsed));
        }
    }

    private static String toHumanString(Duration duration) {
        TimeUnit unit = getSmallestUnit(duration);
        long nanos = duration.toNanos();
        double value = (double) nanos / (double) TimeUnit.NANOSECONDS.convert(1L, unit);
        return String.format(Locale.ROOT, "%.4g %s", value, unitToString(unit));
    }

    private static TimeUnit getSmallestUnit(Duration duration) {
        if (duration.toDays() > 0L) {
            return TimeUnit.DAYS;
        } else if (duration.toHours() > 0L) {
            return TimeUnit.HOURS;
        } else if (duration.toMinutes() > 0L) {
            return TimeUnit.MINUTES;
        } else if (duration.toSeconds() > 0L) {
            return TimeUnit.SECONDS;
        } else if (duration.toMillis() > 0L) {
            return TimeUnit.MILLISECONDS;
        } else {
            return duration.toNanos() > 1000L ? TimeUnit.MICROSECONDS : TimeUnit.NANOSECONDS;
        }
    }

    private static String unitToString(TimeUnit unit) {
        return unit.name().toLowerCase(Locale.ROOT);
    }
}
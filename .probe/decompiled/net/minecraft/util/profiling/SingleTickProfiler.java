package net.minecraft.util.profiling;

import com.mojang.logging.LogUtils;
import java.io.File;
import java.util.function.LongSupplier;
import javax.annotation.Nullable;
import net.minecraft.Util;
import org.slf4j.Logger;

public class SingleTickProfiler {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final LongSupplier realTime;

    private final long saveThreshold;

    private int tick;

    private final File location;

    private ProfileCollector profiler = InactiveProfiler.INSTANCE;

    public SingleTickProfiler(LongSupplier longSupplier0, String string1, long long2) {
        this.realTime = longSupplier0;
        this.location = new File("debug", string1);
        this.saveThreshold = long2;
    }

    public ProfilerFiller startTick() {
        this.profiler = new ActiveProfiler(this.realTime, () -> this.tick, false);
        this.tick++;
        return this.profiler;
    }

    public void endTick() {
        if (this.profiler != InactiveProfiler.INSTANCE) {
            ProfileResults $$0 = this.profiler.getResults();
            this.profiler = InactiveProfiler.INSTANCE;
            if ($$0.getNanoDuration() >= this.saveThreshold) {
                File $$1 = new File(this.location, "tick-results-" + Util.getFilenameFormattedDateTime() + ".txt");
                $$0.saveResults($$1.toPath());
                LOGGER.info("Recorded long tick -- wrote info to: {}", $$1.getAbsolutePath());
            }
        }
    }

    @Nullable
    public static SingleTickProfiler createTickProfiler(String string0) {
        return null;
    }

    public static ProfilerFiller decorateFiller(ProfilerFiller profilerFiller0, @Nullable SingleTickProfiler singleTickProfiler1) {
        return singleTickProfiler1 != null ? ProfilerFiller.tee(singleTickProfiler1.startTick(), profilerFiller0) : profilerFiller0;
    }
}
package net.minecraft.util.profiling.jfr;

import com.mojang.logging.LogUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.server.Bootstrap;
import net.minecraft.util.profiling.jfr.parse.JfrStatsParser;
import net.minecraft.util.profiling.jfr.parse.JfrStatsResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

public class SummaryReporter {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Runnable onDeregistration;

    protected SummaryReporter(Runnable runnable0) {
        this.onDeregistration = runnable0;
    }

    public void recordingStopped(@Nullable Path path0) {
        if (path0 != null) {
            this.onDeregistration.run();
            infoWithFallback(() -> "Dumped flight recorder profiling to " + path0);
            JfrStatsResult $$1;
            try {
                $$1 = JfrStatsParser.parse(path0);
            } catch (Throwable var5) {
                warnWithFallback(() -> "Failed to parse JFR recording", var5);
                return;
            }
            try {
                infoWithFallback($$1::m_185510_);
                Path $$4 = path0.resolveSibling("jfr-report-" + StringUtils.substringBefore(path0.getFileName().toString(), ".jfr") + ".json");
                Files.writeString($$4, $$1.asJson(), StandardOpenOption.CREATE);
                infoWithFallback(() -> "Dumped recording summary to " + $$4);
            } catch (Throwable var4) {
                warnWithFallback(() -> "Failed to output JFR report", var4);
            }
        }
    }

    private static void infoWithFallback(Supplier<String> supplierString0) {
        if (LogUtils.isLoggerActive()) {
            LOGGER.info((String) supplierString0.get());
        } else {
            Bootstrap.realStdoutPrintln((String) supplierString0.get());
        }
    }

    private static void warnWithFallback(Supplier<String> supplierString0, Throwable throwable1) {
        if (LogUtils.isLoggerActive()) {
            LOGGER.warn((String) supplierString0.get(), throwable1);
        } else {
            Bootstrap.realStdoutPrintln((String) supplierString0.get());
            throwable1.printStackTrace(Bootstrap.STDOUT);
        }
    }
}
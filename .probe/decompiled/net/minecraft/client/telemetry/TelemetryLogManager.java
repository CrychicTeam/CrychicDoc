package net.minecraft.client.telemetry;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.util.eventlog.EventLogDirectory;
import org.slf4j.Logger;

public class TelemetryLogManager implements AutoCloseable {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String RAW_EXTENSION = ".json";

    private static final int EXPIRY_DAYS = 7;

    private final EventLogDirectory directory;

    @Nullable
    private CompletableFuture<Optional<TelemetryEventLog>> sessionLog;

    private TelemetryLogManager(EventLogDirectory eventLogDirectory0) {
        this.directory = eventLogDirectory0;
    }

    public static CompletableFuture<Optional<TelemetryLogManager>> open(Path path0) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                EventLogDirectory $$1 = EventLogDirectory.open(path0, ".json");
                $$1.listFiles().prune(LocalDate.now(), 7).compressAll();
                return Optional.of(new TelemetryLogManager($$1));
            } catch (Exception var2) {
                LOGGER.error("Failed to create telemetry log manager", var2);
                return Optional.empty();
            }
        }, Util.backgroundExecutor());
    }

    public CompletableFuture<Optional<TelemetryEventLogger>> openLogger() {
        if (this.sessionLog == null) {
            this.sessionLog = CompletableFuture.supplyAsync(() -> {
                try {
                    EventLogDirectory.RawFile $$0 = this.directory.createNewFile(LocalDate.now());
                    FileChannel $$1 = $$0.openChannel();
                    return Optional.of(new TelemetryEventLog($$1, Util.backgroundExecutor()));
                } catch (IOException var3) {
                    LOGGER.error("Failed to open channel for telemetry event log", var3);
                    return Optional.empty();
                }
            }, Util.backgroundExecutor());
        }
        return this.sessionLog.thenApply(p_262106_ -> p_262106_.map(TelemetryEventLog::m_261088_));
    }

    public void close() {
        if (this.sessionLog != null) {
            this.sessionLog.thenAccept(p_261871_ -> p_261871_.ifPresent(TelemetryEventLog::close));
        }
    }
}
package net.minecraft.client.telemetry;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.concurrent.Executor;
import net.minecraft.util.eventlog.JsonEventLog;
import net.minecraft.util.thread.ProcessorMailbox;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

public class TelemetryEventLog implements AutoCloseable {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final JsonEventLog<TelemetryEventInstance> log;

    private final ProcessorMailbox<Runnable> mailbox;

    public TelemetryEventLog(FileChannel fileChannel0, Executor executor1) {
        this.log = new JsonEventLog<>(TelemetryEventInstance.CODEC, fileChannel0);
        this.mailbox = ProcessorMailbox.create(executor1, "telemetry-event-log");
    }

    public TelemetryEventLogger logger() {
        return p_261508_ -> this.mailbox.tell(() -> {
            try {
                this.log.write(p_261508_);
            } catch (IOException var3) {
                LOGGER.error("Failed to write telemetry event to log", var3);
            }
        });
    }

    public void close() {
        this.mailbox.tell(() -> IOUtils.closeQuietly(this.log));
        this.mailbox.close();
    }
}
package net.minecraft.client.telemetry.events;

import java.time.Duration;
import javax.annotation.Nullable;
import net.minecraft.client.telemetry.TelemetryEventSender;
import net.minecraft.client.telemetry.TelemetryEventType;
import net.minecraft.client.telemetry.TelemetryProperty;

public class WorldLoadTimesEvent {

    private final boolean newWorld;

    @Nullable
    private final Duration worldLoadDuration;

    public WorldLoadTimesEvent(boolean boolean0, @Nullable Duration duration1) {
        this.worldLoadDuration = duration1;
        this.newWorld = boolean0;
    }

    public void send(TelemetryEventSender telemetryEventSender0) {
        if (this.worldLoadDuration != null) {
            telemetryEventSender0.send(TelemetryEventType.WORLD_LOAD_TIMES, p_261740_ -> {
                p_261740_.put(TelemetryProperty.WORLD_LOAD_TIME_MS, (int) this.worldLoadDuration.toMillis());
                p_261740_.put(TelemetryProperty.NEW_WORLD, this.newWorld);
            });
        }
    }
}
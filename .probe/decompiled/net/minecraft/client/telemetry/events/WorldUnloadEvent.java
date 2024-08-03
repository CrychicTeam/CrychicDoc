package net.minecraft.client.telemetry.events;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import net.minecraft.client.telemetry.TelemetryEventSender;
import net.minecraft.client.telemetry.TelemetryEventType;
import net.minecraft.client.telemetry.TelemetryProperty;

public class WorldUnloadEvent {

    private static final int NOT_TRACKING_TIME = -1;

    private Optional<Instant> worldLoadedTime = Optional.empty();

    private long totalTicks;

    private long lastGameTime;

    public void onPlayerInfoReceived() {
        this.lastGameTime = -1L;
        if (this.worldLoadedTime.isEmpty()) {
            this.worldLoadedTime = Optional.of(Instant.now());
        }
    }

    public void setTime(long long0) {
        if (this.lastGameTime != -1L) {
            this.totalTicks = this.totalTicks + Math.max(0L, long0 - this.lastGameTime);
        }
        this.lastGameTime = long0;
    }

    private int getTimeInSecondsSinceLoad(Instant instant0) {
        Duration $$1 = Duration.between(instant0, Instant.now());
        return (int) $$1.toSeconds();
    }

    public void send(TelemetryEventSender telemetryEventSender0) {
        this.worldLoadedTime.ifPresent(p_261953_ -> telemetryEventSender0.send(TelemetryEventType.WORLD_UNLOADED, p_261597_ -> {
            p_261597_.put(TelemetryProperty.SECONDS_SINCE_LOAD, this.getTimeInSecondsSinceLoad(p_261953_));
            p_261597_.put(TelemetryProperty.TICKS_SINCE_LOAD, (int) this.totalTicks);
        }));
    }
}
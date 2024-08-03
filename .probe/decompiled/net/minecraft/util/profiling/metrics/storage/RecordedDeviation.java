package net.minecraft.util.profiling.metrics.storage;

import java.time.Instant;
import net.minecraft.util.profiling.ProfileResults;

public final class RecordedDeviation {

    public final Instant timestamp;

    public final int tick;

    public final ProfileResults profilerResultAtTick;

    public RecordedDeviation(Instant instant0, int int1, ProfileResults profileResults2) {
        this.timestamp = instant0;
        this.tick = int1;
        this.profilerResultAtTick = profileResults2;
    }
}
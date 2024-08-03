package net.minecraftforge.event.level;

import net.minecraft.server.level.ServerLevel;

public class SleepFinishedTimeEvent extends LevelEvent {

    private long newTime;

    private final long minTime;

    public SleepFinishedTimeEvent(ServerLevel level, long newTime, long minTime) {
        super(level);
        this.newTime = newTime;
        this.minTime = minTime;
    }

    public long getNewTime() {
        return this.newTime;
    }

    public boolean setTimeAddition(long newTimeIn) {
        if (this.minTime > newTimeIn) {
            return false;
        } else {
            this.newTime = newTimeIn;
            return true;
        }
    }
}
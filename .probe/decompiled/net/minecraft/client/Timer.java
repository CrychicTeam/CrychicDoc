package net.minecraft.client;

public class Timer {

    public float partialTick;

    public float tickDelta;

    private long lastMs;

    private final float msPerTick;

    public Timer(float float0, long long1) {
        this.msPerTick = 1000.0F / float0;
        this.lastMs = long1;
    }

    public int advanceTime(long long0) {
        this.tickDelta = (float) (long0 - this.lastMs) / this.msPerTick;
        this.lastMs = long0;
        this.partialTick = this.partialTick + this.tickDelta;
        int $$1 = (int) this.partialTick;
        this.partialTick -= (float) $$1;
        return $$1;
    }
}
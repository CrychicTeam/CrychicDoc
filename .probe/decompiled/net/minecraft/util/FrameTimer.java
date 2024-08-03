package net.minecraft.util;

public class FrameTimer {

    public static final int LOGGING_LENGTH = 240;

    private final long[] loggedTimes = new long[240];

    private int logStart;

    private int logLength;

    private int logEnd;

    public void logFrameDuration(long long0) {
        this.loggedTimes[this.logEnd] = long0;
        this.logEnd++;
        if (this.logEnd == 240) {
            this.logEnd = 0;
        }
        if (this.logLength < 240) {
            this.logStart = 0;
            this.logLength++;
        } else {
            this.logStart = this.wrapIndex(this.logEnd + 1);
        }
    }

    public long getAverageDuration(int int0) {
        int $$1 = (this.logStart + int0) % 240;
        int $$2 = this.logStart;
        long $$3;
        for ($$3 = 0L; $$2 != $$1; $$2++) {
            $$3 += this.loggedTimes[$$2];
        }
        return $$3 / (long) int0;
    }

    public int scaleAverageDurationTo(int int0, int int1) {
        return this.scaleSampleTo(this.getAverageDuration(int0), int1, 60);
    }

    public int scaleSampleTo(long long0, int int1, int int2) {
        double $$3 = (double) long0 / (double) (1000000000L / (long) int2);
        return (int) ($$3 * (double) int1);
    }

    public int getLogStart() {
        return this.logStart;
    }

    public int getLogEnd() {
        return this.logEnd;
    }

    public int wrapIndex(int int0) {
        return int0 % 240;
    }

    public long[] getLog() {
        return this.loggedTimes;
    }
}
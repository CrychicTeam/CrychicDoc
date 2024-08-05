package icyllis.modernui.text;

public class Directions {

    static final int RUN_LENGTH_MASK = 67108863;

    static final int RUN_LEVEL_SHIFT = 26;

    static final int RUN_LEVEL_MASK = 63;

    static final int RUN_RTL_FLAG = 67108864;

    public static final Directions ALL_LEFT_TO_RIGHT = new Directions(new int[] { 0, 67108863 });

    public static final Directions ALL_RIGHT_TO_LEFT = new Directions(new int[] { 0, 134217727 });

    public int[] mDirections;

    public Directions(int[] dirs) {
        this.mDirections = dirs;
    }

    public int getRunCount() {
        return this.mDirections.length >> 1;
    }

    public int getRunStart(int runIndex) {
        return this.mDirections[runIndex << 1];
    }

    public int getRunLength(int runIndex) {
        return this.mDirections[(runIndex << 1) + 1] & 67108863;
    }

    public boolean isRunRtl(int runIndex) {
        return (this.mDirections[(runIndex << 1) + 1] & 67108864) != 0;
    }
}
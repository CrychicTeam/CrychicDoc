package net.minecraft.client.resources.metadata.animation;

public class AnimationFrame {

    public static final int UNKNOWN_FRAME_TIME = -1;

    private final int index;

    private final int time;

    public AnimationFrame(int int0) {
        this(int0, -1);
    }

    public AnimationFrame(int int0, int int1) {
        this.index = int0;
        this.time = int1;
    }

    public int getTime(int int0) {
        return this.time == -1 ? int0 : this.time;
    }

    public int getIndex() {
        return this.index;
    }
}
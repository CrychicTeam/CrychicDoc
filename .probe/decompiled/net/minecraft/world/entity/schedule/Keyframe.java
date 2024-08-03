package net.minecraft.world.entity.schedule;

public class Keyframe {

    private final int timeStamp;

    private final float value;

    public Keyframe(int int0, float float1) {
        this.timeStamp = int0;
        this.value = float1;
    }

    public int getTimeStamp() {
        return this.timeStamp;
    }

    public float getValue() {
        return this.value;
    }
}
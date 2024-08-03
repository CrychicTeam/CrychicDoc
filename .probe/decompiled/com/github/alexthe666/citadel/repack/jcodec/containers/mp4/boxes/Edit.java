package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

public class Edit {

    private long duration;

    private long mediaTime;

    private float rate;

    public static Edit createEdit(Edit edit) {
        return new Edit(edit.duration, edit.mediaTime, edit.rate);
    }

    public Edit(long duration, long mediaTime, float rate) {
        this.duration = duration;
        this.mediaTime = mediaTime;
        this.rate = rate;
    }

    public long getDuration() {
        return this.duration;
    }

    public long getMediaTime() {
        return this.mediaTime;
    }

    public float getRate() {
        return this.rate;
    }

    public void shift(long shift) {
        this.mediaTime += shift;
    }

    public void setMediaTime(long l) {
        this.mediaTime = l;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
package net.minecraft.world.entity;

import java.util.function.Consumer;
import net.minecraft.util.Mth;

public class AnimationState {

    private static final long STOPPED = Long.MAX_VALUE;

    private long lastTime = Long.MAX_VALUE;

    private long accumulatedTime;

    public void start(int int0) {
        this.lastTime = (long) int0 * 1000L / 20L;
        this.accumulatedTime = 0L;
    }

    public void startIfStopped(int int0) {
        if (!this.isStarted()) {
            this.start(int0);
        }
    }

    public void animateWhen(boolean boolean0, int int1) {
        if (boolean0) {
            this.startIfStopped(int1);
        } else {
            this.stop();
        }
    }

    public void stop() {
        this.lastTime = Long.MAX_VALUE;
    }

    public void ifStarted(Consumer<AnimationState> consumerAnimationState0) {
        if (this.isStarted()) {
            consumerAnimationState0.accept(this);
        }
    }

    public void updateTime(float float0, float float1) {
        if (this.isStarted()) {
            long $$2 = Mth.lfloor((double) (float0 * 1000.0F / 20.0F));
            this.accumulatedTime = this.accumulatedTime + (long) ((float) ($$2 - this.lastTime) * float1);
            this.lastTime = $$2;
        }
    }

    public long getAccumulatedTime() {
        return this.accumulatedTime;
    }

    public boolean isStarted() {
        return this.lastTime != Long.MAX_VALUE;
    }
}
package dev.xkmc.l2library.base.overlay;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;

public abstract class SideBar<S extends SideBar.Signature<S>> {

    protected final float max_time;

    protected final float max_ease;

    @Nullable
    protected S prev;

    protected float idle = 0.0F;

    protected float ease_time = 0.0F;

    protected float prev_time = -1.0F;

    public SideBar(float duration, float ease) {
        this.max_time = duration;
        this.max_ease = ease;
    }

    public abstract S getSignature();

    public abstract boolean isScreenOn();

    protected boolean isOnHold() {
        return Minecraft.getInstance().options.keyShift.isDown();
    }

    protected boolean ease(float current_time) {
        if (!this.isScreenOn()) {
            this.prev = null;
            this.idle = this.max_time;
            this.ease_time = 0.0F;
            this.prev_time = -1.0F;
            return false;
        } else {
            float time_diff = this.prev_time < 0.0F ? 0.0F : current_time - this.prev_time;
            this.prev_time = current_time;
            S signature = this.getSignature();
            if (!signature.shouldRefreshIdle(this, this.prev) && !this.isOnHold()) {
                this.idle += time_diff;
            } else {
                this.idle = 0.0F;
            }
            this.prev = signature;
            if (this.idle < this.max_time) {
                if (this.ease_time < this.max_ease) {
                    this.ease_time += time_diff;
                    if (this.ease_time > this.max_ease) {
                        this.ease_time = this.max_ease;
                    }
                }
            } else if (this.ease_time > 0.0F) {
                this.ease_time -= time_diff;
                if (this.ease_time < 0.0F) {
                    this.ease_time = 0.0F;
                }
            }
            return this.ease_time > 0.0F;
        }
    }

    public boolean isRendering() {
        return this.isScreenOn() && this.ease_time > 0.0F;
    }

    protected int getXOffset(int width) {
        return 0;
    }

    protected int getYOffset(int height) {
        return 0;
    }

    public static record IntSignature(int val) implements SideBar.Signature<SideBar.IntSignature> {

        public boolean shouldRefreshIdle(SideBar<?> sideBar, @Nullable SideBar.IntSignature old) {
            return old == null || this.val != old.val;
        }
    }

    public interface Signature<S extends SideBar.Signature<S>> {

        boolean shouldRefreshIdle(SideBar<?> var1, @Nullable S var2);
    }
}
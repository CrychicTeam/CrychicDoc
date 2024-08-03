package icyllis.modernui.mc;

import icyllis.modernui.animation.TimeInterpolator;
import javax.annotation.Nonnull;
import net.minecraft.util.Mth;

public class ScrollController {

    private float startValue;

    private float targetValue;

    private float maxValue;

    private float currValue;

    private long startTime;

    private int duration;

    @Nonnull
    private final ScrollController.IListener listener;

    public ScrollController(@Nonnull ScrollController.IListener listener) {
        this.listener = listener;
    }

    public void update(long time) {
        if (this.currValue != this.targetValue) {
            float p = Math.min((float) (time - this.startTime) / (float) this.duration, 1.0F);
            p = TimeInterpolator.DECELERATE.getInterpolation(p);
            this.currValue = Mth.lerp(p, this.startValue, this.targetValue);
            this.listener.onScrollAmountUpdated(this, this.currValue);
        }
    }

    public void setMaxScroll(float max) {
        this.maxValue = max;
    }

    public void setStartValue(float start) {
        this.startValue = start;
    }

    public float getCurrValue() {
        return this.currValue;
    }

    public int getDuration() {
        return this.duration;
    }

    public void scrollBy(float delta, int duration) {
        this.scrollTo(this.targetValue + delta, duration);
    }

    public boolean scrollBy(float delta) {
        return this.scrollTo(this.targetValue + delta);
    }

    public void scrollTo(float target, int duration) {
        this.startTime = UIManager.getElapsedTime();
        this.startValue = this.currValue;
        this.targetValue = Mth.clamp(target, 0.0F, this.maxValue);
        this.duration = duration;
    }

    public boolean scrollTo(float target) {
        float lastTime = (float) this.startTime;
        this.startTime = UIManager.getElapsedTime();
        this.startValue = this.currValue;
        this.targetValue = Mth.clamp(target, 0.0F, this.maxValue);
        if (this.startValue == this.targetValue) {
            return false;
        } else {
            float dis = Math.abs(this.targetValue - this.currValue);
            if ((double) dis > 120.0) {
                this.duration = (int) (Math.sqrt((double) dis / 120.0) * 200.0);
            } else {
                this.duration = 200;
            }
            float det = (float) this.startTime - lastTime;
            if ((double) det < 120.0) {
                this.duration = (int) ((float) this.duration * (det / 600.0F + 0.8F));
            }
            return true;
        }
    }

    public void abortAnimation() {
        this.currValue = this.targetValue;
        this.listener.onScrollAmountUpdated(this, this.currValue);
    }

    public boolean isScrolling() {
        return this.currValue != this.targetValue;
    }

    @FunctionalInterface
    public interface IListener {

        void onScrollAmountUpdated(ScrollController var1, float var2);
    }
}
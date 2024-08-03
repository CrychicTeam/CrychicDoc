package icyllis.modernui.animation;

public class AnticipateOvershootInterpolator implements TimeInterpolator {

    private final float mTension;

    public AnticipateOvershootInterpolator() {
        this.mTension = 3.0F;
    }

    public AnticipateOvershootInterpolator(float tension) {
        this.mTension = tension * 1.5F;
    }

    public AnticipateOvershootInterpolator(float tension, float extraTension) {
        this.mTension = tension * extraTension;
    }

    private static float a(float t, float s) {
        return t * t * ((s + 1.0F) * t - s);
    }

    private static float o(float t, float s) {
        return t * t * ((s + 1.0F) * t + s);
    }

    @Override
    public float getInterpolation(float t) {
        return t < 0.5F ? 0.5F * a(t * 2.0F, this.mTension) : 0.5F * (o(t * 2.0F - 2.0F, this.mTension) + 2.0F);
    }
}
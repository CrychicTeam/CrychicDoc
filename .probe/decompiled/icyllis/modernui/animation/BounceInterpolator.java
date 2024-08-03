package icyllis.modernui.animation;

public class BounceInterpolator implements TimeInterpolator {

    BounceInterpolator() {
    }

    private static float bounce(float t) {
        return t * t * 8.0F;
    }

    @Override
    public float getInterpolation(float t) {
        t *= 1.1226F;
        if (t < 0.3535F) {
            return bounce(t);
        } else if (t < 0.7408F) {
            return bounce(t - 0.54719F) + 0.7F;
        } else {
            return t < 0.9644F ? bounce(t - 0.8526F) + 0.9F : bounce(t - 1.0435F) + 0.95F;
        }
    }
}
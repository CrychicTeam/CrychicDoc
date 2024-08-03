package icyllis.modernui.animation;

import icyllis.modernui.graphics.MathUtil;
import javax.annotation.Nonnull;

public final class ColorEvaluator implements TypeEvaluator<Integer> {

    private static final ColorEvaluator sInstance = new ColorEvaluator();

    private ColorEvaluator() {
    }

    @Nonnull
    public static ColorEvaluator getInstance() {
        return sInstance;
    }

    public static int evaluate(float fraction, int startValue, int endValue) {
        float startA = (float) (startValue >>> 24) / 255.0F;
        float startR = (float) (startValue >> 16 & 0xFF) / 255.0F;
        float startG = (float) (startValue >> 8 & 0xFF) / 255.0F;
        float startB = (float) (startValue & 0xFF) / 255.0F;
        float endA = (float) (endValue >>> 24) / 255.0F;
        float endR = (float) (endValue >> 16 & 0xFF) / 255.0F;
        float endG = (float) (endValue >> 8 & 0xFF) / 255.0F;
        float endB = (float) (endValue & 0xFF) / 255.0F;
        startR = (float) Math.pow((double) startR, 2.2);
        startG = (float) Math.pow((double) startG, 2.2);
        startB = (float) Math.pow((double) startB, 2.2);
        endR = (float) Math.pow((double) endR, 2.2);
        endG = (float) Math.pow((double) endG, 2.2);
        endB = (float) Math.pow((double) endB, 2.2);
        float a = MathUtil.lerp(startA, endA, fraction);
        float r = MathUtil.lerp(startR, endR, fraction);
        float g = MathUtil.lerp(startG, endG, fraction);
        float b = MathUtil.lerp(startB, endB, fraction);
        a *= 255.0F;
        r = (float) Math.pow((double) r, 0.45454545454545453) * 255.0F;
        g = (float) Math.pow((double) g, 0.45454545454545453) * 255.0F;
        b = (float) Math.pow((double) b, 0.45454545454545453) * 255.0F;
        return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);
    }

    @Nonnull
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        return evaluate(fraction, startValue.intValue(), endValue.intValue());
    }
}
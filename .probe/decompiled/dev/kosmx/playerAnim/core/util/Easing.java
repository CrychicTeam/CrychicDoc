package dev.kosmx.playerAnim.core.util;

import org.jetbrains.annotations.Nullable;

public class Easing {

    private static final float c1 = 1.70158F;

    private static final float c2 = 2.5949094F;

    private static final float c3 = 2.70158F;

    private static final float c4 = (float) (Math.PI * 2.0 / 3.0);

    private static final float c5 = (float) (Math.PI * 4.0 / 9.0);

    private static final float n1 = 7.5625F;

    private static final float d1 = 2.75F;

    @Deprecated
    public static float easingFromEnum(@Nullable Ease type, float f) {
        return type != null ? type.invoke(f) : f;
    }

    public static Ease easeFromString(String string) {
        try {
            if (string.equals("step")) {
                return Ease.CONSTANT;
            } else {
                if (string.substring(0, 4).equalsIgnoreCase("EASE")) {
                    string = string.substring(4);
                }
                return Ease.valueOf(string.toUpperCase());
            }
        } catch (Exception var2) {
            return Ease.LINEAR;
        }
    }

    public static float inSine(float f) {
        return (float) (1.0 - Math.cos((double) f * Math.PI / 2.0));
    }

    public static float outSine(float f) {
        return (float) Math.sin((double) f * Math.PI / 2.0);
    }

    public static float inOutSine(float f) {
        return (float) (-(Math.cos(Math.PI * (double) f) - 1.0) / 2.0);
    }

    public static float inCubic(float f) {
        return f * f * f;
    }

    public static float outCubic(float f) {
        return (float) (1.0 - Math.pow((double) (1.0F - f), 3.0));
    }

    public static float inOutCubic(float x) {
        return (float) ((double) x < 0.5 ? (double) (4.0F * x * x * x) : 1.0 - Math.pow((double) (-2.0F * x + 2.0F), 3.0) / 2.0);
    }

    public static float inQuad(float x) {
        return x * x;
    }

    public static float outQuad(float x) {
        return 1.0F - (1.0F - x) * (1.0F - x);
    }

    public static float inOutQuad(float x) {
        return (float) ((double) x < 0.5 ? (double) (2.0F * x * x) : 1.0 - Math.pow((double) (-2.0F * x + 2.0F), 2.0) / 2.0);
    }

    public static float inQuart(float x) {
        return x * x * x * x;
    }

    public static float outQuart(float x) {
        return (float) (1.0 - Math.pow((double) (1.0F - x), 4.0));
    }

    public static float inOutQuart(float x) {
        return (float) ((double) x < 0.5 ? (double) (8.0F * x * x * x * x) : 1.0 - Math.pow((double) (-2.0F * x + 2.0F), 4.0) / 2.0);
    }

    public static float inQuint(float x) {
        return x * x * x * x * x;
    }

    public static float outQuint(float x) {
        return (float) (1.0 - Math.pow((double) (1.0F - x), 5.0));
    }

    public static float inOutQuint(float x) {
        return (float) ((double) x < 0.5 ? (double) (16.0F * x * x * x * x * x) : 1.0 - Math.pow((double) (-2.0F * x + 2.0F), 5.0) / 2.0);
    }

    public static float inExpo(float x) {
        return (float) (x == 0.0F ? 0.0 : Math.pow(2.0, (double) (10.0F * x - 10.0F)));
    }

    public static float outExpo(float x) {
        return (float) (x == 1.0F ? 1.0 : 1.0 - Math.pow(2.0, (double) (-10.0F * x)));
    }

    public static float inOutExpo(float x) {
        return (float) (x == 0.0F ? 0.0 : (x == 1.0F ? 1.0 : ((double) x < 0.5 ? Math.pow(2.0, (double) (20.0F * x - 10.0F)) / 2.0 : (2.0 - Math.pow(2.0, (double) (-20.0F * x + 10.0F))) / 2.0)));
    }

    public static float inCirc(float x) {
        return (float) (1.0 - Math.sqrt(1.0 - Math.pow((double) x, 2.0)));
    }

    public static float outCirc(float x) {
        return (float) Math.sqrt(1.0 - Math.pow((double) (x - 1.0F), 2.0));
    }

    public static float inOutCirc(float x) {
        return (float) ((double) x < 0.5 ? (1.0 - Math.sqrt(1.0 - Math.pow((double) (2.0F * x), 2.0))) / 2.0 : (Math.sqrt(1.0 - Math.pow((double) (-2.0F * x + 2.0F), 2.0)) + 1.0) / 2.0);
    }

    public static float inBack(float x) {
        return 2.70158F * x * x * x - 1.70158F * x * x;
    }

    public static float outBack(float x) {
        return (float) (1.0 + 2.70158F * Math.pow((double) (x - 1.0F), 3.0) + 1.70158F * Math.pow((double) (x - 1.0F), 2.0));
    }

    public static float inOutBack(float x) {
        return (float) ((double) x < 0.5 ? Math.pow((double) (2.0F * x), 2.0) * (double) (7.189819F * x - 2.5949094F) / 2.0 : (Math.pow((double) (2.0F * x - 2.0F), 2.0) * (double) (3.5949094F * (x * 2.0F - 2.0F) + 2.5949094F) + 2.0) / 2.0);
    }

    public static float inElastic(float x) {
        return (float) (x == 0.0F ? 0.0 : (x == 1.0F ? 1.0 : -Math.pow(2.0, (double) (10.0F * x - 10.0F)) * Math.sin(((double) (x * 10.0F) - 10.75) * (float) (Math.PI * 2.0 / 3.0))));
    }

    public static float outElastic(float x) {
        return (float) (x == 0.0F ? 0.0 : (x == 1.0F ? 1.0 : Math.pow(2.0, (double) (-10.0F * x)) * Math.sin(((double) (x * 10.0F) - 0.75) * (float) (Math.PI * 2.0 / 3.0)) + 1.0));
    }

    public static float inOutElastic(float x) {
        return (float) (x == 0.0F ? 0.0 : (x == 1.0F ? 1.0 : ((double) x < 0.5 ? -(Math.pow(2.0, (double) (20.0F * x - 10.0F)) * Math.sin(((double) (20.0F * x) - 11.125) * (float) (Math.PI * 4.0 / 9.0))) / 2.0 : Math.pow(2.0, (double) (-20.0F * x + 10.0F)) * Math.sin(((double) (20.0F * x) - 11.125) * (float) (Math.PI * 4.0 / 9.0)) / 2.0 + 1.0)));
    }

    public static float inBounce(float x) {
        return 1.0F - outBounce(1.0F - x);
    }

    public static float outBounce(float x) {
        if (x < 0.36363637F) {
            return 7.5625F * x * x;
        } else if (x < 0.72727275F) {
            float var3;
            return (float) ((double) (7.5625F * (var3 = (float) ((double) x - 0.5454545454545454)) * var3) + 0.75);
        } else {
            float var1;
            float var2;
            return (double) x < 0.9090909090909091 ? (float) ((double) (7.5625F * (var1 = (float) ((double) x - 0.8181818181818182)) * var1) + 0.9375) : (float) ((double) (7.5625F * (var2 = (float) ((double) x - 0.9545454545454546)) * var2) + 0.984375);
        }
    }

    public static float inOutBounce(float x) {
        return (double) x < 0.5 ? (1.0F - outBounce(1.0F - 2.0F * x)) / 2.0F : (1.0F + outBounce(2.0F * x - 1.0F)) / 2.0F;
    }
}
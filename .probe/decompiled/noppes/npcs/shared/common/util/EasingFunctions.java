package noppes.npcs.shared.common.util;

public class EasingFunctions {

    public static float easeInCubic(float x) {
        return x * x * x;
    }

    public static float easeOutCubic(float x) {
        return 1.0F - (float) Math.pow((double) (1.0F - x), 3.0);
    }

    public static float easeInOutCubic(float x) {
        return (double) x < 0.5 ? 4.0F * x * x * x : 1.0F - (float) Math.pow((double) (-2.0F * x + 2.0F), 3.0) / 2.0F;
    }

    public static float easeInOutQuad(float x) {
        return (double) x < 0.5 ? 2.0F * x * x : 1.0F - (float) Math.pow((double) (-2.0F * x + 2.0F), 2.0) / 2.0F;
    }

    public static float easeInOutQuart(float x) {
        return (double) x < 0.5 ? 8.0F * x * x * x * x : 1.0F - (float) Math.pow((double) (-2.0F * x + 2.0F), 4.0) / 2.0F;
    }
}
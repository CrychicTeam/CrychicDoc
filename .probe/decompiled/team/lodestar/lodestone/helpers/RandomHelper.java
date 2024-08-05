package team.lodestar.lodestone.helpers;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import team.lodestar.lodestone.systems.easing.Easing;

public class RandomHelper {

    public static float weightedEasingLerp(Easing easing, float pDelta, float pStart, float pEnd) {
        float distanceFromMiddle = Mth.abs(0.5F - pDelta) / 0.5F;
        float middleBasedDelta = easing.ease(1.0F - distanceFromMiddle, 0.0F, 1.0F, 1.0F);
        float pMiddle = (pStart + pEnd) / 2.0F;
        return pDelta < 0.5F ? Mth.lerp(middleBasedDelta, pStart, pMiddle) : Mth.lerp(1.0F - middleBasedDelta, pMiddle, pEnd);
    }

    public static float interpolateWithEasing(Easing easing, double pDelta, double pStart, double pEnd) {
        return weightedEasingLerp(easing, (float) pDelta, (float) pStart, (float) pEnd);
    }

    public static int randomBetween(RandomSource pRandom, int min, int max) {
        return randomBetween(pRandom, Easing.SINE_IN_OUT, min, max);
    }

    public static int randomBetween(RandomSource pRandom, Easing easing, int min, int max) {
        return Math.round(EasingHelper.weightedEasingLerp(easing, pRandom.nextFloat(), (float) min, (float) max));
    }

    public static float randomBetween(RandomSource pRandom, float min, float max) {
        return randomBetween(pRandom, Easing.SINE_IN_OUT, min, max);
    }

    public static float randomBetween(RandomSource pRandom, Easing easing, float min, float max) {
        return EasingHelper.weightedEasingLerp(easing, pRandom.nextFloat(), min, max);
    }

    public static double randomBetween(RandomSource pRandom, double min, double max) {
        return randomBetween(pRandom, Easing.SINE_IN_OUT, min, max);
    }

    public static double randomBetween(RandomSource pRandom, Easing easing, double min, double max) {
        return (double) EasingHelper.weightedEasingLerp(easing, (double) pRandom.nextFloat(), min, max);
    }
}
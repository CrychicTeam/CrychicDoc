package team.lodestar.lodestone.helpers;

import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.easing.Easing;

public class EasingHelper {

    public static float weightedEasingLerp(Easing easing, float pDelta, float pStart, float pMiddle, float pEnd) {
        float distanceFromMiddle = Mth.abs(0.5F - pDelta) / 0.5F;
        float middleBasedDelta = easing.ease(1.0F - distanceFromMiddle, 0.0F, 1.0F, 1.0F);
        return pDelta < 0.5F ? Mth.lerp(middleBasedDelta, pStart, pMiddle) : Mth.lerp(1.0F - middleBasedDelta, pMiddle, pEnd);
    }

    public static float weightedEasingLerp(Easing easing, float pDelta, float pStart, float pEnd) {
        return weightedEasingLerp(easing, pDelta, pStart, (pStart + pEnd) / 2.0F, pEnd);
    }

    public static float weightedEasingLerp(Easing easing, double pDelta, double pStart, double pEnd) {
        return weightedEasingLerp(easing, (float) pDelta, (float) pStart, (float) pEnd);
    }
}
package fabric.me.thosea.badoptimizations.other;

import java.util.Objects;
import net.minecraft.class_310;
import net.minecraft.class_638;

public final class CommonColorFactors {

    private static final class_310 CLIENT = (class_310) Objects.requireNonNull(class_310.method_1551(), "loaded too early");

    public static final CommonColorFactors SKY_COLOR = new CommonColorFactors();

    public static final CommonColorFactors LIGHTMAP = new CommonColorFactors();

    private static int lastUpdateTick = -1;

    public static float lastRainGradient;

    public static float lastThunderGradient;

    public static int lastLightningTicks;

    public static float rainGradientMultiplier;

    public static float thunderGradientMultiplier;

    private boolean thisInstanceDirty;

    private boolean didTickChange;

    private long lastTime;

    private CommonColorFactors() {
    }

    public static void tick(float tickDelta) {
        int tick = CLIENT.field_1724.field_6012;
        if (lastUpdateTick != tick) {
            lastUpdateTick = tick;
            class_638 world = CLIENT.field_1687;
            boolean result = false;
            float rainGradient = world.method_8430(tickDelta);
            float thunderGradient = world.method_8478(tickDelta);
            int lightningTicks = world.method_23789();
            if (rainGradient != lastRainGradient) {
                result = true;
                lastRainGradient = rainGradient;
                if (rainGradient > 0.0F) {
                    rainGradientMultiplier = 1.0F - rainGradient * 0.75F;
                } else {
                    rainGradientMultiplier = 0.0F;
                }
            }
            if (thunderGradient != lastThunderGradient) {
                result = true;
                lastThunderGradient = thunderGradient;
                if (thunderGradient > 0.0F) {
                    thunderGradientMultiplier = 1.0F - thunderGradient * 0.75F;
                } else {
                    thunderGradientMultiplier = 0.0F;
                }
            }
            if (lastLightningTicks != lightningTicks) {
                result = true;
                lastLightningTicks = lightningTicks;
            }
            SKY_COLOR.didTickChange = true;
            LIGHTMAP.didTickChange = true;
            if (result) {
                SKY_COLOR.thisInstanceDirty = true;
                LIGHTMAP.thisInstanceDirty = true;
            }
        }
    }

    public boolean isDirty() {
        if (this.thisInstanceDirty) {
            this.thisInstanceDirty = false;
            return true;
        } else {
            return false;
        }
    }

    public boolean didTickChange() {
        if (this.didTickChange) {
            this.didTickChange = false;
            return true;
        } else {
            return false;
        }
    }

    public long getTimeDelta() {
        return Math.abs(CLIENT.field_1687.method_30271() - this.lastTime);
    }

    public void updateLastTime() {
        this.lastTime = CLIENT.field_1687.method_30271();
    }
}
package forge.me.thosea.badoptimizations.other;

import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public final class CommonColorFactors {

    private static final Minecraft CLIENT = (Minecraft) Objects.requireNonNull(Minecraft.getInstance(), "loaded too early");

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
        int tick = CLIENT.player.f_19797_;
        if (lastUpdateTick != tick) {
            lastUpdateTick = tick;
            ClientLevel world = CLIENT.level;
            boolean result = false;
            float rainGradient = world.m_46722_(tickDelta);
            float thunderGradient = world.m_46661_(tickDelta);
            int lightningTicks = world.getSkyFlashTime();
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
        return Math.abs(CLIENT.level.m_8044_() - this.lastTime);
    }

    public void updateLastTime() {
        this.lastTime = CLIENT.level.m_8044_();
    }
}
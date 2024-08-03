package vazkii.patchouli.client.base;

import net.minecraft.client.Minecraft;

public final class ClientTicker {

    public static long ticksInGame = 0L;

    public static float partialTicks = 0.0F;

    public static float delta = 0.0F;

    public static float total = 0.0F;

    private static void calcDelta() {
        float oldTotal = total;
        total = (float) ticksInGame + partialTicks;
        delta = total - oldTotal;
    }

    public static void renderTickStart(float pt) {
        partialTicks = pt;
    }

    public static void renderTickEnd() {
        calcDelta();
    }

    public static void endClientTick(Minecraft mc) {
        ticksInGame++;
        partialTicks = 0.0F;
        calcDelta();
    }
}
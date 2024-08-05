package net.minecraft.client.gui.components;

import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class SplashRenderer {

    public static final SplashRenderer CHRISTMAS = new SplashRenderer("Merry X-mas!");

    public static final SplashRenderer NEW_YEAR = new SplashRenderer("Happy new year!");

    public static final SplashRenderer HALLOWEEN = new SplashRenderer("OOoooOOOoooo! Spooky!");

    private static final int WIDTH_OFFSET = 123;

    private static final int HEIGH_OFFSET = 69;

    private final String splash;

    public SplashRenderer(String string0) {
        this.splash = string0;
    }

    public void render(GuiGraphics guiGraphics0, int int1, Font font2, int int3) {
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate((float) int1 / 2.0F + 123.0F, 69.0F, 0.0F);
        guiGraphics0.pose().mulPose(Axis.ZP.rotationDegrees(-20.0F));
        float $$4 = 1.8F - Mth.abs(Mth.sin((float) (Util.getMillis() % 1000L) / 1000.0F * (float) (Math.PI * 2)) * 0.1F);
        $$4 = $$4 * 100.0F / (float) (font2.width(this.splash) + 32);
        guiGraphics0.pose().scale($$4, $$4, $$4);
        guiGraphics0.drawCenteredString(font2, this.splash, 0, -8, 16776960 | int3);
        guiGraphics0.pose().popPose();
    }
}
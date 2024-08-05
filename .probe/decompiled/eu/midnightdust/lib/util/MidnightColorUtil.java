package eu.midnightdust.lib.util;

import java.awt.Color;

public class MidnightColorUtil {

    public static float hue;

    public static void tick() {
        if (hue > 1.0F) {
            hue = 0.0F;
        }
        hue += 0.01F;
    }

    public static Color hex2Rgb(String colorStr) {
        try {
            return Color.decode("#" + colorStr.replace("#", ""));
        } catch (Exception var2) {
            return Color.BLACK;
        }
    }

    public static Color radialRainbow(float saturation, float brightness) {
        return Color.getHSBColor(hue, saturation, brightness);
    }
}
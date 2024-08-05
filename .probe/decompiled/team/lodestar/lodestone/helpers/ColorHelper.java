package team.lodestar.lodestone.helpers;

import java.awt.Color;
import java.util.List;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.easing.Easing;

public class ColorHelper {

    public static Color getColor(int decimal) {
        return new Color(decimal);
    }

    public static void RGBToHSV(Color color, float[] hsv) {
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);
    }

    public static int getColor(Color color) {
        return FastColor.ARGB32.color(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
    }

    public static int getColor(int r, int g, int b) {
        return FastColor.ARGB32.color(255, r, g, b);
    }

    public static int getColor(int r, int g, int b, int a) {
        return FastColor.ARGB32.color(a, r, g, b);
    }

    public static int getColor(float r, float g, float b, float a) {
        return FastColor.ARGB32.color((int) (a * 255.0F), (int) (r * 255.0F), (int) (g * 255.0F), (int) (b * 255.0F));
    }

    public static Color colorLerp(Easing easing, float pct, Color startColor, Color endColor) {
        pct = Mth.clamp(pct, 0.0F, 1.0F);
        int br = startColor.getRed();
        int bg = startColor.getGreen();
        int bb = startColor.getBlue();
        int dr = endColor.getRed();
        int dg = endColor.getGreen();
        int db = endColor.getBlue();
        float ease = easing.ease(pct, 0.0F, 1.0F, 1.0F);
        int red = (int) Mth.lerp(ease, (float) br, (float) dr);
        int green = (int) Mth.lerp(ease, (float) bg, (float) dg);
        int blue = (int) Mth.lerp(ease, (float) bb, (float) db);
        return new Color(Mth.clamp(red, 0, 255), Mth.clamp(green, 0, 255), Mth.clamp(blue, 0, 255));
    }

    public static Color colorLerp(Easing easing, float pct, float min, float max, Color startColor, Color endColor) {
        pct = Mth.clamp(pct, 0.0F, 1.0F);
        int br = startColor.getRed();
        int bg = startColor.getGreen();
        int bb = startColor.getBlue();
        int dr = endColor.getRed();
        int dg = endColor.getGreen();
        int db = endColor.getBlue();
        float ease = easing.ease(pct, min, max, 1.0F);
        int red = (int) Mth.lerp(ease, (float) br, (float) dr);
        int green = (int) Mth.lerp(ease, (float) bg, (float) dg);
        int blue = (int) Mth.lerp(ease, (float) bb, (float) db);
        return new Color(Mth.clamp(red, 0, 255), Mth.clamp(green, 0, 255), Mth.clamp(blue, 0, 255));
    }

    public static Color multicolorLerp(Easing easing, float pct, Color... colors) {
        return multicolorLerp(easing, pct, List.of(colors));
    }

    public static Color multicolorLerp(Easing easing, float pct, List<Color> colors) {
        pct = Mth.clamp(pct, 0.0F, 1.0F);
        int colorCount = colors.size() - 1;
        float lerp = easing.ease(pct, 0.0F, 1.0F, 1.0F);
        float colorIndex = (float) colorCount * lerp;
        int index = (int) Mth.clamp(colorIndex, 0.0F, (float) colorCount);
        Color color = (Color) colors.get(index);
        Color nextColor = index == colorCount ? color : (Color) colors.get(index + 1);
        return colorLerp(easing, colorIndex - (float) ((int) colorIndex), color, nextColor);
    }

    public static Color multicolorLerp(Easing easing, float pct, float min, float max, Color... colors) {
        return multicolorLerp(easing, pct, min, max, List.of(colors));
    }

    public static Color multicolorLerp(Easing easing, float pct, float min, float max, List<Color> colors) {
        pct = Mth.clamp(pct, 0.0F, 1.0F);
        int colorCount = colors.size() - 1;
        float lerp = easing.ease(pct, 0.0F, 1.0F, 1.0F);
        float colorIndex = (float) colorCount * lerp;
        int index = (int) Mth.clamp(colorIndex, 0.0F, (float) colorCount);
        Color color = (Color) colors.get(index);
        Color nextColor = index == colorCount ? color : (Color) colors.get(index + 1);
        return colorLerp(easing, colorIndex - (float) ((int) colorIndex), min, max, nextColor, color);
    }

    public static Color darker(Color color, int times) {
        return darker(color, times, 0.7F);
    }

    public static Color darker(Color color, int power, float factor) {
        float FACTOR = (float) Math.pow((double) factor, (double) power);
        return new Color(Math.max((int) ((float) color.getRed() * FACTOR), 0), Math.max((int) ((float) color.getGreen() * FACTOR), 0), Math.max((int) ((float) color.getBlue() * FACTOR), 0), color.getAlpha());
    }

    public static Color brighter(Color color, int power) {
        return brighter(color, power, 0.7F);
    }

    public static Color brighter(Color color, int power, float factor) {
        float FACTOR = (float) Math.pow((double) factor, (double) power);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();
        int i = (int) (1.0 / (1.0 - (double) FACTOR));
        if (r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        } else {
            if (r > 0 && r < i) {
                r = i;
            }
            if (g > 0 && g < i) {
                g = i;
            }
            if (b > 0 && b < i) {
                b = i;
            }
            return new Color(Math.min((int) ((float) r / FACTOR), 255), Math.min((int) ((float) g / FACTOR), 255), Math.min((int) ((float) b / FACTOR), 255), alpha);
        }
    }
}
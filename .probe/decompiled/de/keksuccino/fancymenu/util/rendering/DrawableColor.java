package de.keksuccino.fancymenu.util.rendering;

import java.awt.Color;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DrawableColor {

    public static final DrawableColor EMPTY = of(new Color(255, 255, 255));

    public static final DrawableColor WHITE = of(new Color(255, 255, 255));

    public static final DrawableColor BLACK = of(new Color(0, 0, 0));

    protected Color color;

    protected int colorInt;

    protected String hex;

    @NotNull
    public static DrawableColor of(@NotNull Color color) {
        Objects.requireNonNull(color);
        DrawableColor c = new DrawableColor();
        c.color = color;
        c.colorInt = color.getRGB();
        c.hex = convertColorToHexString(color);
        if (c.hex == null) {
            c.hex = "#ffffffff";
        }
        return c;
    }

    @NotNull
    public static DrawableColor of(@NotNull String hex) {
        Objects.requireNonNull(hex);
        hex = hex.replace(" ", "");
        if (!hex.startsWith("#")) {
            hex = "#" + hex;
        }
        DrawableColor c = new DrawableColor();
        c.color = convertHexStringToColor(hex);
        if (c.color == null) {
            return EMPTY;
        } else {
            c.colorInt = c.color.getRGB();
            c.hex = hex;
            return c;
        }
    }

    @NotNull
    public static DrawableColor of(int r, int g, int b) {
        return of(r, g, b, 255);
    }

    @NotNull
    public static DrawableColor of(int r, int g, int b, int a) {
        DrawableColor c = new DrawableColor();
        try {
            c.color = new Color(r, g, b, a);
        } catch (Exception var6) {
            var6.printStackTrace();
            return EMPTY;
        }
        c.colorInt = c.color.getRGB();
        c.hex = convertColorToHexString(c.color);
        return c.hex != null ? c : EMPTY;
    }

    protected DrawableColor() {
    }

    @NotNull
    public Color getColor() {
        return this.color;
    }

    public int getColorInt() {
        return this.colorInt;
    }

    public int getColorIntWithAlpha(float alpha) {
        return RenderingUtils.replaceAlphaInColor(this.colorInt, alpha);
    }

    @NotNull
    public String getHex() {
        return this.hex == null ? "#ffffffff" : this.hex;
    }

    public DrawableColor copy() {
        DrawableColor c = new DrawableColor();
        c.color = this.color;
        c.colorInt = this.colorInt;
        c.hex = this.hex;
        return c;
    }

    @Nullable
    protected static Color convertHexStringToColor(@NotNull String hex) {
        try {
            hex = hex.replace("#", "");
            if (hex.length() == 6) {
                return new Color(Integer.valueOf(hex.substring(0, 2), 16), Integer.valueOf(hex.substring(2, 4), 16), Integer.valueOf(hex.substring(4, 6), 16));
            }
            if (hex.length() == 8) {
                return new Color(Integer.valueOf(hex.substring(0, 2), 16), Integer.valueOf(hex.substring(2, 4), 16), Integer.valueOf(hex.substring(4, 6), 16), Integer.valueOf(hex.substring(6, 8), 16));
            }
        } catch (Exception var2) {
        }
        return null;
    }

    @Nullable
    protected static String convertColorToHexString(@NotNull Color color) {
        try {
            return String.format("#%02X%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        } catch (Exception var2) {
            return null;
        }
    }
}
package me.shedaniel.math;

public final class Color {

    private final int a;

    private Color(int var1) {
        this.a = var1;
    }

    public static Color ofTransparent(int var0) {
        return new Color(var0);
    }

    public static Color ofOpaque(int var0) {
        return new Color(0xFF000000 | var0);
    }

    public static Color ofRGB(float var0, float var1, float var2) {
        return ofRGBA(var0, var1, var2, 1.0F);
    }

    public static Color ofRGB(int var0, int var1, int var2) {
        return ofRGBA(var0, var1, var2, 255);
    }

    public static Color ofRGBA(float var0, float var1, float var2, float var3) {
        return ofRGBA((int) ((double) (var0 * 255.0F) + 0.5), (int) ((double) (var1 * 255.0F) + 0.5), (int) ((double) (var2 * 255.0F) + 0.5), (int) ((double) (var3 * 255.0F) + 0.5));
    }

    public static Color ofRGBA(int var0, int var1, int var2, int var3) {
        return new Color((var3 & 0xFF) << 24 | (var0 & 0xFF) << 16 | (var1 & 0xFF) << 8 | var2 & 0xFF);
    }

    public static Color ofHSB(float var0, float var1, float var2) {
        return ofOpaque(HSBtoRGB(var0, var1, var2));
    }

    public static int HSBtoRGB(float var0, float var1, float var2) {
        int var3 = 0;
        int var4 = 0;
        int var5 = 0;
        if (var1 == 0.0F) {
            var3 = var4 = var5 = (int) (var2 * 255.0F + 0.5F);
        } else {
            float var9;
            float var6 = (var9 = (var0 - (float) Math.floor((double) var0)) * 6.0F) - (float) Math.floor((double) var9);
            float var7 = var2 * (1.0F - var1);
            float var8 = var2 * (1.0F - var1 * var6);
            var1 = var2 * (1.0F - var1 * (1.0F - var6));
            switch((int) var9) {
                case 0:
                    var3 = (int) (var2 * 255.0F + 0.5F);
                    var4 = (int) (var1 * 255.0F + 0.5F);
                    var5 = (int) (var7 * 255.0F + 0.5F);
                    break;
                case 1:
                    var3 = (int) (var8 * 255.0F + 0.5F);
                    var4 = (int) (var2 * 255.0F + 0.5F);
                    var5 = (int) (var7 * 255.0F + 0.5F);
                    break;
                case 2:
                    var3 = (int) (var7 * 255.0F + 0.5F);
                    var4 = (int) (var2 * 255.0F + 0.5F);
                    var5 = (int) (var1 * 255.0F + 0.5F);
                    break;
                case 3:
                    var3 = (int) (var7 * 255.0F + 0.5F);
                    var4 = (int) (var8 * 255.0F + 0.5F);
                    var5 = (int) (var2 * 255.0F + 0.5F);
                    break;
                case 4:
                    var3 = (int) (var1 * 255.0F + 0.5F);
                    var4 = (int) (var7 * 255.0F + 0.5F);
                    var5 = (int) (var2 * 255.0F + 0.5F);
                    break;
                case 5:
                    var3 = (int) (var2 * 255.0F + 0.5F);
                    var4 = (int) (var7 * 255.0F + 0.5F);
                    var5 = (int) (var8 * 255.0F + 0.5F);
            }
        }
        return 0xFF000000 | var3 << 16 | var4 << 8 | var5;
    }

    public final int getColor() {
        return this.a;
    }

    public final int getAlpha() {
        return this.a >>> 24;
    }

    public final int getRed() {
        return this.a >> 16 & 0xFF;
    }

    public final int getGreen() {
        return this.a >> 8 & 0xFF;
    }

    public final int getBlue() {
        return this.a & 0xFF;
    }

    public final Color brighter(double var1) {
        int var3 = this.getRed();
        int var4 = this.getGreen();
        int var5 = this.getBlue();
        int var6 = (int) (1.0 / (1.0 - 1.0 / var1));
        if (var3 == 0 && var4 == 0 && var5 == 0) {
            return ofRGBA(var6, var6, var6, this.getAlpha());
        } else {
            if (var3 > 0 && var3 < var6) {
                var3 = var6;
            }
            if (var4 > 0 && var4 < var6) {
                var4 = var6;
            }
            if (var5 > 0 && var5 < var6) {
                var5 = var6;
            }
            return ofRGBA(Math.min((int) ((double) var3 / (1.0 / var1)), 255), Math.min((int) ((double) var4 / (1.0 / var1)), 255), Math.min((int) ((double) var5 / (1.0 / var1)), 255), this.getAlpha());
        }
    }

    public final Color darker(double var1) {
        return ofRGBA(Math.max((int) ((double) this.getRed() * (1.0 / var1)), 0), Math.max((int) ((double) this.getGreen() * (1.0 / var1)), 0), Math.max((int) ((double) this.getBlue() * (1.0 / var1)), 0), this.getAlpha());
    }

    public final boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else {
            return var1 == null || this.getClass() != var1.getClass() ? false : this.a == ((Color) var1).a;
        }
    }

    public final int hashCode() {
        return this.a;
    }

    public final String toString() {
        return String.valueOf(this.a);
    }
}
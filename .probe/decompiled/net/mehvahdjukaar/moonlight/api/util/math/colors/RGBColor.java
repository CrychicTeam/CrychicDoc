package net.mehvahdjukaar.moonlight.api.util.math.colors;

import net.minecraft.util.Mth;

public class RGBColor extends BaseColor<RGBColor> {

    public RGBColor(int value) {
        this((float) getR(value) / 255.0F, (float) getG(value) / 255.0F, (float) getB(value) / 255.0F, (float) getA(value) / 255.0F);
    }

    public RGBColor(float r, float g, float b, float a) {
        super(Mth.clamp(r, 0.0F, 1.0F), Mth.clamp(g, 0.0F, 1.0F), Mth.clamp(b, 0.0F, 1.0F), Mth.clamp(a, 0.0F, 1.0F));
    }

    public static int getA(int abgr) {
        return abgr >> 24 & 0xFF;
    }

    public static int getR(int abgr) {
        return abgr & 0xFF;
    }

    public static int getG(int agbgr) {
        return agbgr >> 8 & 0xFF;
    }

    public static int getB(int agbgr) {
        return agbgr >> 16 & 0xFF;
    }

    public static int combine(int alpha, int blue, int green, int red) {
        return (alpha & 0xFF) << 24 | (blue & 0xFF) << 16 | (green & 0xFF) << 8 | red & 0xFF;
    }

    public String toString() {
        return String.format("R: %s, G: %s, B %s", (int) (255.0F * this.red()), (int) (255.0F * this.green()), (int) (255.0F * this.blue()));
    }

    public float red() {
        return this.v0;
    }

    public float green() {
        return this.v1;
    }

    public float blue() {
        return this.v2;
    }

    public float alpha() {
        return this.v3;
    }

    public RGBColor withRed(float red) {
        return new RGBColor(red, this.green(), this.blue(), this.alpha());
    }

    public RGBColor withGreen(float green) {
        return new RGBColor(this.red(), green, this.blue(), this.alpha());
    }

    public RGBColor withBlue(float blue) {
        return new RGBColor(this.red(), this.green(), blue, this.alpha());
    }

    public RGBColor withAlpha(float alpha) {
        return new RGBColor(this.red(), this.green(), this.blue(), alpha);
    }

    @Override
    public RGBColor asRGB() {
        return this;
    }

    public static RGBColor averageColors(RGBColor... colors) {
        float size = (float) colors.length;
        float r = 0.0F;
        float g = 0.0F;
        float b = 0.0F;
        float a = 0.0F;
        for (RGBColor c : colors) {
            r += c.red();
            g += c.green();
            b += c.blue();
            a += c.alpha();
        }
        return new RGBColor(r / size, g / size, b / size, a / size);
    }

    public RGBColor multiply(float red, float green, float blue, float alpha) {
        return new RGBColor(Mth.clamp(red * this.red(), 0.0F, 1.0F), Mth.clamp(green * this.green(), 0.0F, 1.0F), Mth.clamp(blue * this.blue(), 0.0F, 1.0F), Mth.clamp(alpha * this.alpha(), 0.0F, 1.0F));
    }

    @Deprecated(forRemoval = true)
    public RGBColor multiply(RGBColor color, float hue, float chroma, float luminance, float alpha) {
        return this.multiply(hue, chroma, luminance, alpha);
    }

    public RGBColor mixWith(RGBColor color, float bias) {
        float i = 1.0F - bias;
        float r = this.red() * i + color.red() * bias;
        float g = this.green() * i + color.green() * bias;
        float b = this.blue() * i + color.blue() * bias;
        float a = this.alpha() * i + color.alpha() * bias;
        return new RGBColor(r, g, b, a);
    }

    public RGBColor fromRGB(RGBColor rgb) {
        return this;
    }

    public int toInt() {
        return combine((int) (this.alpha() * 255.0F), (int) (this.blue() * 255.0F), (int) (this.green() * 255.0F), (int) (this.red() * 255.0F));
    }
}
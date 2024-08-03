package net.mehvahdjukaar.moonlight.api.util.math.colors;

import net.minecraft.util.Mth;

public class LUVColor extends BaseColor<LUVColor> {

    public LUVColor(float l, float u, float v, float alpha) {
        super(l, u, v, alpha);
    }

    public String toString() {
        return String.format("L: %s, U: %s, V %s", (int) (255.0F * this.luminance()), (int) (255.0F * this.u()), (int) (255.0F * this.v()));
    }

    public float luminance() {
        return this.v0;
    }

    public float u() {
        return this.v1;
    }

    public float v() {
        return this.v2;
    }

    public float alpha() {
        return this.v3;
    }

    public LUVColor withLuminance(float luminance) {
        return new LUVColor(luminance, this.u(), this.v(), this.alpha());
    }

    public LUVColor withU(float u) {
        return new LUVColor(this.luminance(), u, this.v(), this.alpha());
    }

    public LUVColor withV(float v) {
        return new LUVColor(this.luminance(), this.u(), v, this.alpha());
    }

    public LUVColor withAlpha(float alpha) {
        return new LUVColor(this.luminance(), this.u(), this.v(), alpha);
    }

    public static LUVColor averageColors(LUVColor... colors) {
        float size = (float) colors.length;
        float r = 0.0F;
        float g = 0.0F;
        float b = 0.0F;
        float a = 0.0F;
        for (LUVColor c : colors) {
            r += c.luminance();
            g += c.u();
            b += c.v();
            a += c.alpha();
        }
        return new LUVColor(r / size, g / size, b / size, a / size);
    }

    @Override
    public LUVColor asLUV() {
        return this;
    }

    @Override
    public RGBColor asRGB() {
        return ColorSpaces.XYZtoRGB(ColorSpaces.LUVtoXYZ(this));
    }

    public LUVColor multiply(float luminance, float u, float v, float alpha) {
        return new LUVColor(Mth.clamp(luminance * this.luminance(), 0.0F, 1.0F), Mth.clamp(u * this.u(), 0.0F, 1.0F), Mth.clamp(v * this.v(), 0.0F, 1.0F), Mth.clamp(alpha * this.alpha(), 0.0F, 1.0F));
    }

    @Deprecated(forRemoval = true)
    public LUVColor multiply(LUVColor color, float hue, float chroma, float luminance, float alpha) {
        return this.multiply(hue, chroma, luminance, alpha);
    }

    public LUVColor mixWith(LUVColor color, float bias) {
        float i = 1.0F - bias;
        float r = this.luminance() * i + color.luminance() * bias;
        float g = this.u() * i + color.u() * bias;
        float b = this.v() * i + color.v() * bias;
        float a = this.alpha() * i + color.alpha() * bias;
        return new LUVColor(r, g, b, a);
    }

    public LUVColor fromRGB(RGBColor rgb) {
        return rgb.asLUV();
    }
}
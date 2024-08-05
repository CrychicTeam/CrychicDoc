package net.mehvahdjukaar.moonlight.api.util.math.colors;

public class LABColor extends BaseColor<LABColor> {

    public LABColor(float l, float a, float b, float alpha) {
        super(l, a, b, alpha);
    }

    public String toString() {
        return String.format("L: %s, A: %s, B %s", (int) (255.0F * this.luminance()), (int) (255.0F * this.a()), (int) (255.0F * this.b()));
    }

    public float luminance() {
        return this.v0;
    }

    public float a() {
        return this.v1;
    }

    public float b() {
        return this.v2;
    }

    public float alpha() {
        return this.v3;
    }

    public LABColor withLuminance(float luminance) {
        return new LABColor(luminance, this.a(), this.b(), this.alpha());
    }

    public LABColor withA(float a) {
        return new LABColor(this.luminance(), a, this.b(), this.alpha());
    }

    public LABColor withB(float b) {
        return new LABColor(this.luminance(), this.a(), b, this.alpha());
    }

    public LABColor withAlpha(float alpha) {
        return new LABColor(this.luminance(), this.a(), this.b(), alpha);
    }

    public static LABColor averageColors(LABColor... colors) {
        float size = (float) colors.length;
        float r = 0.0F;
        float g = 0.0F;
        float b = 0.0F;
        float a = 0.0F;
        for (LABColor c : colors) {
            r += c.luminance();
            g += c.a();
            b += c.b();
            a += c.alpha();
        }
        return new LABColor(r / size, g / size, b / size, a / size);
    }

    @Override
    public LABColor asLAB() {
        return this;
    }

    @Override
    public RGBColor asRGB() {
        return ColorSpaces.XYZtoRGB(ColorSpaces.LABtoXYZ(this));
    }

    public LABColor multiply(float luminance, float a, float b, float alpha) {
        return new LABColor(luminance * this.luminance(), a * this.a(), b * this.b(), alpha * this.alpha());
    }

    @Deprecated(forRemoval = true)
    public LABColor multiply(LABColor color, float hue, float chroma, float luminance, float alpha) {
        return this.multiply(hue, chroma, luminance, alpha);
    }

    public LABColor mixWith(LABColor color, float bias) {
        float i = 1.0F - bias;
        float r = this.luminance() * i + color.luminance() * bias;
        float g = this.a() * i + color.a() * bias;
        float b = this.b() * i + color.b() * bias;
        float a = this.alpha() * i + color.alpha() * bias;
        return new LABColor(r, g, b, a);
    }

    public LABColor fromRGB(RGBColor rgb) {
        return rgb.asLAB();
    }
}
package net.mehvahdjukaar.moonlight.api.util.math.colors;

import net.minecraft.util.Mth;

public class XYZColor extends BaseColor<XYZColor> {

    public XYZColor(float x, float y, float z, float a) {
        super(x, y, z, a);
    }

    public String toString() {
        return String.format("x: %s,y: %s, z %s", this.x(), this.y(), this.z());
    }

    public float x() {
        return this.v0;
    }

    public float y() {
        return this.v1;
    }

    public float z() {
        return this.v2;
    }

    public float alpha() {
        return this.v3;
    }

    public XYZColor withX(float x) {
        return new XYZColor(x, this.y(), this.z(), this.alpha());
    }

    public XYZColor withY(float y) {
        return new XYZColor(this.x(), y, this.z(), this.alpha());
    }

    public XYZColor withZ(float z) {
        return new XYZColor(this.x(), this.y(), z, this.alpha());
    }

    public XYZColor withAlpha(float alpha) {
        return new XYZColor(this.x(), this.y(), this.z(), alpha);
    }

    public XYZColor multiply(float x, float y, float z, float alpha) {
        return new XYZColor(Mth.clamp(x * this.x(), 0.0F, 1.0F), Mth.clamp(y * this.y(), 0.0F, 1.0F), Mth.clamp(z * this.z(), 0.0F, 1.0F), Mth.clamp(alpha * this.alpha(), 0.0F, 1.0F));
    }

    @Deprecated(forRemoval = true)
    public XYZColor multiply(XYZColor color, float hue, float chroma, float luminance, float alpha) {
        return this.multiply(hue, chroma, luminance, alpha);
    }

    @Override
    public RGBColor asRGB() {
        return ColorSpaces.XYZtoRGB(this);
    }

    @Override
    public XYZColor asXYZ() {
        return this;
    }

    public XYZColor fromRGB(RGBColor rgb) {
        return rgb.asXYZ();
    }
}
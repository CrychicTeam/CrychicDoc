package net.mehvahdjukaar.moonlight.api.util.math.colors;

import java.util.Arrays;
import java.util.stream.Stream;
import net.minecraft.util.Mth;

public class HCLColor extends BaseColor<HCLColor> {

    public HCLColor(float h, float c, float l, float a) {
        super(h, c, l, a);
    }

    public String toString() {
        return String.format("H: %s, C: %s, L %s", (int) (255.0F * this.hue()), (int) (255.0F * this.chroma()), (int) (255.0F * this.luminance()));
    }

    public float hue() {
        return this.v0;
    }

    public float chroma() {
        return this.v1;
    }

    public float luminance() {
        return this.v2;
    }

    public float alpha() {
        return this.v3;
    }

    public HCLColor withHue(float hue) {
        return new HCLColor(hue, this.chroma(), this.luminance(), this.alpha());
    }

    public HCLColor withChroma(float chroma) {
        return new HCLColor(this.hue(), chroma, this.luminance(), this.alpha());
    }

    public HCLColor withLuminance(float luminance) {
        return new HCLColor(this.hue(), this.chroma(), luminance, this.alpha());
    }

    public HCLColor withAlpha(float alpha) {
        return new HCLColor(this.hue(), this.chroma(), this.luminance(), alpha);
    }

    @Override
    public RGBColor asRGB() {
        return ColorSpaces.HCLtoLAB(this).asRGB();
    }

    @Override
    public HCLColor asHCL() {
        return this;
    }

    public static HCLColor averageColors(HCLColor... colors) {
        float size = (float) colors.length;
        Stream<Float> list = Arrays.stream(colors).map(HCLColor::hue);
        Float[] hues = (Float[]) list.toArray(Float[]::new);
        float cr = 0.0F;
        float l = 0.0F;
        float a = 0.0F;
        for (HCLColor c : colors) {
            cr += c.chroma();
            l += c.luminance();
            a += c.alpha();
        }
        return new HCLColor(averageAngles(hues), cr / size, l / size, a / size);
    }

    public HCLColor mixWith(HCLColor color, float bias) {
        float i = 1.0F - bias;
        float h = weightedAverageAngles(this.hue(), color.hue(), bias);
        while (h < 0.0F) {
            h++;
        }
        float c = this.chroma() * i + color.chroma() * bias;
        float b = this.luminance() * i + color.luminance() * bias;
        float a = this.alpha() * i + color.alpha() * bias;
        return new HCLColor(h, c, b, a);
    }

    public HCLColor multiply(float hue, float chroma, float luminance, float alpha) {
        return new HCLColor(Mth.clamp(hue * this.hue(), 0.0F, 1.0F), Mth.clamp(chroma * this.chroma(), 0.0F, 1.0F), Mth.clamp(luminance * this.luminance(), 0.0F, 1.0F), Mth.clamp(alpha * this.alpha(), 0.0F, 1.0F));
    }

    @Deprecated(forRemoval = true)
    public HCLColor multiply(HCLColor color, float hue, float chroma, float luminance, float alpha) {
        return this.multiply(hue, chroma, luminance, alpha);
    }

    public HCLColor fromRGB(RGBColor rgb) {
        return rgb.asHCL();
    }

    public float distTo(HCLColor other) {
        float h = this.hue();
        float h2 = other.hue();
        float c = this.chroma();
        float c2 = other.chroma();
        double x = (double) c * Math.cos((double) h * Math.PI * 2.0) - (double) c2 * Math.cos((double) h2 * Math.PI * 2.0);
        double y = (double) c * Math.sin((double) h * Math.PI * 2.0) - (double) c2 * Math.sin((double) h2 * Math.PI * 2.0);
        return (float) Math.sqrt(x * x + y * y + (double) ((this.luminance() - other.luminance()) * (this.luminance() - other.luminance())));
    }
}
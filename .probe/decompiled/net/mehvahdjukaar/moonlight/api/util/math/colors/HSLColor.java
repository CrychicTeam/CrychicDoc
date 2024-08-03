package net.mehvahdjukaar.moonlight.api.util.math.colors;

import java.util.Arrays;
import java.util.stream.Stream;
import net.minecraft.util.Mth;
import oshi.annotation.concurrent.Immutable;

@Immutable
public class HSLColor extends BaseColor<HSLColor> {

    public HSLColor(float h, float s, float l, float a) {
        super(h, s, l, a);
    }

    public String toString() {
        return String.format("H: %s, S: %s, L %s", (int) (255.0F * this.hue()), (int) (255.0F * this.saturation()), (int) (255.0F * this.lightness()));
    }

    public float hue() {
        return this.v0;
    }

    public float saturation() {
        return this.v1;
    }

    public float lightness() {
        return this.v2;
    }

    public float alpha() {
        return this.v3;
    }

    public HSLColor withHue(float hue) {
        return new HSLColor(hue, this.saturation(), this.lightness(), this.alpha());
    }

    public HSLColor withSaturation(float saturation) {
        return new HSLColor(this.hue(), saturation, this.lightness(), this.alpha());
    }

    public HSLColor withLightness(float lightness) {
        return new HSLColor(this.hue(), this.saturation(), lightness, this.alpha());
    }

    public HSLColor withAlpha(float alpha) {
        return new HSLColor(this.hue(), this.saturation(), this.lightness(), alpha);
    }

    @Override
    public RGBColor asRGB() {
        return ColorSpaces.HSLtoRGB(this);
    }

    @Override
    public HSLColor asHSL() {
        return this;
    }

    public static HSLColor averageColors(HSLColor... colors) {
        float size = (float) colors.length;
        Stream<Float> list = Arrays.stream(colors).map(HSLColor::hue);
        Float[] hues = (Float[]) list.toArray(Float[]::new);
        float s = 0.0F;
        float l = 0.0F;
        float a = 0.0F;
        for (HSLColor c : colors) {
            s += c.saturation();
            l += c.lightness();
            a += c.alpha();
        }
        return new HSLColor(averageAngles(hues), s / size, l / size, a / size);
    }

    public HSLColor multiply(float hue, float saturation, float lightness, float alpha) {
        return new HSLColor(Mth.clamp(hue * this.hue(), 0.0F, 1.0F), Mth.clamp(saturation * this.saturation(), 0.0F, 1.0F), Mth.clamp(lightness * this.lightness(), 0.0F, 1.0F), Mth.clamp(alpha * this.alpha(), 0.0F, 1.0F));
    }

    @Deprecated(forRemoval = true)
    public HSLColor multiply(HSLColor color, float hue, float chroma, float luminance, float alpha) {
        return this.multiply(hue, chroma, luminance, alpha);
    }

    public HSLColor mixWith(HSLColor color, float bias) {
        float i = 1.0F - bias;
        if (bias >= 0.0F && bias <= 1.0F) {
            float h = weightedAverageAngles(this.hue(), color.hue(), bias);
            while (h < 0.0F) {
                h++;
            }
            float s = this.saturation() * i + color.saturation() * bias;
            float l = this.lightness() * i + color.lightness() * bias;
            float a = this.alpha() * i + color.alpha() * bias;
            return new HSLColor(h, s, l, a);
        } else {
            throw new IllegalArgumentException("bias must be between 0 and one");
        }
    }

    public HSLColor fromRGB(RGBColor rgb) {
        return rgb.asHSL();
    }

    public float distTo(HSLColor other) {
        float h = this.hue();
        float h2 = other.hue();
        float c = this.saturation();
        float c2 = other.saturation();
        double x = (double) c * Math.cos((double) h * Math.PI * 2.0) - (double) c2 * Math.cos((double) h2 * Math.PI * 2.0);
        double y = (double) c * Math.sin((double) h * Math.PI * 2.0) - (double) c2 * Math.sin((double) h2 * Math.PI * 2.0);
        return (float) Math.sqrt(x * x + y * y + (double) ((this.lightness() - other.lightness()) * (this.lightness() - other.lightness())));
    }
}
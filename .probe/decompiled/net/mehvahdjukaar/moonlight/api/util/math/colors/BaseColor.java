package net.mehvahdjukaar.moonlight.api.util.math.colors;

import com.mojang.serialization.Codec;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.util.math.ColorUtils;
import net.minecraft.util.Mth;

public abstract class BaseColor<T extends BaseColor<T>> {

    protected final float v0;

    protected final float v1;

    protected final float v2;

    protected final float v3;

    @Deprecated(forRemoval = true)
    public static final Codec<Integer> CODEC = ColorUtils.CODEC;

    protected BaseColor(float v0, float v1, float v2, float v3) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public float distTo(T other) {
        return (float) Math.sqrt((double) ((this.v0 - other.v0) * (this.v0 - other.v0) + (this.v1 - other.v1) * (this.v1 - other.v1) + (this.v2 - other.v2) * (this.v2 - other.v2)));
    }

    public T mixWith(T color) {
        return this.mixWith(color, 0.5F);
    }

    public T mixWith(T color, float bias) {
        return color;
    }

    public abstract T multiply(float var1, float var2, float var3, float var4);

    public static <C extends BaseColor<C>> C mixColors(List<C> colors) {
        int size = colors.size();
        C mixed = (C) colors.get(0);
        for (int i = 1; i < size; i++) {
            mixed = mixed.mixWith((C) colors.get(i), 1.0F / ((float) i + 1.0F));
        }
        return mixed;
    }

    public static <C extends BaseColor<C>> C mixColors(C... colors) {
        return mixColors(List.of(colors));
    }

    public abstract RGBColor asRGB();

    public HSLColor asHSL() {
        return this instanceof HSLColor c ? c : ColorSpaces.RGBtoHSL(this.asRGB());
    }

    public HSVColor asHSV() {
        return this instanceof HSVColor c ? c : ColorSpaces.RGBtoHSV(this.asRGB());
    }

    public XYZColor asXYZ() {
        return this instanceof XYZColor c ? c : ColorSpaces.RGBtoXYZ(this.asRGB());
    }

    public LABColor asLAB() {
        return this instanceof LABColor c ? c : ColorSpaces.XYZtoLAB(this.asXYZ());
    }

    public HCLColor asHCL() {
        return this instanceof HCLColor c ? c : ColorSpaces.LABtoHCL(this.asLAB());
    }

    public LUVColor asLUV() {
        return this instanceof LUVColor c ? c : ColorSpaces.XYZtoLUV(this.asXYZ());
    }

    public HCLVColor asHCLV() {
        return this instanceof HCLVColor c ? c : ColorSpaces.LUVtoHCLV(this.asLUV());
    }

    public static float weightedAverageAngles(float a, float b, float bias) {
        return Mth.rotLerp(bias, a * 360.0F, b * 360.0F) / 360.0F;
    }

    protected static float averageAngles(Float... angles) {
        float x = 0.0F;
        float y = 0.0F;
        Float[] a = angles;
        int var4 = angles.length;
        for (int var5 = 0; var5 < var4; var5++) {
            float ax = a[var5];
            assert ax >= 0.0F && ax <= 1.0F;
            x = (float) ((double) x + Math.cos((double) ((float) ((double) ax * Math.PI * 2.0))));
            y = (float) ((double) y + Math.sin((double) ((float) ((double) ax * Math.PI * 2.0))));
        }
        double ax = Math.atan2((double) y, (double) x) / (Math.PI * 2);
        return (float) ax;
    }

    public abstract T fromRGB(RGBColor var1);
}
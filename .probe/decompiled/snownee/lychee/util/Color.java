package snownee.lychee.util;

import com.google.common.hash.Hashing;
import java.util.function.UnaryOperator;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class Color {

    public static final Color TRANSPARENT_BLACK = new Color(0, 0, 0, 0).setImmutable();

    public static final Color BLACK = new Color(0, 0, 0).setImmutable();

    public static final Color WHITE = new Color(255, 255, 255).setImmutable();

    public static final Color RED = new Color(255, 0, 0).setImmutable();

    protected boolean mutable = true;

    protected int value;

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public Color(int r, int g, int b, int a) {
        this.value = (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) << 0;
    }

    public Color(float r, float g, float b, float a) {
        this((int) (0.5 + (double) (255.0F * Mth.clamp(r, 0.0F, 1.0F))), (int) (0.5 + (double) (255.0F * Mth.clamp(g, 0.0F, 1.0F))), (int) (0.5 + (double) (255.0F * Mth.clamp(b, 0.0F, 1.0F))), (int) (0.5 + (double) (255.0F * Mth.clamp(a, 0.0F, 1.0F))));
    }

    public Color(int rgba) {
        this.value = rgba;
    }

    public Color(int rgb, boolean hasAlpha) {
        if (hasAlpha) {
            this.value = rgb;
        } else {
            this.value = rgb | 0xFF000000;
        }
    }

    public Color copy() {
        return this.copy(true);
    }

    public Color copy(boolean mutable) {
        return mutable ? new Color(this.value) : new Color(this.value).setImmutable();
    }

    public Color setImmutable() {
        this.mutable = false;
        return this;
    }

    public int getRed() {
        return this.getRGB() >> 16 & 0xFF;
    }

    public int getGreen() {
        return this.getRGB() >> 8 & 0xFF;
    }

    public int getBlue() {
        return this.getRGB() >> 0 & 0xFF;
    }

    public int getAlpha() {
        return this.getRGB() >> 24 & 0xFF;
    }

    public float getRedAsFloat() {
        return (float) this.getRed() / 255.0F;
    }

    public float getGreenAsFloat() {
        return (float) this.getGreen() / 255.0F;
    }

    public float getBlueAsFloat() {
        return (float) this.getBlue() / 255.0F;
    }

    public float getAlphaAsFloat() {
        return (float) this.getAlpha() / 255.0F;
    }

    public int getRGB() {
        return this.value;
    }

    public Vec3 asVector() {
        return new Vec3((double) this.getRedAsFloat(), (double) this.getGreenAsFloat(), (double) this.getBlueAsFloat());
    }

    public Vector3f asVectorF() {
        return new Vector3f(this.getRedAsFloat(), this.getGreenAsFloat(), this.getBlueAsFloat());
    }

    public Color setRed(int r) {
        return this.ensureMutable().setRedUnchecked(r);
    }

    public Color setGreen(int g) {
        return this.ensureMutable().setGreenUnchecked(g);
    }

    public Color setBlue(int b) {
        return this.ensureMutable().setBlueUnchecked(b);
    }

    public Color setAlpha(int a) {
        return this.ensureMutable().setAlphaUnchecked(a);
    }

    public Color setRed(float r) {
        return this.ensureMutable().setRedUnchecked((int) (255.0F * Mth.clamp(r, 0.0F, 1.0F)));
    }

    public Color setGreen(float g) {
        return this.ensureMutable().setGreenUnchecked((int) (255.0F * Mth.clamp(g, 0.0F, 1.0F)));
    }

    public Color setBlue(float b) {
        return this.ensureMutable().setBlueUnchecked((int) (255.0F * Mth.clamp(b, 0.0F, 1.0F)));
    }

    public Color setAlpha(float a) {
        return this.ensureMutable().setAlphaUnchecked((int) (255.0F * Mth.clamp(a, 0.0F, 1.0F)));
    }

    public Color scaleAlpha(float factor) {
        return this.ensureMutable().setAlphaUnchecked((int) ((float) this.getAlpha() * Mth.clamp(factor, 0.0F, 1.0F)));
    }

    public Color mixWith(Color other, float weight) {
        return this.ensureMutable().setRedUnchecked((int) ((float) this.getRed() + (float) (other.getRed() - this.getRed()) * weight)).setGreenUnchecked((int) ((float) this.getGreen() + (float) (other.getGreen() - this.getGreen()) * weight)).setBlueUnchecked((int) ((float) this.getBlue() + (float) (other.getBlue() - this.getBlue()) * weight)).setAlphaUnchecked((int) ((float) this.getAlpha() + (float) (other.getAlpha() - this.getAlpha()) * weight));
    }

    public Color darker() {
        int a = this.getAlpha();
        return this.ensureMutable().mixWith(BLACK, 0.25F).setAlphaUnchecked(a);
    }

    public Color brighter() {
        int a = this.getAlpha();
        return this.ensureMutable().mixWith(WHITE, 0.25F).setAlphaUnchecked(a);
    }

    public Color setValue(int value) {
        return this.ensureMutable().setValueUnchecked(value);
    }

    public Color modifyValue(UnaryOperator<Integer> function) {
        int newValue = (Integer) function.apply(this.value);
        return newValue == this.value ? this : this.ensureMutable().setValueUnchecked(newValue);
    }

    protected Color ensureMutable() {
        return this.mutable ? this : new Color(this.value);
    }

    protected Color setRedUnchecked(int r) {
        this.value = this.value & -16711681 | (r & 0xFF) << 16;
        return this;
    }

    protected Color setGreenUnchecked(int g) {
        this.value = this.value & -65281 | (g & 0xFF) << 8;
        return this;
    }

    protected Color setBlueUnchecked(int b) {
        this.value = this.value & -256 | (b & 0xFF) << 0;
        return this;
    }

    protected Color setAlphaUnchecked(int a) {
        this.value = this.value & 16777215 | (a & 0xFF) << 24;
        return this;
    }

    protected Color setValueUnchecked(int value) {
        this.value = value;
        return this;
    }

    public static Color mixColors(@NotNull Color c1, @NotNull Color c2, float w) {
        return new Color((int) ((float) c1.getRed() + (float) (c2.getRed() - c1.getRed()) * w), (int) ((float) c1.getGreen() + (float) (c2.getGreen() - c1.getGreen()) * w), (int) ((float) c1.getBlue() + (float) (c2.getBlue() - c1.getBlue()) * w), (int) ((float) c1.getAlpha() + (float) (c2.getAlpha() - c1.getAlpha()) * w));
    }

    public static Color mixColors(@NotNull Couple<Color> colors, float w) {
        return mixColors(colors.getFirst(), colors.getSecond(), w);
    }

    public static int mixColors(int color1, int color2, float w) {
        int a1 = color1 >> 24;
        int r1 = color1 >> 16 & 0xFF;
        int g1 = color1 >> 8 & 0xFF;
        int b1 = color1 & 0xFF;
        int a2 = color2 >> 24;
        int r2 = color2 >> 16 & 0xFF;
        int g2 = color2 >> 8 & 0xFF;
        int b2 = color2 & 0xFF;
        return ((int) ((float) a1 + (float) (a2 - a1) * w) << 24) + ((int) ((float) r1 + (float) (r2 - r1) * w) << 16) + ((int) ((float) g1 + (float) (g2 - g1) * w) << 8) + ((int) ((float) b1 + (float) (b2 - b1) * w) << 0);
    }

    public static Color rainbowColor(int timeStep) {
        int localTimeStep = Math.abs(timeStep) % 1536;
        int timeStepInPhase = localTimeStep % 256;
        int phaseBlue = localTimeStep / 256;
        int red = colorInPhase(phaseBlue + 4, timeStepInPhase);
        int green = colorInPhase(phaseBlue + 2, timeStepInPhase);
        int blue = colorInPhase(phaseBlue, timeStepInPhase);
        return new Color(red, green, blue);
    }

    private static int colorInPhase(int phase, int progress) {
        phase %= 6;
        if (phase <= 1) {
            return 0;
        } else if (phase == 2) {
            return progress;
        } else {
            return phase <= 4 ? 255 : 255 - progress;
        }
    }

    public static Color generateFromLong(long l) {
        return rainbowColor(Hashing.crc32().hashLong(l).asInt()).mixWith(WHITE, 0.5F);
    }
}
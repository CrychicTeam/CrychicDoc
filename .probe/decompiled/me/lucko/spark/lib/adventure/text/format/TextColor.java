package me.lucko.spark.lib.adventure.text.format;

import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.Examinable;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.util.HSVLike;
import me.lucko.spark.lib.adventure.util.RGBLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public interface TextColor extends Comparable<TextColor>, Examinable, RGBLike, StyleBuilderApplicable, TextFormat {

    @NotNull
    static TextColor color(final int value) {
        int truncatedValue = value & 16777215;
        NamedTextColor named = NamedTextColor.namedColor(truncatedValue);
        return (TextColor) (named != null ? named : new TextColorImpl(truncatedValue));
    }

    @NotNull
    static TextColor color(@NotNull final RGBLike rgb) {
        return rgb instanceof TextColor ? (TextColor) rgb : color(rgb.red(), rgb.green(), rgb.blue());
    }

    @NotNull
    static TextColor color(@NotNull final HSVLike hsv) {
        float s = hsv.s();
        float v = hsv.v();
        if (s == 0.0F) {
            return color(v, v, v);
        } else {
            float h = hsv.h() * 6.0F;
            int i = (int) Math.floor((double) h);
            float f = h - (float) i;
            float p = v * (1.0F - s);
            float q = v * (1.0F - s * f);
            float t = v * (1.0F - s * (1.0F - f));
            if (i == 0) {
                return color(v, t, p);
            } else if (i == 1) {
                return color(q, v, p);
            } else if (i == 2) {
                return color(p, v, t);
            } else if (i == 3) {
                return color(p, q, v);
            } else {
                return i == 4 ? color(t, p, v) : color(v, p, q);
            }
        }
    }

    @NotNull
    static TextColor color(@Range(from = 0L, to = 255L) final int r, @Range(from = 0L, to = 255L) final int g, @Range(from = 0L, to = 255L) final int b) {
        return color((r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF);
    }

    @NotNull
    static TextColor color(final float r, final float g, final float b) {
        return color((int) (r * 255.0F), (int) (g * 255.0F), (int) (b * 255.0F));
    }

    @Nullable
    static TextColor fromHexString(@NotNull final String string) {
        if (string.startsWith("#")) {
            try {
                int hex = Integer.parseInt(string.substring(1), 16);
                return color(hex);
            } catch (NumberFormatException var2) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Nullable
    static TextColor fromCSSHexString(@NotNull final String string) {
        if (string.startsWith("#")) {
            String hexString = string.substring(1);
            if (hexString.length() != 3 && hexString.length() != 6) {
                return null;
            } else {
                int hex;
                try {
                    hex = Integer.parseInt(hexString, 16);
                } catch (NumberFormatException var6) {
                    return null;
                }
                if (hexString.length() == 6) {
                    return color(hex);
                } else {
                    int red = (hex & 3840) >> 8 | (hex & 3840) >> 4;
                    int green = (hex & 240) >> 4 | hex & 240;
                    int blue = (hex & 15) << 4 | hex & 15;
                    return color(red, green, blue);
                }
            }
        } else {
            return null;
        }
    }

    int value();

    @NotNull
    default String asHexString() {
        return String.format("#%06x", this.value());
    }

    @Range(from = 0L, to = 255L)
    @Override
    default int red() {
        return this.value() >> 16 & 0xFF;
    }

    @Range(from = 0L, to = 255L)
    @Override
    default int green() {
        return this.value() >> 8 & 0xFF;
    }

    @Range(from = 0L, to = 255L)
    @Override
    default int blue() {
        return this.value() & 0xFF;
    }

    @NotNull
    static TextColor lerp(final float t, @NotNull final RGBLike a, @NotNull final RGBLike b) {
        float clampedT = Math.min(1.0F, Math.max(0.0F, t));
        int ar = a.red();
        int br = b.red();
        int ag = a.green();
        int bg = b.green();
        int ab = a.blue();
        int bb = b.blue();
        return color(Math.round((float) ar + clampedT * (float) (br - ar)), Math.round((float) ag + clampedT * (float) (bg - ag)), Math.round((float) ab + clampedT * (float) (bb - ab)));
    }

    @Override
    default void styleApply(@NotNull final Style.Builder style) {
        style.color(this);
    }

    default int compareTo(final TextColor that) {
        return Integer.compare(this.value(), that.value());
    }

    @NotNull
    @Override
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.asHexString()));
    }
}
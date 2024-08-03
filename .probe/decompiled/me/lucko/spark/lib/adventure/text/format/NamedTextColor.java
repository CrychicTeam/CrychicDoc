package me.lucko.spark.lib.adventure.text.format;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.util.HSVLike;
import me.lucko.spark.lib.adventure.util.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public final class NamedTextColor implements TextColor {

    private static final int BLACK_VALUE = 0;

    private static final int DARK_BLUE_VALUE = 170;

    private static final int DARK_GREEN_VALUE = 43520;

    private static final int DARK_AQUA_VALUE = 43690;

    private static final int DARK_RED_VALUE = 11141120;

    private static final int DARK_PURPLE_VALUE = 11141290;

    private static final int GOLD_VALUE = 16755200;

    private static final int GRAY_VALUE = 11184810;

    private static final int DARK_GRAY_VALUE = 5592405;

    private static final int BLUE_VALUE = 5592575;

    private static final int GREEN_VALUE = 5635925;

    private static final int AQUA_VALUE = 5636095;

    private static final int RED_VALUE = 16733525;

    private static final int LIGHT_PURPLE_VALUE = 16733695;

    private static final int YELLOW_VALUE = 16777045;

    private static final int WHITE_VALUE = 16777215;

    public static final NamedTextColor BLACK = new NamedTextColor("black", 0);

    public static final NamedTextColor DARK_BLUE = new NamedTextColor("dark_blue", 170);

    public static final NamedTextColor DARK_GREEN = new NamedTextColor("dark_green", 43520);

    public static final NamedTextColor DARK_AQUA = new NamedTextColor("dark_aqua", 43690);

    public static final NamedTextColor DARK_RED = new NamedTextColor("dark_red", 11141120);

    public static final NamedTextColor DARK_PURPLE = new NamedTextColor("dark_purple", 11141290);

    public static final NamedTextColor GOLD = new NamedTextColor("gold", 16755200);

    public static final NamedTextColor GRAY = new NamedTextColor("gray", 11184810);

    public static final NamedTextColor DARK_GRAY = new NamedTextColor("dark_gray", 5592405);

    public static final NamedTextColor BLUE = new NamedTextColor("blue", 5592575);

    public static final NamedTextColor GREEN = new NamedTextColor("green", 5635925);

    public static final NamedTextColor AQUA = new NamedTextColor("aqua", 5636095);

    public static final NamedTextColor RED = new NamedTextColor("red", 16733525);

    public static final NamedTextColor LIGHT_PURPLE = new NamedTextColor("light_purple", 16733695);

    public static final NamedTextColor YELLOW = new NamedTextColor("yellow", 16777045);

    public static final NamedTextColor WHITE = new NamedTextColor("white", 16777215);

    private static final List<NamedTextColor> VALUES = Collections.unmodifiableList(Arrays.asList(BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE));

    public static final Index<String, NamedTextColor> NAMES = Index.create(constant -> constant.name, VALUES);

    private final String name;

    private final int value;

    private final HSVLike hsv;

    @Nullable
    public static NamedTextColor namedColor(final int value) {
        switch(value) {
            case 0:
                return BLACK;
            case 170:
                return DARK_BLUE;
            case 43520:
                return DARK_GREEN;
            case 43690:
                return DARK_AQUA;
            case 5592405:
                return DARK_GRAY;
            case 5592575:
                return BLUE;
            case 5635925:
                return GREEN;
            case 5636095:
                return AQUA;
            case 11141120:
                return DARK_RED;
            case 11141290:
                return DARK_PURPLE;
            case 11184810:
                return GRAY;
            case 16733525:
                return RED;
            case 16733695:
                return LIGHT_PURPLE;
            case 16755200:
                return GOLD;
            case 16777045:
                return YELLOW;
            case 16777215:
                return WHITE;
            default:
                return null;
        }
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @Nullable
    public static NamedTextColor ofExact(final int value) {
        switch(value) {
            case 0:
                return BLACK;
            case 170:
                return DARK_BLUE;
            case 43520:
                return DARK_GREEN;
            case 43690:
                return DARK_AQUA;
            case 5592405:
                return DARK_GRAY;
            case 5592575:
                return BLUE;
            case 5635925:
                return GREEN;
            case 5636095:
                return AQUA;
            case 11141120:
                return DARK_RED;
            case 11141290:
                return DARK_PURPLE;
            case 11184810:
                return GRAY;
            case 16733525:
                return RED;
            case 16733695:
                return LIGHT_PURPLE;
            case 16755200:
                return GOLD;
            case 16777045:
                return YELLOW;
            case 16777215:
                return WHITE;
            default:
                return null;
        }
    }

    @NotNull
    public static NamedTextColor nearestTo(@NotNull final TextColor any) {
        if (any instanceof NamedTextColor) {
            return (NamedTextColor) any;
        } else {
            Objects.requireNonNull(any, "color");
            float matchedDistance = Float.MAX_VALUE;
            NamedTextColor match = (NamedTextColor) VALUES.get(0);
            int i = 0;
            for (int length = VALUES.size(); i < length; i++) {
                NamedTextColor potential = (NamedTextColor) VALUES.get(i);
                float distance = distance(any.asHSV(), potential.asHSV());
                if (distance < matchedDistance) {
                    match = potential;
                    matchedDistance = distance;
                }
                if (distance == 0.0F) {
                    break;
                }
            }
            return match;
        }
    }

    private static float distance(@NotNull final HSVLike self, @NotNull final HSVLike other) {
        float hueDistance = 3.0F * Math.min(Math.abs(self.h() - other.h()), 1.0F - Math.abs(self.h() - other.h()));
        float saturationDiff = self.s() - other.s();
        float valueDiff = self.v() - other.v();
        return hueDistance * hueDistance + saturationDiff * saturationDiff + valueDiff * valueDiff;
    }

    private NamedTextColor(final String name, final int value) {
        this.name = name;
        this.value = value;
        this.hsv = HSVLike.fromRGB(this.red(), this.green(), this.blue());
    }

    @Override
    public int value() {
        return this.value;
    }

    @NotNull
    @Override
    public HSVLike asHSV() {
        return this.hsv;
    }

    @NotNull
    public String toString() {
        return this.name;
    }

    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(Stream.of(ExaminableProperty.of("name", this.name)), TextColor.super.examinableProperties());
    }
}
package org.violetmoon.quark.content.tools.base;

import java.util.Collection;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;

public class RuneColor implements StringRepresentable {

    private static final HashMap<String, RuneColor> BY_NAME = new HashMap();

    private static final HashMap<DyeColor, RuneColor> BY_COLOR = new HashMap();

    public static final RuneColor WHITE = new RuneColor(DyeColor.WHITE);

    public static final RuneColor ORANGE = new RuneColor(DyeColor.ORANGE);

    public static final RuneColor MAGENTA = new RuneColor(DyeColor.MAGENTA);

    public static final RuneColor LIGHT_BLUE = new RuneColor(DyeColor.LIGHT_BLUE);

    public static final RuneColor YELLOW = new RuneColor(DyeColor.YELLOW);

    public static final RuneColor LIME = new RuneColor(DyeColor.LIME);

    public static final RuneColor PINK = new RuneColor(DyeColor.PINK);

    public static final RuneColor GRAY = new RuneColor(DyeColor.GRAY);

    public static final RuneColor LIGHT_GRAY = new RuneColor(DyeColor.LIGHT_GRAY);

    public static final RuneColor CYAN = new RuneColor(DyeColor.CYAN);

    public static final RuneColor PURPLE = new RuneColor(DyeColor.PURPLE);

    public static final RuneColor BLUE = new RuneColor(DyeColor.BLUE);

    public static final RuneColor BROWN = new RuneColor(DyeColor.BROWN);

    public static final RuneColor GREEN = new RuneColor(DyeColor.GREEN);

    public static final RuneColor RED = new RuneColor(DyeColor.RED);

    public static final RuneColor BLACK = new RuneColor(DyeColor.BLACK, 4210752);

    public static final RuneColor RAINBOW = new RuneColor("rainbow", ChatFormatting.WHITE);

    public static final RuneColor BLANK = new RuneColor("blank", ChatFormatting.GRAY);

    private final DyeColor dyeColor;

    private final String name;

    private final TextColor textColor;

    RuneColor(DyeColor color) {
        this(color, color.getTextColor());
    }

    RuneColor(DyeColor color, int textColor) {
        this(color.getSerializedName(), textColor, color);
    }

    RuneColor(String name, ChatFormatting textColor) {
        this(name, textColor.getColor() != null ? textColor.getColor() : -1, null);
    }

    public RuneColor(String name, int textColor, @Nullable DyeColor dyeColor) {
        this.dyeColor = dyeColor;
        this.name = name;
        this.textColor = TextColor.fromRgb(textColor);
        BY_NAME.put(name, this);
        if (dyeColor != null) {
            BY_COLOR.put(dyeColor, this);
        }
    }

    public static Collection<RuneColor> values() {
        return BY_NAME.values();
    }

    public TextColor getTextColor() {
        return this.textColor;
    }

    @Nullable
    public DyeColor getDyeColor() {
        return this.dyeColor;
    }

    @Nonnull
    @Override
    public String getSerializedName() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    @Nullable
    public static RuneColor byName(String name) {
        return (RuneColor) BY_NAME.get(name);
    }

    @Nullable
    public static RuneColor byDyeColor(DyeColor dyeColor) {
        return (RuneColor) BY_COLOR.get(dyeColor);
    }

    static {
        for (DyeColor color : DyeColor.values()) {
            if (BY_COLOR.get(color) == null) {
                new RuneColor(color);
            }
        }
    }
}
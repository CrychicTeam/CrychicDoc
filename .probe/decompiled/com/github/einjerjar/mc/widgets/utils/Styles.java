package com.github.einjerjar.mc.widgets.utils;

import net.minecraft.network.chat.Style;

public class Styles {

    protected static final Style headerBold = Style.EMPTY.withBold(true).withColor(65280).withItalic(true);

    protected static final Style header = Style.EMPTY.withColor(65280).withItalic(true);

    protected static final Style muted = Style.EMPTY.withColor(5592405);

    protected static final Style muted2 = Style.EMPTY.withColor(8947848);

    protected static final Style normal = Style.EMPTY;

    protected static final Style red = Style.EMPTY.withColor(16711680);

    protected static final Style green = Style.EMPTY.withColor(65280);

    protected static final Style blue = Style.EMPTY.withColor(255);

    protected static final Style yellow = Style.EMPTY.withColor(16776960);

    protected static final Style cyan = Style.EMPTY.withColor(65535);

    protected static final Style purple = Style.EMPTY.withColor(16711935);

    private Styles() {
    }

    public static Style headerBold() {
        return headerBold;
    }

    public static Style header() {
        return header;
    }

    public static Style muted() {
        return muted;
    }

    public static Style muted2() {
        return muted2;
    }

    public static Style normal() {
        return normal;
    }

    public static Style red() {
        return red;
    }

    public static Style green() {
        return green;
    }

    public static Style blue() {
        return blue;
    }

    public static Style yellow() {
        return yellow;
    }

    public static Style cyan() {
        return cyan;
    }

    public static Style purple() {
        return purple;
    }
}
package com.simibubi.create.content.trains.signal;

import com.simibubi.create.foundation.utility.Color;

public enum EdgeGroupColor {

    YELLOW(15450709),
    GREEN(5357652),
    BLUE(5476833),
    ORANGE(14904886),
    LAVENDER(13341370),
    RED(10761528),
    CYAN(7264985),
    BROWN(10583128),
    WHITE(15065564);

    private Color color;

    private int mask;

    private EdgeGroupColor(int color) {
        this.color = new Color(color);
        this.mask = 1 << this.ordinal();
    }

    public int strikeFrom(int mask) {
        return this == WHITE ? mask : mask | this.mask;
    }

    public Color get() {
        return this.color;
    }

    public static EdgeGroupColor getDefault() {
        return values()[0];
    }

    public static EdgeGroupColor findNextAvailable(int mask) {
        EdgeGroupColor[] values = values();
        for (int i = 0; i < values.length; i++) {
            if ((mask & 1) == 0) {
                return values[i];
            }
            mask >>= 1;
        }
        return WHITE;
    }
}
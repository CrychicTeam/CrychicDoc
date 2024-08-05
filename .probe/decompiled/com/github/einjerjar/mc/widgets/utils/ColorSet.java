package com.github.einjerjar.mc.widgets.utils;

public final class ColorSet {

    private final int text;

    private final int bg;

    private final int border;

    public ColorSet(int text, int bg, int border) {
        this.text = text;
        this.bg = bg;
        this.border = border;
    }

    public ColorSet(int color, ColorType colorType) {
        int base = color & 16777215;
        int cText = 0xFF000000 | base;
        int cBorder = 0xFF000000 | base;
        int cBg = 1426063360 | base;
        switch(colorType) {
            case HOVER:
                cBg = -2013265920 | base;
                break;
            case ACTIVE:
                cBg = 0xFF000000 | base;
                cText = 0xFF000000 | returnDarkerColor(base);
                break;
            case DISABLED:
                cText = -2013265920 | base;
                cBorder = -2013265920 | base;
                cBg = 855638016 | base;
        }
        this.text = cText;
        this.border = cBorder;
        this.bg = cBg;
    }

    private static int returnDarkerColor(int color) {
        float ratio = 0.8F;
        int a = color >> 24 & 0xFF;
        int r = (int) ((float) (color >> 16 & 0xFF) * ratio);
        int g = (int) ((float) (color >> 8 & 0xFF) * ratio);
        int b = (int) ((float) (color & 0xFF) * ratio);
        return a << 24 | r << 16 | g << 8 | b;
    }

    public String toString() {
        return "ColorSet(text=" + this.text() + ", bg=" + this.bg() + ", border=" + this.border() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ColorSet other)) {
            return false;
        } else if (this.text() != other.text()) {
            return false;
        } else {
            return this.bg() != other.bg() ? false : this.border() == other.border();
        }
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.text();
        result = result * 59 + this.bg();
        return result * 59 + this.border();
    }

    public int text() {
        return this.text;
    }

    public int bg() {
        return this.bg;
    }

    public int border() {
        return this.border;
    }
}
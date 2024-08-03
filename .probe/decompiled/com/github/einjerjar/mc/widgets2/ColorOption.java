package com.github.einjerjar.mc.widgets2;

public class ColorOption {

    public static final ColorOption baseBG = builder().build();

    public static final ColorOption baseFG = builder().transparencyBase(-16777216).transparencyFocus(-16777216).transparencyHover(-16777216).transparencyActive(-16777216).transparencyDisabled(-16777216).build();

    protected int color;

    protected int transparencyBase;

    protected int transparencyHover;

    protected int transparencyActive;

    protected int transparencyFocus;

    protected int transparencyDisabled;

    public int base() {
        return this.transparencyBase | this.color;
    }

    public int hover() {
        return this.transparencyHover | this.color;
    }

    public int active() {
        return this.transparencyActive | this.color;
    }

    public int focus() {
        return this.transparencyFocus | this.color;
    }

    public int disabled() {
        return this.transparencyDisabled | this.color;
    }

    public int fromState(WidgetState s) {
        switch(s) {
            case DISABLED:
                return this.disabled();
            case ACTIVE:
                return this.active();
            case HOVER:
                return this.hover();
            case FOCUS:
                return this.focus();
            default:
                return this.base();
        }
    }

    private static int $default$color() {
        return 16777215;
    }

    private static int $default$transparencyBase() {
        return 0;
    }

    private static int $default$transparencyHover() {
        return 855638016;
    }

    private static int $default$transparencyActive() {
        return 1426063360;
    }

    private static int $default$transparencyFocus() {
        return 285212672;
    }

    private static int $default$transparencyDisabled() {
        return 0;
    }

    ColorOption(int color, int transparencyBase, int transparencyHover, int transparencyActive, int transparencyFocus, int transparencyDisabled) {
        this.color = color;
        this.transparencyBase = transparencyBase;
        this.transparencyHover = transparencyHover;
        this.transparencyActive = transparencyActive;
        this.transparencyFocus = transparencyFocus;
        this.transparencyDisabled = transparencyDisabled;
    }

    public static ColorOption.ColorOptionBuilder builder() {
        return new ColorOption.ColorOptionBuilder();
    }

    public int color() {
        return this.color;
    }

    public ColorOption color(int color) {
        this.color = color;
        return this;
    }

    public int transparencyBase() {
        return this.transparencyBase;
    }

    public ColorOption transparencyBase(int transparencyBase) {
        this.transparencyBase = transparencyBase;
        return this;
    }

    public int transparencyHover() {
        return this.transparencyHover;
    }

    public ColorOption transparencyHover(int transparencyHover) {
        this.transparencyHover = transparencyHover;
        return this;
    }

    public int transparencyActive() {
        return this.transparencyActive;
    }

    public ColorOption transparencyActive(int transparencyActive) {
        this.transparencyActive = transparencyActive;
        return this;
    }

    public int transparencyFocus() {
        return this.transparencyFocus;
    }

    public ColorOption transparencyFocus(int transparencyFocus) {
        this.transparencyFocus = transparencyFocus;
        return this;
    }

    public int transparencyDisabled() {
        return this.transparencyDisabled;
    }

    public ColorOption transparencyDisabled(int transparencyDisabled) {
        this.transparencyDisabled = transparencyDisabled;
        return this;
    }

    public static class ColorOptionBuilder {

        private boolean color$set;

        private int color$value;

        private boolean transparencyBase$set;

        private int transparencyBase$value;

        private boolean transparencyHover$set;

        private int transparencyHover$value;

        private boolean transparencyActive$set;

        private int transparencyActive$value;

        private boolean transparencyFocus$set;

        private int transparencyFocus$value;

        private boolean transparencyDisabled$set;

        private int transparencyDisabled$value;

        ColorOptionBuilder() {
        }

        public ColorOption.ColorOptionBuilder color(int color) {
            this.color$value = color;
            this.color$set = true;
            return this;
        }

        public ColorOption.ColorOptionBuilder transparencyBase(int transparencyBase) {
            this.transparencyBase$value = transparencyBase;
            this.transparencyBase$set = true;
            return this;
        }

        public ColorOption.ColorOptionBuilder transparencyHover(int transparencyHover) {
            this.transparencyHover$value = transparencyHover;
            this.transparencyHover$set = true;
            return this;
        }

        public ColorOption.ColorOptionBuilder transparencyActive(int transparencyActive) {
            this.transparencyActive$value = transparencyActive;
            this.transparencyActive$set = true;
            return this;
        }

        public ColorOption.ColorOptionBuilder transparencyFocus(int transparencyFocus) {
            this.transparencyFocus$value = transparencyFocus;
            this.transparencyFocus$set = true;
            return this;
        }

        public ColorOption.ColorOptionBuilder transparencyDisabled(int transparencyDisabled) {
            this.transparencyDisabled$value = transparencyDisabled;
            this.transparencyDisabled$set = true;
            return this;
        }

        public ColorOption build() {
            int color$value = this.color$value;
            if (!this.color$set) {
                color$value = ColorOption.$default$color();
            }
            int transparencyBase$value = this.transparencyBase$value;
            if (!this.transparencyBase$set) {
                transparencyBase$value = ColorOption.$default$transparencyBase();
            }
            int transparencyHover$value = this.transparencyHover$value;
            if (!this.transparencyHover$set) {
                transparencyHover$value = ColorOption.$default$transparencyHover();
            }
            int transparencyActive$value = this.transparencyActive$value;
            if (!this.transparencyActive$set) {
                transparencyActive$value = ColorOption.$default$transparencyActive();
            }
            int transparencyFocus$value = this.transparencyFocus$value;
            if (!this.transparencyFocus$set) {
                transparencyFocus$value = ColorOption.$default$transparencyFocus();
            }
            int transparencyDisabled$value = this.transparencyDisabled$value;
            if (!this.transparencyDisabled$set) {
                transparencyDisabled$value = ColorOption.$default$transparencyDisabled();
            }
            return new ColorOption(color$value, transparencyBase$value, transparencyHover$value, transparencyActive$value, transparencyFocus$value, transparencyDisabled$value);
        }

        public String toString() {
            return "ColorOption.ColorOptionBuilder(color$value=" + this.color$value + ", transparencyBase$value=" + this.transparencyBase$value + ", transparencyHover$value=" + this.transparencyHover$value + ", transparencyActive$value=" + this.transparencyActive$value + ", transparencyFocus$value=" + this.transparencyFocus$value + ", transparencyDisabled$value=" + this.transparencyDisabled$value + ")";
        }
    }
}
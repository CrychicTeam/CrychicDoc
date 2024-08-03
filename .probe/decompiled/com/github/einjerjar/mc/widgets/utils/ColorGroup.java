package com.github.einjerjar.mc.widgets.utils;

public final class ColorGroup {

    private final ColorSet normal;

    private final ColorSet hover;

    private final ColorSet active;

    private final ColorSet disabled;

    public ColorGroup(ColorSet normal, ColorSet hover, ColorSet active, ColorSet disabled) {
        this.normal = normal;
        this.hover = hover;
        this.active = active;
        this.disabled = disabled;
    }

    public ColorGroup(int color) {
        this.normal = new ColorSet(color, ColorType.NORMAL);
        this.hover = new ColorSet(color, ColorType.HOVER);
        this.active = new ColorSet(color, ColorType.ACTIVE);
        this.disabled = new ColorSet(color, ColorType.DISABLED);
    }

    public ColorSet getVariant(boolean hover, boolean active, boolean disabled) {
        if (disabled) {
            return this.disabled;
        } else if (active) {
            return this.active;
        } else {
            return hover ? this.hover : this.normal;
        }
    }

    public String toString() {
        return "ColorGroup(normal=" + this.normal() + ", hover=" + this.hover() + ", active=" + this.active() + ", disabled=" + this.disabled() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ColorGroup other)) {
            return false;
        } else {
            Object this$normal = this.normal();
            Object other$normal = other.normal();
            if (this$normal == null ? other$normal == null : this$normal.equals(other$normal)) {
                Object this$hover = this.hover();
                Object other$hover = other.hover();
                if (this$hover == null ? other$hover == null : this$hover.equals(other$hover)) {
                    Object this$active = this.active();
                    Object other$active = other.active();
                    if (this$active == null ? other$active == null : this$active.equals(other$active)) {
                        Object this$disabled = this.disabled();
                        Object other$disabled = other.disabled();
                        return this$disabled == null ? other$disabled == null : this$disabled.equals(other$disabled);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $normal = this.normal();
        result = result * 59 + ($normal == null ? 43 : $normal.hashCode());
        Object $hover = this.hover();
        result = result * 59 + ($hover == null ? 43 : $hover.hashCode());
        Object $active = this.active();
        result = result * 59 + ($active == null ? 43 : $active.hashCode());
        Object $disabled = this.disabled();
        return result * 59 + ($disabled == null ? 43 : $disabled.hashCode());
    }

    public ColorSet normal() {
        return this.normal;
    }

    public ColorSet hover() {
        return this.hover;
    }

    public ColorSet active() {
        return this.active;
    }

    public ColorSet disabled() {
        return this.disabled;
    }
}
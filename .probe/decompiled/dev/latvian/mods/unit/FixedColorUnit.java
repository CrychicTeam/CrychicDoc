package dev.latvian.mods.unit;

import dev.latvian.mods.unit.token.UnitToken;

public class FixedColorUnit extends Unit implements UnitToken {

    public static final FixedColorUnit WHITE = new FixedColorUnit(-1, true);

    public static final FixedColorUnit BLACK = new FixedColorUnit(-16777216, true);

    public static final FixedColorUnit TRANSPARENT = new FixedColorUnit(0, true);

    public final int color;

    public final boolean alpha;

    public static FixedColorUnit of(int color, boolean alpha) {
        if (color == -1) {
            return WHITE;
        } else if (color == -16777216) {
            return BLACK;
        } else {
            return color == 0 ? TRANSPARENT : new FixedColorUnit(color, alpha);
        }
    }

    private FixedColorUnit(int c, boolean a) {
        this.color = c;
        this.alpha = a;
    }

    @Override
    public double get(UnitVariables variables) {
        return (double) this.getInt(variables);
    }

    @Override
    public int getInt(UnitVariables variables) {
        return this.alpha ? this.color : this.color | 0xFF000000;
    }

    @Override
    public boolean getBoolean(UnitVariables variables) {
        return !this.alpha || (this.color >> 24 & 0xFF) != 0;
    }

    @Override
    public void toString(StringBuilder builder) {
        builder.append(String.format(this.alpha ? "#%08X" : "#%06X", this.color));
    }

    @Override
    public Unit withAlpha(Unit a) {
        if (a instanceof FixedNumberUnit u) {
            if (u.value >= 1.0) {
                return of(this.color, false);
            } else {
                return u.value <= 0.0 ? of(this.color & 16777215, true) : of(this.color & 16777215 | (int) (u.value * 255.0) << 24, true);
            }
        } else {
            return super.withAlpha(a);
        }
    }
}
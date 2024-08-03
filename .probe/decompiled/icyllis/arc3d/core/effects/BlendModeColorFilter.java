package icyllis.arc3d.core.effects;

import icyllis.arc3d.core.BlendMode;
import icyllis.arc3d.core.Color;
import icyllis.arc3d.core.ColorFilter;
import icyllis.arc3d.core.Size;
import java.util.Objects;
import javax.annotation.Nullable;

public class BlendModeColorFilter extends ColorFilter {

    private static final BlendModeColorFilter CLEAR = new BlendModeColorFilter(BlendMode.SRC);

    private final float[] mColor;

    private final BlendMode mMode;

    public BlendModeColorFilter(int color, BlendMode mode) {
        this.mColor = Color.load_and_premul(color);
        this.mMode = mode;
    }

    public BlendModeColorFilter(@Size(4L) float[] color, boolean srcIsPremul, BlendMode mode) {
        this(mode);
        if (srcIsPremul) {
            System.arraycopy(color, 0, this.mColor, 0, 4);
        } else {
            float alpha = this.mColor[3] = color[3];
            this.mColor[0] = color[0] * alpha;
            this.mColor[1] = color[1] * alpha;
            this.mColor[2] = color[2] * alpha;
        }
    }

    private BlendModeColorFilter(BlendMode mode) {
        this.mColor = new float[4];
        this.mMode = mode;
    }

    @Nullable
    public static BlendModeColorFilter make(int color, BlendMode mode) {
        Objects.requireNonNull(mode);
        if (mode == BlendMode.CLEAR) {
            return CLEAR;
        } else {
            int alpha = color >>> 24;
            if (mode == BlendMode.SRC_OVER) {
                if (alpha == 0) {
                    mode = BlendMode.DST;
                } else if (alpha == 255) {
                    mode = BlendMode.SRC;
                }
            }
            if (mode == BlendMode.DST) {
                return null;
            } else {
                if (alpha == 0) {
                    switch(mode) {
                        case DST_OVER:
                        case DST_OUT:
                        case SRC_ATOP:
                        case XOR:
                        case PLUS:
                        case PLUS_CLAMPED:
                        case MINUS:
                        case MINUS_CLAMPED:
                            return null;
                        default:
                            if (mode.isAdvanced()) {
                                return null;
                            }
                    }
                }
                return alpha == 255 && mode == BlendMode.DST_IN ? null : new BlendModeColorFilter(color, mode);
            }
        }
    }

    @Nullable
    public static BlendModeColorFilter make(@Size(4L) float[] color, boolean srcIsPremul, BlendMode mode) {
        Objects.requireNonNull(mode);
        if (mode == BlendMode.CLEAR) {
            return CLEAR;
        } else {
            float alpha = color[3];
            if (mode == BlendMode.SRC_OVER) {
                if (alpha == 0.0F) {
                    mode = BlendMode.DST;
                } else if (alpha == 1.0F) {
                    mode = BlendMode.SRC;
                }
            }
            if (mode == BlendMode.DST) {
                return null;
            } else {
                if (alpha == 0.0F) {
                    switch(mode) {
                        case DST_OVER:
                        case DST_OUT:
                        case SRC_ATOP:
                        case XOR:
                        case PLUS:
                        case PLUS_CLAMPED:
                        case MINUS:
                        case MINUS_CLAMPED:
                            return null;
                        default:
                            if (mode.isAdvanced()) {
                                return null;
                            }
                    }
                }
                return alpha == 1.0F && mode == BlendMode.DST_IN ? null : new BlendModeColorFilter(color, srcIsPremul, mode);
            }
        }
    }

    public float[] getColor4f() {
        return this.mColor;
    }

    public BlendMode getMode() {
        return this.mMode;
    }

    @Override
    public boolean isAlphaUnchanged() {
        return switch(this.mMode) {
            case SRC_ATOP, DST ->
                true;
            default ->
                false;
        };
    }

    @Override
    public void filterColor4f(float[] col, float[] out) {
        this.mMode.apply(this.mColor, col, out);
    }
}
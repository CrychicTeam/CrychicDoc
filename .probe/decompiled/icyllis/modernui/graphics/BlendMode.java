package icyllis.modernui.graphics;

public enum BlendMode {

    CLEAR,
    SRC,
    DST,
    SRC_OVER,
    DST_OVER,
    SRC_IN,
    DST_IN,
    SRC_OUT,
    DST_OUT,
    SRC_ATOP,
    DST_ATOP,
    XOR,
    PLUS,
    PLUS_CLAMPED,
    MINUS,
    MINUS_CLAMPED,
    MODULATE,
    MULTIPLY,
    SCREEN,
    OVERLAY,
    DARKEN,
    LIGHTEN,
    COLOR_DODGE,
    COLOR_BURN,
    HARD_LIGHT,
    SOFT_LIGHT,
    DIFFERENCE,
    EXCLUSION,
    SUBTRACT,
    DIVIDE,
    LINEAR_DODGE,
    LINEAR_BURN,
    VIVID_LIGHT,
    LINEAR_LIGHT,
    PIN_LIGHT,
    HARD_MIX,
    DARKER_COLOR,
    LIGHTER_COLOR,
    HUE,
    SATURATION,
    COLOR,
    LUMINOSITY;

    public static final BlendMode ADD = LINEAR_DODGE;

    static final BlendMode[] VALUES = values();

    final icyllis.arc3d.core.BlendMode mBlendMode = icyllis.arc3d.core.BlendMode.mode(this.ordinal());

    private BlendMode() {
        assert this.mBlendMode.name().equals(this.name());
    }

    public icyllis.arc3d.core.BlendMode nativeBlendMode() {
        return this.mBlendMode;
    }
}
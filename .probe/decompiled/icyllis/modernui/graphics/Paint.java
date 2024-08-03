package icyllis.modernui.graphics;

import icyllis.arc3d.core.ColorFilter;
import icyllis.arc3d.core.ImageFilter;
import icyllis.arc3d.core.MaskFilter;
import icyllis.arc3d.core.Shader;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.concurrent.GuardedBy;
import org.jetbrains.annotations.ApiStatus.Internal;

public class Paint extends icyllis.arc3d.core.Paint {

    public static final int FILL = 0;

    public static final int STROKE = 1;

    public static final int FILL_AND_STROKE = 2;

    public static final int CAP_BUTT = 0;

    public static final int CAP_ROUND = 4;

    public static final int CAP_SQUARE = 8;

    public static final int JOIN_MITER = 0;

    public static final int JOIN_ROUND = 16;

    public static final int JOIN_BEVEL = 32;

    public static final int NORMAL = 0;

    public static final int BOLD = 1;

    public static final int ITALIC = 2;

    public static final int BOLD_ITALIC = 3;

    public static final int FONT_STYLE_MASK = 3;

    static final int TEXT_ANTI_ALIAS_DEFAULT = 0;

    static final int TEXT_ANTI_ALIAS_OFF = 8;

    static final int TEXT_ANTI_ALIAS_ON = 12;

    static final int TEXT_ANTI_ALIAS_MASK = 12;

    public static final int LINEAR_TEXT_FLAG = 16;

    private static final Paint[] sPool = new Paint[8];

    @GuardedBy("sPool")
    private static int sPoolSize;

    protected int mFontFlags;

    protected float mFontSize;

    public Paint() {
    }

    public Paint(@Nullable Paint paint) {
        this.set(paint);
    }

    @NonNull
    public static Paint obtain() {
        synchronized (sPool) {
            if (sPoolSize != 0) {
                int i = --sPoolSize;
                Paint p = sPool[i];
                sPool[i] = null;
                return p;
            }
        }
        return new Paint();
    }

    public void recycle() {
        this.reset();
        synchronized (sPool) {
            if (sPoolSize != sPool.length) {
                sPool[sPoolSize++] = this;
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.mFontFlags = 0;
        this.mFontSize = 16.0F;
    }

    public void set(Paint paint) {
        super.set(paint);
        if (paint != null) {
            this.mFontFlags = paint.mFontFlags;
            this.mFontSize = paint.mFontSize;
        }
    }

    @Override
    public int getColor() {
        return super.getColor();
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
    }

    @Override
    public int getAlpha() {
        return super.getAlpha();
    }

    @Override
    public void setAlpha(int a) {
        super.setAlpha(a);
    }

    @Override
    public float getAlphaF() {
        return super.getAlphaF();
    }

    @Override
    public void setAlphaF(float a) {
        super.setAlphaF(a);
    }

    @Override
    public void setARGB(int a, int r, int g, int b) {
        super.setARGB(a, r, g, b);
    }

    @Override
    public void setARGB(float a, float r, float g, float b) {
        super.setARGB(a, r, g, b);
    }

    @Override
    public int getStyle() {
        return super.getStyle();
    }

    @Override
    public void setStyle(int style) {
        super.setStyle(style);
    }

    @Override
    public int getStrokeCap() {
        return super.getStrokeCap();
    }

    @Override
    public void setStrokeCap(int cap) {
        super.setStrokeCap(cap);
    }

    @Override
    public int getStrokeJoin() {
        return super.getStrokeJoin();
    }

    @Override
    public void setStrokeJoin(int join) {
        super.setStrokeJoin(join);
    }

    @Override
    public float getStrokeWidth() {
        return super.getStrokeWidth();
    }

    @Override
    public void setStrokeWidth(float width) {
        super.setStrokeWidth(width);
    }

    @Override
    public float getStrokeMiter() {
        return super.getStrokeMiter();
    }

    @Override
    public void setStrokeMiter(float miter) {
        super.setStrokeMiter(miter);
    }

    @Nullable
    @Override
    public Shader getShader() {
        return super.getShader();
    }

    @Override
    public void setShader(@Nullable Shader shader) {
        super.setShader(shader);
    }

    @Nullable
    @Override
    public ColorFilter getColorFilter() {
        return super.getColorFilter();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        super.setColorFilter(colorFilter);
    }

    @Nullable
    public final BlendMode getBlendMode() {
        icyllis.arc3d.core.BlendMode mode = getBlendModeDirect(this);
        return mode != null ? BlendMode.VALUES[mode.ordinal()] : null;
    }

    public final void setBlendMode(@Nullable BlendMode mode) {
        this.setBlender(mode != null ? mode.mBlendMode : null);
    }

    @Nullable
    @Override
    public MaskFilter getMaskFilter() {
        return super.getMaskFilter();
    }

    @Override
    public void setMaskFilter(@Nullable MaskFilter maskFilter) {
        super.setMaskFilter(maskFilter);
    }

    @Nullable
    @Override
    public ImageFilter getImageFilter() {
        return super.getImageFilter();
    }

    @Override
    public void setImageFilter(@Nullable ImageFilter imageFilter) {
        super.setImageFilter(imageFilter);
    }

    public final int getTextStyle() {
        return this.getFontStyle();
    }

    public int getFontStyle() {
        return this.mFontFlags & 3;
    }

    public final void setTextStyle(int textStyle) {
        this.setFontStyle(textStyle);
    }

    public void setFontStyle(int fontStyle) {
        if ((fontStyle & -4) == 0) {
            this.mFontFlags |= fontStyle;
        } else {
            this.mFontFlags &= -4;
        }
    }

    public float getTextSize() {
        return this.mFontSize;
    }

    public int getFontSize() {
        return (int) ((double) this.mFontSize + 0.5);
    }

    public void setTextSize(float textSize) {
        if (textSize > 0.0F) {
            this.mFontSize = textSize;
        }
    }

    public void setFontSize(int fontSize) {
        if (fontSize > 0) {
            this.mFontSize = (float) fontSize;
        }
    }

    public boolean isTextAntiAlias() {
        return switch(this.mFontFlags & 12) {
            case 8 ->
                false;
            case 12 ->
                true;
            default ->
                this.isAntiAlias();
        };
    }

    public void setTextAntiAlias(boolean textAntiAlias) {
        this.mFontFlags = this.mFontFlags & -13 | (textAntiAlias ? 12 : 8);
    }

    public boolean isLinearText() {
        return (this.mFontFlags & 16) != 0;
    }

    public void setLinearText(boolean linearText) {
        if (linearText) {
            this.mFontFlags &= -17;
        } else {
            this.mFontFlags |= 16;
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.mFontFlags;
        return 31 * result + Float.hashCode(this.mFontSize);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof Paint paint) ? false : super.equals((icyllis.arc3d.core.Paint) paint) && this.mFontFlags == paint.mFontFlags && this.mFontSize == paint.mFontSize;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface TextStyle {
    }
}
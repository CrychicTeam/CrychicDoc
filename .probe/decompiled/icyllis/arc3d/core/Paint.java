package icyllis.arc3d.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class Paint {

    public static final int FILL = 0;

    public static final int STROKE = 1;

    public static final int STROKE_AND_FILL = 2;

    public static final int FILL_AND_STROKE = 2;

    private static final int STYLE_MASK = 3;

    public static final int CAP_BUTT = 0;

    public static final int CAP_ROUND = 4;

    public static final int CAP_SQUARE = 8;

    private static final int CAP_MASK = 12;

    public static final int JOIN_MITER = 0;

    public static final int JOIN_ROUND = 16;

    public static final int JOIN_BEVEL = 32;

    private static final int JOIN_MASK = 48;

    public static final int ALIGN_CENTER = 0;

    public static final int ALIGN_INSIDE = 64;

    public static final int ALIGN_OUTSIDE = 128;

    private static final int ALIGN_MASK = 192;

    public static final int FILTER_MODE_NEAREST = 0;

    public static final int FILTER_MODE_LINEAR = 256;

    private static final int FILTER_MODE_MASK = 256;

    public static final int MIPMAP_MODE_NONE = 0;

    public static final int MIPMAP_MODE_NEAREST = 512;

    public static final int MIPMAP_MODE_LINEAR = 1024;

    private static final int MIPMAP_MODE_MASK = 1536;

    private static final int MAX_ANISOTROPY_SHIFT = 11;

    private static final int MAX_ANISOTROPY_MASK = 63488;

    private static final int ANTI_ALIAS_MASK = 65536;

    private static final int DITHER_MASK = 131072;

    private static final int DEFAULT_FLAGS = 66836;

    private float mR;

    private float mG;

    private float mB;

    private float mA;

    private float mWidth;

    private float mMiterLimit;

    private float mSmoothWidth;

    private Shader mShader;

    private Blender mBlender;

    private MaskFilter mMaskFilter;

    private ColorFilter mColorFilter;

    private ImageFilter mImageFilter;

    private int mFlags;

    public Paint() {
        this.reset();
    }

    public Paint(@Nullable Paint paint) {
        this.set(paint);
    }

    public void reset() {
        this.mR = 1.0F;
        this.mG = 1.0F;
        this.mB = 1.0F;
        this.mA = 1.0F;
        this.mWidth = 2.0F;
        this.mMiterLimit = 4.0F;
        this.mSmoothWidth = 0.0F;
        this.mShader = null;
        this.mBlender = null;
        this.mMaskFilter = null;
        this.mColorFilter = null;
        this.mImageFilter = null;
        this.mFlags = 66836;
    }

    public void set(Paint paint) {
        if (paint == null) {
            this.reset();
        } else {
            this.mR = paint.mR;
            this.mG = paint.mG;
            this.mB = paint.mB;
            this.mA = paint.mA;
            this.mWidth = paint.mWidth;
            this.mMiterLimit = paint.mMiterLimit;
            this.mSmoothWidth = paint.mSmoothWidth;
            this.mShader = paint.mShader;
            this.mBlender = paint.mBlender;
            this.mMaskFilter = paint.mMaskFilter;
            this.mColorFilter = paint.mColorFilter;
            this.mImageFilter = paint.mImageFilter;
            this.mFlags = paint.mFlags;
        }
    }

    public int getColor() {
        return (int) (this.mA * 255.0F + 0.5F) << 24 | (int) (this.mR * 255.0F + 0.5F) << 16 | (int) (this.mG * 255.0F + 0.5F) << 8 | (int) (this.mB * 255.0F + 0.5F);
    }

    public void setColor(int color) {
        this.mR = (float) (color >> 16 & 0xFF) / 255.0F;
        this.mG = (float) (color >> 8 & 0xFF) / 255.0F;
        this.mB = (float) (color & 0xFF) / 255.0F;
        this.mA = (float) (color >>> 24) / 255.0F;
    }

    public float getAlphaF() {
        return this.mA;
    }

    public int getAlpha() {
        return (int) (this.mA * 255.0F + 0.5F);
    }

    public void setAlphaF(float a) {
        this.mA = MathUtil.pin(a, 0.0F, 1.0F);
    }

    public void setAlpha(int a) {
        this.mA = MathUtil.pin((float) a / 255.0F, 0.0F, 1.0F);
    }

    public final float r() {
        return this.mR;
    }

    public final float g() {
        return this.mG;
    }

    public final float b() {
        return this.mB;
    }

    public final float a() {
        return this.mA;
    }

    public final void setRGB(int r, int g, int b) {
        this.mR = MathUtil.pin((float) r / 255.0F, 0.0F, 1.0F);
        this.mG = MathUtil.pin((float) g / 255.0F, 0.0F, 1.0F);
        this.mB = MathUtil.pin((float) b / 255.0F, 0.0F, 1.0F);
    }

    public final void setRGB(float r, float g, float b) {
        this.mR = MathUtil.pin(r, 0.0F, 1.0F);
        this.mG = MathUtil.pin(g, 0.0F, 1.0F);
        this.mB = MathUtil.pin(b, 0.0F, 1.0F);
    }

    public final void setRGBA(int r, int g, int b, int a) {
        this.mR = MathUtil.pin((float) r / 255.0F, 0.0F, 1.0F);
        this.mG = MathUtil.pin((float) g / 255.0F, 0.0F, 1.0F);
        this.mB = MathUtil.pin((float) b / 255.0F, 0.0F, 1.0F);
        this.mA = MathUtil.pin((float) a / 255.0F, 0.0F, 1.0F);
    }

    public final void setRGBA(float r, float g, float b, float a) {
        this.mR = MathUtil.pin(r, 0.0F, 1.0F);
        this.mG = MathUtil.pin(g, 0.0F, 1.0F);
        this.mB = MathUtil.pin(b, 0.0F, 1.0F);
        this.mA = MathUtil.pin(a, 0.0F, 1.0F);
    }

    public void setARGB(int a, int r, int g, int b) {
        this.mR = MathUtil.pin((float) r / 255.0F, 0.0F, 1.0F);
        this.mG = MathUtil.pin((float) g / 255.0F, 0.0F, 1.0F);
        this.mB = MathUtil.pin((float) b / 255.0F, 0.0F, 1.0F);
        this.mA = MathUtil.pin((float) a / 255.0F, 0.0F, 1.0F);
    }

    public void setARGB(float a, float r, float g, float b) {
        this.mR = MathUtil.pin(r, 0.0F, 1.0F);
        this.mG = MathUtil.pin(g, 0.0F, 1.0F);
        this.mB = MathUtil.pin(b, 0.0F, 1.0F);
        this.mA = MathUtil.pin(a, 0.0F, 1.0F);
    }

    public final boolean isAntiAlias() {
        return (this.mFlags & 65536) != 0;
    }

    public final void setAntiAlias(boolean aa) {
        if (aa) {
            this.mFlags |= 65536;
        } else {
            this.mFlags &= -65537;
        }
    }

    public final boolean isDither() {
        return (this.mFlags & 131072) != 0;
    }

    public final void setDither(boolean dither) {
        if (dither) {
            this.mFlags |= 131072;
        } else {
            this.mFlags &= -131073;
        }
    }

    public int getStyle() {
        return this.mFlags & 3;
    }

    public void setStyle(int style) {
        this.mFlags = this.mFlags & -4 | style & 3;
    }

    public final void setStroke(boolean stroke) {
        this.mFlags = this.mFlags & -4 | (stroke ? 1 : 0);
    }

    public int getStrokeCap() {
        return this.mFlags & 12;
    }

    public void setStrokeCap(int cap) {
        this.mFlags = this.mFlags & -13 | cap & 12;
    }

    public int getStrokeJoin() {
        return this.mFlags & 48;
    }

    public void setStrokeJoin(int join) {
        this.mFlags = this.mFlags & -49 | join & 48;
    }

    public final int getStrokeAlign() {
        return this.mFlags & 192;
    }

    public final void setStrokeAlign(int align) {
        this.mFlags = this.mFlags & -193 | align & 192;
    }

    public float getStrokeWidth() {
        return this.mWidth;
    }

    public void setStrokeWidth(float width) {
        this.mWidth = Math.max(width, 0.0F);
    }

    public float getStrokeMiter() {
        return this.mMiterLimit;
    }

    public void setStrokeMiter(float miter) {
        this.mMiterLimit = Math.max(miter, 0.0F);
    }

    public final float getSmoothWidth() {
        return this.mSmoothWidth;
    }

    public final void setSmoothWidth(float smooth) {
        this.mSmoothWidth = Math.max(smooth, 0.0F);
    }

    public final boolean isFilter() {
        return (this.mFlags & 256) != 0;
    }

    public final void setFilter(boolean filter) {
        if (filter) {
            this.mFlags = (this.mFlags | 256) & -63489;
        } else {
            this.mFlags &= -63745;
        }
    }

    public final int getFilterMode() {
        return this.mFlags & 256;
    }

    public final void setFilterMode(int filter) {
        this.mFlags = this.mFlags & -257 | filter & 256;
    }

    public final int getMipmapMode() {
        return this.mFlags & 1536;
    }

    public final void setMipmapMode(int mipmap) {
        this.mFlags = this.mFlags & -1537 | mipmap & 1536;
    }

    public final boolean isAnisotropy() {
        return (this.mFlags & 63488) != 0;
    }

    public final int getMaxAnisotropy() {
        return (this.mFlags & 63488) >> 11;
    }

    public final void setMaxAnisotropy(int maxAnisotropy) {
        this.mFlags = this.mFlags & -63489 | MathUtil.clamp(maxAnisotropy, 0, 16) << 11;
    }

    @Nullable
    public Shader getShader() {
        return this.mShader;
    }

    public void setShader(@Nullable Shader shader) {
        this.mShader = shader;
    }

    @Nullable
    public ColorFilter getColorFilter() {
        return this.mColorFilter;
    }

    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
    }

    public final boolean isSrcOver() {
        return this.mBlender == null || this.mBlender.asBlendMode() == BlendMode.SRC_OVER;
    }

    @Nullable
    public final Blender getBlender() {
        return this.mBlender;
    }

    public final void setBlender(@Nullable Blender blender) {
        this.mBlender = blender;
    }

    @Nullable
    public MaskFilter getMaskFilter() {
        return this.mMaskFilter;
    }

    public void setMaskFilter(@Nullable MaskFilter maskFilter) {
        this.mMaskFilter = maskFilter;
    }

    @Nullable
    public ImageFilter getImageFilter() {
        return this.mImageFilter;
    }

    public void setImageFilter(@Nullable ImageFilter imageFilter) {
        this.mImageFilter = imageFilter;
    }

    public boolean nothingToDraw() {
        BlendMode mode = getBlendModeDirect(this);
        if (mode != null) {
            switch(mode) {
                case SRC_OVER:
                case SRC_ATOP:
                case DST_OUT:
                case DST_OVER:
                case PLUS:
                    if (this.getAlpha() == 0) {
                        return !isBlendedImageFilter(this.mImageFilter);
                    }
                    break;
                case DST:
                    return true;
            }
        }
        return false;
    }

    @Internal
    public final boolean canComputeFastBounds() {
        return this.mImageFilter == null || this.mImageFilter.canComputeFastBounds();
    }

    @Internal
    public final void computeFastBounds(Rect2fc orig, Rect2f storage) {
        int style = this.getStyle();
        if (style == 0 && this.mMaskFilter == null && this.mImageFilter == null) {
            storage.set(orig);
        } else {
            storage.set(orig);
            int align = this.getStrokeAlign();
            if (style != 0 && align != 64 && this.mWidth > 0.0F) {
                float multiplier = 1.0F;
                if (this.getStrokeJoin() == 0) {
                    multiplier = Math.max(multiplier, this.mMiterLimit);
                }
                if (this.getStrokeCap() == 8) {
                    multiplier = Math.max(multiplier, 1.4142135F);
                }
                float stroke = this.mWidth * multiplier;
                if (align == 0) {
                    stroke *= 0.5F;
                }
                storage.inset(-stroke, -stroke);
            }
            if (this.mMaskFilter != null) {
                this.mMaskFilter.computeFastBounds(storage, storage);
            }
            if (this.mImageFilter != null) {
                this.mImageFilter.computeFastBounds(storage, storage);
            }
        }
    }

    public int hashCode() {
        int result = this.mFlags;
        result = 31 * result + Float.hashCode(this.mR);
        result = 31 * result + Float.hashCode(this.mG);
        result = 31 * result + Float.hashCode(this.mB);
        result = 31 * result + Float.hashCode(this.mA);
        result = 31 * result + Float.hashCode(this.mWidth);
        result = 31 * result + Float.hashCode(this.mMiterLimit);
        result = 31 * result + Float.hashCode(this.mSmoothWidth);
        result = 31 * result + Objects.hashCode(this.mShader);
        result = 31 * result + Objects.hashCode(this.mBlender);
        result = 31 * result + Objects.hashCode(this.mMaskFilter);
        result = 31 * result + Objects.hashCode(this.mColorFilter);
        return 31 * result + Objects.hashCode(this.mImageFilter);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return o instanceof Paint paint ? this.equals(paint) : false;
        }
    }

    protected final boolean equals(Paint paint) {
        return this.mFlags == paint.mFlags && this.mR == paint.mR && this.mG == paint.mG && this.mB == paint.mB && this.mA == paint.mA && this.mWidth == paint.mWidth && this.mMiterLimit == paint.mMiterLimit && this.mSmoothWidth == paint.mSmoothWidth && Objects.equals(this.mShader, paint.mShader) && Objects.equals(this.mBlender, paint.mBlender) && Objects.equals(this.mMaskFilter, paint.mMaskFilter) && Objects.equals(this.mColorFilter, paint.mColorFilter) && Objects.equals(this.mImageFilter, paint.mImageFilter);
    }

    public String toString() {
        StringBuilder s = new StringBuilder("Paint{");
        s.append("mColor4f=(");
        s.append(this.mR);
        s.append(", ");
        s.append(this.mG);
        s.append(", ");
        s.append(this.mB);
        s.append(", ");
        s.append(this.mA);
        int style = this.getStyle();
        s.append("), mStyle=");
        if (style == 0) {
            s.append("FILL");
        } else if (style == 1) {
            s.append("STROKE");
        } else {
            s.append("FILL|STROKE");
        }
        int cap = this.getStrokeCap();
        s.append(", mCap=");
        if (cap == 4) {
            s.append("ROUND");
        } else if (cap == 8) {
            s.append("SQUARE");
        } else {
            s.append("BUTT");
        }
        int join = this.getStrokeJoin();
        s.append(", mJoin=");
        if (join == 16) {
            s.append("ROUND");
        } else if (join == 32) {
            s.append("BEVEL");
        } else {
            s.append("MITER");
        }
        int align = this.getStrokeAlign();
        s.append(", mAlign=");
        if (align == 0) {
            s.append("CENTER");
        } else if (align == 64) {
            s.append("INSIDE");
        } else {
            s.append("OUTSIDE");
        }
        s.append(", mAntiAlias=");
        s.append(this.isAntiAlias());
        s.append(", mDither=");
        s.append(this.isDither());
        s.append(", mStrokeWidth=");
        s.append(this.mWidth);
        s.append(", mStrokeMiter=");
        s.append(this.mMiterLimit);
        s.append(", mSmoothWidth=");
        s.append(this.mSmoothWidth);
        s.append(", mShader=");
        s.append(this.mShader);
        s.append(", mBlender=");
        s.append(this.mBlender);
        s.append(", mMaskFilter=");
        s.append(this.mMaskFilter);
        s.append(", mColorFilter=");
        s.append(this.mColorFilter);
        s.append(", mImageFilter=");
        s.append(this.mImageFilter);
        s.append('}');
        return s.toString();
    }

    public static int getAlphaDirect(@Nullable Paint paint) {
        return paint != null ? paint.getAlpha() : 255;
    }

    public static BlendMode getBlendModeDirect(@Nullable Paint paint) {
        if (paint != null) {
            Blender blender = paint.getBlender();
            if (blender != null) {
                return blender.asBlendMode();
            }
        }
        return BlendMode.SRC_OVER;
    }

    public static boolean isBlendedShader(@Nullable Shader shader) {
        return shader != null && !shader.isOpaque();
    }

    public static boolean isBlendedColorFilter(@Nullable ColorFilter filter) {
        return filter != null && !filter.isAlphaUnchanged();
    }

    public static boolean isBlendedImageFilter(@Nullable ImageFilter filter) {
        return filter != null;
    }

    public static boolean isOpaquePaint(@Nullable Paint paint) {
        if (paint == null) {
            return true;
        } else if (paint.getAlpha() == 255 && !isBlendedShader(paint.mShader) && !isBlendedColorFilter(paint.mColorFilter)) {
            BlendMode mode = getBlendModeDirect(paint);
            return mode == BlendMode.SRC_OVER || mode == BlendMode.SRC;
        } else {
            return false;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Align {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Cap {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FilterMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Join {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MipmapMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Style {
    }
}
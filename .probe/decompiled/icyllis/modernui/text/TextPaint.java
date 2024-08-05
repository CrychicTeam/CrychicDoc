package icyllis.modernui.text;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.ColorInt;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.text.CharArrayIterator;
import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.graphics.text.FontPaint;
import icyllis.modernui.graphics.text.GraphemeBreak;
import icyllis.modernui.util.Pools;
import java.util.Locale;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.ApiStatus.Internal;

public class TextPaint extends Paint {

    public static final int UNDERLINE_FLAG = 1024;

    public static final int STRIKETHROUGH_FLAG = 2048;

    private static final Pools.Pool<TextPaint> sPool = Pools.newSynchronizedPool(4);

    private final FontPaint mInternalPaint = new FontPaint();

    private Typeface mTypeface;

    private Locale mLocale;

    @ColorInt
    public int bgColor;

    public int baselineShift;

    @ColorInt
    public int linkColor = -11297803;

    public float density = 1.0F;

    @ColorInt
    public int underlineColor = 0;

    public TextPaint() {
        this.mTypeface = ModernUI.getSelectedTypeface();
        this.mLocale = ModernUI.getSelectedLocale();
    }

    @Internal
    public TextPaint(@NonNull TextPaint paint) {
        this.set(paint);
        this.mTypeface = paint.mTypeface;
        this.mLocale = paint.mLocale;
    }

    @NonNull
    public static TextPaint obtain() {
        TextPaint paint = sPool.acquire();
        return paint == null ? new TextPaint() : paint;
    }

    @Override
    public void recycle() {
        sPool.release(this);
    }

    public void set(@NonNull TextPaint paint) {
        super.set(paint);
        this.mTypeface = paint.mTypeface;
        this.mLocale = paint.mLocale;
        this.bgColor = paint.bgColor;
        this.baselineShift = paint.baselineShift;
    }

    public void setTypeface(@NonNull Typeface typeface) {
        this.mTypeface = typeface;
    }

    @NonNull
    public Typeface getTypeface() {
        return this.mTypeface;
    }

    public void setTextLocale(@NonNull Locale locale) {
        if (!locale.equals(this.mLocale)) {
            this.mLocale = locale;
        }
    }

    @NonNull
    public Locale getTextLocale() {
        return this.mLocale;
    }

    public final boolean isUnderline() {
        return (this.mFontFlags & 1024) != 0;
    }

    public void setUnderline(boolean underline) {
        if (underline) {
            this.mFontFlags |= 1024;
        } else {
            this.mFontFlags &= -1025;
        }
    }

    public float getUnderlineOffset(@NonNull FontMetricsInt fm) {
        return (float) fm.descent / 3.0F;
    }

    public float getUnderlineThickness(@NonNull FontMetricsInt fm) {
        return (float) (-fm.ascent) / 12.0F;
    }

    public final boolean isStrikethrough() {
        return (this.mFontFlags & 2048) != 0;
    }

    public void setStrikethrough(boolean strikethrough) {
        if (strikethrough) {
            this.mFontFlags |= 2048;
        } else {
            this.mFontFlags &= -2049;
        }
    }

    public float getStrikethroughOffset(@NonNull FontMetricsInt fm) {
        return (float) fm.ascent / 2.0F;
    }

    public float getStrikethroughThickness(@NonNull FontMetricsInt fm) {
        return (float) (-fm.ascent) / 12.0F;
    }

    public int getTextRunCursor(@NonNull char[] text, int contextStart, int contextLength, int offset, int op) {
        int contextEnd = contextStart + contextLength;
        if ((contextStart | contextEnd | offset | contextEnd - contextStart | offset - contextStart | contextEnd - offset | text.length - contextEnd | op) >= 0 && op <= 4) {
            return GraphemeBreak.sUseICU ? GraphemeBreak.getTextRunCursorICU(new CharArrayIterator(text, contextStart, contextEnd), this.mLocale, offset, op) : GraphemeBreak.getTextRunCursorImpl(null, text, contextStart, contextLength, offset, op);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public int getTextRunCursor(@NonNull CharSequence text, int contextStart, int contextEnd, int offset, int op) {
        if (!(text instanceof String) && !(text instanceof SpannedString) && !(text instanceof SpannableString)) {
            int contextLen = contextEnd - contextStart;
            char[] buf = new char[contextLen];
            TextUtils.getChars(text, contextStart, contextEnd, buf, 0);
            offset = this.getTextRunCursor(buf, 0, contextLen, offset - contextStart, op);
            return offset == -1 ? -1 : offset + contextStart;
        } else {
            return GraphemeBreak.getTextRunCursor(text.toString(), this.mLocale, contextStart, contextEnd, offset, op);
        }
    }

    int getFontFlags() {
        return this.mFontFlags;
    }

    void setFontFlags(int flags) {
        this.mFontFlags = flags;
    }

    public boolean equalsForTextMeasurement(@Nonnull TextPaint paint) {
        return !this.getInternalPaint().isMetricAffecting(paint.getInternalPaint());
    }

    @NonNull
    public FontMetricsInt getFontMetricsInt() {
        FontMetricsInt fm = new FontMetricsInt();
        this.getFontMetricsInt(fm);
        return fm;
    }

    public int getFontMetricsInt(@Nullable FontMetricsInt fm) {
        return this.getInternalPaint().getFontMetricsInt(fm);
    }

    @NonNull
    public final FontPaint getInternalPaint() {
        FontPaint p = this.mInternalPaint;
        p.setFont(this.mTypeface);
        p.setLocale(this.mLocale);
        p.setFontSize(this.getFontSize());
        p.setFontStyle(this.getFontStyle());
        p.setRenderFlags(FontPaint.computeRenderFlags(this));
        return p;
    }

    @NonNull
    public final FontPaint createInternalPaint() {
        return new FontPaint(this.getInternalPaint());
    }
}
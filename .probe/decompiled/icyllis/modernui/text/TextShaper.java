package icyllis.modernui.text;

import com.ibm.icu.text.Bidi;
import icyllis.modernui.annotation.IntRange;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.text.ShapedText;
import java.util.Objects;

public class TextShaper {

    public static void shapeText(@NonNull CharSequence text, @IntRange(from = 0L) int start, @IntRange(from = 0L) int count, @NonNull TextDirectionHeuristic dir, @NonNull TextPaint paint, @NonNull TextShaper.GlyphsConsumer consumer) {
        Objects.requireNonNull(text);
        if (!(text instanceof Spanned)) {
            consumer.accept(start, count, shapeText(text, start, count, dir, paint), paint, 0.0F, 0.0F);
        } else {
            MeasuredParagraph mp = MeasuredParagraph.buildForBidi(text, start, start + count, dir, null);
            TextLine tl = TextLine.obtain();
            try {
                tl.set(paint, text, start, start + count, mp.getParagraphDir(), mp.getDirections(0, count), false, null, -1, -1);
                tl.shape(consumer);
            } finally {
                tl.recycle();
                mp.recycle();
            }
        }
    }

    @NonNull
    public static ShapedText shapeText(@NonNull char[] text, @IntRange(from = 0L) int start, @IntRange(from = 0L) int count, @NonNull TextDirectionHeuristic dir, @NonNull TextPaint paint) {
        Objects.requireNonNull(dir);
        Objects.checkFromIndexSize(start, count, text.length);
        int bidiFlags;
        if ((dir == TextDirectionHeuristics.LTR || dir == TextDirectionHeuristics.FIRSTSTRONG_LTR || dir == TextDirectionHeuristics.ANYRTL_LTR) && !Bidi.requiresBidi(text, start, start + count)) {
            bidiFlags = 4;
        } else if (dir == TextDirectionHeuristics.LTR) {
            bidiFlags = 0;
        } else if (dir == TextDirectionHeuristics.RTL) {
            bidiFlags = 1;
        } else if (dir == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
            bidiFlags = 2;
        } else if (dir == TextDirectionHeuristics.FIRSTSTRONG_RTL) {
            bidiFlags = 3;
        } else {
            boolean isRtl = dir.isRtl(text, start, start + count);
            bidiFlags = isRtl ? 1 : 0;
        }
        if (bidiFlags != 4 && (start != 0 || count != text.length)) {
            char[] para = new char[count];
            System.arraycopy(text, start, para, 0, count);
            return new ShapedText(para, 0, count, 0, count, bidiFlags, paint.getInternalPaint());
        } else {
            return new ShapedText(text, start, start + count, start, start + count, bidiFlags, paint.getInternalPaint());
        }
    }

    @NonNull
    public static ShapedText shapeText(@NonNull CharSequence text, @IntRange(from = 0L) int start, @IntRange(from = 0L) int count, @NonNull TextDirectionHeuristic dir, @NonNull TextPaint paint) {
        Objects.checkFromIndexSize(start, count, text.length());
        boolean mayTemp = dir == TextDirectionHeuristics.LTR || dir == TextDirectionHeuristics.FIRSTSTRONG_LTR || dir == TextDirectionHeuristics.ANYRTL_LTR;
        char[] buf;
        if (mayTemp) {
            buf = TextUtils.obtain(count);
        } else {
            buf = new char[count];
        }
        ShapedText var7;
        try {
            TextUtils.getChars(text, start, start + count, buf, 0);
            var7 = shapeText(buf, 0, count, dir, paint);
        } finally {
            if (mayTemp) {
                TextUtils.recycle(buf);
            }
        }
        return var7;
    }

    @NonNull
    public static ShapedText shapeTextRun(@NonNull char[] text, @IntRange(from = 0L) int start, @IntRange(from = 0L) int count, int contextStart, int contextCount, boolean isRtl, @NonNull TextPaint paint) {
        int bidiFlags = isRtl ? 5 : 4;
        return new ShapedText(text, contextStart, contextStart + contextCount, start, start + count, bidiFlags, paint.getInternalPaint());
    }

    @NonNull
    public static ShapedText shapeTextRun(@NonNull CharSequence text, @IntRange(from = 0L) int start, @IntRange(from = 0L) int count, int contextStart, int contextCount, boolean isRtl, @NonNull TextPaint paint) {
        Objects.checkFromIndexSize(contextStart, contextCount, text.length());
        char[] buf = TextUtils.obtain(contextCount);
        ShapedText var8;
        try {
            TextUtils.getChars(text, contextStart, contextStart + contextCount, buf, 0);
            var8 = shapeTextRun(buf, start - contextStart, count, 0, contextCount, isRtl, paint);
        } finally {
            TextUtils.recycle(buf);
        }
        return var8;
    }

    private TextShaper() {
    }

    public interface GlyphsConsumer {

        void accept(@IntRange(from = 0L) int var1, @IntRange(from = 0L) int var2, @NonNull ShapedText var3, @NonNull TextPaint var4, float var5, float var6);
    }
}
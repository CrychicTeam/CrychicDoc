package icyllis.modernui.text;

import com.ibm.icu.util.ULocale;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.text.Font;
import icyllis.modernui.graphics.text.LayoutPiece;
import icyllis.modernui.graphics.text.ShapedText;
import icyllis.modernui.text.style.AbsoluteSizeSpan;
import icyllis.modernui.text.style.AlignmentSpan;
import icyllis.modernui.text.style.BackgroundColorSpan;
import icyllis.modernui.text.style.CharacterStyle;
import icyllis.modernui.text.style.ForegroundColorSpan;
import icyllis.modernui.text.style.LeadingMarginSpan;
import icyllis.modernui.text.style.LineBackgroundSpan;
import icyllis.modernui.text.style.LocaleSpan;
import icyllis.modernui.text.style.RelativeSizeSpan;
import icyllis.modernui.text.style.StrikethroughSpan;
import icyllis.modernui.text.style.StyleSpan;
import icyllis.modernui.text.style.SubscriptSpan;
import icyllis.modernui.text.style.SuperscriptSpan;
import icyllis.modernui.text.style.TrailingMarginSpan;
import icyllis.modernui.text.style.TypefaceSpan;
import icyllis.modernui.text.style.URLSpan;
import icyllis.modernui.text.style.UnderlineSpan;
import icyllis.modernui.util.Parcel;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class TextUtils {

    private static final char[][] sTemp = new char[4][];

    static final char ELLIPSIS_FILLER = '\ufeff';

    private static final String ELLIPSIS_NORMAL = "…";

    private static final char[] ELLIPSIS_NORMAL_ARRAY = "…".toCharArray();

    @Internal
    public static final int FIRST_SPAN = 1;

    @Internal
    public static final int ALIGNMENT_SPAN = 1;

    @Internal
    public static final int FOREGROUND_COLOR_SPAN = 2;

    @Internal
    public static final int RELATIVE_SIZE_SPAN = 3;

    @Internal
    public static final int SCALE_X_SPAN = 4;

    @Internal
    public static final int STRIKETHROUGH_SPAN = 5;

    @Internal
    public static final int UNDERLINE_SPAN = 6;

    @Internal
    public static final int STYLE_SPAN = 7;

    @Internal
    public static final int BULLET_SPAN = 8;

    @Internal
    public static final int QUOTE_SPAN = 9;

    @Internal
    public static final int LEADING_MARGIN_SPAN = 10;

    @Internal
    public static final int URL_SPAN = 11;

    @Internal
    public static final int BACKGROUND_COLOR_SPAN = 12;

    @Internal
    public static final int TYPEFACE_SPAN = 13;

    @Internal
    public static final int SUPERSCRIPT_SPAN = 14;

    @Internal
    public static final int SUBSCRIPT_SPAN = 15;

    @Internal
    public static final int ABSOLUTE_SIZE_SPAN = 16;

    @Internal
    public static final int TEXT_APPEARANCE_SPAN = 17;

    @Internal
    public static final int ANNOTATION = 18;

    @Internal
    public static final int SUGGESTION_SPAN = 19;

    @Internal
    public static final int SPELL_CHECK_SPAN = 20;

    @Internal
    public static final int SUGGESTION_RANGE_SPAN = 21;

    @Internal
    public static final int EASY_EDIT_SPAN = 22;

    @Internal
    public static final int LOCALE_SPAN = 23;

    @Internal
    public static final int TTS_SPAN = 24;

    @Internal
    public static final int ACCESSIBILITY_CLICKABLE_SPAN = 25;

    @Internal
    public static final int ACCESSIBILITY_URL_SPAN = 26;

    @Internal
    public static final int LINE_BACKGROUND_SPAN = 27;

    @Internal
    public static final int LINE_HEIGHT_SPAN = 28;

    @Internal
    public static final int ACCESSIBILITY_REPLACEMENT_SPAN = 29;

    @Internal
    public static final int TRAILING_MARGIN_SPAN = 30;

    @Internal
    public static final int LAST_SPAN = 30;

    private static final String[] sBinaryCompacts = new String[] { "bytes", "KB", "MB", "GB", "TB", "PB", "EB" };

    private TextUtils() {
    }

    @NonNull
    public static String getEllipsisString(@NonNull TextUtils.TruncateAt method) {
        return "…";
    }

    @Internal
    @NonNull
    public static char[] getEllipsisChars(@NonNull TextUtils.TruncateAt method) {
        return ELLIPSIS_NORMAL_ARRAY;
    }

    @Internal
    @NonNull
    public static char[] obtain(int len) {
        if (len > 2000) {
            return new char[len];
        } else {
            char[] buf = null;
            synchronized (sTemp) {
                char[][] pool = sTemp;
                for (int i = pool.length - 1; i >= 0; i--) {
                    if ((buf = pool[i]) != null && buf.length >= len) {
                        pool[i] = null;
                        break;
                    }
                }
            }
            if (buf == null || buf.length < len) {
                buf = new char[len];
            }
            return buf;
        }
    }

    @Internal
    public static void recycle(@NonNull char[] temp) {
        if (temp.length <= 2000) {
            synchronized (sTemp) {
                char[][] pool = sTemp;
                for (int i = 0; i < pool.length; i++) {
                    if (pool[i] == null) {
                        pool[i] = temp;
                        break;
                    }
                }
            }
        }
    }

    public static CharSequence stringOrSpannedString(CharSequence source) {
        if (source == null) {
            return null;
        } else if (source instanceof SpannedString) {
            return source;
        } else {
            return (CharSequence) (source instanceof Spanned ? new SpannedString(source) : source.toString());
        }
    }

    public static boolean isEmpty(@Nullable CharSequence csq) {
        return csq == null || csq.isEmpty();
    }

    public static boolean contentEquals(@Nullable CharSequence a, @Nullable CharSequence b) {
        if (a == b) {
            return true;
        } else {
            int length;
            if (a != null && b != null && (length = a.length()) == b.length()) {
                if (a instanceof String && b instanceof String) {
                    return a.equals(b);
                } else {
                    for (int i = 0; i < length; i++) {
                        if (a.charAt(i) != b.charAt(i)) {
                            return false;
                        }
                    }
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public static void getChars(@NonNull CharSequence s, int srcBegin, int srcEnd, @NonNull char[] dst, int dstBegin) {
        if (s instanceof String) {
            ((String) s).getChars(srcBegin, srcEnd, dst, dstBegin);
        } else if (s instanceof GetChars) {
            ((GetChars) s).getChars(srcBegin, srcEnd, dst, dstBegin);
        } else if (s instanceof StringBuffer) {
            ((StringBuffer) s).getChars(srcBegin, srcEnd, dst, dstBegin);
        } else if (s instanceof StringBuilder) {
            ((StringBuilder) s).getChars(srcBegin, srcEnd, dst, dstBegin);
        } else if (s instanceof CharBuffer buf) {
            buf.get(buf.position() + srcBegin, dst, dstBegin, srcEnd - srcBegin);
        } else {
            for (int i = srcBegin; i < srcEnd; i++) {
                dst[dstBegin++] = s.charAt(i);
            }
        }
    }

    @NonNull
    public static <T> List<T> removeEmptySpans(@NonNull List<T> spans, @NonNull Spanned spanned) {
        List<T> copy = null;
        for (int i = 0; i < spans.size(); i++) {
            T span = (T) spans.get(i);
            int start = spanned.getSpanStart(span);
            int end = spanned.getSpanEnd(span);
            if (start == end) {
                if (copy == null) {
                    copy = new ArrayList(i);
                    for (int j = 0; j < i; j++) {
                        copy.add(spans.get(j));
                    }
                }
            } else if (copy != null) {
                copy.add(span);
            }
        }
        return copy == null ? spans : copy;
    }

    public static String substring(CharSequence source, int start, int end) {
        if (source instanceof String) {
            return ((String) source).substring(start, end);
        } else if (source instanceof StringBuilder) {
            return ((StringBuilder) source).substring(start, end);
        } else if (source instanceof StringBuffer) {
            return ((StringBuffer) source).substring(start, end);
        } else if (source instanceof SpannableStringInternal) {
            return source.toString().substring(start, end);
        } else {
            char[] temp = obtain(end - start);
            getChars(source, start, end, temp, 0);
            String ret = new String(temp, 0, end - start);
            recycle(temp);
            return ret;
        }
    }

    public static int indexOf(CharSequence s, char ch) {
        return indexOf(s, ch, 0);
    }

    public static int indexOf(CharSequence s, char ch, int start) {
        return s instanceof String ? ((String) s).indexOf(ch, start) : indexOf(s, ch, start, s.length());
    }

    public static int indexOf(@NonNull CharSequence s, char ch, int start, int end) {
        Class<? extends CharSequence> c = s.getClass();
        if (!(s instanceof GetChars) && c != StringBuffer.class && c != StringBuilder.class && c != String.class && !(s instanceof CharBuffer)) {
            for (int i = start; i < end; i++) {
                if (s.charAt(i) == ch) {
                    return i;
                }
            }
            return -1;
        } else {
            char[] temp = obtain(500);
            while (start < end) {
                int segend = start + 500;
                if (segend > end) {
                    segend = end;
                }
                getChars(s, start, segend, temp, 0);
                int count = segend - start;
                for (int ix = 0; ix < count; ix++) {
                    if (temp[ix] == ch) {
                        recycle(temp);
                        return ix + start;
                    }
                }
                start = segend;
            }
            recycle(temp);
            return -1;
        }
    }

    public static int lastIndexOf(CharSequence s, char ch) {
        return lastIndexOf(s, ch, s.length() - 1);
    }

    public static int lastIndexOf(CharSequence s, char ch, int last) {
        Class<? extends CharSequence> c = s.getClass();
        return c == String.class ? ((String) s).lastIndexOf(ch, last) : lastIndexOf(s, ch, 0, last);
    }

    public static int lastIndexOf(CharSequence s, char ch, int start, int last) {
        if (last < 0) {
            return -1;
        } else {
            if (last >= s.length()) {
                last = s.length() - 1;
            }
            int end = last + 1;
            Class<? extends CharSequence> c = s.getClass();
            if (!(s instanceof GetChars) && c != StringBuffer.class && c != StringBuilder.class && c != String.class && !(s instanceof CharBuffer)) {
                for (int i = end - 1; i >= start; i--) {
                    if (s.charAt(i) == ch) {
                        return i;
                    }
                }
                return -1;
            } else {
                char[] temp = obtain(500);
                while (start < end) {
                    int segstart = end - 500;
                    if (segstart < start) {
                        segstart = start;
                    }
                    getChars(s, segstart, end, temp, 0);
                    int count = end - segstart;
                    for (int ix = count - 1; ix >= 0; ix--) {
                        if (temp[ix] == ch) {
                            recycle(temp);
                            return ix + segstart;
                        }
                    }
                    end = segstart;
                }
                recycle(temp);
                return -1;
            }
        }
    }

    public static void writeToParcel(@Nullable CharSequence cs, @NonNull Parcel dest, int flags) {
        if (cs == null) {
            dest.writeInt(0);
        } else if (cs instanceof Spanned sp) {
            dest.writeInt(2);
            dest.writeString(cs.toString());
            for (Object o : sp.getSpans(0, cs.length(), Object.class)) {
                Object target = o;
                if (o instanceof CharacterStyle) {
                    target = ((CharacterStyle) o).getUnderlying();
                }
                if (target instanceof ParcelableSpan span) {
                    int id = span.getSpanTypeId();
                    if (id < 1 || id > 30) {
                        throw new AssertionError(id);
                    }
                    dest.writeInt(id);
                    span.writeToParcel(dest, flags);
                    dest.writeInt(sp.getSpanStart(o));
                    dest.writeInt(sp.getSpanEnd(o));
                    dest.writeInt(sp.getSpanFlags(o));
                }
            }
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            dest.writeString(cs.toString());
        }
    }

    @Nullable
    public static CharSequence createFromParcel(@NonNull Parcel p) {
        int type = p.readInt();
        if (type == 0) {
            return null;
        } else {
            String s = p.readString();
            if (type == 1) {
                return s;
            } else {
                assert type == 2 && s != null;
                SpannableString sp = new SpannableString(s);
                while ((type = p.readInt()) != 0) {
                    switch(type) {
                        case 1:
                            readSpan(p, sp, new AlignmentSpan.Standard(p));
                            break;
                        case 2:
                            readSpan(p, sp, new ForegroundColorSpan(p));
                            break;
                        case 3:
                            readSpan(p, sp, new RelativeSizeSpan(p));
                        case 4:
                        case 8:
                        case 9:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 24:
                        case 25:
                        case 26:
                        case 28:
                        case 29:
                        default:
                            break;
                        case 5:
                            readSpan(p, sp, new StrikethroughSpan(p));
                            break;
                        case 6:
                            readSpan(p, sp, new UnderlineSpan(p));
                            break;
                        case 7:
                            readSpan(p, sp, new StyleSpan(p));
                            break;
                        case 10:
                            readSpan(p, sp, new LeadingMarginSpan.Standard(p));
                            break;
                        case 11:
                            readSpan(p, sp, new URLSpan(p));
                            break;
                        case 12:
                            readSpan(p, sp, new BackgroundColorSpan(p));
                            break;
                        case 13:
                            readSpan(p, sp, new TypefaceSpan(p));
                            break;
                        case 14:
                            readSpan(p, sp, new SuperscriptSpan(p));
                            break;
                        case 15:
                            readSpan(p, sp, new SubscriptSpan(p));
                            break;
                        case 16:
                            readSpan(p, sp, new AbsoluteSizeSpan(p));
                            break;
                        case 23:
                            readSpan(p, sp, new LocaleSpan(p));
                            break;
                        case 27:
                            readSpan(p, sp, new LineBackgroundSpan.Standard(p));
                            break;
                        case 30:
                            readSpan(p, sp, new TrailingMarginSpan.Standard(p));
                    }
                }
                return sp;
            }
        }
    }

    private static void readSpan(Parcel p, Spannable sp, Object o) {
        sp.setSpan(o, p.readInt(), p.readInt(), p.readInt());
    }

    public static void drawTextRun(@NonNull Canvas canvas, @NonNull char[] text, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, @NonNull TextPaint paint) {
        if ((start | end | contextStart | contextEnd | start - contextStart | end - start | contextEnd - end | text.length - contextEnd) < 0) {
            throw new IndexOutOfBoundsException();
        } else if (start != end) {
            ShapedText.doLayoutRun(text, contextStart, contextEnd, start, end, isRtl, paint.getInternalPaint(), null, (piece, offsetX) -> drawTextRun(canvas, piece, x + offsetX, y, paint));
        }
    }

    public static void drawTextRun(@NonNull Canvas canvas, @NonNull CharSequence text, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, @NonNull TextPaint paint) {
        if ((start | end | contextStart | contextEnd | start - contextStart | end - start | contextEnd - end | text.length() - contextEnd) < 0) {
            throw new IndexOutOfBoundsException();
        } else if (start != end) {
            int len = contextEnd - contextStart;
            char[] buf = obtain(len);
            getChars(text, contextStart, contextEnd, buf, 0);
            ShapedText.doLayoutRun(buf, 0, len, start - contextStart, end - contextStart, isRtl, paint.getInternalPaint(), null, (piece, offsetX) -> drawTextRun(canvas, piece, x + offsetX, y, paint));
            recycle(buf);
        }
    }

    @Internal
    static void drawTextRun(@NonNull Canvas canvas, @NonNull LayoutPiece piece, float x, float y, @NonNull Paint paint) {
        if (piece.getAdvance() != 0.0F && piece.getGlyphs().length != 0 && !canvas.quickReject(x, y + (float) piece.getAscent(), x + piece.getAdvance(), y + (float) piece.getDescent())) {
            int nGlyphs = piece.getGlyphCount();
            if (nGlyphs != 0) {
                Font lastFont = piece.getFont(0);
                int lastPos = 0;
                int currPos;
                for (currPos = 1; currPos < nGlyphs; currPos++) {
                    Font curFont = piece.getFont(currPos);
                    if (lastFont != curFont) {
                        canvas.drawGlyphs(piece.getGlyphs(), lastPos, piece.getPositions(), lastPos << 1, currPos - lastPos, lastFont, x, y, paint);
                        lastFont = curFont;
                        lastPos = currPos;
                    }
                }
                canvas.drawGlyphs(piece.getGlyphs(), lastPos, piece.getPositions(), lastPos << 1, currPos - lastPos, lastFont, x, y, paint);
            }
        }
    }

    @NonNull
    public static CharSequence ellipsize(@NonNull CharSequence text, @NonNull TextPaint p, float avail, @NonNull TextUtils.TruncateAt where) {
        return ellipsize(text, p, avail, where, false, null);
    }

    @NonNull
    public static CharSequence ellipsize(@NonNull CharSequence text, @NonNull TextPaint paint, float avail, @NonNull TextUtils.TruncateAt where, boolean preserveLength, @Nullable TextUtils.EllipsizeCallback callback) {
        return ellipsize(text, paint, avail, where, preserveLength, callback, TextDirectionHeuristics.FIRSTSTRONG_LTR, getEllipsisString(where));
    }

    @NonNull
    private static CharSequence ellipsize(@NonNull CharSequence text, @NonNull TextPaint paint, float avail, @NonNull TextUtils.TruncateAt where, boolean preserveLength, @Nullable TextUtils.EllipsizeCallback callback, @NonNull TextDirectionHeuristic textDir, @NonNull String ellipsis) {
        int len = text.length();
        MeasuredParagraph mt = null;
        float ellipsisWidth;
        try {
            mt = MeasuredParagraph.buildForStaticLayout(paint, null, ellipsis, 0, ellipsis.length(), textDir, false, null);
            ellipsisWidth = mt.getAdvance(0, ellipsis.length());
        } finally {
            if (mt != null) {
                mt.recycle();
                MeasuredParagraph var30 = null;
            }
        }
        String ss;
        try {
            mt = MeasuredParagraph.buildForStaticLayout(paint, null, text, 0, text.length(), textDir, false, null);
            float width = mt.getAdvance(0, text.length());
            if (width <= avail) {
                if (callback != null) {
                    callback.ellipsized(0, 0);
                }
                return text;
            }
            avail -= ellipsisWidth;
            int left = 0;
            int right = len;
            if (avail >= 0.0F) {
                if (where == TextUtils.TruncateAt.START) {
                    right = len - mt.breakText(len, false, avail);
                } else if (where == TextUtils.TruncateAt.END) {
                    left = mt.breakText(len, true, avail);
                } else {
                    right = len - mt.breakText(len, false, avail / 2.0F);
                    avail -= mt.getAdvance(right, len);
                    left = mt.breakText(right, true, avail);
                }
            }
            if (callback != null) {
                callback.ellipsized(left, right);
            }
            char[] buf = mt.getChars();
            Spanned sp = text instanceof Spanned ? (Spanned) text : null;
            int removed = right - left;
            int remaining = len - removed;
            if (!preserveLength) {
                if (remaining == 0) {
                    return "";
                }
                if (sp != null) {
                    SpannableStringBuilder ssb = new SpannableStringBuilder();
                    ssb.append(text, 0, left);
                    ssb.append(ellipsis);
                    ssb.append(text, right, len);
                    return ssb;
                }
                return String.valueOf(buf, 0, left) + ellipsis + String.valueOf(buf, right, len - right);
            }
            if (remaining > 0 && removed >= ellipsis.length()) {
                ellipsis.getChars(0, ellipsis.length(), buf, left);
                left += ellipsis.length();
            }
            for (int i = left; i < right; i++) {
                buf[i] = '\ufeff';
            }
            String s = new String(buf, 0, len);
            if (sp != null) {
                SpannableString ssx = new SpannableString(s);
                copySpansFrom(sp, 0, len, Object.class, ssx, 0);
                return ssx;
            }
            ss = s;
        } finally {
            if (mt != null) {
                mt.recycle();
            }
        }
        return ss;
    }

    public static CharSequence concat(@NonNull CharSequence... elements) {
        if (elements.length == 0) {
            return "";
        } else {
            CharSequence first = elements[0];
            if (elements.length == 1) {
                return first;
            } else {
                boolean spanned = first instanceof Spanned;
                for (int i = 1; !spanned && i < elements.length; i++) {
                    spanned = elements[i] instanceof Spanned;
                }
                if (spanned) {
                    SpannableStringBuilder ssb = new SpannableStringBuilder();
                    for (CharSequence piece : elements) {
                        ssb.append((CharSequence) (piece == null ? "null" : piece));
                    }
                    return new SpannedString(ssb);
                } else {
                    return String.join("", elements);
                }
            }
        }
    }

    public static CharSequence concat(@NonNull Iterable<? extends CharSequence> elements) {
        Iterator<? extends CharSequence> it = elements.iterator();
        if (!it.hasNext()) {
            return "";
        } else {
            CharSequence first = (CharSequence) it.next();
            if (!it.hasNext()) {
                return first;
            } else {
                boolean spanned = first instanceof Spanned;
                while (!spanned && it.hasNext()) {
                    spanned = it.next() instanceof Spanned;
                }
                if (spanned) {
                    SpannableStringBuilder ssb = new SpannableStringBuilder();
                    for (CharSequence piece : elements) {
                        ssb.append((CharSequence) (piece == null ? "null" : piece));
                    }
                    return new SpannedString(ssb);
                } else {
                    return String.join("", elements);
                }
            }
        }
    }

    @NonNull
    public static CharSequence join(@NonNull CharSequence delimiter, @NonNull CharSequence... elements) {
        if (elements.length == 0) {
            return "";
        } else {
            CharSequence first = elements[0];
            if (elements.length == 1) {
                return (CharSequence) (first instanceof Spanned ? SpannedString.valueOf(first) : String.valueOf(first));
            } else {
                boolean spanned = first instanceof Spanned || delimiter instanceof Spanned;
                for (int i = 1; !spanned && i < elements.length; i++) {
                    spanned = elements[i] instanceof Spanned;
                }
                if (spanned) {
                    SpannableStringBuilder ssb = new SpannableStringBuilder();
                    ssb.append((CharSequence) (first == null ? "null" : first));
                    for (int i = 1; i < elements.length; i++) {
                        ssb.append(delimiter);
                        CharSequence piece = elements[i];
                        ssb.append((CharSequence) (piece == null ? "null" : piece));
                    }
                    return new SpannedString(ssb);
                } else {
                    return String.join(delimiter, elements);
                }
            }
        }
    }

    @NonNull
    public static CharSequence join(@NonNull CharSequence delimiter, @NonNull Iterable<? extends CharSequence> elements) {
        Iterator<? extends CharSequence> it = elements.iterator();
        if (!it.hasNext()) {
            return "";
        } else {
            CharSequence first = (CharSequence) it.next();
            if (!it.hasNext()) {
                return (CharSequence) (first instanceof Spanned ? SpannedString.valueOf(first) : String.valueOf(first));
            } else {
                boolean spanned = first instanceof Spanned || delimiter instanceof Spanned;
                while (!spanned && it.hasNext()) {
                    spanned = it.next() instanceof Spanned;
                }
                if (spanned) {
                    SpannableStringBuilder ssb = new SpannableStringBuilder();
                    it = elements.iterator();
                    it.next();
                    ssb.append((CharSequence) (first == null ? "null" : first));
                    do {
                        ssb.append(delimiter);
                        CharSequence piece = (CharSequence) it.next();
                        ssb.append((CharSequence) (piece == null ? "null" : piece));
                    } while (it.hasNext());
                    return new SpannedString(ssb);
                } else {
                    return String.join(delimiter, elements);
                }
            }
        }
    }

    @NonNull
    public static String binaryCompact(long num) {
        if (num <= 0L) {
            return "0 bytes";
        } else if (num < 1024L) {
            return num + " bytes";
        } else {
            int i = (63 - Long.numberOfLeadingZeros(num)) / 10;
            return String.format("%.2f %s", (double) num / (double) (1L << i * 10), sBinaryCompacts[i]);
        }
    }

    public static void binaryCompact(@NonNull Appendable a, long num) {
        try {
            if (num <= 0L) {
                a.append("0 bytes");
            } else if (num < 1024L) {
                if (a instanceof StringBuilder) {
                    ((StringBuilder) a).append(num);
                } else {
                    a.append(String.valueOf(num));
                }
                a.append(" bytes");
            } else {
                int i = (63 - Long.numberOfLeadingZeros(num)) / 10;
                new Formatter(a).format("%.2f %s", (double) num / (double) (1L << i * 10), sBinaryCompacts[i]);
            }
        } catch (IOException var4) {
        }
    }

    public static void copySpansFrom(@NonNull Spanned source, int start, int end, @Nullable Class<?> type, @NonNull Spannable dest, int destoff) {
        if (type == null) {
            type = Object.class;
        }
        for (Object span : source.getSpans(start, end, type)) {
            int st = source.getSpanStart(span);
            int en = source.getSpanEnd(span);
            int fl = source.getSpanFlags(span);
            if (st < start) {
                st = start;
            }
            if (en > end) {
                en = end;
            }
            dest.setSpan(span, st - start + destoff, en - start + destoff, fl);
        }
    }

    static boolean couldAffectRtl(char c) {
        return 1424 <= c && c <= 2303 || c == 8206 || c == 8207 || 8234 <= c && c <= 8238 || 8294 <= c && c <= 8297 || '\ud800' <= c && c <= '\udfff' || 'יִ' <= c && c <= '\ufdff' || 'ﹰ' <= c && c <= '\ufefe';
    }

    public static int getLayoutDirectionFromLocale(@Nullable Locale locale) {
        return locale != null && !locale.equals(Locale.ROOT) && ULocale.forLocale(locale).isRightToLeft() ? 1 : 0;
    }

    @NonNull
    public static String validateSurrogatePairs(@NonNull String text) {
        int n = text.length();
        StringBuilder b = null;
        for (int i = 0; i < n; i++) {
            char c1 = text.charAt(i);
            if (Character.isHighSurrogate(c1) && i + 1 < n) {
                char c2 = text.charAt(i + 1);
                if (Character.isLowSurrogate(c2)) {
                    if (b != null) {
                        b.append(c1).append(c2);
                    }
                    i++;
                } else {
                    if (b == null) {
                        b = new StringBuilder(n);
                        b.append(text, 0, i);
                    }
                    b.append('�');
                }
            } else if (Character.isSurrogate(c1)) {
                if (b == null) {
                    b = new StringBuilder(n);
                    b.append(text, 0, i);
                }
                b.append('�');
            } else if (b != null) {
                b.append(c1);
            }
        }
        return b != null ? b.toString() : text;
    }

    public static int distance(@NonNull CharSequence a, @NonNull CharSequence b) {
        if (a == b) {
            return 0;
        } else {
            int m = a.length();
            int n = b.length();
            if (m != 0 && n != 0) {
                return m < n ? distance0(b, a, n, m) : distance0(a, b, m, n);
            } else {
                return m | n;
            }
        }
    }

    private static int distance0(@NonNull CharSequence a, @NonNull CharSequence b, int m, int n) {
        int[] d = new int[n + 1];
        int j = 1;
        while (j <= n) {
            d[j] = j++;
        }
        for (int i = 1; i <= m; i++) {
            d[0] = i;
            int w = i - 1;
            for (int var9 = 1; var9 <= n; var9++) {
                int c = Math.min(Math.min(d[var9], d[var9 - 1]) + 1, a.charAt(i - 1) == b.charAt(var9 - 1) ? w : w + 1);
                w = d[var9];
                d[var9] = c;
            }
        }
        return d[n];
    }

    @FunctionalInterface
    public interface EllipsizeCallback {

        void ellipsized(int var1, int var2);
    }

    public static enum TruncateAt {

        START, MIDDLE, END, @Deprecated
        MARQUEE
    }
}
package icyllis.modernui.graphics.text;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.BreakIterator;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class GraphemeBreak {

    public static final int AFTER = 0;

    public static final int AT_OR_AFTER = 1;

    public static final int BEFORE = 2;

    public static final int AT_OR_BEFORE = 3;

    public static final int AT = 4;

    public static boolean sUseICU = true;

    private GraphemeBreak() {
    }

    public static int getTextRunCursor(@Nonnull String text, @Nonnull Locale locale, int contextStart, int contextEnd, int offset, int op) {
        if ((contextStart | contextEnd | offset | contextEnd - contextStart | offset - contextStart | contextEnd - offset | text.length() - contextEnd | op) >= 0 && op <= 4) {
            return sUseICU ? getTextRunCursorICU(new StringCharacterIterator(text, contextStart, contextEnd, contextStart), locale, offset, op) : getTextRunCursorImpl(null, text.toCharArray(), contextStart, contextEnd - contextStart, offset, op);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public static void forTextRun(@Nonnull char[] text, @Nonnull Locale locale, int contextStart, int contextEnd, @Nonnull GraphemeBreak.ClusterConsumer consumer) {
        if (sUseICU) {
            BreakIterator breaker = BreakIterator.getCharacterInstance(locale);
            breaker.setText(new CharArrayIterator(text, contextStart, contextEnd));
            int prevOffset = contextStart;
            int offset;
            while ((offset = breaker.following(prevOffset)) != -1) {
                consumer.accept(prevOffset, offset);
                prevOffset = offset;
            }
        } else {
            int count = contextEnd - contextStart;
            int prevOffset = contextStart;
            int offset;
            while ((offset = getTextRunCursorImpl(null, text, contextStart, count, prevOffset, 0)) != prevOffset) {
                consumer.accept(prevOffset, offset);
                prevOffset = offset;
            }
        }
    }

    public static int getTextRunCursorICU(CharacterIterator text, Locale locale, int offset, int op) {
        int oof = offset;
        BreakIterator breaker = BreakIterator.getCharacterInstance(locale);
        breaker.setText(text);
        switch(op) {
            case 0:
                offset = breaker.following(offset);
                break;
            case 1:
                if (!breaker.isBoundary(offset)) {
                    offset = breaker.following(offset);
                }
                break;
            case 2:
                offset = breaker.preceding(offset);
                break;
            case 3:
                if (!breaker.isBoundary(offset)) {
                    offset = breaker.preceding(offset);
                }
                break;
            case 4:
                if (!breaker.isBoundary(offset)) {
                    return -1;
                }
        }
        return offset == -1 ? oof : offset;
    }

    public static int getTextRunCursorImpl(@Nullable float[] advances, @Nonnull char[] buf, int start, int count, int offset, int op) {
        switch(op) {
            case 0:
                if (offset < start + count) {
                    offset++;
                }
            case 1:
                while (!isGraphemeBreak(advances, buf, start, count, offset)) {
                    offset++;
                }
                break;
            case 2:
                if (offset > start) {
                    offset--;
                }
            case 3:
                while (!isGraphemeBreak(advances, buf, start, count, offset)) {
                    offset--;
                }
                break;
            case 4:
                if (!isGraphemeBreak(advances, buf, start, count, offset)) {
                    offset = -1;
                }
        }
        return offset;
    }

    public static boolean isGraphemeBreak(@Nullable float[] advances, @Nonnull char[] buf, int start, int count, int offset) {
        if (offset <= start || offset >= start + count) {
            return true;
        } else if (Character.isLowSurrogate(buf[offset])) {
            return !Character.isHighSurrogate(buf[offset - 1]);
        } else {
            int offsetBack = offset + 1;
            char _c1;
            int c2 = _c1 = buf[offset];
            char _c2;
            if (Character.isHighSurrogate(_c1) && offsetBack != start + count && Character.isLowSurrogate(_c2 = buf[offsetBack])) {
                offsetBack++;
                c2 = Character.toCodePoint(_c1, _c2);
            }
            offsetBack = offset - 1;
            int c1 = _c1 = buf[offsetBack];
            if (Character.isLowSurrogate(_c1) && offsetBack > start && Character.isHighSurrogate(_c2 = buf[offsetBack - 1])) {
                offsetBack--;
                c1 = Character.toCodePoint(_c2, _c1);
            }
            int p1 = tailoredGraphemeClusterBreak(c1);
            int p2 = tailoredGraphemeClusterBreak(c2);
            if (p1 == 2 && p2 == 5) {
                return false;
            } else if (p1 == 1 || p1 == 2 || p1 == 5) {
                return true;
            } else if (p2 != 1 && p2 != 2 && p2 != 5) {
                if (p1 != 4 || p2 != 4 && p2 != 9 && p2 != 6 && p2 != 7) {
                    if (p1 != 6 && p1 != 9 || p2 != 9 && p2 != 8) {
                        if ((p1 == 7 || p1 == 8) && p2 == 8) {
                            return false;
                        } else {
                            boolean c2_has_advance = advances != null && (double) advances[offset - start] != 0.0;
                            if (c2_has_advance) {
                                return true;
                            } else if (p2 != 3 && p2 != 17 && p2 != 10 && p1 != 11) {
                                if (offsetBack > start && p1 == 17 && UCharacter.hasBinaryProperty(c2, 64)) {
                                    int backBarrier = offsetBack - 1;
                                    int c0 = _c1 = buf[backBarrier];
                                    if (Character.isLowSurrogate(_c1) && backBarrier > start && Character.isHighSurrogate(_c2 = buf[backBarrier - 1])) {
                                        backBarrier--;
                                        c0 = Character.toCodePoint(_c2, _c1);
                                    }
                                    for (int p0 = tailoredGraphemeClusterBreak(c0); p0 == 3 && backBarrier > start; p0 = tailoredGraphemeClusterBreak(c0)) {
                                        c0 = _c1 = buf[--backBarrier];
                                        if (Character.isLowSurrogate(_c1) && backBarrier > start && Character.isHighSurrogate(_c2 = buf[backBarrier - 1])) {
                                            backBarrier--;
                                            c0 = Character.toCodePoint(_c2, _c1);
                                        }
                                    }
                                    if (UCharacter.hasBinaryProperty(c0, 64)) {
                                        return false;
                                    }
                                }
                                if (p1 != 12 || p2 != 12) {
                                    return UCharacter.getIntPropertyValue(c1, 4098) != 9 || isPureKiller(c1) || UCharacter.getIntPropertyValue(c2, 4101) != 5;
                                } else if (advances != null) {
                                    return false;
                                } else {
                                    int backBarrierx = Math.max(start, offsetBack - 1000);
                                    int offsetBack1 = offsetBack;
                                    while (offsetBack1 > backBarrierx) {
                                        int c0x = _c1 = buf[--offsetBack1];
                                        if (Character.isLowSurrogate(_c1) && offsetBack1 > backBarrierx && Character.isHighSurrogate(_c2 = buf[offsetBack1 - 1])) {
                                            offsetBack1--;
                                            c0x = Character.toCodePoint(_c2, _c1);
                                        }
                                        if (tailoredGraphemeClusterBreak(c0x) != 12) {
                                            offsetBack1 += Character.charCount(c0x);
                                            break;
                                        }
                                    }
                                    return (offset - offsetBack1) % 4 == 0;
                                }
                            } else {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public static boolean isPureKiller(int c) {
        return c == 3642 || c == 3662 || c == 3972 || c == 4154 || c == 5908 || c == 5940 || c == 6097 || c == 7082 || c == 7154 || c == 7155 || c == 43014 || c == 43347 || c == 44013 || c == 69940 || c == 70378 || c == 71467;
    }

    public static int tailoredGraphemeClusterBreak(int c) {
        if (c == 173 || c == 1564 || c == 6158 || c == 8203 || c == 8206 || c == 8207 || 8234 <= c && c <= 8238 || (c | 15) == 8303 || c == 65279 || (c | 127) == 917631) {
            return 3;
        } else {
            return c == 3635 ? 0 : UCharacter.getIntPropertyValue(c, 4114);
        }
    }

    @FunctionalInterface
    public interface ClusterConsumer {

        void accept(int var1, int var2);
    }
}
package icyllis.modernui.graphics.text;

public class LayoutUtils {

    public static boolean isStretchableSpace(char c) {
        return c == ' ';
    }

    public static boolean isWordBreakAfter(char c) {
        return c == ' ' || 8192 <= c && c <= 8202 || c == 12288 ? true : 8294 <= c && c <= 8297 || 8234 <= c && c <= 8238 || c == 8206 || c == 8207;
    }

    public static boolean isWordBreakBefore(char c) {
        return isWordBreakAfter(c) || 13312 <= c && c <= '\u9fff';
    }

    public static int getPrevWordBreakForCache(char[] buf, int start, int end, int offset) {
        if (offset <= start) {
            return start;
        } else {
            if (offset > end) {
                offset = end;
            }
            if (isWordBreakBefore(buf[offset - 1])) {
                return offset - 1;
            } else {
                for (int i = offset - 1; i > start; i--) {
                    if (isWordBreakBefore(buf[i]) || isWordBreakAfter(buf[i - 1])) {
                        return i;
                    }
                }
                return start;
            }
        }
    }

    public static int getNextWordBreakForCache(char[] buf, int start, int end, int offset) {
        if (offset >= end) {
            return end;
        } else {
            if (offset < start) {
                offset = start;
            }
            if (isWordBreakAfter(buf[offset])) {
                return offset + 1;
            } else {
                for (int i = offset + 1; i < end; i++) {
                    if (isWordBreakBefore(buf[i])) {
                        return i;
                    }
                }
                return end;
            }
        }
    }
}
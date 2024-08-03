package info.journeymap.shaded.org.eclipse.jetty.util;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    private static final Logger LOG = Log.getLogger(StringUtil.class);

    private static final Trie<String> CHARSETS = new ArrayTrie<>(256);

    public static final String ALL_INTERFACES = "0.0.0.0";

    public static final String CRLF = "\r\n";

    @Deprecated
    public static final String __LINE_SEPARATOR = System.lineSeparator();

    public static final String __ISO_8859_1 = "iso-8859-1";

    public static final String __UTF8 = "utf-8";

    public static final String __UTF16 = "utf-16";

    public static final char[] lowercases = new char[] { '\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007', '\b', '\t', '\n', '\u000b', '\f', '\r', '\u000e', '\u000f', '\u0010', '\u0011', '\u0012', '\u0013', '\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001a', '\u001b', '\u001c', '\u001d', '\u001e', '\u001f', ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', '\u007f' };

    public static String normalizeCharset(String s) {
        String n = CHARSETS.get(s);
        return n == null ? s : n;
    }

    public static String normalizeCharset(String s, int offset, int length) {
        String n = CHARSETS.get(s, offset, length);
        return n == null ? s.substring(offset, offset + length) : n;
    }

    public static String asciiToLowerCase(String s) {
        if (s == null) {
            return null;
        } else {
            char[] c = null;
            int i = s.length();
            while (i-- > 0) {
                char c1 = s.charAt(i);
                if (c1 <= 127) {
                    char c2 = lowercases[c1];
                    if (c1 != c2) {
                        c = s.toCharArray();
                        c[i] = c2;
                        break;
                    }
                }
            }
            while (i-- > 0) {
                if (c[i] <= 127) {
                    c[i] = lowercases[c[i]];
                }
            }
            return c == null ? s : new String(c);
        }
    }

    public static boolean startsWithIgnoreCase(String s, String w) {
        if (w == null) {
            return true;
        } else if (s != null && s.length() >= w.length()) {
            for (int i = 0; i < w.length(); i++) {
                char c1 = s.charAt(i);
                char c2 = w.charAt(i);
                if (c1 != c2) {
                    if (c1 <= 127) {
                        c1 = lowercases[c1];
                    }
                    if (c2 <= 127) {
                        c2 = lowercases[c2];
                    }
                    if (c1 != c2) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean endsWithIgnoreCase(String s, String w) {
        if (w == null) {
            return true;
        } else if (s == null) {
            return false;
        } else {
            int sl = s.length();
            int wl = w.length();
            if (sl < wl) {
                return false;
            } else {
                int i = wl;
                while (i-- > 0) {
                    char c1 = s.charAt(--sl);
                    char c2 = w.charAt(i);
                    if (c1 != c2) {
                        if (c1 <= 127) {
                            c1 = lowercases[c1];
                        }
                        if (c2 <= 127) {
                            c2 = lowercases[c2];
                        }
                        if (c1 != c2) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
    }

    public static int indexFrom(String s, String chars) {
        for (int i = 0; i < s.length(); i++) {
            if (chars.indexOf(s.charAt(i)) >= 0) {
                return i;
            }
        }
        return -1;
    }

    public static String replace(String s, String sub, String with) {
        int c = 0;
        int i = s.indexOf(sub, c);
        if (i == -1) {
            return s;
        } else {
            StringBuilder buf = new StringBuilder(s.length() + with.length());
            do {
                buf.append(s.substring(c, i));
                buf.append(with);
                c = i + sub.length();
            } while ((i = s.indexOf(sub, c)) != -1);
            if (c < s.length()) {
                buf.append(s.substring(c, s.length()));
            }
            return buf.toString();
        }
    }

    @Deprecated
    public static String unquote(String s) {
        return QuotedStringTokenizer.unquote(s);
    }

    public static void append(StringBuilder buf, String s, int offset, int length) {
        synchronized (buf) {
            int end = offset + length;
            for (int i = offset; i < end && i < s.length(); i++) {
                buf.append(s.charAt(i));
            }
        }
    }

    public static void append(StringBuilder buf, byte b, int base) {
        int bi = 255 & b;
        int c = 48 + bi / base % base;
        if (c > 57) {
            c = 97 + (c - 48 - 10);
        }
        buf.append((char) c);
        c = 48 + bi % base;
        if (c > 57) {
            c = 97 + (c - 48 - 10);
        }
        buf.append((char) c);
    }

    public static void append2digits(StringBuffer buf, int i) {
        if (i < 100) {
            buf.append((char) (i / 10 + 48));
            buf.append((char) (i % 10 + 48));
        }
    }

    public static void append2digits(StringBuilder buf, int i) {
        if (i < 100) {
            buf.append((char) (i / 10 + 48));
            buf.append((char) (i % 10 + 48));
        }
    }

    public static String nonNull(String s) {
        return s == null ? "" : s;
    }

    public static boolean equals(String s, char[] buf, int offset, int length) {
        if (s.length() != length) {
            return false;
        } else {
            for (int i = 0; i < length; i++) {
                if (buf[offset + i] != s.charAt(i)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static String toUTF8String(byte[] b, int offset, int length) {
        return new String(b, offset, length, StandardCharsets.UTF_8);
    }

    public static String toString(byte[] b, int offset, int length, String charset) {
        try {
            return new String(b, offset, length, charset);
        } catch (UnsupportedEncodingException var5) {
            throw new IllegalArgumentException(var5);
        }
    }

    public static int indexOfControlChars(String str) {
        if (str == null) {
            return -1;
        } else {
            int len = str.length();
            for (int i = 0; i < len; i++) {
                if (Character.isISOControl(str.codePointAt(i))) {
                    return i;
                }
            }
            return -1;
        }
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        } else {
            int len = str.length();
            for (int i = 0; i < len; i++) {
                if (!Character.isWhitespace(str.codePointAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean isNotBlank(String str) {
        if (str == null) {
            return false;
        } else {
            int len = str.length();
            for (int i = 0; i < len; i++) {
                if (!Character.isWhitespace(str.codePointAt(i))) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean isUTF8(String charset) {
        return "utf-8".equalsIgnoreCase(charset) || "utf-8".equalsIgnoreCase(normalizeCharset(charset));
    }

    public static String printable(String name) {
        if (name == null) {
            return null;
        } else {
            StringBuilder buf = new StringBuilder(name.length());
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if (!Character.isISOControl(c)) {
                    buf.append(c);
                }
            }
            return buf.toString();
        }
    }

    public static String printable(byte[] b) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            char c = (char) b[i];
            if (!Character.isWhitespace(c) && (c <= ' ' || c >= 127)) {
                buf.append("0x");
                TypeUtil.toHex(b[i], buf);
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    public static byte[] getBytes(String s) {
        return s.getBytes(StandardCharsets.ISO_8859_1);
    }

    public static byte[] getUtf8Bytes(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] getBytes(String s, String charset) {
        try {
            return s.getBytes(charset);
        } catch (Exception var3) {
            LOG.warn(var3);
            return s.getBytes();
        }
    }

    public static String sidBytesToString(byte[] sidBytes) {
        StringBuilder sidString = new StringBuilder();
        sidString.append("S-");
        sidString.append(Byte.toString(sidBytes[0])).append('-');
        StringBuilder tmpBuilder = new StringBuilder();
        for (int i = 2; i <= 7; i++) {
            tmpBuilder.append(Integer.toHexString(sidBytes[i] & 255));
        }
        sidString.append(Long.parseLong(tmpBuilder.toString(), 16));
        int subAuthorityCount = sidBytes[1];
        for (int i = 0; i < subAuthorityCount; i++) {
            int offset = i * 4;
            tmpBuilder.setLength(0);
            tmpBuilder.append(String.format("%02X%02X%02X%02X", sidBytes[11 + offset] & 255, sidBytes[10 + offset] & 255, sidBytes[9 + offset] & 255, sidBytes[8 + offset] & 255));
            sidString.append('-').append(Long.parseLong(tmpBuilder.toString(), 16));
        }
        return sidString.toString();
    }

    public static byte[] sidStringToBytes(String sidString) {
        String[] sidTokens = sidString.split("-");
        int subAuthorityCount = sidTokens.length - 3;
        int byteCount = 0;
        byte[] sidBytes = new byte[8 + 4 * subAuthorityCount];
        sidBytes[byteCount++] = (byte) Integer.parseInt(sidTokens[1]);
        sidBytes[byteCount++] = (byte) subAuthorityCount;
        String hexStr = Long.toHexString(Long.parseLong(sidTokens[2]));
        while (hexStr.length() < 12) {
            hexStr = "0" + hexStr;
        }
        for (int i = 0; i < hexStr.length(); i += 2) {
            sidBytes[byteCount++] = (byte) Integer.parseInt(hexStr.substring(i, i + 2), 16);
        }
        for (int i = 3; i < sidTokens.length; i++) {
            hexStr = Long.toHexString(Long.parseLong(sidTokens[i]));
            while (hexStr.length() < 8) {
                hexStr = "0" + hexStr;
            }
            for (int j = hexStr.length(); j > 0; j -= 2) {
                sidBytes[byteCount++] = (byte) Integer.parseInt(hexStr.substring(j - 2, j), 16);
            }
        }
        return sidBytes;
    }

    public static int toInt(String string, int from) {
        int val = 0;
        boolean started = false;
        boolean minus = false;
        for (int i = from; i < string.length(); i++) {
            char b = string.charAt(i);
            if (b <= ' ') {
                if (started) {
                    break;
                }
            } else if (b >= '0' && b <= '9') {
                val = val * 10 + (b - '0');
                started = true;
            } else {
                if (b != '-' || started) {
                    break;
                }
                minus = true;
            }
        }
        if (started) {
            return minus ? -val : val;
        } else {
            throw new NumberFormatException(string);
        }
    }

    public static long toLong(String string) {
        long val = 0L;
        boolean started = false;
        boolean minus = false;
        for (int i = 0; i < string.length(); i++) {
            char b = string.charAt(i);
            if (b <= ' ') {
                if (started) {
                    break;
                }
            } else if (b >= '0' && b <= '9') {
                val = val * 10L + (long) (b - '0');
                started = true;
            } else {
                if (b != '-' || started) {
                    break;
                }
                minus = true;
            }
        }
        if (started) {
            return minus ? -val : val;
        } else {
            throw new NumberFormatException(string);
        }
    }

    public static String truncate(String str, int maxSize) {
        if (str == null) {
            return null;
        } else {
            return str.length() <= maxSize ? str : str.substring(0, maxSize);
        }
    }

    public static String[] arrayFromString(String s) {
        if (s == null) {
            return new String[0];
        } else if (s.startsWith("[") && s.endsWith("]")) {
            return s.length() == 2 ? new String[0] : csvSplit(s, 1, s.length() - 2);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String[] csvSplit(String s) {
        return s == null ? null : csvSplit(s, 0, s.length());
    }

    public static String[] csvSplit(String s, int off, int len) {
        if (s == null) {
            return null;
        } else if (off >= 0 && len >= 0 && off <= s.length()) {
            List<String> list = new ArrayList();
            csvSplit(list, s, off, len);
            return (String[]) list.toArray(new String[list.size()]);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static List<String> csvSplit(List<String> list, String s, int off, int len) {
        if (list == null) {
            list = new ArrayList();
        }
        StringUtil.CsvSplitState state = StringUtil.CsvSplitState.PRE_DATA;
        StringBuilder out = new StringBuilder();
        int last = -1;
        while (len > 0) {
            char ch = s.charAt(off++);
            len--;
            switch(state) {
                case PRE_DATA:
                    if (!Character.isWhitespace(ch)) {
                        if ('"' == ch) {
                            state = StringUtil.CsvSplitState.QUOTE;
                        } else if (',' == ch) {
                            list.add("");
                        } else {
                            state = StringUtil.CsvSplitState.DATA;
                            out.append(ch);
                        }
                    }
                    break;
                case DATA:
                    if (Character.isWhitespace(ch)) {
                        last = out.length();
                        out.append(ch);
                        state = StringUtil.CsvSplitState.WHITE;
                    } else if (',' == ch) {
                        list.add(out.toString());
                        out.setLength(0);
                        state = StringUtil.CsvSplitState.PRE_DATA;
                    } else {
                        out.append(ch);
                    }
                    break;
                case WHITE:
                    if (Character.isWhitespace(ch)) {
                        out.append(ch);
                    } else if (',' == ch) {
                        out.setLength(last);
                        list.add(out.toString());
                        out.setLength(0);
                        state = StringUtil.CsvSplitState.PRE_DATA;
                    } else {
                        state = StringUtil.CsvSplitState.DATA;
                        out.append(ch);
                        last = -1;
                    }
                    break;
                case QUOTE:
                    if ('\\' == ch) {
                        state = StringUtil.CsvSplitState.SLOSH;
                    } else if ('"' == ch) {
                        list.add(out.toString());
                        out.setLength(0);
                        state = StringUtil.CsvSplitState.POST_DATA;
                    } else {
                        out.append(ch);
                    }
                    break;
                case SLOSH:
                    out.append(ch);
                    state = StringUtil.CsvSplitState.QUOTE;
                    break;
                case POST_DATA:
                    if (',' == ch) {
                        state = StringUtil.CsvSplitState.PRE_DATA;
                    }
            }
        }
        switch(state) {
            case PRE_DATA:
            case POST_DATA:
            default:
                break;
            case DATA:
            case QUOTE:
            case SLOSH:
                list.add(out.toString());
                break;
            case WHITE:
                out.setLength(last);
                list.add(out.toString());
        }
        return list;
    }

    public static String sanitizeXmlString(String html) {
        if (html == null) {
            return null;
        } else {
            int i;
            label53: for (i = 0; i < html.length(); i++) {
                char c = html.charAt(i);
                switch(c) {
                    case '"':
                    case '&':
                    case '\'':
                    case '<':
                    case '>':
                        break label53;
                    default:
                        if (Character.isISOControl(c) && !Character.isWhitespace(c)) {
                            break label53;
                        }
                }
            }
            if (i == html.length()) {
                return html;
            } else {
                StringBuilder out = new StringBuilder(html.length() * 4 / 3);
                out.append(html, 0, i);
                for (; i < html.length(); i++) {
                    char c = html.charAt(i);
                    switch(c) {
                        case '"':
                            out.append("&quot;");
                            break;
                        case '&':
                            out.append("&amp;");
                            break;
                        case '\'':
                            out.append("&apos;");
                            break;
                        case '<':
                            out.append("&lt;");
                            break;
                        case '>':
                            out.append("&gt;");
                            break;
                        default:
                            if (Character.isISOControl(c) && !Character.isWhitespace(c)) {
                                out.append('?');
                            } else {
                                out.append(c);
                            }
                    }
                }
                return out.toString();
            }
        }
    }

    public static String valueOf(Object object) {
        return object == null ? null : String.valueOf(object);
    }

    static {
        CHARSETS.put("utf-8", "utf-8");
        CHARSETS.put("utf8", "utf-8");
        CHARSETS.put("utf-16", "utf-16");
        CHARSETS.put("utf16", "utf-16");
        CHARSETS.put("iso-8859-1", "iso-8859-1");
        CHARSETS.put("iso_8859_1", "iso-8859-1");
    }

    static enum CsvSplitState {

        PRE_DATA,
        QUOTE,
        SLOSH,
        DATA,
        WHITE,
        POST_DATA
    }
}
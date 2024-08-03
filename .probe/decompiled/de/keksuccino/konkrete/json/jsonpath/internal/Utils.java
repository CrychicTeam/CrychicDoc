package de.keksuccino.konkrete.json.jsonpath.internal;

import de.keksuccino.konkrete.json.jsonpath.JsonPathException;
import java.io.Closeable;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

public final class Utils {

    public static String join(String delimiter, String wrap, Iterable<?> objs) {
        Iterator<?> iter = objs.iterator();
        if (!iter.hasNext()) {
            return "";
        } else {
            StringBuilder buffer = new StringBuilder();
            buffer.append(wrap).append(iter.next()).append(wrap);
            while (iter.hasNext()) {
                buffer.append(delimiter).append(wrap).append(iter.next()).append(wrap);
            }
            return buffer.toString();
        }
    }

    public static String join(String delimiter, Iterable<?> objs) {
        return join(delimiter, "", objs);
    }

    public static String concat(CharSequence... strings) {
        if (strings.length == 0) {
            return "";
        } else if (strings.length == 1) {
            return strings[0].toString();
        } else {
            int length = 0;
            int indexOfSingleNonEmptyString = -1;
            for (int i = 0; i < strings.length; i++) {
                CharSequence charSequence = strings[i];
                int len = charSequence.length();
                length += len;
                if (indexOfSingleNonEmptyString != -2 && len > 0) {
                    if (indexOfSingleNonEmptyString == -1) {
                        indexOfSingleNonEmptyString = i;
                    } else {
                        indexOfSingleNonEmptyString = -2;
                    }
                }
            }
            if (length == 0) {
                return "";
            } else if (indexOfSingleNonEmptyString > 0) {
                return strings[indexOfSingleNonEmptyString].toString();
            } else {
                StringBuilder sb = new StringBuilder(length);
                for (CharSequence charSequence : strings) {
                    sb.append(charSequence);
                }
                return sb.toString();
            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
        }
    }

    public static String escape(String str, boolean escapeSingleQuote) {
        if (str == null) {
            return null;
        } else {
            int len = str.length();
            StringWriter writer = new StringWriter(len * 2);
            for (int i = 0; i < len; i++) {
                char ch = str.charAt(i);
                if (ch > 4095) {
                    writer.write("\\u" + hex(ch));
                } else if (ch > 255) {
                    writer.write("\\u0" + hex(ch));
                } else if (ch > 127) {
                    writer.write("\\u00" + hex(ch));
                } else if (ch < ' ') {
                    switch(ch) {
                        case '\b':
                            writer.write(92);
                            writer.write(98);
                            break;
                        case '\t':
                            writer.write(92);
                            writer.write(116);
                            break;
                        case '\n':
                            writer.write(92);
                            writer.write(110);
                            break;
                        case '\u000b':
                        default:
                            if (ch > 15) {
                                writer.write("\\u00" + hex(ch));
                            } else {
                                writer.write("\\u000" + hex(ch));
                            }
                            break;
                        case '\f':
                            writer.write(92);
                            writer.write(102);
                            break;
                        case '\r':
                            writer.write(92);
                            writer.write(114);
                    }
                } else {
                    switch(ch) {
                        case '"':
                            writer.write(92);
                            writer.write(34);
                            break;
                        case '\'':
                            if (escapeSingleQuote) {
                                writer.write(92);
                            }
                            writer.write(39);
                            break;
                        case '/':
                            writer.write(92);
                            writer.write(47);
                            break;
                        case '\\':
                            writer.write(92);
                            writer.write(92);
                            break;
                        default:
                            writer.write(ch);
                    }
                }
            }
            return writer.toString();
        }
    }

    public static String unescape(String str) {
        if (str == null) {
            return null;
        } else {
            int len = str.length();
            StringWriter writer = new StringWriter(len);
            StringBuilder unicode = new StringBuilder(4);
            boolean hadSlash = false;
            boolean inUnicode = false;
            for (int i = 0; i < len; i++) {
                char ch = str.charAt(i);
                if (inUnicode) {
                    unicode.append(ch);
                    if (unicode.length() == 4) {
                        try {
                            int value = Integer.parseInt(unicode.toString(), 16);
                            writer.write((char) value);
                            unicode.setLength(0);
                            inUnicode = false;
                            hadSlash = false;
                        } catch (NumberFormatException var9) {
                            throw new JsonPathException("Unable to parse unicode value: " + unicode, var9);
                        }
                    }
                } else if (hadSlash) {
                    hadSlash = false;
                    switch(ch) {
                        case '"':
                            writer.write(34);
                            break;
                        case '\'':
                            writer.write(39);
                            break;
                        case '\\':
                            writer.write(92);
                            break;
                        case 'b':
                            writer.write(8);
                            break;
                        case 'f':
                            writer.write(12);
                            break;
                        case 'n':
                            writer.write(10);
                            break;
                        case 'r':
                            writer.write(13);
                            break;
                        case 't':
                            writer.write(9);
                            break;
                        case 'u':
                            inUnicode = true;
                            break;
                        default:
                            writer.write(ch);
                    }
                } else if (ch == '\\') {
                    hadSlash = true;
                } else {
                    writer.write(ch);
                }
            }
            if (hadSlash) {
                writer.write(92);
            }
            return writer.toString();
        }
    }

    public static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    static int indexOf(CharSequence cs, CharSequence searchChar, int start) {
        return cs.toString().indexOf(searchChar.toString(), start);
    }

    public static <T> T notNull(T object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        } else {
            return object;
        }
    }

    public static <T> T notNull(T object, String message, Object... values) {
        if (object == null) {
            throw new IllegalArgumentException(String.format(message, values));
        } else {
            return object;
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void onlyOneIsTrue(String message, boolean... expressions) {
        if (!onlyOneIsTrueNonThrow(expressions)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static boolean onlyOneIsTrueNonThrow(boolean... expressions) {
        int count = 0;
        for (boolean expression : expressions) {
            if (expression) {
                if (++count > 1) {
                    return false;
                }
            }
        }
        return 1 == count;
    }

    public static <T extends CharSequence> T notEmpty(T chars, String message) {
        if (chars != null && chars.length() != 0) {
            return chars;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static byte[] notEmpty(byte[] bytes, String message) {
        if (bytes != null && bytes.length != 0) {
            return bytes;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T extends CharSequence> T notEmpty(T chars, String message, Object... values) {
        if (chars != null && chars.length() != 0) {
            return chars;
        } else {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static String toString(Object o) {
        return null == o ? null : o.toString();
    }

    private Utils() {
    }
}
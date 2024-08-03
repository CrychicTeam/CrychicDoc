package dev.ftb.mods.ftblibrary.snbt;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.util.function.BooleanSupplier;

public class SNBTUtils {

    public static final BooleanSupplier ALWAYS_TRUE = () -> true;

    public static final char[] ESCAPE_CHARS = new char[128];

    public static final char[] REVERSE_ESCAPE_CHARS = new char[128];

    public static boolean isSimpleCharacter(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '.' || c == '_' || c == '-' || c == '+' || c == 8734;
    }

    public static boolean isSimpleString(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (!isSimpleCharacter(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static int getNumberType(String s) {
        if (s.isEmpty()) {
            return 8;
        } else {
            char last = Character.toLowerCase(s.charAt(s.length() - 1));
            if (Character.isDigit(last) && Ints.tryParse(s) != null) {
                return 3;
            } else {
                String start = s.substring(0, s.length() - 1);
                if (last == 'b' && Ints.tryParse(start) != null) {
                    return 1;
                } else if (last == 's' && Ints.tryParse(start) != null) {
                    return 2;
                } else if (last == 'l' && Longs.tryParse(start) != null) {
                    return 4;
                } else if (last == 'f' && Floats.tryParse(start) != null) {
                    return 5;
                } else if (last == 'd' && Doubles.tryParse(start) != null) {
                    return 6;
                } else {
                    return Floats.tryParse(s) != null ? -6 : 8;
                }
            }
        }
    }

    public static String handleEscape(String string) {
        return isSimpleString(string) ? string : quoteAndEscape(string);
    }

    public static String quoteAndEscape(String string) {
        int len = string.length();
        StringBuilder sb = new StringBuilder(len + 2);
        sb.append('"');
        for (int i = 0; i < len; i++) {
            char c = string.charAt(i);
            if (c < ESCAPE_CHARS.length && ESCAPE_CHARS[c] != 0) {
                sb.append('\\');
                sb.append(ESCAPE_CHARS[c]);
            } else {
                sb.append(c);
            }
        }
        sb.append('"');
        return sb.toString();
    }

    static {
        ESCAPE_CHARS[34] = '"';
        ESCAPE_CHARS[92] = '\\';
        ESCAPE_CHARS[9] = 't';
        ESCAPE_CHARS[8] = 'b';
        ESCAPE_CHARS[10] = 'n';
        ESCAPE_CHARS[13] = 'r';
        ESCAPE_CHARS[12] = 'f';
        for (int i = 0; i < ESCAPE_CHARS.length; i++) {
            if (ESCAPE_CHARS[i] != 0) {
                REVERSE_ESCAPE_CHARS[ESCAPE_CHARS[i]] = (char) i;
            }
        }
    }
}
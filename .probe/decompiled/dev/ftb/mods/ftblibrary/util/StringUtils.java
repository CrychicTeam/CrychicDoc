package dev.ftb.mods.ftblibrary.util;

import dev.ftb.mods.ftblibrary.math.Bits;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Nullable;

public class StringUtils {

    public static final String ALLOWED_TEXT_CHARS = " .-_!@#$%^&*()+=\\/,<>?'\"[]{}|;:`~";

    public static final char FORMATTING_CHAR = '§';

    public static final String[] EMPTY_ARRAY = new String[0];

    public static final char[] HEX = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static final Predicate<String> ALWAYS_TRUE = s -> true;

    public static final int FLAG_ID_ALLOW_EMPTY = 1;

    public static final int FLAG_ID_FIX = 2;

    public static final int FLAG_ID_ONLY_LOWERCASE = 4;

    public static final int FLAG_ID_ONLY_UNDERLINE = 8;

    public static final int FLAG_ID_ONLY_UNDERLINE_OR_PERIOD = 24;

    public static final int FLAG_ID_DEFAULTS = 14;

    public static final Comparator<Object> IGNORE_CASE_COMPARATOR = (o1, o2) -> String.valueOf(o1).compareToIgnoreCase(String.valueOf(o2));

    public static final Comparator<Object> ID_COMPARATOR = (o1, o2) -> getID(o1, 2).compareToIgnoreCase(getID(o2, 2));

    public static final Map<String, String> TEMP_MAP = new HashMap();

    public static final DecimalFormat DOUBLE_FORMATTER_00 = new DecimalFormat("#0.00");

    public static final DecimalFormat DOUBLE_FORMATTER_0 = new DecimalFormat("#0.0");

    public static final int[] INT_SIZE_TABLE = new int[] { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE };

    private static final Pattern NOT_SNAKE_CASE_PATTERN = Pattern.compile("[^a-z0-9_]");

    private static final Pattern REPEATING_UNDERSCORE_PATTERN = Pattern.compile("_{2,}");

    private static final Pattern FORMATTING_CODE_PATTERN = Pattern.compile("(?i)[\\&§]([0-9A-FK-OR])");

    public static boolean ignoreResourceLocationErrors = false;

    public static String unformatted(String string) {
        return string.isEmpty() ? string : FORMATTING_CODE_PATTERN.matcher(string).replaceAll("");
    }

    public static String addFormatting(String string) {
        return FORMATTING_CODE_PATTERN.matcher(string).replaceAll("§$1");
    }

    public static String toSnakeCase(String string) {
        return string.isEmpty() ? string : REPEATING_UNDERSCORE_PATTERN.matcher(NOT_SNAKE_CASE_PATTERN.matcher(unformatted(string).toLowerCase()).replaceAll("_")).replaceAll("_");
    }

    public static String emptyIfNull(@Nullable Object o) {
        return o == null ? "" : o.toString();
    }

    public static String getRawID(Object o) {
        if (o instanceof StringRepresentable) {
            return ((StringRepresentable) o).getSerializedName();
        } else {
            return o instanceof Enum ? ((Enum) o).name() : String.valueOf(o);
        }
    }

    public static String getID(Object o, int flags) {
        String id = getRawID(o);
        if (flags == 0) {
            return id;
        } else {
            boolean fix = Bits.getFlag(flags, 2);
            if (!fix && id.isEmpty() && !Bits.getFlag(flags, 1)) {
                throw new NullPointerException("ID can't be empty!");
            } else {
                if (Bits.getFlag(flags, 4)) {
                    if (fix) {
                        id = id.toLowerCase();
                    } else if (!id.equals(id.toLowerCase())) {
                        throw new IllegalArgumentException("ID can't contain uppercase characters!");
                    }
                }
                if (Bits.getFlag(flags, 8)) {
                    if (fix) {
                        id = id.toLowerCase();
                    } else if (!id.equals(id.toLowerCase())) {
                        throw new IllegalArgumentException("ID can't contain uppercase characters!");
                    }
                }
                if (Bits.getFlag(flags, 8)) {
                    boolean allowPeriod = Bits.getFlag(flags, 16);
                    char[] chars = id.toCharArray();
                    for (int i = 0; i < chars.length; i++) {
                        if ((chars[i] != '.' || !allowPeriod) && !isTextChar(chars[i], true)) {
                            if (!fix) {
                                throw new IllegalArgumentException("ID contains invalid character: '" + chars[i] + "'!");
                            }
                            chars[i] = '_';
                        }
                    }
                    id = new String(chars);
                }
                return id;
            }
        }
    }

    public static String[] shiftArray(@Nullable String[] s) {
        if (s != null && s.length > 1) {
            String[] s1 = new String[s.length - 1];
            System.arraycopy(s, 1, s1, 0, s1.length);
            return s1;
        } else {
            return EMPTY_ARRAY;
        }
    }

    public static boolean isASCIIChar(char c) {
        return c > 0 && c < 256;
    }

    public static boolean isTextChar(char c, boolean onlyAZ09) {
        return isASCIIChar(c) && (c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || !onlyAZ09 && " .-_!@#$%^&*()+=\\/,<>?'\"[]{}|;:`~".indexOf(c) != -1);
    }

    public static void replace(List<String> txt, String s, String s1) {
        if (!txt.isEmpty()) {
            for (int i = 0; i < txt.size(); i++) {
                String s2 = (String) txt.get(i);
                if (s2 != null && s2.length() > 0) {
                    s2 = s2.replace(s, s1);
                    txt.set(i, s2);
                }
            }
        }
    }

    public static String replace(String s, char c, char with) {
        if (s.isEmpty()) {
            return s;
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                char c1 = s.charAt(i);
                sb.append(c1 == c ? with : c1);
            }
            return sb.toString();
        }
    }

    public static String joinSpaceUntilEnd(int startIndex, CharSequence[] o) {
        if (startIndex >= 0 && o.length > startIndex) {
            StringBuilder sb = new StringBuilder();
            for (int i = startIndex; i < o.length; i++) {
                sb.append(o[i]);
                if (i != o.length - 1) {
                    sb.append(' ');
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public static String firstUppercase(String s) {
        if (s.length() == 0) {
            return s;
        } else {
            char c = Character.toUpperCase(s.charAt(0));
            return s.length() == 1 ? Character.toString(c) : c + s.substring(1);
        }
    }

    public static String fillString(CharSequence s, char fill, int length) {
        int sl = s.length();
        char[] c = new char[Math.max(sl, length)];
        for (int i = 0; i < c.length; i++) {
            if (i >= sl) {
                c[i] = fill;
            } else {
                c[i] = s.charAt(i);
            }
        }
        return new String(c);
    }

    public static String removeAllWhitespace(String s) {
        char[] chars = new char[s.length()];
        int j = 0;
        for (int i = 0; i < chars.length; i++) {
            char c = s.charAt(i);
            if (c > ' ') {
                chars[j] = c;
                j++;
            }
        }
        return new String(chars, 0, j);
    }

    public static String formatDouble0(double value) {
        return value == (double) ((long) value) ? String.format("%,d", (long) value) : DOUBLE_FORMATTER_0.format(value);
    }

    public static String formatDouble00(double value) {
        return value == (double) ((long) value) ? String.format("%,d", (long) value) : DOUBLE_FORMATTER_00.format(value);
    }

    public static String formatDouble(double value, boolean fancy) {
        if (Double.isNaN(value)) {
            return "NaN";
        } else if (value == Double.POSITIVE_INFINITY) {
            return "+Inf";
        } else if (value == Double.NEGATIVE_INFINITY) {
            return "-Inf";
        } else if (value == 9.223372E18F) {
            return "2^63-1";
        } else if (value == -9.223372E18F) {
            return "-2^63";
        } else if (value == 0.0) {
            return "0";
        } else if (!fancy) {
            return formatDouble00(value);
        } else if (value >= 1.0E9) {
            return formatDouble00(value / 1.0E9) + "B";
        } else if (value >= 1000000.0) {
            return formatDouble00(value / 1000000.0) + "M";
        } else {
            return value >= 10000.0 ? formatDouble00(value / 1000.0) + "K" : formatDouble00(value);
        }
    }

    public static String formatDouble(double value) {
        return formatDouble(value, false);
    }

    public static Map<String, String> parse(Map<String, String> map, String s) {
        if (map == TEMP_MAP) {
            map.clear();
        }
        for (String entry : s.split(",")) {
            String[] val = entry.split("=");
            for (String key : val[0].split("&")) {
                map.put(key, val[1]);
            }
        }
        return map;
    }

    public static String fixTabs(String string, int tabSize) {
        String with;
        if (tabSize == 2) {
            with = "  ";
        } else if (tabSize == 4) {
            with = "    ";
        } else {
            char[] c = new char[tabSize];
            Arrays.fill(c, ' ');
            with = new String(c);
        }
        return string.replace("\t", with);
    }

    public static int stringSize(int x) {
        int i = 0;
        while (x > INT_SIZE_TABLE[i]) {
            i++;
        }
        return i + 1;
    }

    public static String add0s(int number, int max) {
        int size = stringSize(max);
        int nsize = stringSize(number);
        return "0".repeat(Math.max(0, size - nsize)) + number;
    }

    public static String camelCaseToWords(String key) {
        StringBuilder builder = new StringBuilder();
        boolean pu = false;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            boolean u = Character.isUpperCase(c);
            if (!pu && u) {
                builder.append(' ');
            }
            pu = u;
            if (i == 0) {
                c = Character.toUpperCase(c);
            }
            builder.append(c);
        }
        return builder.toString();
    }

    public static Map<String, String> splitProperties(String s) {
        Map<String, String> map = new LinkedHashMap();
        for (String s1 : s.split(" ")) {
            if (!s1.isEmpty()) {
                String[] s2 = s1.split(":", 2);
                map.put(s2[0], s2.length == 2 ? s2[1].replace("%20", " ") : "");
            }
        }
        return map;
    }

    static {
        DOUBLE_FORMATTER_00.setRoundingMode(RoundingMode.DOWN);
        DOUBLE_FORMATTER_0.setRoundingMode(RoundingMode.DOWN);
    }
}
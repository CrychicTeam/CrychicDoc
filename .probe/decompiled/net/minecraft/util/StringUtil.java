package net.minecraft.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

    private static final Pattern LINE_PATTERN = Pattern.compile("\\r\\n|\\v");

    private static final Pattern LINE_END_PATTERN = Pattern.compile("(?:\\r\\n|\\v)$");

    public static String formatTickDuration(int int0) {
        int $$1 = int0 / 20;
        int $$2 = $$1 / 60;
        $$1 %= 60;
        int $$3 = $$2 / 60;
        $$2 %= 60;
        return $$3 > 0 ? String.format(Locale.ROOT, "%02d:%02d:%02d", $$3, $$2, $$1) : String.format(Locale.ROOT, "%02d:%02d", $$2, $$1);
    }

    public static String stripColor(String string0) {
        return STRIP_COLOR_PATTERN.matcher(string0).replaceAll("");
    }

    public static boolean isNullOrEmpty(@Nullable String string0) {
        return StringUtils.isEmpty(string0);
    }

    public static String truncateStringIfNecessary(String string0, int int1, boolean boolean2) {
        if (string0.length() <= int1) {
            return string0;
        } else {
            return boolean2 && int1 > 3 ? string0.substring(0, int1 - 3) + "..." : string0.substring(0, int1);
        }
    }

    public static int lineCount(String string0) {
        if (string0.isEmpty()) {
            return 0;
        } else {
            Matcher $$1 = LINE_PATTERN.matcher(string0);
            int $$2 = 1;
            while ($$1.find()) {
                $$2++;
            }
            return $$2;
        }
    }

    public static boolean endsWithNewLine(String string0) {
        return LINE_END_PATTERN.matcher(string0).find();
    }

    public static String trimChatMessage(String string0) {
        return truncateStringIfNecessary(string0, 256, false);
    }
}
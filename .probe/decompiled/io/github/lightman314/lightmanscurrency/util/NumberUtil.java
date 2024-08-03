package io.github.lightman314.lightmanscurrency.util;

import java.text.DecimalFormat;

public class NumberUtil {

    public static String GetPrettyString(int count) {
        return new DecimalFormat().format((long) count);
    }

    public static boolean IsInteger(String text) {
        if (text == null) {
            return false;
        } else {
            try {
                int nfe = Integer.parseInt(text);
                return true;
            } catch (NumberFormatException var2) {
                return false;
            }
        }
    }

    public static int GetIntegerValue(String text, int defaultValue) {
        return IsInteger(text) ? Integer.parseInt(text) : defaultValue;
    }

    public static String getAsStringOfLength(int i, int length) {
        StringBuilder value = new StringBuilder(Integer.toString(i));
        while (value.length() < length) {
            value.insert(0, "0");
        }
        return value.toString();
    }

    public static String prettyInteger(int i) {
        return new DecimalFormat().format((long) i);
    }
}
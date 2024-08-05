package harmonised.pmmo.client.utils;

import java.util.Locale;

public class DP {

    private static char[] prefix = new char[] { ' ', 'k', 'm', 'g', 't', 'p', 'e', 'z', 'y' };

    public static String dprefix(long input) {
        int length = String.valueOf(input).length();
        int prefixId = length / 3;
        prefixId = length % 3 == 0 ? prefixId - 1 : prefixId;
        input = (long) ((double) input / Math.pow(10.0, (double) (length - 3)));
        Double output = (double) input;
        int decimalPlaces = 3 - (length - length / 3 * 3);
        decimalPlaces = decimalPlaces == 3 ? 0 : decimalPlaces;
        output = output / Math.pow(10.0, (double) decimalPlaces);
        return String.format(Locale.ENGLISH, "%." + decimalPlaces + "f", output) + prefix[prefixId];
    }

    public static String dp(Float input) {
        return dp(input.doubleValue());
    }

    public static String dp(Double input) {
        return String.format(Locale.ENGLISH, "%.2f", input);
    }

    public static String dpCustom(Double input, int decPlaces) {
        return String.format(Locale.ENGLISH, "%." + decPlaces + "f", input);
    }

    public static String dpSoft(double input) {
        if (input % 1.0 == 0.0) {
            return String.format(Locale.ENGLISH, "%.0f", input);
        } else {
            return input * 10.0 % 1.0 == 0.0 ? String.format(Locale.ENGLISH, "%.1f", input) : String.format(Locale.ENGLISH, "%.2f", input);
        }
    }

    public static String dpSoft(float input) {
        if (input % 1.0F == 0.0F) {
            return String.format(Locale.ENGLISH, "%.0f", input);
        } else {
            return input * 10.0F % 1.0F == 0.0F ? String.format(Locale.ENGLISH, "%.1f", input) : String.format(Locale.ENGLISH, "%.2f", input);
        }
    }
}
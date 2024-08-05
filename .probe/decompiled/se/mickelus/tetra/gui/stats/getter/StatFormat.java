package se.mickelus.tetra.gui.stats.getter;

import java.text.DecimalFormat;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StatFormat {

    public static final StatFormat noDecimal = new StatFormat("%.0f");

    public static final StatFormat oneDecimal = new StatFormat("%.01f");

    public static final StatFormat twoDecimal = new StatFormat("%.02f");

    public static final StatFormat abbreviate = new StatFormat.StatFormatAbbreviate();

    private final String format;

    public StatFormat(String format) {
        this.format = format;
    }

    public String get(double value) {
        return String.format(this.format, value);
    }

    private static class StatFormatAbbreviate extends StatFormat {

        private static final DecimalFormat abbreviateFormat = new DecimalFormat("#.#");

        public StatFormatAbbreviate() {
            super("%.0f");
        }

        @Override
        public String get(double value) {
            return Math.abs(value) >= 1000.0 ? abbreviateFormat.format(value / 1000.0) + "k" : super.get(value);
        }
    }
}
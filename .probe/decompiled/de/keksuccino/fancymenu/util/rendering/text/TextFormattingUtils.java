package de.keksuccino.fancymenu.util.rendering.text;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class TextFormattingUtils {

    private static final String FORMATTING_CODE_BLACK = "0";

    private static final String FORMATTING_CODE_DARK_BLUE = "1";

    private static final String FORMATTING_CODE_DARK_GREEN = "2";

    private static final String FORMATTING_CODE_DARK_AQUA = "3";

    private static final String FORMATTING_CODE_DARK_RED = "4";

    private static final String FORMATTING_CODE_DARK_PURPLE = "5";

    private static final String FORMATTING_CODE_ORANGE = "6";

    private static final String FORMATTING_CODE_GREY = "7";

    private static final String FORMATTING_CODE_DARK_GREY = "8";

    private static final String FORMATTING_CODE_BLUE = "9";

    private static final String FORMATTING_CODE_GREEN = "a";

    private static final String FORMATTING_CODE_AQUA = "b";

    private static final String FORMATTING_CODE_RED = "c";

    private static final String FORMATTING_CODE_PURPLE = "d";

    private static final String FORMATTING_CODE_YELLOW = "e";

    private static final String FORMATTING_CODE_WHITE = "f";

    private static final String FORMATTING_CODE_BOLD = "l";

    private static final String FORMATTING_CODE_STRIKE = "m";

    private static final String FORMATTING_CODE_UNDERLINE = "n";

    private static final String FORMATTING_CODE_ITALIC = "o";

    private static final String FORMATTING_CODE_MAGIC = "k";

    private static final String FORMATTING_CODE_RESET = "r";

    @NotNull
    public static String replaceFormattingCodes(@NotNull String in, @NotNull String oldPrefix, @NotNull String newPrefix) {
        Objects.requireNonNull(in);
        in = StringUtils.replace(in, oldPrefix + "0", newPrefix + "0");
        in = StringUtils.replace(in, oldPrefix + "1", newPrefix + "1");
        in = StringUtils.replace(in, oldPrefix + "2", newPrefix + "2");
        in = StringUtils.replace(in, oldPrefix + "3", newPrefix + "3");
        in = StringUtils.replace(in, oldPrefix + "4", newPrefix + "4");
        in = StringUtils.replace(in, oldPrefix + "5", newPrefix + "5");
        in = StringUtils.replace(in, oldPrefix + "6", newPrefix + "6");
        in = StringUtils.replace(in, oldPrefix + "7", newPrefix + "7");
        in = StringUtils.replace(in, oldPrefix + "8", newPrefix + "8");
        in = StringUtils.replace(in, oldPrefix + "9", newPrefix + "9");
        in = StringUtils.replace(in, oldPrefix + "a", newPrefix + "a");
        in = StringUtils.replace(in, oldPrefix + "b", newPrefix + "b");
        in = StringUtils.replace(in, oldPrefix + "c", newPrefix + "c");
        in = StringUtils.replace(in, oldPrefix + "d", newPrefix + "d");
        in = StringUtils.replace(in, oldPrefix + "e", newPrefix + "e");
        in = StringUtils.replace(in, oldPrefix + "f", newPrefix + "f");
        in = StringUtils.replace(in, oldPrefix + "l", newPrefix + "l");
        in = StringUtils.replace(in, oldPrefix + "m", newPrefix + "m");
        in = StringUtils.replace(in, oldPrefix + "n", newPrefix + "n");
        in = StringUtils.replace(in, oldPrefix + "o", newPrefix + "o");
        in = StringUtils.replace(in, oldPrefix + "k", newPrefix + "k");
        return StringUtils.replace(in, oldPrefix + "r", newPrefix + "r");
    }
}
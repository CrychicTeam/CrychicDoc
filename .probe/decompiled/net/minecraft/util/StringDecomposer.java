package net.minecraft.util;

import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;

public class StringDecomposer {

    private static final char REPLACEMENT_CHAR = '�';

    private static final Optional<Object> STOP_ITERATION = Optional.of(Unit.INSTANCE);

    private static boolean feedChar(Style style0, FormattedCharSink formattedCharSink1, int int2, char char3) {
        return Character.isSurrogate(char3) ? formattedCharSink1.accept(int2, style0, 65533) : formattedCharSink1.accept(int2, style0, char3);
    }

    public static boolean iterate(String string0, Style style1, FormattedCharSink formattedCharSink2) {
        int $$3 = string0.length();
        for (int $$4 = 0; $$4 < $$3; $$4++) {
            char $$5 = string0.charAt($$4);
            if (Character.isHighSurrogate($$5)) {
                if ($$4 + 1 >= $$3) {
                    if (!formattedCharSink2.accept($$4, style1, 65533)) {
                        return false;
                    }
                    break;
                }
                char $$6 = string0.charAt($$4 + 1);
                if (Character.isLowSurrogate($$6)) {
                    if (!formattedCharSink2.accept($$4, style1, Character.toCodePoint($$5, $$6))) {
                        return false;
                    }
                    $$4++;
                } else if (!formattedCharSink2.accept($$4, style1, 65533)) {
                    return false;
                }
            } else if (!feedChar(style1, formattedCharSink2, $$4, $$5)) {
                return false;
            }
        }
        return true;
    }

    public static boolean iterateBackwards(String string0, Style style1, FormattedCharSink formattedCharSink2) {
        int $$3 = string0.length();
        for (int $$4 = $$3 - 1; $$4 >= 0; $$4--) {
            char $$5 = string0.charAt($$4);
            if (Character.isLowSurrogate($$5)) {
                if ($$4 - 1 < 0) {
                    if (!formattedCharSink2.accept(0, style1, 65533)) {
                        return false;
                    }
                    break;
                }
                char $$6 = string0.charAt($$4 - 1);
                if (Character.isHighSurrogate($$6)) {
                    if (!formattedCharSink2.accept(--$$4, style1, Character.toCodePoint($$6, $$5))) {
                        return false;
                    }
                } else if (!formattedCharSink2.accept($$4, style1, 65533)) {
                    return false;
                }
            } else if (!feedChar(style1, formattedCharSink2, $$4, $$5)) {
                return false;
            }
        }
        return true;
    }

    public static boolean iterateFormatted(String string0, Style style1, FormattedCharSink formattedCharSink2) {
        return iterateFormatted(string0, 0, style1, formattedCharSink2);
    }

    public static boolean iterateFormatted(String string0, int int1, Style style2, FormattedCharSink formattedCharSink3) {
        return iterateFormatted(string0, int1, style2, style2, formattedCharSink3);
    }

    public static boolean iterateFormatted(String string0, int int1, Style style2, Style style3, FormattedCharSink formattedCharSink4) {
        int $$5 = string0.length();
        Style $$6 = style2;
        for (int $$7 = int1; $$7 < $$5; $$7++) {
            char $$8 = string0.charAt($$7);
            if ($$8 == 167) {
                if ($$7 + 1 >= $$5) {
                    break;
                }
                char $$9 = string0.charAt($$7 + 1);
                ChatFormatting $$10 = ChatFormatting.getByCode($$9);
                if ($$10 != null) {
                    $$6 = $$10 == ChatFormatting.RESET ? style3 : $$6.applyLegacyFormat($$10);
                }
                $$7++;
            } else if (Character.isHighSurrogate($$8)) {
                if ($$7 + 1 >= $$5) {
                    if (!formattedCharSink4.accept($$7, $$6, 65533)) {
                        return false;
                    }
                    break;
                }
                char $$11 = string0.charAt($$7 + 1);
                if (Character.isLowSurrogate($$11)) {
                    if (!formattedCharSink4.accept($$7, $$6, Character.toCodePoint($$8, $$11))) {
                        return false;
                    }
                    $$7++;
                } else if (!formattedCharSink4.accept($$7, $$6, 65533)) {
                    return false;
                }
            } else if (!feedChar($$6, formattedCharSink4, $$7, $$8)) {
                return false;
            }
        }
        return true;
    }

    public static boolean iterateFormatted(FormattedText formattedText0, Style style1, FormattedCharSink formattedCharSink2) {
        return !formattedText0.visit((p_14302_, p_14303_) -> iterateFormatted(p_14303_, 0, p_14302_, formattedCharSink2) ? Optional.empty() : STOP_ITERATION, style1).isPresent();
    }

    public static String filterBrokenSurrogates(String string0) {
        StringBuilder $$1 = new StringBuilder();
        iterate(string0, Style.EMPTY, (p_14343_, p_14344_, p_14345_) -> {
            $$1.appendCodePoint(p_14345_);
            return true;
        });
        return $$1.toString();
    }

    public static String getPlainText(FormattedText formattedText0) {
        StringBuilder $$1 = new StringBuilder();
        iterateFormatted(formattedText0, Style.EMPTY, (p_14323_, p_14324_, p_14325_) -> {
            $$1.appendCodePoint(p_14325_);
            return true;
        });
        return $$1.toString();
    }
}
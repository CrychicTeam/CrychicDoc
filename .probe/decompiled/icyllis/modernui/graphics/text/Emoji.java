package icyllis.modernui.graphics.text;

import com.ibm.icu.lang.UCharacter;

public final class Emoji {

    public static final int ZERO_WIDTH_JOINER = 8205;

    public static final int COMBINING_ENCLOSING_KEYCAP = 8419;

    public static final int VARIATION_SELECTOR_15 = 65038;

    public static final int VARIATION_SELECTOR_16 = 65039;

    public static final int CANCEL_TAG = 917631;

    public static boolean isEmoji(int codePoint) {
        return UCharacter.hasBinaryProperty(codePoint, 57);
    }

    public static boolean isEmojiPresentation(int codePoint) {
        return UCharacter.hasBinaryProperty(codePoint, 58);
    }

    public static boolean isEmojiModifier(int codePoint) {
        return UCharacter.hasBinaryProperty(codePoint, 59);
    }

    public static boolean isEmojiModifierBase(int codePoint) {
        return codePoint != 129309 && codePoint != 129340 ? UCharacter.hasBinaryProperty(codePoint, 60) : true;
    }

    public static boolean isRegionalIndicatorSymbol(int codePoint) {
        return 127462 <= codePoint && codePoint <= 127487;
    }

    public static boolean isKeycapBase(int codePoint) {
        return 48 <= codePoint && codePoint <= 57 || codePoint == 35 || codePoint == 42;
    }

    public static boolean isTagSpecChar(int codePoint) {
        return 917536 <= codePoint && codePoint <= 917630;
    }
}
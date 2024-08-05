package icyllis.modernui.mc.text;

import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Style;

public class CharacterStyle {

    public static final int NORMAL = 0;

    public static final int BOLD_MASK = 16777216;

    public static final int ITALIC_MASK = 33554432;

    public static final int FONT_STYLE_MASK = 50331648;

    public static final int UNDERLINE_MASK = 67108864;

    public static final int STRIKETHROUGH_MASK = 134217728;

    public static final int EFFECT_MASK = 201326592;

    public static final int OBFUSCATED_MASK = 268435456;

    public static final int LAYOUT_MASK = 520093696;

    public static final int COLOR_EMOJI_REPLACEMENT = 536870912;

    public static final int BITMAP_REPLACEMENT = 1073741824;

    public static final int ANY_BITMAP_REPLACEMENT = 1610612736;

    public static final int COLOR_MASK = 16777215;

    public static final int IMPLICIT_COLOR_MASK = Integer.MIN_VALUE;

    public static final int FULL_COLOR_MASK = -2130706433;

    public final int mStringIndex;

    public final int mStripIndex;

    private final int mFlags;

    @Deprecated
    public CharacterStyle(int stringIndex, int stripIndex, Style style) {
        this.mStringIndex = stringIndex;
        this.mStripIndex = stripIndex;
        this.mFlags = flatten(style);
    }

    public static int flatten(@Nonnull Style style) {
        int v = 0;
        if (style.getColor() == null) {
            v |= Integer.MIN_VALUE;
        } else {
            v |= style.getColor().getValue() & 16777215;
        }
        if (style.isBold()) {
            v |= 16777216;
        }
        if (style.isItalic()) {
            v |= 33554432;
        }
        if (style.isUnderlined()) {
            v |= 67108864;
        }
        if (style.isStrikethrough()) {
            v |= 134217728;
        }
        if (style.isObfuscated()) {
            v |= 268435456;
        }
        return v;
    }

    public static boolean equalsForTextLayout(@Nonnull Style a, @Nonnull Style b) {
        return a == b || a.isBold() == b.isBold() && a.isItalic() == b.isItalic() && a.isUnderlined() == b.isUnderlined() && a.isStrikethrough() == b.isStrikethrough() && a.isObfuscated() == b.isObfuscated() && Objects.equals(a.getColor(), b.getColor()) && Objects.equals(a.getFont(), b.getFont());
    }

    public int getFullColor() {
        return this.mFlags & -2130706433;
    }

    public boolean isBold() {
        return (this.mFlags & 16777216) != 0;
    }

    public boolean isItalic() {
        return (this.mFlags & 33554432) != 0;
    }

    public int getFontStyle() {
        return this.mFlags & 50331648;
    }

    public boolean isUnderlined() {
        return (this.mFlags & 67108864) != 0;
    }

    public boolean isStrikethrough() {
        return (this.mFlags & 134217728) != 0;
    }

    public int getEffect() {
        return this.mFlags & 201326592;
    }

    public boolean isObfuscated() {
        return (this.mFlags & 268435456) != 0;
    }

    public boolean isFormattingCode() {
        return false;
    }

    public boolean isMetricAffecting(@Nonnull CharacterStyle s) {
        return (this.mFlags & 50331648) != (s.mFlags & 50331648);
    }

    public boolean isLayoutAffecting(@Nonnull CharacterStyle s) {
        return (this.mFlags & 520093696) != (s.mFlags & 520093696);
    }

    public String toString() {
        return "CharacterStyle{stringIndex=" + this.mStringIndex + ",stripIndex=" + this.mStripIndex + ",flags=0x" + Integer.toHexString(this.mFlags) + "}";
    }
}
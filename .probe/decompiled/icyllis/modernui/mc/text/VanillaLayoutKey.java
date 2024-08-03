package icyllis.modernui.mc.text;

import javax.annotation.Nonnull;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class VanillaLayoutKey {

    private String mText;

    private ResourceLocation mFont;

    private int mCode;

    private int mHash;

    public VanillaLayoutKey() {
    }

    private VanillaLayoutKey(@Nonnull VanillaLayoutKey key) {
        this.mText = key.mText;
        this.mFont = key.mFont;
        this.mCode = key.mCode;
        this.mHash = key.mHash;
    }

    public VanillaLayoutKey update(@Nonnull String text, @Nonnull Style style) {
        this.mText = text;
        this.mFont = style.getFont();
        this.mCode = CharacterStyle.flatten(style);
        this.mHash = 0;
        return this;
    }

    public int hashCode() {
        int h = this.mHash;
        if (h == 0) {
            String s = this.mText;
            int var3 = s.hashCode();
            int var4 = 31 * var3 + this.mFont.hashCode();
            h = 31 * var4 + this.mCode;
            this.mHash = h;
        }
        return h;
    }

    public boolean equals(Object o) {
        if (o.getClass() != VanillaLayoutKey.class) {
            return false;
        } else {
            VanillaLayoutKey key = (VanillaLayoutKey) o;
            if (this.mCode != key.mCode) {
                return false;
            } else {
                return !this.mFont.equals(key.mFont) ? false : this.mText.equals(key.mText);
            }
        }
    }

    public String toString() {
        return "VanillaLayoutKey{mText=" + this.mText + ", mFont=" + this.mFont + ", mCode=" + this.mCode + ", mHash=" + this.mHash + "}";
    }

    public VanillaLayoutKey copy() {
        return new VanillaLayoutKey(this);
    }
}
package icyllis.modernui.mc.text;

import javax.annotation.Nonnull;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;

public class VanillaTextWrapper implements FormattedCharSequence {

    @Nonnull
    public final String mText;

    public VanillaTextWrapper(@Nonnull String text) {
        this.mText = text;
    }

    @Override
    public boolean accept(@Nonnull FormattedCharSink sink) {
        return StringDecomposer.iterate(this.mText, Style.EMPTY, sink);
    }
}
package icyllis.modernui.mc.text;

import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;

public class FormattedTextWrapper implements FormattedCharSequence {

    @Nonnull
    public final FormattedText mText;

    public FormattedTextWrapper(@Nonnull FormattedText text) {
        this.mText = text;
    }

    @Override
    public boolean accept(@Nonnull FormattedCharSink sink) {
        return this.mText.visit((style, text) -> StringDecomposer.iterateFormatted(text, style, sink) ? Optional.empty() : FormattedText.STOP_ITERATION, Style.EMPTY).isEmpty();
    }
}
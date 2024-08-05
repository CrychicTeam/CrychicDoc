package icyllis.modernui.mc.text.mixin;

import icyllis.modernui.mc.text.FormattedTextWrapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ Language.class })
public class MixinLanguage {

    @Overwrite
    public List<FormattedCharSequence> getVisualOrder(@Nonnull List<FormattedText> texts) {
        List<FormattedCharSequence> list = new ArrayList(texts.size());
        for (FormattedText text : texts) {
            list.add(new FormattedTextWrapper(text));
        }
        return list;
    }
}
package icyllis.modernui.mc.text.mixin;

import icyllis.modernui.mc.text.FormattedTextWrapper;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ ClientLanguage.class })
public class MixinClientLanguage {

    @Overwrite
    public FormattedCharSequence getVisualOrder(FormattedText text) {
        return new FormattedTextWrapper(text);
    }
}
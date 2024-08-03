package icyllis.modernui.mc.text.mixin;

import icyllis.modernui.mc.text.FormattedTextWrapper;
import javax.annotation.Nonnull;
import net.minecraft.client.resources.language.FormattedBidiReorder;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ FormattedBidiReorder.class })
public class MixinBidiReorder {

    @Nonnull
    @Overwrite
    public static FormattedCharSequence reorder(FormattedText text, boolean defaultRtl) {
        return new FormattedTextWrapper(text);
    }
}
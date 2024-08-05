package icyllis.modernui.mc.text.mixin;

import icyllis.modernui.mc.text.VanillaTextWrapper;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ CommandSuggestions.class })
public abstract class MixinCommandSuggestions {

    @Redirect(method = { "formatChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/FormattedCharSequence;forward(Ljava/lang/String;Lnet/minecraft/network/chat/Style;)Lnet/minecraft/util/FormattedCharSequence;"))
    private FormattedCharSequence onFormatChat(String viewText, Style style) {
        return new VanillaTextWrapper(viewText);
    }
}
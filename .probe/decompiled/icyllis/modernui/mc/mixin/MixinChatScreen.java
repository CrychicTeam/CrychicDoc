package icyllis.modernui.mc.mixin;

import icyllis.modernui.mc.FontResourceManager;
import icyllis.modernui.mc.IModernEditBox;
import icyllis.modernui.mc.ModernUIClient;
import icyllis.modernui.mc.MuiModApi;
import java.util.regex.Matcher;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ChatScreen.class })
public class MixinChatScreen {

    @Shadow
    protected EditBox input;

    @Unique
    private boolean modernUI_MC$broadcasting;

    @Inject(method = { "onEdited" }, at = { @At("HEAD") })
    private void _onEdited(String s, CallbackInfo ci) {
        if (!this.modernUI_MC$broadcasting && ModernUIClient.sEmojiShortcodes && !this.input.getValue().startsWith("/") && (!(this.input instanceof IModernEditBox) || !((IModernEditBox) this.input).modernUI_MC$getUndoManager().isInUndo())) {
            FontResourceManager manager = FontResourceManager.getInstance();
            label27: while (true) {
                Matcher matcher = MuiModApi.EMOJI_SHORTCODE_PATTERN.matcher(this.input.getValue());
                while (matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();
                    if (end - start > 2) {
                        String replacement = manager.lookupEmojiShortcode(this.input.getValue().substring(start + 1, end - 1));
                        if (replacement != null) {
                            this.modernUI_MC$broadcasting = true;
                            this.input.setHighlightPos(start);
                            this.input.setCursorPosition(end);
                            this.input.insertText(replacement);
                            this.modernUI_MC$broadcasting = false;
                            continue label27;
                        }
                    }
                }
                return;
            }
        }
    }
}
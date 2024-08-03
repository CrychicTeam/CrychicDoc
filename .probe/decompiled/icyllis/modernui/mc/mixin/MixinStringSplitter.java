package icyllis.modernui.mc.mixin;

import com.ibm.icu.text.BreakIterator;
import icyllis.modernui.ModernUI;
import icyllis.modernui.text.method.WordIterator;
import java.text.StringCharacterIterator;
import net.minecraft.client.StringSplitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ StringSplitter.class })
public class MixinStringSplitter {

    @Inject(method = { "getWordPosition" }, at = { @At("HEAD") }, cancellable = true)
    private static void getWordPosition(String value, int dir, int cursor, boolean withEndSpace, CallbackInfoReturnable<Integer> cir) {
        if (dir == -1 || dir == 1) {
            int offset;
            if (withEndSpace) {
                WordIterator wordIterator = new WordIterator();
                wordIterator.setCharSequence(value, cursor, cursor);
                if (dir == -1) {
                    offset = wordIterator.preceding(cursor);
                } else {
                    offset = wordIterator.following(cursor);
                }
            } else {
                BreakIterator wordIterator = BreakIterator.getWordInstance(ModernUI.getSelectedLocale());
                wordIterator.setText(new StringCharacterIterator(value, cursor));
                if (dir == -1) {
                    offset = wordIterator.preceding(cursor);
                } else {
                    offset = wordIterator.following(cursor);
                }
            }
            if (offset != -1) {
                cir.setReturnValue(offset);
            } else {
                cir.setReturnValue(cursor);
            }
        }
    }
}
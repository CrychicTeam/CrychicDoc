package journeymap.common.mixin.client;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ TranslatableContents.class })
public class TranslatableContentsMixin {

    @Shadow
    @Final
    private Object[] args;

    @Inject(method = { "getArgument(I)Lnet/minecraft/network/chat/FormattedText;" }, at = { @At("HEAD") }, cancellable = true)
    public void journeymap_getArguments(int i, CallbackInfoReturnable<FormattedText> cir) {
        if (i >= this.args.length) {
            cir.setReturnValue(Component.f_130760_);
            cir.cancel();
        }
    }
}
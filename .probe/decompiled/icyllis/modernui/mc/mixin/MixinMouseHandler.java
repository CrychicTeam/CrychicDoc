package icyllis.modernui.mc.mixin;

import icyllis.modernui.mc.MuiModApi;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ MouseHandler.class })
public class MixinMouseHandler {

    @Inject(method = { "onScroll" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseScrolled(DDD)Z") })
    private void onScrollCallback(long handle, double xoffset, double yoffset, CallbackInfo ci) {
        MuiModApi.dispatchOnScroll(xoffset, yoffset);
    }
}
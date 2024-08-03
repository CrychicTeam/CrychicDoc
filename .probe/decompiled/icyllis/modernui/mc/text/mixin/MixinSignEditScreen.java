package icyllis.modernui.mc.text.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ AbstractSignEditScreen.class })
public class MixinSignEditScreen {

    @Inject(method = { "renderSignText" }, at = { @At("HEAD") })
    private void onRenderSignText(GuiGraphics gr, CallbackInfo ci) {
        gr.flush();
    }
}
package noppes.npcs.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.client.gui.custom.GuiCustom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Screen.class })
public class ScreenMixin {

    @Inject(at = { @At("TAIL") }, method = { "init*" })
    private void renderToBuffer(Minecraft mc, int width, int height, CallbackInfo callbackInfo) {
        if (this instanceof GuiCustom gui && gui.subgui != null) {
            gui.subgui.m_6575_(mc, width, height);
        }
    }
}
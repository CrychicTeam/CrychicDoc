package de.keksuccino.konkrete.mixin.client;

import de.keksuccino.konkrete.events.ScreenMouseClickedEvent;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ MouseHandler.class })
public class MixinMouseHandler {

    @Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;afterMouseAction()V", shift = Shift.AFTER) }, method = { "onPress" }, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onOnPress(long window, int i1, int i2, int i3, CallbackInfo info, boolean flag, int i, boolean[] aboolean, double d0, double d1, Screen screen) {
        Screen.wrapScreenError(() -> MinecraftForge.EVENT_BUS.post(new ScreenMouseClickedEvent(d0, d1, i)), "Konkrete mouseClicked event handler", this.getClass().getCanonicalName());
    }
}
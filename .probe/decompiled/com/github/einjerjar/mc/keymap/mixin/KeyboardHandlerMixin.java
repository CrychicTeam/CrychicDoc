package com.github.einjerjar.mc.keymap.mixin;

import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeymapRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ KeyboardHandler.class })
public class KeyboardHandlerMixin {

    private KeyComboData lastValidCombo = null;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(at = { @At("HEAD") }, method = { "keyPress" }, cancellable = true)
    private void onKeyPress(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (windowPointer == this.minecraft.getWindow().getWindow()) {
            Screen screen = this.minecraft.screen;
            if (screen == null) {
                KeyComboData kd = new KeyComboData(key, Screen.hasAltDown(), Screen.hasShiftDown(), Screen.hasControlDown());
                if (this.lastValidCombo != null && action == 0 && this.lastValidCombo.keyCode() == kd.keyCode() && KeymapRegistry.bindMap().inverse().containsKey(this.lastValidCombo)) {
                    KeyMapping k = (KeyMapping) KeymapRegistry.bindMap().inverse().get(this.lastValidCombo);
                    k.setDown(false);
                    this.lastValidCombo = null;
                    ci.cancel();
                    return;
                }
                if (kd.onlyKey()) {
                    return;
                }
                if (KeymapRegistry.bindMap().inverse().containsKey(kd)) {
                    KeyMapping k = (KeyMapping) KeymapRegistry.bindMap().inverse().get(kd);
                    k.setDown(action == 1 || action == 2);
                    k.clickCount++;
                    this.lastValidCombo = kd;
                    ci.cancel();
                }
            }
        }
    }
}
package me.srrapero720.embeddiumplus.mixins.impl.borderless;

import me.srrapero720.embeddiumplus.EmbyConfig;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ KeyboardHandler.class })
public class KeyboardF11Mixin {

    @Shadow
    @Final
    public Minecraft minecraft;

    @Inject(method = { "keyPress" }, at = { @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;toggleFullScreen()V") }, cancellable = true)
    public void redirect$handleFullScreenToggle(long pWindowPointer, int pKey, int pScanCode, int pAction, int pModifiers, CallbackInfo ci) {
        switch((EmbyConfig.AttachMode) EmbyConfig.borderlessAttachModeF11.get()) {
            case ATTACH:
                EmbyConfig.setFullScreenMode(this.minecraft.options, EmbyConfig.FullScreenMode.nextOf((EmbyConfig.FullScreenMode) EmbyConfig.fullScreen.get()));
                break;
            case REPLACE:
                EmbyConfig.setFullScreenMode(this.minecraft.options, EmbyConfig.FullScreenMode.nextBorderless((EmbyConfig.FullScreenMode) EmbyConfig.fullScreen.get()));
                break;
            case OFF:
                EmbyConfig.setFullScreenMode(this.minecraft.options, EmbyConfig.FullScreenMode.nextFullscreen((EmbyConfig.FullScreenMode) EmbyConfig.fullScreen.get()));
        }
        ci.cancel();
    }
}
package com.simibubi.create.foundation.mixin.client;

import com.mojang.blaze3d.platform.Window;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Minecraft.class })
public class WindowResizeMixin {

    @Shadow
    @Final
    private Window window;

    @Inject(method = { "resizeDisplay()V" }, at = { @At("TAIL") })
    private void create$updateWindowSize(CallbackInfo ci) {
        UIRenderHelper.updateWindowSize(this.window);
    }
}
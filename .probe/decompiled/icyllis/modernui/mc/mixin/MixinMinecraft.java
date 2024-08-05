package icyllis.modernui.mc.mixin;

import com.mojang.blaze3d.platform.Window;
import icyllis.modernui.mc.BlurHandler;
import icyllis.modernui.mc.ModernUIClient;
import icyllis.modernui.mc.MuiModApi;
import icyllis.modernui.mc.UIManager;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Minecraft.class })
public abstract class MixinMinecraft {

    @Shadow
    @Nullable
    public Screen screen;

    @Shadow
    @Final
    private Window window;

    @Shadow
    public abstract boolean isWindowActive();

    @Inject(method = { "setScreen" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;", opcode = 181) })
    private void onSetScreen(Screen guiScreen, CallbackInfo ci) {
        MuiModApi.dispatchOnScreenChange(this.screen, guiScreen);
    }

    @Inject(method = { "allowsTelemetry" }, at = { @At("HEAD") }, cancellable = true)
    private void onAllowsTelemetry(CallbackInfoReturnable<Boolean> info) {
        if (ModernUIClient.sRemoveTelemetrySession) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = { "getFramerateLimit" }, at = { @At("HEAD") }, cancellable = true)
    private void onGetFramerateLimit(CallbackInfoReturnable<Integer> info) {
        if ((BlurHandler.sFramerateInactive != 0 || BlurHandler.sFramerateMinimized != 0) && !this.isWindowActive()) {
            if (BlurHandler.sFramerateMinimized != 0 && BlurHandler.sFramerateMinimized < BlurHandler.sFramerateInactive && GLFW.glfwGetWindowAttrib(this.window.getWindow(), 131074) != 0) {
                info.setReturnValue(Math.min(BlurHandler.sFramerateMinimized, this.window.getFramerateLimit()));
            } else if (BlurHandler.sFramerateInactive != 0) {
                info.setReturnValue(Math.min(BlurHandler.sFramerateInactive, this.window.getFramerateLimit()));
            }
        }
    }

    @Inject(method = { "close" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/Util;shutdownExecutors()V") })
    private void onClose(CallbackInfo ci) {
        UIManager.destroy();
    }
}
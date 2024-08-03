package me.jellysquid.mods.sodium.mixin.features.gui.hooks.console;

import me.jellysquid.mods.sodium.client.gui.console.ConsoleHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GameRenderer.class })
public class GameRendererMixin {

    @Shadow
    @Final
    Minecraft minecraft;

    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    @Unique
    private static boolean HAS_RENDERED_OVERLAY_ONCE = false;

    @Inject(method = { "render" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;flush()V", shift = Shift.AFTER) })
    private void onRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (Minecraft.getInstance().getOverlay() == null || HAS_RENDERED_OVERLAY_ONCE) {
            this.minecraft.getProfiler().push("sodium_console_overlay");
            GuiGraphics drawContext = new GuiGraphics(this.minecraft, this.renderBuffers.bufferSource());
            ConsoleHooks.render(drawContext, GLFW.glfwGetTime());
            drawContext.flush();
            this.minecraft.getProfiler().pop();
            HAS_RENDERED_OVERLAY_ONCE = true;
        }
    }
}
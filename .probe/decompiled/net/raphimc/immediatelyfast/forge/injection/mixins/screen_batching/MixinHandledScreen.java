package net.raphimc.immediatelyfast.forge.injection.mixins.screen_batching;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { AbstractContainerScreen.class }, priority = 500)
public abstract class MixinHandledScreen {

    @Shadow
    public static void renderSlotHighlight(GuiGraphics context, int x, int y, int z, int slotColor) {
    }

    @Redirect(method = { "render" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;renderSlotHighlight(Lnet/minecraft/client/gui/DrawContext;IIII)V"))
    private void drawSlotHightlightOnTop(GuiGraphics context, int x, int y, int z, int slotColor) {
        BatchingBuffers.beginItemOverlayRendering();
        renderSlotHighlight(context, x, y, z, slotColor);
        BatchingBuffers.endItemOverlayRendering();
    }
}
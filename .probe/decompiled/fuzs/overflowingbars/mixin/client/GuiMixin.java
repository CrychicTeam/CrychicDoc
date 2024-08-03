package fuzs.overflowingbars.mixin.client;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Gui.class })
abstract class GuiMixin {

    @Inject(method = { "renderExperienceBar" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;experienceLevel:I", ordinal = 0) })
    public void renderExperienceBar$0(GuiGraphics guiGraphics, int xPos, CallbackInfo callback) {
        if (OverflowingBars.CONFIG.get(ClientConfig.class).moveExperienceAboveBar) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0, -3.0, 0.0);
        }
    }

    @Inject(method = { "renderExperienceBar" }, at = { @At("TAIL") })
    public void renderExperienceBar$1(GuiGraphics guiGraphics, int xPos, CallbackInfo callback) {
        if (OverflowingBars.CONFIG.get(ClientConfig.class).moveExperienceAboveBar) {
            guiGraphics.pose().popPose();
        }
    }
}
package me.jellysquid.mods.sodium.mixin.features.render.gui.debug;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ForgeGui.class })
public abstract class ForgeGuiMixin extends Gui {

    private DebugScreenOverlay embeddium$debugOverlay;

    public ForgeGuiMixin(Minecraft pMinecraft, ItemRenderer pItemRenderer) {
        super(pMinecraft, pItemRenderer);
    }

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void accessDebugOverlay(Minecraft mc, CallbackInfo ci) {
        this.embeddium$debugOverlay = (DebugScreenOverlay) ObfuscationReflectionHelper.getPrivateValue(ForgeGui.class, (ForgeGui) this, "debugOverlay");
    }

    @Inject(method = { "renderHUDText" }, at = { @At(value = "INVOKE", target = "Lnet/minecraftforge/eventbus/api/IEventBus;post(Lnet/minecraftforge/eventbus/api/Event;)Z", shift = Shift.AFTER, remap = false) }, remap = false)
    private void renderLinesVanilla(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci, @Local(ordinal = 0) ArrayList<String> listL, @Local(ordinal = 1) ArrayList<String> listR) {
        DebugScreenOverlayAccessor accessor = (DebugScreenOverlayAccessor) this.embeddium$debugOverlay;
        guiGraphics.drawManaged(() -> {
            accessor.invokeRenderLines(guiGraphics, listL, true);
            accessor.invokeRenderLines(guiGraphics, listR, false);
        });
        listL.clear();
        listR.clear();
    }
}
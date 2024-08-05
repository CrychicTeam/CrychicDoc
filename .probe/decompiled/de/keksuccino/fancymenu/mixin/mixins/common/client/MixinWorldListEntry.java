package de.keksuccino.fancymenu.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import de.keksuccino.fancymenu.FancyMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ WorldSelectionList.WorldListEntry.class })
public class MixinWorldListEntry {

    @WrapWithCondition(at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V") }, method = { "render" })
    private boolean wrapBlitInRenderFancyMenu(GuiGraphics graphics, ResourceLocation loc, int int0, int int1, float float2, float float3, int int4, int int5, int int6, int int7) {
        return int7 == 32 ? FancyMenu.getOptions().showSingleplayerScreenWorldIcons.getValue() : true;
    }

    @WrapWithCondition(at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V") }, method = { "render" })
    private boolean wrapFillInRenderFancyMenu(GuiGraphics graphics, int int0, int int1, int int2, int int3, int int4) {
        return FancyMenu.getOptions().showSingleplayerScreenWorldIcons.getValue();
    }
}
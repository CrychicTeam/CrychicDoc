package de.keksuccino.fancymenu.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import de.keksuccino.fancymenu.util.rendering.ui.widget.CustomizableWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ AbstractButton.class })
public class MixinAbstractButton {

    @WrapWithCondition(method = { "renderWidget" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitNineSliced(Lnet/minecraft/resources/ResourceLocation;IIIIIIIIII)V") })
    private boolean wrapBlitNineSlicedFancyMenu(GuiGraphics graphics, ResourceLocation $$0, int $$1, int $$2, int $$3, int $$4, int $$5, int $$6, int $$7, int $$8, int $$9, int $$10) {
        AbstractButton button = (AbstractButton) this;
        return ((CustomizableWidget) this).renderCustomBackgroundFancyMenu(button, graphics, button.m_252754_(), button.m_252907_(), button.m_5711_(), button.m_93694_());
    }
}
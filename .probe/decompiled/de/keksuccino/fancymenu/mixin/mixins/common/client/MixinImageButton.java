package de.keksuccino.fancymenu.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.widget.CustomizableWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ImageButton.class })
public abstract class MixinImageButton {

    @WrapWithCondition(method = { "renderWidget" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ImageButton;renderTexture(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/ResourceLocation;IIIIIIIII)V") })
    private boolean wrapRenderTextureFancyMenu(ImageButton instance, GuiGraphics graphics, ResourceLocation location, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i0) {
        ImageButton button = (ImageButton) this;
        CustomizableWidget customizable = (CustomizableWidget) this;
        boolean renderVanilla = ((CustomizableWidget) this).renderCustomBackgroundFancyMenu(button, graphics, button.m_252754_(), button.m_252907_(), button.m_5711_(), button.m_93694_());
        if (!renderVanilla && (customizable.getCustomLabelFancyMenu() != null && !button.m_198029_() || customizable.getHoverLabelFancyMenu() != null && button.m_198029_())) {
            int labelColor = button.f_93623_ ? 16777215 : 10526880;
            button.m_280139_(graphics, Minecraft.getInstance().font, labelColor | Mth.ceil(((IMixinAbstractWidget) button).getAlphaFancyMenu() * 255.0F) << 24);
        }
        RenderSystem.enableBlend();
        graphics.setColor(1.0F, 1.0F, 1.0F, ((IMixinAbstractWidget) button).getAlphaFancyMenu());
        return renderVanilla;
    }

    @Inject(method = { "renderWidget" }, at = { @At("RETURN") })
    private void afterRenderWidgetFancyMenu(GuiGraphics graphics, int $$1, int $$2, float $$3, CallbackInfo ci) {
        RenderingUtils.resetShaderColor(graphics);
    }
}
package de.keksuccino.fancymenu.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayer;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayerHandler;
import de.keksuccino.fancymenu.events.screen.RenderedScreenBackgroundEvent;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.fancymenu.util.rendering.ui.widget.UniqueWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ CreateWorldScreen.class })
public class MixinCreateWorldScreen extends Screen {

    protected MixinCreateWorldScreen(Component $$0) {
        super($$0);
    }

    @WrapOperation(method = { "init" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;") })
    private <T extends LayoutElement> T wrapAddChildInInit_FancyMenu(GridLayout.RowHelper instance, T layoutElement, Operation<T> original) {
        if (layoutElement instanceof Button b && b.m_6035_() instanceof MutableComponent c && c.getContents() instanceof TranslatableContents t) {
            if ("selectWorld.create".equals(t.getKey())) {
                ((UniqueWidget) b).setWidgetIdentifierFancyMenu("create_world_button");
            }
            if ("gui.cancel".equals(t.getKey())) {
                ((UniqueWidget) b).setWidgetIdentifierFancyMenu("cancel_button");
            }
        }
        return (T) original.call(new Object[] { instance, layoutElement });
    }

    @WrapWithCondition(method = { "render" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V") })
    private boolean wrapFooterSeparatorRenderingInRender_FancyMenu(GuiGraphics instance, ResourceLocation resourceLocation0, int int1, int int2, float float3, float float4, int int5, int int6, int int7, int int8) {
        if (ScreenCustomization.isCustomizationEnabledForScreen(this)) {
            ScreenCustomizationLayer layer = ScreenCustomizationLayerHandler.getLayerOfScreen(this);
            if (layer != null) {
                return layer.layoutBase.renderScrollListFooterShadow;
            }
        }
        return true;
    }

    @Inject(method = { "renderDirtBackground" }, at = { @At("RETURN") })
    private void afterRenderDirtBackgroundInCreateWorldFancyMenu(GuiGraphics graphics, CallbackInfo info) {
        EventHandler.INSTANCE.postEvent(new RenderedScreenBackgroundEvent(this, graphics));
    }
}
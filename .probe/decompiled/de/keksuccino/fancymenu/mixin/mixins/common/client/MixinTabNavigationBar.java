package de.keksuccino.fancymenu.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayer;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayerHandler;
import de.keksuccino.fancymenu.events.widget.RenderTabNavigationBarHeaderBackgroundEvent;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ TabNavigationBar.class })
public class MixinTabNavigationBar {

    @WrapWithCondition(method = { "render" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V") })
    private boolean wrapHeaderSeparatorRenderingInRender_FancyMenu(GuiGraphics instance, ResourceLocation resourceLocation0, int int1, int int2, float float3, float float4, int int5, int int6, int int7, int int8) {
        if (this.isBarPartOfCurrentScreen_FancyMenu() && ScreenCustomization.isCustomizationEnabledForScreen(Minecraft.getInstance().screen)) {
            ScreenCustomizationLayer layer = ScreenCustomizationLayerHandler.getLayerOfScreen(Minecraft.getInstance().screen);
            if (layer != null) {
                return layer.layoutBase.renderScrollListHeaderShadow;
            }
        }
        return true;
    }

    @WrapOperation(method = { "render" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V") })
    private void wrapHeaderBackgroundRenderingInRender_FancyMenu(GuiGraphics instance, int fromX, int fromY, int toX, int toY, int color, Operation<Void> original) {
        if (this.isBarPartOfCurrentScreen_FancyMenu()) {
            RenderTabNavigationBarHeaderBackgroundEvent.Pre pre = new RenderTabNavigationBarHeaderBackgroundEvent.Pre(this.getBar_FancyMenu(), instance, toX, toY);
            EventHandler.INSTANCE.postEvent(pre);
            if (!pre.isCanceled()) {
                original.call(new Object[] { instance, fromX, fromY, toX, toY, color });
            }
            EventHandler.INSTANCE.postEvent(new RenderTabNavigationBarHeaderBackgroundEvent.Post(this.getBar_FancyMenu(), instance, toX, toY));
        } else {
            original.call(new Object[] { instance, fromX, fromY, toX, toY, color });
        }
    }

    @Inject(method = { "setFocused(Lnet/minecraft/client/gui/components/events/GuiEventListener;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/tabs/TabManager;setCurrentTab(Lnet/minecraft/client/gui/components/tabs/Tab;Z)V", shift = Shift.AFTER) })
    private void after_setCurrentTab_in_setFocused_FancyMenu(GuiEventListener guiEventListener, CallbackInfo info) {
        this.reInitScreenAfterTabChanged_FancyMenu();
    }

    @Inject(method = { "keyPressed" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/tabs/TabNavigationBar;selectTab(IZ)V", shift = Shift.AFTER) })
    private void after_selectTab_in_keyPressed_FancyMenu(int i, CallbackInfoReturnable<Boolean> info) {
        this.reInitScreenAfterTabChanged_FancyMenu();
    }

    @Unique
    private void reInitScreenAfterTabChanged_FancyMenu() {
        if (Minecraft.getInstance().screen != null && ScreenCustomization.isCustomizationEnabledForScreen(Minecraft.getInstance().screen) && this.isBarPartOfCurrentScreen_FancyMenu()) {
            ScreenCustomization.reInitCurrentScreen();
        }
    }

    @Unique
    private boolean isBarPartOfCurrentScreen_FancyMenu() {
        return Minecraft.getInstance().screen == null ? false : ((IMixinScreen) Minecraft.getInstance().screen).getChildrenFancyMenu().contains(this.getBar_FancyMenu());
    }

    @Unique
    private TabNavigationBar getBar_FancyMenu() {
        return (TabNavigationBar) this;
    }
}
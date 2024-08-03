package de.keksuccino.fancymenu.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.deep.layers.DeepScreenCustomizationLayers;
import de.keksuccino.fancymenu.customization.deep.layers.titlescreen.realmsnotification.TitleScreenRealmsNotificationDeepElement;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayer;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayerHandler;
import de.keksuccino.fancymenu.events.screen.RenderedScreenBackgroundEvent;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ TitleScreen.class })
public abstract class MixinTitleScreen extends Screen {

    @Shadow
    public boolean fading;

    @Unique
    boolean handleRealmsNotificationFancyMenu = false;

    @Unique
    GuiGraphics cachedGraphics_FancyMenu = null;

    @Unique
    boolean shouldRenderVanillaBackground_FancyMenu = true;

    private MixinTitleScreen() {
        super(Component.empty());
    }

    @Inject(method = { "render" }, at = { @At("HEAD") })
    private void head_render_FancyMenu(GuiGraphics graphics, int $$1, int $$2, float $$3, CallbackInfo info) {
        this.shouldRenderVanillaBackground_FancyMenu = true;
        this.cachedGraphics_FancyMenu = graphics;
    }

    @WrapOperation(method = { "render" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PanoramaRenderer;render(FF)V") })
    private void wrap_PanoramaRenderer_render_in_render_FancyMenu(PanoramaRenderer instance, float deltaT, float alpha, Operation<Void> original) {
        ScreenCustomizationLayer l = ScreenCustomizationLayerHandler.getLayerOfScreen(this);
        if (l == null || !ScreenCustomization.isCustomizationEnabledForScreen(this) || this.cachedGraphics_FancyMenu == null) {
            original.call(new Object[] { instance, deltaT, alpha });
        } else if (l.layoutBase.menuBackground != null) {
            RenderSystem.enableBlend();
            this.cachedGraphics_FancyMenu.fill(0, 0, this.f_96543_, this.f_96544_, 0);
            RenderingUtils.resetShaderColor(this.cachedGraphics_FancyMenu);
            this.shouldRenderVanillaBackground_FancyMenu = false;
        } else {
            original.call(new Object[] { instance, deltaT, alpha });
        }
        EventHandler.INSTANCE.postEvent(new RenderedScreenBackgroundEvent(this, this.cachedGraphics_FancyMenu));
    }

    @WrapWithCondition(method = { "render" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V") })
    private boolean wrap_blit_in_render_FancyMenu(GuiGraphics instance, ResourceLocation atlasLocation, int x, int y, int width, int height, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        return this.shouldRenderVanillaBackground_FancyMenu;
    }

    @WrapWithCondition(method = { "render" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/LogoRenderer;renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IF)V") })
    private boolean cancelVanillaLogoRenderingFancyMenu(LogoRenderer instance, GuiGraphics $$0, int $$1, float $$2) {
        return !ScreenCustomization.isCustomizationEnabledForScreen(this);
    }

    @WrapWithCondition(method = { "render" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/SplashRenderer;render(Lnet/minecraft/client/gui/GuiGraphics;ILnet/minecraft/client/gui/Font;I)V") })
    private boolean cancelVanillaSplashRenderingFancyMenu(SplashRenderer instance, GuiGraphics $$0, int $$1, Font $$2, int $$3) {
        return !ScreenCustomization.isCustomizationEnabledForScreen(this);
    }

    @Inject(method = { "render" }, at = { @At("HEAD") })
    private void beforeRenderFancyMenu(GuiGraphics $$0, int $$1, int $$2, float $$3, CallbackInfo ci) {
        this.handleRealmsNotificationFancyMenu = true;
        if (ScreenCustomization.isCustomizationEnabledForScreen(this)) {
            this.fading = false;
        }
    }

    @Inject(method = { "render" }, at = { @At("RETURN") })
    private void afterRenderFancyMenu(GuiGraphics $$0, int $$1, int $$2, float $$3, CallbackInfo ci) {
        this.handleRealmsNotificationFancyMenu = false;
    }

    @Inject(method = { "realmsNotificationsEnabled" }, at = { @At("HEAD") }, cancellable = true)
    private void cancelVanillaRealmsNotificationIconRenderingFancyMenu(CallbackInfoReturnable<Boolean> info) {
        if (this.handleRealmsNotificationFancyMenu && ScreenCustomization.isCustomizationEnabledForScreen(this)) {
            ScreenCustomizationLayer layer = ScreenCustomizationLayerHandler.getLayerOfScreen(this);
            if (layer != null && layer.getElementByInstanceIdentifier("deep:" + DeepScreenCustomizationLayers.TITLE_SCREEN.realmsNotification.getIdentifier()) instanceof TitleScreenRealmsNotificationDeepElement d && d.isHidden()) {
                info.setReturnValue(false);
            }
        }
    }

    @WrapWithCondition(method = { "render" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/AbstractWidget;setAlpha(F)V") })
    private boolean wrapRenderAlphaFancyMenu(AbstractWidget instance, float alpha) {
        return !ScreenCustomization.isCustomizationEnabledForScreen(this);
    }
}
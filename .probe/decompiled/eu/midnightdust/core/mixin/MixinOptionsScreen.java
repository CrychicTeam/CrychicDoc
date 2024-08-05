package eu.midnightdust.core.mixin;

import eu.midnightdust.core.config.MidnightLibConfig;
import eu.midnightdust.core.screen.MidnightConfigOverviewScreen;
import eu.midnightdust.lib.util.PlatformFunctions;
import eu.midnightdust.lib.util.screen.TexturedOverlayButtonWidget;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ OptionsScreen.class })
public class MixinOptionsScreen extends Screen {

    private static final ResourceLocation MIDNIGHTLIB_ICON_TEXTURE = new ResourceLocation("midnightlib", "textures/gui/midnightlib_button.png");

    protected MixinOptionsScreen(Component title) {
        super(title);
    }

    @Inject(at = { @At("HEAD") }, method = { "init" })
    private void midnightlib$init(CallbackInfo ci) {
        if (MidnightLibConfig.config_screen_list.equals(MidnightLibConfig.ConfigButton.TRUE) || MidnightLibConfig.config_screen_list.equals(MidnightLibConfig.ConfigButton.MODMENU) && !PlatformFunctions.isModLoaded("modmenu")) {
            this.m_142416_(new TexturedOverlayButtonWidget(this.f_96543_ / 2 + 158, this.f_96544_ / 6 - 12, 20, 20, 0, 0, 20, MIDNIGHTLIB_ICON_TEXTURE, 32, 64, buttonWidget -> ((Minecraft) Objects.requireNonNull(this.f_96541_)).setScreen(new MidnightConfigOverviewScreen(this)), Component.translatable("midnightlib.overview.title")));
        }
    }
}
package net.minecraft.client.gui.screens;

import net.minecraft.Util;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class AccessibilityOptionsScreen extends SimpleOptionsSubScreen {

    private static OptionInstance<?>[] options(Options options0) {
        return new OptionInstance[] { options0.narrator(), options0.showSubtitles(), options0.highContrast(), options0.autoJump(), options0.textBackgroundOpacity(), options0.backgroundForChatOnly(), options0.chatOpacity(), options0.chatLineSpacing(), options0.chatDelay(), options0.notificationDisplayTime(), options0.toggleCrouch(), options0.toggleSprint(), options0.screenEffectScale(), options0.fovEffectScale(), options0.darknessEffectScale(), options0.damageTiltStrength(), options0.glintSpeed(), options0.glintStrength(), options0.hideLightningFlash(), options0.darkMojangStudiosBackground(), options0.panoramaSpeed() };
    }

    public AccessibilityOptionsScreen(Screen screen0, Options options1) {
        super(screen0, options1, Component.translatable("options.accessibility.title"), options(options1));
    }

    @Override
    protected void init() {
        super.init();
        AbstractWidget $$0 = this.f_96668_.findOption(this.f_96282_.highContrast());
        if ($$0 != null && !this.f_96541_.getResourcePackRepository().getAvailableIds().contains("high_contrast")) {
            $$0.active = false;
            $$0.setTooltip(Tooltip.create(Component.translatable("options.accessibility.high_contrast.error.tooltip")));
        }
    }

    @Override
    protected void createFooter() {
        this.m_142416_(Button.builder(Component.translatable("options.accessibility.link"), p_280784_ -> this.f_96541_.setScreen(new ConfirmLinkScreen(p_280783_ -> {
            if (p_280783_) {
                Util.getPlatform().openUri("https://aka.ms/MinecraftJavaAccessibility");
            }
            this.f_96541_.setScreen(this);
        }, "https://aka.ms/MinecraftJavaAccessibility", true))).bounds(this.f_96543_ / 2 - 155, this.f_96544_ - 27, 150, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280785_ -> this.f_96541_.setScreen(this.f_96281_)).bounds(this.f_96543_ / 2 + 5, this.f_96544_ - 27, 150, 20).build());
    }
}
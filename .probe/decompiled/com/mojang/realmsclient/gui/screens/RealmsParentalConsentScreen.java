package com.mojang.realmsclient.gui.screens;

import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsParentalConsentScreen extends RealmsScreen {

    private static final Component MESSAGE = Component.translatable("mco.account.privacyinfo");

    private final Screen nextScreen;

    private MultiLineLabel messageLines = MultiLineLabel.EMPTY;

    public RealmsParentalConsentScreen(Screen screen0) {
        super(GameNarrator.NO_TITLE);
        this.nextScreen = screen0;
    }

    @Override
    public void init() {
        Component $$0 = Component.translatable("mco.account.update");
        Component $$1 = CommonComponents.GUI_BACK;
        int $$2 = Math.max(this.f_96547_.width($$0), this.f_96547_.width($$1)) + 30;
        Component $$3 = Component.translatable("mco.account.privacy.info");
        int $$4 = (int) ((double) this.f_96547_.width($$3) * 1.2);
        this.m_142416_(Button.builder($$3, p_88873_ -> Util.getPlatform().openUri("https://aka.ms/MinecraftGDPR")).bounds(this.f_96543_ / 2 - $$4 / 2, m_120774_(11), $$4, 20).build());
        this.m_142416_(Button.builder($$0, p_88871_ -> Util.getPlatform().openUri("https://aka.ms/UpdateMojangAccount")).bounds(this.f_96543_ / 2 - ($$2 + 5), m_120774_(13), $$2, 20).build());
        this.m_142416_(Button.builder($$1, p_280730_ -> this.f_96541_.setScreen(this.nextScreen)).bounds(this.f_96543_ / 2 + 5, m_120774_(13), $$2, 20).build());
        this.messageLines = MultiLineLabel.create(this.f_96547_, MESSAGE, (int) Math.round((double) this.f_96543_ * 0.9));
    }

    @Override
    public Component getNarrationMessage() {
        return MESSAGE;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.messageLines.renderCentered(guiGraphics0, this.f_96543_ / 2, 15, 15, 16777215);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }
}
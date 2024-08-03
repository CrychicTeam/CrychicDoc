package com.mojang.realmsclient.gui.screens;

import net.minecraft.SharedConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsClientOutdatedScreen extends RealmsScreen {

    private static final Component INCOMPATIBLE_TITLE = Component.translatable("mco.client.incompatible.title");

    private static final Component[] INCOMPATIBLE_MESSAGES_SNAPSHOT = new Component[] { Component.translatable("mco.client.incompatible.msg.line1"), Component.translatable("mco.client.incompatible.msg.line2"), Component.translatable("mco.client.incompatible.msg.line3") };

    private static final Component[] INCOMPATIBLE_MESSAGES = new Component[] { Component.translatable("mco.client.incompatible.msg.line1"), Component.translatable("mco.client.incompatible.msg.line2") };

    private final Screen lastScreen;

    public RealmsClientOutdatedScreen(Screen screen0) {
        super(INCOMPATIBLE_TITLE);
        this.lastScreen = screen0;
    }

    @Override
    public void init() {
        this.m_142416_(Button.builder(CommonComponents.GUI_BACK, p_280710_ -> this.f_96541_.setScreen(this.lastScreen)).bounds(this.f_96543_ / 2 - 100, m_120774_(12), 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, m_120774_(3), 16711680);
        Component[] $$4 = this.getMessages();
        for (int $$5 = 0; $$5 < $$4.length; $$5++) {
            guiGraphics0.drawCenteredString(this.f_96547_, $$4[$$5], this.f_96543_ / 2, m_120774_(5) + $$5 * 12, 16777215);
        }
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    private Component[] getMessages() {
        return SharedConstants.getCurrentVersion().isStable() ? INCOMPATIBLE_MESSAGES : INCOMPATIBLE_MESSAGES_SNAPSHOT;
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 != 257 && int0 != 335 && int0 != 256) {
            return super.m_7933_(int0, int1, int2);
        } else {
            this.f_96541_.setScreen(this.lastScreen);
            return true;
        }
    }
}
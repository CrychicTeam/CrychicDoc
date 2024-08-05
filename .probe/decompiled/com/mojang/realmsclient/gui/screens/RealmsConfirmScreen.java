package com.mojang.realmsclient.gui.screens;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsConfirmScreen extends RealmsScreen {

    protected BooleanConsumer callback;

    private final Component title1;

    private final Component title2;

    public RealmsConfirmScreen(BooleanConsumer booleanConsumer0, Component component1, Component component2) {
        super(GameNarrator.NO_TITLE);
        this.callback = booleanConsumer0;
        this.title1 = component1;
        this.title2 = component2;
    }

    @Override
    public void init() {
        this.m_142416_(Button.builder(CommonComponents.GUI_YES, p_88562_ -> this.callback.accept(true)).bounds(this.f_96543_ / 2 - 105, m_120774_(9), 100, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_NO, p_88559_ -> this.callback.accept(false)).bounds(this.f_96543_ / 2 + 5, m_120774_(9), 100, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.title1, this.f_96543_ / 2, m_120774_(3), 16777215);
        guiGraphics0.drawCenteredString(this.f_96547_, this.title2, this.f_96543_ / 2, m_120774_(5), 16777215);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }
}
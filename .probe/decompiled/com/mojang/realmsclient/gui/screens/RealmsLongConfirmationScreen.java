package com.mojang.realmsclient.gui.screens;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsLongConfirmationScreen extends RealmsScreen {

    static final Component WARNING = Component.translatable("mco.warning");

    static final Component INFO = Component.translatable("mco.info");

    private final RealmsLongConfirmationScreen.Type type;

    private final Component line2;

    private final Component line3;

    protected final BooleanConsumer callback;

    private final boolean yesNoQuestion;

    public RealmsLongConfirmationScreen(BooleanConsumer booleanConsumer0, RealmsLongConfirmationScreen.Type realmsLongConfirmationScreenType1, Component component2, Component component3, boolean boolean4) {
        super(GameNarrator.NO_TITLE);
        this.callback = booleanConsumer0;
        this.type = realmsLongConfirmationScreenType1;
        this.line2 = component2;
        this.line3 = component3;
        this.yesNoQuestion = boolean4;
    }

    @Override
    public void init() {
        if (this.yesNoQuestion) {
            this.m_142416_(Button.builder(CommonComponents.GUI_YES, p_88751_ -> this.callback.accept(true)).bounds(this.f_96543_ / 2 - 105, m_120774_(8), 100, 20).build());
            this.m_142416_(Button.builder(CommonComponents.GUI_NO, p_88749_ -> this.callback.accept(false)).bounds(this.f_96543_ / 2 + 5, m_120774_(8), 100, 20).build());
        } else {
            this.m_142416_(Button.builder(CommonComponents.GUI_OK, p_88746_ -> this.callback.accept(true)).bounds(this.f_96543_ / 2 - 50, m_120774_(8), 100, 20).build());
        }
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinLines(this.type.text, this.line2, this.line3);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.callback.accept(false);
            return true;
        } else {
            return super.m_7933_(int0, int1, int2);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.type.text, this.f_96543_ / 2, m_120774_(2), this.type.colorCode);
        guiGraphics0.drawCenteredString(this.f_96547_, this.line2, this.f_96543_ / 2, m_120774_(4), 16777215);
        guiGraphics0.drawCenteredString(this.f_96547_, this.line3, this.f_96543_ / 2, m_120774_(6), 16777215);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    public static enum Type {

        WARNING(RealmsLongConfirmationScreen.WARNING, 16711680), INFO(RealmsLongConfirmationScreen.INFO, 8226750);

        public final int colorCode;

        public final Component text;

        private Type(Component p_287726_, int p_287605_) {
            this.text = p_287726_;
            this.colorCode = p_287605_;
        }
    }
}
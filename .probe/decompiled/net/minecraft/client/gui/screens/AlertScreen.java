package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class AlertScreen extends Screen {

    private static final int LABEL_Y = 90;

    private final Component messageText;

    private MultiLineLabel message = MultiLineLabel.EMPTY;

    private final Runnable callback;

    private final Component okButton;

    private final boolean shouldCloseOnEsc;

    public AlertScreen(Runnable runnable0, Component component1, Component component2) {
        this(runnable0, component1, component2, CommonComponents.GUI_BACK, true);
    }

    public AlertScreen(Runnable runnable0, Component component1, Component component2, Component component3, boolean boolean4) {
        super(component1);
        this.callback = runnable0;
        this.messageText = component2;
        this.okButton = component3;
        this.shouldCloseOnEsc = boolean4;
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(super.getNarrationMessage(), this.messageText);
    }

    @Override
    protected void init() {
        super.init();
        this.message = MultiLineLabel.create(this.f_96547_, this.messageText, this.f_96543_ - 50);
        int $$0 = this.message.getLineCount() * 9;
        int $$1 = Mth.clamp(90 + $$0 + 12, this.f_96544_ / 6 + 96, this.f_96544_ - 24);
        int $$2 = 150;
        this.m_142416_(Button.builder(this.okButton, p_95533_ -> this.callback.run()).bounds((this.f_96543_ - 150) / 2, $$1, 150, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 70, 16777215);
        this.message.renderCentered(guiGraphics0, this.f_96543_ / 2, 90);
        super.render(guiGraphics0, int1, int2, float3);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return this.shouldCloseOnEsc;
    }
}
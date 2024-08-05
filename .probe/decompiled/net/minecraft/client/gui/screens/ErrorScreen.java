package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ErrorScreen extends Screen {

    private final Component message;

    public ErrorScreen(Component component0, Component component1) {
        super(component0);
        this.message = component1;
    }

    @Override
    protected void init() {
        super.init();
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_280801_ -> this.f_96541_.setScreen(null)).bounds(this.f_96543_ / 2 - 100, 140, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        guiGraphics0.fillGradient(0, 0, this.f_96543_, this.f_96544_, -12574688, -11530224);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 90, 16777215);
        guiGraphics0.drawCenteredString(this.f_96547_, this.message, this.f_96543_ / 2, 110, 16777215);
        super.render(guiGraphics0, int1, int2, float3);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
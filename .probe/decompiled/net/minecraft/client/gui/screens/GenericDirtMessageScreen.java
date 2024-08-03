package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class GenericDirtMessageScreen extends Screen {

    public GenericDirtMessageScreen(Component component0) {
        super(component0);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280039_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 70, 16777215);
        super.render(guiGraphics0, int1, int2, float3);
    }
}
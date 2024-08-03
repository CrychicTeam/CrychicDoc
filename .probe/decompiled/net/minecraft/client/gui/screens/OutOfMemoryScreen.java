package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class OutOfMemoryScreen extends Screen {

    private MultiLineLabel message = MultiLineLabel.EMPTY;

    public OutOfMemoryScreen() {
        super(Component.translatable("outOfMemory.title"));
    }

    @Override
    protected void init() {
        this.m_142416_(Button.builder(CommonComponents.GUI_TO_TITLE, p_280810_ -> this.f_96541_.setScreen(new TitleScreen())).bounds(this.f_96543_ / 2 - 155, this.f_96544_ / 4 + 120 + 12, 150, 20).build());
        this.m_142416_(Button.builder(Component.translatable("menu.quit"), p_280811_ -> this.f_96541_.stop()).bounds(this.f_96543_ / 2 - 155 + 160, this.f_96544_ / 4 + 120 + 12, 150, 20).build());
        this.message = MultiLineLabel.create(this.f_96547_, Component.translatable("outOfMemory.message"), 295);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, this.f_96544_ / 4 - 60 + 20, 16777215);
        this.message.renderLeftAligned(guiGraphics0, this.f_96543_ / 2 - 145, this.f_96544_ / 4, 9, 10526880);
        super.render(guiGraphics0, int1, int2, float3);
    }
}
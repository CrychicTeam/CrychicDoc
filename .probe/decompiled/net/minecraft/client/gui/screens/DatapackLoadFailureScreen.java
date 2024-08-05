package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class DatapackLoadFailureScreen extends Screen {

    private MultiLineLabel message = MultiLineLabel.EMPTY;

    private final Runnable callback;

    public DatapackLoadFailureScreen(Runnable runnable0) {
        super(Component.translatable("datapackFailure.title"));
        this.callback = runnable0;
    }

    @Override
    protected void init() {
        super.init();
        this.message = MultiLineLabel.create(this.f_96547_, this.m_96636_(), this.f_96543_ - 50);
        this.m_142416_(Button.builder(Component.translatable("datapackFailure.safeMode"), p_95905_ -> this.callback.run()).bounds(this.f_96543_ / 2 - 155, this.f_96544_ / 6 + 96, 150, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_TO_TITLE, p_280793_ -> this.f_96541_.setScreen(null)).bounds(this.f_96543_ / 2 - 155 + 160, this.f_96544_ / 6 + 96, 150, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.message.renderCentered(guiGraphics0, this.f_96543_ / 2, 70);
        super.render(guiGraphics0, int1, int2, float3);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
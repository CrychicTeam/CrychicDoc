package net.minecraft.client.gui.screens;

import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.network.chat.Component;

public class OptionsSubScreen extends Screen {

    protected final Screen lastScreen;

    protected final Options options;

    public OptionsSubScreen(Screen screen0, Options options1, Component component2) {
        super(component2);
        this.lastScreen = screen0;
        this.options = options1;
    }

    @Override
    public void removed() {
        this.f_96541_.options.save();
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.lastScreen);
    }

    protected void basicListRender(GuiGraphics guiGraphics0, OptionsList optionsList1, int int2, int int3, float float4) {
        this.m_280273_(guiGraphics0);
        optionsList1.m_88315_(guiGraphics0, int2, int3, float4);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 20, 16777215);
        super.render(guiGraphics0, int2, int3, float4);
    }
}
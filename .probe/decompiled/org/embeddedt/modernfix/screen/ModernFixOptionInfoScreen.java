package org.embeddedt.modernfix.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class ModernFixOptionInfoScreen extends Screen {

    private final Screen lastScreen;

    private final Component description;

    public ModernFixOptionInfoScreen(Screen lastScreen, String optionName) {
        super(Component.literal(optionName));
        this.lastScreen = lastScreen;
        this.description = Component.translatable("modernfix.option." + optionName);
    }

    @Override
    protected void init() {
        super.init();
        this.m_142416_(new Button.Builder(CommonComponents.GUI_DONE, button -> this.onClose()).pos(this.f_96543_ / 2 - 100, this.f_96544_ - 29).size(200, 20).build());
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.lastScreen);
    }

    private void drawMultilineString(GuiGraphics guiGraphics, Font fr, Component str, int x, int y) {
        for (FormattedCharSequence s : fr.split(str, this.f_96543_ - 50)) {
            guiGraphics.drawString(fr, s, x, y, 16777215, true);
            y += 9;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        guiGraphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 8, 16777215);
        this.drawMultilineString(guiGraphics, this.f_96541_.font, this.description, 10, 50);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
}
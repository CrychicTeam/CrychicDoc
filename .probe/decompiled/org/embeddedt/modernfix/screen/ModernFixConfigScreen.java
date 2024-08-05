package org.embeddedt.modernfix.screen;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ModernFixConfigScreen extends Screen {

    private OptionList optionList;

    private Screen lastScreen;

    public boolean madeChanges = false;

    private Button doneButton;

    private Button wikiButton;

    private double lastScrollAmount = 0.0;

    public ModernFixConfigScreen(Screen lastScreen) {
        super(Component.translatable("modernfix.config"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        this.optionList = new OptionList(this, this.f_96541_);
        this.optionList.m_93410_(this.lastScrollAmount);
        this.m_7787_(this.optionList);
        this.wikiButton = new Button.Builder(Component.translatable("modernfix.config.wiki"), arg -> Util.getPlatform().openUri("https://github.com/embeddedt/ModernFix/wiki/Summary-of-Patches")).pos(this.f_96543_ / 2 - 155, this.f_96544_ - 29).size(150, 20).build();
        this.doneButton = new Button.Builder(CommonComponents.GUI_DONE, arg -> this.onClose()).pos(this.f_96543_ / 2 - 155 + 160, this.f_96544_ - 29).size(150, 20).build();
        this.m_142416_(this.wikiButton);
        this.m_142416_(this.doneButton);
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.lastScreen);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        this.optionList.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 8, 16777215);
        this.doneButton.m_93666_((Component) (this.madeChanges ? Component.translatable("modernfix.config.done_restart") : CommonComponents.GUI_DONE));
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    public void setLastScrollAmount(double d) {
        this.lastScrollAmount = d;
    }
}
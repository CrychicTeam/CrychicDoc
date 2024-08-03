package yesman.epicfight.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.client.gui.component.EpicFightOptionList;
import yesman.epicfight.config.EpicFightOptions;
import yesman.epicfight.main.EpicFightMod;

@OnlyIn(Dist.CLIENT)
public class EpicFightOptionSubScreen extends Screen {

    protected final Screen lastScreen;

    protected final EpicFightOptions config;

    public EpicFightOptionSubScreen(Screen parentScreen, EpicFightOptions config, Component title) {
        super(title);
        this.lastScreen = parentScreen;
        this.config = config;
    }

    @Override
    protected void init() {
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, button -> {
            EpicFightMod.CLIENT_CONFIGS.save();
            this.onClose();
        }).bounds(this.f_96543_ / 2 - 100, this.f_96544_ - 28, 200, 20).build());
    }

    @Override
    public void removed() {
        this.config.save();
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.lastScreen);
    }

    protected void basicListRender(GuiGraphics guiGraphics, EpicFightOptionList optionList, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        optionList.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 20, 16777215);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
}
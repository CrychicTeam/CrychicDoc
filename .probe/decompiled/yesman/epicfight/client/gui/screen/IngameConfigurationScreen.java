package yesman.epicfight.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.config.EpicFightOptions;
import yesman.epicfight.main.EpicFightMod;

@OnlyIn(Dist.CLIENT)
public class IngameConfigurationScreen extends Screen {

    protected final Screen parentScreen;

    public IngameConfigurationScreen(Minecraft mc, Screen screen) {
        super(Component.translatable("gui.epicfight.configurations"));
        this.parentScreen = screen;
    }

    @Override
    protected void init() {
        EpicFightOptions configs = EpicFightMod.CLIENT_CONFIGS;
        this.m_142416_(Button.builder(Component.translatable("gui.epicfight.button.graphics"), button -> Minecraft.getInstance().setScreen(new EpicFightGraphicOptionScreen(this, configs))).pos(this.f_96543_ / 2 - 165, 42).size(160, 20).build());
        this.m_142416_(Button.builder(Component.translatable("gui.epicfight.button.controls"), button -> Minecraft.getInstance().setScreen(new EpicFightControlOptionScreen(this, configs))).pos(this.f_96543_ / 2 + 5, 42).size(160, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, button -> this.f_96541_.setScreen(this.parentScreen)).bounds(this.f_96543_ / 2 - 100, this.f_96544_ - 40, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280039_(guiGraphics);
        guiGraphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 15, 16777215);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.parentScreen);
    }
}
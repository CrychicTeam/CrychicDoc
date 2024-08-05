package net.blay09.mods.craftingtweaks.client;

import net.blay09.mods.craftingtweaks.api.ButtonProperties;
import net.blay09.mods.craftingtweaks.api.ButtonState;
import net.blay09.mods.craftingtweaks.api.ButtonStateProperties;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GuiImageButton extends Button {

    private static final ResourceLocation texture = new ResourceLocation("craftingtweaks", "gui.png");

    protected ButtonProperties properties;

    public GuiImageButton(int x, int y, ButtonProperties properties) {
        super(x, y, properties.getWidth(), properties.getHeight(), Component.empty(), it -> {
        }, Button.DEFAULT_NARRATION);
        this.properties = properties;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.f_93622_ = this.f_93623_ && this.f_93624_ && mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        ButtonState state = this.f_93622_ ? ButtonState.HOVER : ButtonState.NORMAL;
        if (!this.f_93623_) {
            state = ButtonState.DISABLED;
        }
        ButtonStateProperties stateProperties = this.properties.getState(state);
        guiGraphics.blit(texture, this.m_252754_(), this.m_252907_(), stateProperties.getTextureX(), stateProperties.getTextureY(), this.f_93618_, this.f_93619_);
    }
}
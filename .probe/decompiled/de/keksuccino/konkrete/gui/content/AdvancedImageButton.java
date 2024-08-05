package de.keksuccino.konkrete.gui.content;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;

public class AdvancedImageButton extends AdvancedButton {

    private ResourceLocation image;

    public AdvancedImageButton(int x, int y, int widthIn, int heightIn, ResourceLocation image, boolean handleClick, Button.OnPress onPress) {
        super(x, y, widthIn, heightIn, "", handleClick, onPress);
        this.image = image;
    }

    public AdvancedImageButton(int x, int y, int widthIn, int heightIn, ResourceLocation image, Button.OnPress onPress) {
        super(x, y, widthIn, heightIn, "", onPress);
        this.image = image;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(this.image, this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.m_5711_(), this.m_93694_(), this.m_5711_(), this.m_93694_());
    }

    public void setImage(ResourceLocation image) {
        this.image = image;
    }
}
package fr.frinn.custommachinery.client.element;

import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.common.guielement.TextGuiElement;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class TextGuiElementWidget extends AbstractGuiElementWidget<TextGuiElement> {

    public TextGuiElementWidget(TextGuiElement element, IMachineScreen screen) {
        super(element, screen, element.getText());
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int posX = switch(this.getElement().getAlignment()) {
            case CENTER ->
                this.m_252754_() - Minecraft.getInstance().font.width(this.getElement().getText().getString()) / 2;
            case RIGHT ->
                this.m_252754_() - Minecraft.getInstance().font.width(this.getElement().getText().getString());
            default ->
                this.m_252754_();
        };
        int posY = this.m_252907_();
        graphics.pose().pushPose();
        float scaleX = 1.0F;
        float scaleY = 1.0F;
        if (this.getElement().getWidth() >= 0) {
            scaleX = (float) this.getElement().getWidth() / (float) Minecraft.getInstance().font.width(this.getElement().getText());
        }
        if (this.getElement().getHeight() >= 0) {
            scaleY = (float) this.getElement().getHeight() / 9.0F;
        }
        if (scaleX == 1.0F && scaleY != 1.0F) {
            scaleX = scaleY;
        } else if (scaleX != 1.0F && scaleY == 1.0F) {
            scaleY = scaleX;
        }
        if (scaleX != 1.0F) {
            graphics.pose().translate((float) this.m_252754_(), (float) this.m_252907_(), 0.0F);
            graphics.pose().scale(scaleX, scaleY, 1.0F);
            graphics.pose().translate((float) (-this.m_252754_()), (float) (-this.m_252907_()), 0.0F);
        }
        graphics.drawString(Minecraft.getInstance().font, this.getElement().getText(), posX, posY, 0, false);
        graphics.pose().popPose();
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return false;
    }
}
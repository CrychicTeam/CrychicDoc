package net.zanckor.questapi.example.client.screen.button;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.zanckor.questapi.mod.common.util.MCUtilClient;

public class TextButton extends Button {

    float scale;

    int maxLength;

    public TextButton(int xPosition, int yPosition, int width, int height, float scale, Component component, int maxLength, Button.OnPress onPress) {
        super(xPosition, yPosition, width, height, component, onPress, f_252438_);
        this.scale = scale;
        this.maxLength = maxLength;
    }

    @Override
    public void render(GuiGraphics graphics, int xPos, int yPos, float v) {
        PoseStack poseStack = graphics.pose();
        Style style = !this.m_5953_((double) xPos, (double) yPos) ? this.m_6035_().getStyle() : this.m_6035_().getStyle().withUnderlined(true);
        poseStack.pushPose();
        poseStack.translate((float) this.m_252754_(), (float) this.m_252907_(), 0.0F);
        poseStack.scale(this.scale, this.scale, 1.0F);
        MCUtilClient.renderLine(graphics, poseStack, this.maxLength, 0.0F, 0.0F, this.scale, 12.0F, Component.literal(this.m_6035_().getString()).withStyle(style), Minecraft.getInstance().font);
        poseStack.popPose();
    }
}
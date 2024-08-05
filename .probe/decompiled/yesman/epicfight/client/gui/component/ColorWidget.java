package yesman.epicfight.client.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.utils.math.QuaternionUtils;
import yesman.epicfight.config.OptionHandler;

@OnlyIn(Dist.CLIENT)
public class ColorWidget extends AbstractSliderButton {

    private static final int[] COLOR_ARRAY = new int[] { -65536, -256, -16711936, -16711681, -16776961, -65281, -65536 };

    private final OptionHandler.DoubleOptionHandler colorOption;

    public ColorWidget(int x, int y, int width, int height, Component message, double defaultValue, OptionHandler.DoubleOptionHandler option) {
        super(x, y, width, height, message, defaultValue);
        this.colorOption = option;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontrenderer = minecraft.font;
        RenderSystem.enableBlend();
        for (int i = 0; i < 6; i++) {
            this.fillGradient(guiGraphics, this.m_252754_() + this.f_93618_ * i / 6, this.m_252907_(), this.m_252754_() + this.f_93618_ * (i + 1) / 6, this.m_252907_() + this.f_93619_, COLOR_ARRAY[i], COLOR_ARRAY[i + 1]);
        }
        this.renderBg(guiGraphics, minecraft, mouseX, mouseY);
        int j = this.getFGColor();
        guiGraphics.drawCenteredString(fontrenderer, this.m_6035_(), this.m_252754_() + this.f_93618_ / 2, this.m_252907_() + (this.f_93619_ - 8) / 2, j | Mth.ceil(this.f_93625_ * 255.0F) << 24);
    }

    protected void renderBg(GuiGraphics guiGraphics, Minecraft minecraft, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, f_93617_);
        int i = (this.m_198029_() ? 2 : 1) * 20;
        int minX = this.m_252754_() + (int) (this.f_93577_ * (double) (this.f_93618_ - 8));
        guiGraphics.blit(f_93617_, minX, this.m_252907_(), 0, 46 + i, 4, 20);
        guiGraphics.blit(f_93617_, minX + 4, this.m_252907_(), 196, 46 + i, 4, 20);
        guiGraphics.fill(minX + 1, this.m_252907_() + 1, minX + 7, this.m_252907_() + 19, toColorInteger(this.f_93577_));
    }

    @Override
    protected void applyValue() {
        this.colorOption.setValue(this.f_93577_);
    }

    @Override
    protected void updateMessage() {
    }

    public static int toColorInteger(double value) {
        int packedColor = 0;
        for (int i = 0; i < 6; i++) {
            double min = 0.16666666666666666 * (double) i;
            double max = 0.16666666666666666 * (double) (i + 1);
            if (value >= min && value <= max) {
                double lerpFactor = (value - min) / (max - min);
                int colorA = COLOR_ARRAY[i];
                int colorB = COLOR_ARRAY[i + 1];
                int f = colorA >> 24 & 0xFF;
                int f1 = colorA >> 16 & 0xFF;
                int f2 = colorA >> 8 & 0xFF;
                int f3 = colorA & 0xFF;
                int f4 = colorB >> 24 & 0xFF;
                int f5 = colorB >> 16 & 0xFF;
                int f6 = colorB >> 8 & 0xFF;
                int f7 = colorB & 0xFF;
                int r = (int) Mth.lerp(lerpFactor, (double) f, (double) f4);
                int g = (int) Mth.lerp(lerpFactor, (double) f1, (double) f5);
                int b = (int) Mth.lerp(lerpFactor, (double) f2, (double) f6);
                int a = (int) Mth.lerp(lerpFactor, (double) f3, (double) f7);
                packedColor = r << 24 | g << 16 | b << 8 | a;
            }
        }
        return packedColor;
    }

    protected void fillGradient(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int colorA, int colorB) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        int width = x2 - x1;
        int height = y2 - y1;
        int newX = x1 + width / 2;
        int newY = y1 + height / 2;
        poseStack.translate((float) newX, (float) newY, 0.0F);
        poseStack.mulPose(QuaternionUtils.ZP.rotationDegrees(-90.0F));
        guiGraphics.fillGradient(-height / 2, -width / 2, height / 2, width / 2, colorA, colorB);
        poseStack.popPose();
    }
}
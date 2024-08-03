package mezz.jei.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.common.gui.elements.DrawableNineSliceTexture;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.util.ImmutableRect2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;

public class GuiIconButtonSmall extends Button {

    private final IDrawable icon;

    private final Textures textures;

    public GuiIconButtonSmall(int x, int y, int widthIn, int heightIn, IDrawable icon, Button.OnPress pressable, Textures textures) {
        super(x, y, widthIn, heightIn, CommonComponents.EMPTY, pressable, Button.DEFAULT_NARRATION);
        this.icon = icon;
        this.textures = textures;
    }

    public ImmutableRect2i getArea() {
        return new ImmutableRect2i(this.m_252754_(), this.m_252907_(), this.m_5711_(), this.m_93694_());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            boolean hovered = this.m_5953_((double) mouseX, (double) mouseY);
            DrawableNineSliceTexture texture = this.textures.getButtonForState(this.f_93623_, hovered);
            texture.draw(guiGraphics, this.m_252754_(), this.m_252907_(), this.m_5711_(), this.m_93694_());
            int color = -2039584;
            if (!this.f_93623_) {
                color = -6250336;
            } else if (hovered) {
                color = -1;
            }
            float red = (float) (color >> 16 & 0xFF) / 255.0F;
            float blue = (float) (color >> 8 & 0xFF) / 255.0F;
            float green = (float) (color & 0xFF) / 255.0F;
            float alpha = (float) (color >> 24 & 0xFF) / 255.0F;
            RenderSystem.setShaderColor(red, blue, green, alpha);
            double xOffset = (double) this.m_252754_() + (double) (this.m_5711_() - this.icon.getWidth()) / 2.0;
            double yOffset = (double) this.m_252907_() + (double) (this.m_93694_() - this.icon.getHeight()) / 2.0;
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(xOffset, yOffset, 0.0);
            this.icon.draw(guiGraphics);
            poseStack.popPose();
        }
    }
}
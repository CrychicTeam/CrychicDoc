package noppes.npcs.shared.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;

public class GuiMenuSideButton extends GuiButtonNop {

    public static final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/menusidebutton.png");

    public boolean active;

    public GuiMenuSideButton(IGuiInterface gui, int i, int j, int k, String s) {
        this(gui, i, j, k, 200, 20, s);
    }

    public GuiMenuSideButton(IGuiInterface gui, int i, int j, int k, int l, int i1, String s) {
        super(gui, i, j, k, l, i1, s);
    }

    @Override
    public void render(GuiGraphics graphics, int i, int j, float partialTicks) {
        if (this.f_93624_) {
            Minecraft minecraft = Minecraft.getInstance();
            Font fontrenderer = minecraft.font;
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, resource);
            int width = this.f_93618_ + (this.active ? 2 : 0);
            this.f_93622_ = i >= this.m_252754_() && j >= this.m_252907_() && i < this.m_252754_() + width && j < this.m_252907_() + this.f_93619_;
            int k = !this.active ? 0 : (this.f_93622_ ? 2 : 1);
            graphics.blit(resource, this.m_252754_(), this.m_252907_(), 0, k * 22, width, this.f_93619_);
            String text = "";
            float maxWidth = (float) width * 0.75F;
            String displayString = this.m_6035_().getString();
            if ((float) fontrenderer.width(displayString) > maxWidth) {
                for (int h = 0; h < displayString.length(); h++) {
                    char c = displayString.charAt(h);
                    if ((float) fontrenderer.width(text + c) > maxWidth) {
                        break;
                    }
                    text = text + c;
                }
                text = text + "...";
            } else {
                text = displayString;
            }
            if (this.active) {
                graphics.drawCenteredString(fontrenderer, text, this.m_252754_() + width / 2, this.m_252907_() + (this.f_93619_ - 8) / 2, 16777120);
            } else if (this.f_93622_) {
                graphics.drawCenteredString(fontrenderer, text, this.m_252754_() + width / 2, this.m_252907_() + (this.f_93619_ - 8) / 2, 16777120);
            } else {
                graphics.drawCenteredString(fontrenderer, text, this.m_252754_() + width / 2, this.m_252907_() + (this.f_93619_ - 8) / 2, 14737632);
            }
        }
    }

    @Override
    public boolean mouseClicked(double i, double j, int button) {
        return !this.active ? super.m_6375_(i, j, button) : false;
    }
}
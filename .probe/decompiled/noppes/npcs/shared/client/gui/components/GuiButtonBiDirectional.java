package noppes.npcs.shared.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;

public class GuiButtonBiDirectional extends GuiButtonNop {

    public static final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/arrowbuttons.png");

    private int color = 16777215;

    public GuiButtonBiDirectional(IGuiInterface gui, int id, int x, int y, int width, int height, String[] arr, int current) {
        super(gui, id, x, y, width, height, arr, current);
    }

    public GuiButtonBiDirectional(IGuiInterface gui, int id, int x, int y, int width, int height, int current, String... arr) {
        super(gui, id, x, y, width, height, arr, current);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            boolean hover = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            boolean hoverL = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + 14 && mouseY < this.m_252907_() + this.f_93619_;
            boolean hoverR = !hoverL && mouseX >= this.m_252754_() + this.f_93618_ - 14 && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            Minecraft mc = Minecraft.getInstance();
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, resource);
            graphics.blit(resource, this.m_252754_(), this.m_252907_(), 0, hoverL ? 40 : 20, 11, 20);
            graphics.blit(resource, this.m_252754_() + this.f_93618_ - 11, this.m_252907_(), 11, (!hover || hoverL) && !hoverR ? 20 : 40, 11, 20);
            int l = this.color;
            if (this.packedFGColor != 0) {
                l = this.packedFGColor;
            } else if (!this.f_93623_) {
                l = 10526880;
            } else if (hover) {
                l = 16777120;
            }
            String text = "";
            float maxWidth = (float) (this.f_93618_ - 36);
            String displayString = this.m_6035_().getString();
            if ((float) mc.font.width(displayString) > maxWidth) {
                for (int h = 0; h < displayString.length(); h++) {
                    char c = displayString.charAt(h);
                    text = text + c;
                    if ((float) mc.font.width(text) > maxWidth) {
                        break;
                    }
                }
                text = text + "...";
            } else {
                text = displayString;
            }
            if (hover) {
                text = "§n" + text;
            }
            graphics.drawString(mc.font, text, this.m_252754_() + this.f_93618_ / 2, this.m_252907_() + (this.f_93619_ - 8) / 2, l);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int value = this.getValue();
        if (this.m_5953_(mouseX, mouseY) && this.display != null && this.display.length != 0) {
            boolean hoverL = mouseX >= (double) this.m_252754_() && mouseY >= (double) this.m_252907_() && mouseX < (double) (this.m_252754_() + 14) && mouseY < (double) (this.m_252907_() + this.f_93619_);
            boolean hoverR = !hoverL && mouseX >= (double) (this.m_252754_() + 14) && mouseY >= (double) this.m_252907_() && mouseX < (double) (this.m_252754_() + this.f_93618_) && mouseY < (double) (this.m_252907_() + this.f_93619_);
            if (hoverR) {
                value = (value + 1) % this.display.length;
            }
            if (hoverL) {
                if (value <= 0) {
                    value = this.display.length;
                }
                value--;
            }
            this.setDisplay(value);
        }
        return super.m_6375_(mouseX, mouseY, button);
    }

    @Override
    public void onClick(double x, double y) {
        if (!this.gui.hasSubGui()) {
            this.gui.buttonEvent(this);
        }
    }
}
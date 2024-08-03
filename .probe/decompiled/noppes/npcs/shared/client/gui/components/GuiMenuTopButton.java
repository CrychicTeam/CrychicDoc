package noppes.npcs.shared.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;

public class GuiMenuTopButton extends GuiButtonNop {

    public static final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/menutopbutton.png");

    protected int height;

    public boolean active;

    public boolean hover = false;

    public boolean rotated = false;

    public GuiMenuTopButton(IGuiInterface gui, int i, int j, int k, String s) {
        super(gui, i, j, k, s);
        this.active = false;
        this.f_93618_ = Minecraft.getInstance().font.width(this.m_6035_()) + 12;
        this.height = 20;
    }

    public GuiMenuTopButton(IGuiInterface gui, int i, GuiButtonNop parent, String s) {
        this(gui, i, parent.m_252754_() + parent.m_5711_(), parent.m_252907_(), s);
    }

    @Override
    public void render(GuiGraphics graphics, int i, int j, float partialTicks) {
        if (this.f_93624_) {
            PoseStack matrixStack = graphics.pose();
            Minecraft mc = Minecraft.getInstance();
            matrixStack.pushPose();
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, resource);
            int height = this.height - (this.active ? 0 : 2);
            this.hover = i >= this.m_252754_() && j >= this.m_252907_() && i < this.m_252754_() + this.m_5711_() && j < this.m_252907_() + height;
            int k = !this.active ? 1 : (this.hover ? 2 : 0);
            graphics.blit(resource, this.m_252754_(), this.m_252907_(), 0, k * 20, this.m_5711_() / 2, height);
            graphics.blit(resource, this.m_252754_() + this.m_5711_() / 2, this.m_252907_(), 200 - this.m_5711_() / 2, k * 20, this.m_5711_() / 2, height);
            Font fontrenderer = mc.font;
            if (this.rotated) {
                matrixStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            }
            if (this.active) {
                graphics.drawCenteredString(fontrenderer, this.m_6035_(), this.m_252754_() + this.m_5711_() / 2, this.m_252907_() + (height - 8) / 2, 16777120);
            } else if (this.hover) {
                graphics.drawCenteredString(fontrenderer, this.m_6035_(), this.m_252754_() + this.m_5711_() / 2, this.m_252907_() + (height - 8) / 2, 16777120);
            } else {
                graphics.drawCenteredString(fontrenderer, this.m_6035_(), this.m_252754_() + this.m_5711_() / 2, this.m_252907_() + (height - 8) / 2, 14737632);
            }
            matrixStack.popPose();
        }
    }

    @Override
    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        return false;
    }

    @Override
    public boolean mouseReleased(double i, double j, int button) {
        return false;
    }

    @Override
    public boolean mouseClicked(double i, double j, int button) {
        boolean bo = !this.active && this.f_93624_ && this.hover;
        if (bo) {
            this.onClick(i, j);
        }
        return bo;
    }

    @Override
    public void onClick(double x, double y) {
        this.gui.buttonEvent(this);
    }
}
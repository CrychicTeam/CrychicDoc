package journeymap.client.ui.component;

import journeymap.client.render.RenderWrapper;
import journeymap.client.ui.GuiUtils;
import journeymap.common.properties.config.BooleanField;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;

public class CheckBox extends BooleanPropertyButton {

    public int boxWidth = 11;

    String glyph = "✔";

    public CheckBox(String label, boolean checked, net.minecraft.client.gui.components.Button.OnPress pressable) {
        this(label, null, pressable);
        this.toggled = checked;
    }

    public CheckBox(String label, BooleanField field, net.minecraft.client.gui.components.Button.OnPress pressable) {
        super(label, label, field, pressable);
        this.setHeight(9 + 2);
        this.m_93674_(this.getFitWidth(this.fontRenderer));
    }

    public CheckBox(String label, boolean checked) {
        this(label, checked, emptyPressable());
    }

    public CheckBox(String label, BooleanField field) {
        this(label, field, emptyPressable());
    }

    @Override
    public int getFitWidth(Font fr) {
        return super.getFitWidth(fr) + this.boxWidth + 2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float ticks) {
        if (this.f_93624_) {
            this.setHovered(this.isEnabled() && mouseX >= super.m_252754_() && mouseY >= super.m_252907_() && mouseX < super.m_252754_() + this.f_93618_ && mouseY < super.m_252907_() + this.f_93619_);
            int yoffset = (this.f_93619_ - this.boxWidth) / 2;
            RenderWrapper.setShader(GameRenderer::m_172817_);
            GuiUtils.drawContinuousTexturedBox(graphics.pose(), f_93617_, super.m_252754_(), super.m_252907_() + yoffset, 0, 46, this.boxWidth, this.boxWidth, 200, 20, 2, 3, 2, 2, 0.0F);
            this.m_7979_((double) mouseX, (double) mouseY, 0, (double) super.m_252754_(), (double) super.m_252907_());
            int color = 14737632;
            if (this.m_274382_()) {
                color = 16777120;
            } else if (!this.isEnabled()) {
                color = 4210752;
            } else if (this.labelColor != null) {
                color = this.labelColor;
            } else if (this.getActiveColor() != 0) {
                color = this.getActiveColor();
            }
            int labelPad = 4;
            if (this.toggled) {
                graphics.drawCenteredString(this.fontRenderer, this.glyph, super.m_252754_() + this.boxWidth / 2 + 1, super.m_252907_() + 1 + yoffset, color);
            }
            graphics.drawString(this.fontRenderer, this.m_6035_(), super.m_252754_() + this.boxWidth + labelPad, super.m_252907_() + 2 + yoffset, color);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isEnabled() && this.f_93624_ && mouseX >= (double) super.m_252754_() && mouseY >= (double) super.m_252907_() && mouseX < (double) (super.m_252754_() + this.f_93618_) && mouseY < (double) (super.m_252907_() + this.f_93619_)) {
            this.toggle();
        }
        return super.m_6375_(mouseX, mouseY, button);
    }

    public boolean keyTyped(char c, int i) {
        if (this.isEnabled() && i == 32) {
            this.toggle();
            return true;
        } else {
            return false;
        }
    }
}
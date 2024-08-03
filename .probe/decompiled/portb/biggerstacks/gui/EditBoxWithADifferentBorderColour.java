package portb.biggerstacks.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class EditBoxWithADifferentBorderColour extends EditBox {

    private static final int BORDER_COLOUR = -3684409;

    public EditBoxWithADifferentBorderColour(Font pFont, int pX, int pY, int pWidth, int pHeight, Component pMessage) {
        super(pFont, pX, pY, pWidth, pHeight, pMessage);
        this.m_94149_((p_94147_, p_94148_) -> FormattedCharSequence.forward(p_94147_, Style.EMPTY));
    }

    public EditBoxWithADifferentBorderColour(Font pFont, int pX, int pY, int pWidth, int pHeight, @Nullable EditBox editBox0, Component pMessage) {
        super(pFont, pX, pY, pWidth, pHeight, editBox0, pMessage);
        this.m_94149_((p_94147_, p_94148_) -> FormattedCharSequence.forward(p_94147_, Style.EMPTY));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (this.m_94213_()) {
            if (this.m_94219_()) {
                int i = this.m_93696_() ? -1 : -3684409;
                guiGraphics0.fill(this.m_252754_() - 1, this.m_252907_() - 1, this.m_252754_() + this.f_93618_ + 1, this.m_252907_() + this.f_93619_ + 1, i);
                guiGraphics0.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.f_93618_, this.m_252907_() + this.f_93619_, -16777216);
            }
            int i2 = this.f_94098_ ? this.f_94103_ : this.f_94104_;
            int j = this.f_94101_ - this.f_94100_;
            int k = this.f_94102_ - this.f_94100_;
            String s = this.f_94092_.plainSubstrByWidth(this.f_94093_.substring(this.f_94100_), this.m_94210_());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = this.m_93696_() && this.f_94095_ / 6 % 2 == 0 && flag;
            int l = this.f_94096_ ? this.m_252754_() + 4 : this.m_252754_();
            int i1 = this.f_94096_ ? this.m_252907_() + (this.f_93619_ - 8) / 2 : this.m_252907_();
            int j1 = l;
            if (k > s.length()) {
                k = s.length();
            }
            if (!s.isEmpty()) {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = guiGraphics0.drawString(this.f_94092_, (FormattedCharSequence) this.f_94091_.apply(s1, this.f_94100_), l, i1, i2);
            }
            boolean flag2 = this.f_94101_ < this.f_94093_.length() || this.f_94093_.length() >= this.m_94216_();
            int k1 = j1;
            if (!flag) {
                k1 = j > 0 ? l + this.f_93618_ : l;
            } else if (flag2) {
                k1 = j1 - 1;
                j1--;
            }
            if (!s.isEmpty() && flag && j < s.length()) {
                guiGraphics0.drawString(this.f_94092_, (FormattedCharSequence) this.f_94091_.apply(s.substring(j), this.f_94101_), j1, i1, i2);
            }
            if (this.f_256828_ != null && s.isEmpty() && !this.m_93696_()) {
                guiGraphics0.drawString(this.f_94092_, this.f_256828_, j1, i1, i2);
            }
            if (!flag2 && this.f_94088_ != null) {
                guiGraphics0.drawString(this.f_94092_, this.f_94088_, k1 - 1, i1, -8355712);
            }
            if (flag1) {
                if (flag2) {
                    guiGraphics0.fill(RenderType.guiOverlay(), k1, i1 - 1, k1 + 1, i1 + 1 + 9, -3092272);
                } else {
                    guiGraphics0.drawString(this.f_94092_, "_", k1, i1, i2);
                }
            }
            if (k != j) {
                int l1 = l + this.f_94092_.width(s.substring(0, k));
                this.m_264315_(guiGraphics0, k1, i1 - 1, l1 - 1, i1 + 1 + 9);
            }
        }
    }
}
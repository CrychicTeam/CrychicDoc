package vazkii.patchouli.client.book.page;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageWithText;

public class PageText extends PageWithText {

    String title;

    public void setText(String text) {
        this.text = IVariable.wrap(text);
    }

    @Override
    public int getTextHeight() {
        if (this.pageNum == 0) {
            return 22;
        } else {
            return this.title != null && !this.title.isEmpty() ? 12 : -4;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pticks) {
        super.render(graphics, mouseX, mouseY, pticks);
        if (this.pageNum == 0) {
            boolean renderedSmol = false;
            String smolText = "";
            if (this.mc.options.advancedItemTooltips) {
                ResourceLocation res = this.parent.getEntry().getId();
                smolText = res.toString();
            } else if (this.entry.getAddedBy() != null) {
                smolText = I18n.get("patchouli.gui.lexicon.added_by", this.entry.getAddedBy());
            }
            if (!smolText.isEmpty()) {
                graphics.pose().scale(0.5F, 0.5F, 1.0F);
                this.parent.drawCenteredStringNoShadow(graphics, smolText, 116, 12, this.book.headerColor);
                graphics.pose().scale(2.0F, 2.0F, 1.0F);
                renderedSmol = true;
            }
            this.parent.drawCenteredStringNoShadow(graphics, this.parent.getEntry().getName().getVisualOrderText(), 58, renderedSmol ? -3 : 0, this.book.headerColor);
            GuiBook.drawSeparator(graphics, this.book, 0, 12);
        } else if (this.title != null && !this.title.isEmpty()) {
            this.parent.drawCenteredStringNoShadow(graphics, this.i18n(this.title), 58, 0, this.book.headerColor);
        }
    }
}
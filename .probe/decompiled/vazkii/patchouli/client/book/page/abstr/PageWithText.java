package vazkii.patchouli.client.book.page.abstr;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

public abstract class PageWithText extends BookPage {

    protected IVariable text;

    transient BookTextRenderer textRender;

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);
        if (this.text == null) {
            this.text = IVariable.wrap("");
        }
        this.textRender = new BookTextRenderer(parent, this.text.as(Component.class), 0, this.getTextHeight());
    }

    public abstract int getTextHeight();

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pticks) {
        if (this.shouldRenderText()) {
            this.textRender.render(graphics, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return this.shouldRenderText() && this.textRender.click(mouseX, mouseY, mouseButton);
    }

    public boolean shouldRenderText() {
        return true;
    }
}
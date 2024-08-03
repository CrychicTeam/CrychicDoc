package vazkii.patchouli.client.book.page;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.gui.button.GuiButtonBookArrowSmall;
import vazkii.patchouli.client.book.page.abstr.PageWithText;

public class PageImage extends PageWithText {

    ResourceLocation[] images;

    String title;

    boolean border;

    transient int index;

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);
        int x = 90;
        int y = 100;
        this.addButton(new GuiButtonBookArrowSmall(parent, x, y, true, () -> this.index > 0, this::handleButtonArrow));
        this.addButton(new GuiButtonBookArrowSmall(parent, x + 10, y, false, () -> this.index < this.images.length - 1, this::handleButtonArrow));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pticks) {
        int x = 5;
        int y = 7;
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        graphics.pose().scale(0.5F, 0.5F, 0.5F);
        graphics.blit(this.images[this.index], x * 2 + 6, y * 2 + 6, 0, 0, 200, 200);
        graphics.pose().scale(2.0F, 2.0F, 2.0F);
        if (this.border) {
            GuiBook.drawFromTexture(graphics, this.book, x, y, 405, 149, 106, 106);
        }
        if (this.title != null && !this.title.isEmpty()) {
            this.parent.drawCenteredStringNoShadow(graphics, this.i18n(this.title), 58, -3, this.book.headerColor);
        }
        if (this.images.length > 1 && this.border) {
            int xs = x + 83;
            int ys = y + 92;
            graphics.fill(xs, ys, xs + 20, ys + 11, 1140850688);
            graphics.fill(xs - 1, ys - 1, xs + 20, ys + 11, 1140850688);
        }
        super.render(graphics, mouseX, mouseY, pticks);
    }

    public void handleButtonArrow(Button button) {
        boolean left = ((GuiButtonBookArrowSmall) button).left;
        if (left) {
            this.index--;
        } else {
            this.index++;
        }
    }

    @Override
    public int getTextHeight() {
        return 120;
    }
}
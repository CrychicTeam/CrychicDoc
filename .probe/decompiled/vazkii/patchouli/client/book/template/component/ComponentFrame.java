package vazkii.patchouli.client.book.template.component;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.template.TemplateComponent;

public class ComponentFrame extends TemplateComponent {

    @Override
    public void build(BookContentsBuilder builder, BookPage page, BookEntry entry, int pageNum) {
        if (this.x == -1) {
            this.x = 5;
        }
        if (this.y == -1) {
            this.y = 7;
        }
    }

    @Override
    public void render(GuiGraphics graphics, BookPage page, int mouseX, int mouseY, float pticks) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        GuiBook.drawFromTexture(graphics, page.book, this.x, this.y, 405, 149, 106, 106);
    }
}
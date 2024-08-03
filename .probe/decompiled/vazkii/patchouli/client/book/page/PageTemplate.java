package vazkii.patchouli.client.book.page;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.Level;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.template.BookTemplate;
import vazkii.patchouli.client.book.template.JsonVariableWrapper;

public class PageTemplate extends BookPage {

    private transient BookTemplate template = null;

    private transient boolean resolved = false;

    @Override
    public void build(Level level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        super.build(level, entry, builder, pageNum);
        if (!this.resolved) {
            this.template = BookTemplate.createTemplate(this.book, builder, this.type, null);
            this.resolved = true;
        }
        JsonVariableWrapper wrapper = new JsonVariableWrapper(this.sourceObject);
        this.template.compile(level, builder, wrapper);
        this.template.build(builder, this, entry, pageNum);
    }

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);
        this.template.onDisplayed(this, parent, left, top);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pticks) {
        this.template.render(graphics, this, mouseX, mouseY, pticks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return this.template.mouseClicked(this, mouseX, mouseY, mouseButton);
    }
}
package vazkii.patchouli.client.book.gui.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.EntryDisplayState;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.common.book.Book;

public class GuiButtonBookMarkRead extends GuiButtonBook {

    private final Book book;

    public GuiButtonBookMarkRead(GuiBook parent, int x, int y) {
        super(parent, x, y, 308, 31, 11, 11, Button::m_5691_, getTooltip(parent.book));
        this.book = parent.book;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int px = this.m_252754_() + 1;
        int py = (int) ((double) this.m_252907_() + 0.5);
        GuiBook.drawFromTexture(graphics, this.book, this.m_252754_(), this.m_252907_(), 285, 160, 13, 10);
        GuiBook.drawFromTexture(graphics, this.book, px, py, this.u, this.v, this.f_93618_, this.f_93619_);
        if (this.m_198029_()) {
            GuiBook.drawFromTexture(graphics, this.book, px, py, this.u + 11, this.v, this.f_93618_, this.f_93619_);
            this.parent.setTooltip(this.getTooltipLines());
        }
        graphics.drawString(this.parent.getMinecraft().font, "+", px, py, 65281, true);
    }

    @Override
    public void onPress() {
        for (BookEntry entry : this.book.getContents().entries.values()) {
            if (isMainPage(this.book)) {
                this.markEntry(entry);
            } else {
                this.markCategoryAsRead(entry, entry.getCategory(), this.book.getContents().entries.size());
            }
        }
    }

    private void markCategoryAsRead(BookEntry entry, BookCategory category, int maxRecursion) {
        if (category.getName().equals(this.book.getContents().getCurrentGui().m_96636_())) {
            this.markEntry(entry);
        } else if (!category.isRootCategory() && maxRecursion > 0) {
            this.markCategoryAsRead(entry, entry.getCategory().getParentCategory(), maxRecursion - 1);
        }
    }

    private void markEntry(BookEntry entry) {
        boolean dirty = false;
        ResourceLocation key = entry.getId();
        if (!entry.isLocked() && entry.getReadState().equals(EntryDisplayState.UNREAD)) {
            PersistentData.BookData data = PersistentData.data.getBookData(this.book);
            if (!data.viewedEntries.contains(key)) {
                data.viewedEntries.add(key);
                dirty = true;
                entry.markReadStateDirty();
            }
        }
        if (dirty) {
            PersistentData.save();
        }
    }

    private static Component getTooltip(Book book) {
        String text = isMainPage(book) ? "patchouli.gui.lexicon.button.mark_all_read" : "patchouli.gui.lexicon.button.mark_category_read";
        return Component.translatable(text);
    }

    private static boolean isMainPage(Book book) {
        return !book.getContents().currentGui.canSeeBackButton();
    }
}
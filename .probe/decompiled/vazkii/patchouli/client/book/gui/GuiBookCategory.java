package vazkii.patchouli.client.book.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.resources.language.I18n;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.button.GuiButtonCategory;
import vazkii.patchouli.common.book.Book;

public class GuiBookCategory extends GuiBookEntryList {

    private final BookCategory category;

    private int subcategoryButtonCount;

    public GuiBookCategory(Book book, BookCategory category) {
        super(book, category.getName());
        this.category = category;
    }

    @Override
    protected String getDescriptionText() {
        return this.category.getDescription();
    }

    @Override
    protected Collection<BookEntry> getEntries() {
        return this.category.getEntries();
    }

    @Override
    void drawForegroundElements(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawForegroundElements(graphics, mouseX, mouseY, partialTicks);
        if (this.getEntries().isEmpty() && this.subcategoryButtonCount <= 16 && this.subcategoryButtonCount > 0) {
            int bottomSeparator = 55 + 24 * ((this.subcategoryButtonCount - 1) / 4 + 1);
            drawSeparator(graphics, this.book, 141, bottomSeparator);
        }
    }

    @Override
    protected void addSubcategoryButtons() {
        int i = 0;
        List<BookCategory> categories = new ArrayList(this.book.getContents().categories.values());
        categories.removeIf(cat -> cat.getParentCategory() != this.category || cat.shouldHide());
        Collections.sort(categories);
        this.subcategoryButtonCount = categories.size();
        boolean rightPageFree = this.getEntries().isEmpty();
        int baseX;
        int baseY;
        if (rightPageFree) {
            baseX = 151;
            baseY = 43;
        } else {
            baseX = 25;
            baseY = 174 - categories.size() / 4 * 20 - (!this.book.advancementsEnabled() ? 38 : 64);
        }
        for (BookCategory ocategory : categories) {
            int x = baseX + i % 4 * 24;
            int y = baseY + i / 4 * (rightPageFree ? 24 : 20);
            Button button = new GuiButtonCategory(this, x, y, ocategory, this::handleButtonCategory);
            this.m_142416_(button);
            this.entryButtons.add(button);
            i++;
        }
    }

    @Override
    protected String getChapterListTitle() {
        return this.getEntries().isEmpty() && this.subcategoryButtonCount > 0 ? I18n.get("patchouli.gui.lexicon.categories") : super.getChapterListTitle();
    }

    @Override
    protected String getNoEntryMessage() {
        return this.subcategoryButtonCount > 0 ? "" : super.getNoEntryMessage();
    }

    @Override
    protected EditBox createSearchBar() {
        EditBox widget = super.createSearchBar();
        if (this.getEntries().isEmpty()) {
            widget.f_93623_ = false;
            widget.setEditable(false);
            widget.setVisible(false);
        }
        return widget;
    }

    @Override
    protected boolean doesEntryCountForProgress(BookEntry entry) {
        return entry.getCategory() == this.category;
    }

    public boolean equals(Object obj) {
        return obj == this || obj instanceof GuiBookCategory && ((GuiBookCategory) obj).category == this.category && ((GuiBookCategory) obj).spread == this.spread;
    }

    public int hashCode() {
        return Objects.hashCode(this.category) * 31 + Objects.hashCode(this.spread);
    }

    @Override
    public boolean canBeOpened() {
        return !this.category.isLocked() && !this.equals(Minecraft.getInstance().screen);
    }
}
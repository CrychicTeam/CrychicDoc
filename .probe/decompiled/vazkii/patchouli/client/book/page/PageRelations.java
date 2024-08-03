package vazkii.patchouli.client.book.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.gui.button.GuiButtonEntry;
import vazkii.patchouli.client.book.page.abstr.PageWithText;

public class PageRelations extends PageWithText {

    List<String> entries;

    String title;

    transient List<BookEntry> entryObjs;

    @Override
    public void build(Level level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        super.build(level, entry, builder, pageNum);
        this.entryObjs = new ArrayList();
        for (String s : this.entries) {
            ResourceLocation targetId = new ResourceLocation(s);
            BookEntry targetEntry = builder.getEntry(targetId);
            if (targetEntry == null) {
                throw new IllegalArgumentException("Could not find entry " + targetId);
            }
            this.entryObjs.add(targetEntry);
        }
    }

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);
        List<BookEntry> displayedEntries = new ArrayList(this.entryObjs);
        displayedEntries.removeIf(BookEntry::shouldHide);
        Collections.sort(displayedEntries);
        for (int i = 0; i < displayedEntries.size(); i++) {
            Button button = new GuiButtonEntry(parent, 0, 20 + i * 11, (BookEntry) displayedEntries.get(i), this::handleButtonEntry);
            this.addButton(button);
        }
    }

    public void handleButtonEntry(Button button) {
        GuiBookEntry.displayOrBookmark(this.parent, ((GuiButtonEntry) button).getEntry());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pticks) {
        this.parent.drawCenteredStringNoShadow(graphics, this.title != null && !this.title.isEmpty() ? this.i18n(this.title) : I18n.get("patchouli.gui.lexicon.relations"), 58, 0, this.book.headerColor);
        GuiBook.drawSeparator(graphics, this.book, 0, 12);
        super.render(graphics, mouseX, mouseY, pticks);
    }

    @Override
    public int getTextHeight() {
        return 22 + this.entryObjs.size() * 11;
    }
}
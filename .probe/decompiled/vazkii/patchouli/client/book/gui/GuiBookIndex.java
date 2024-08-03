package vazkii.patchouli.client.book.gui;

import java.util.Collection;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.common.book.Book;

public class GuiBookIndex extends GuiBookEntryList {

    public GuiBookIndex(Book book) {
        super(book, Component.translatable("patchouli.gui.lexicon.index"));
    }

    @Override
    protected String getDescriptionText() {
        return I18n.get("patchouli.gui.lexicon.index.info");
    }

    @Override
    protected Collection<BookEntry> getEntries() {
        return this.book.getContents().entries.values();
    }
}
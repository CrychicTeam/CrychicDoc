package vazkii.patchouli.client.book.gui;

import java.util.Collection;
import java.util.stream.Collectors;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.common.book.Book;

public class GuiBookHistory extends GuiBookEntryList {

    public GuiBookHistory(Book book) {
        super(book, Component.translatable("patchouli.gui.lexicon.history"));
    }

    @Override
    protected String getDescriptionText() {
        return I18n.get("patchouli.gui.lexicon.history.info");
    }

    @Override
    protected boolean shouldDrawProgressBar() {
        return false;
    }

    @Override
    protected boolean shouldSortEntryList() {
        return false;
    }

    @Override
    protected Collection<BookEntry> getEntries() {
        PersistentData.BookData data = PersistentData.data.getBookData(this.book);
        return (Collection<BookEntry>) data.history.stream().map(res -> (BookEntry) this.book.getContents().entries.get(res)).filter(e -> e != null && !e.isLocked()).collect(Collectors.toList());
    }
}
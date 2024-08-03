package vazkii.patchouli.client.book;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.gui.GuiBookLanding;
import vazkii.patchouli.client.book.template.BookTemplate;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.util.ItemStackUtil;

public class BookContents extends AbstractReadStateHolder {

    public static final Map<ResourceLocation, Supplier<BookTemplate>> addonTemplates = new ConcurrentHashMap();

    private final Book book;

    public final Map<ResourceLocation, BookCategory> categories;

    public final Map<ResourceLocation, BookEntry> entries;

    @Nullable
    public final BookCategory pamphletCategory;

    private final Map<ItemStackUtil.StackWrapper, Pair<BookEntry, Integer>> recipeMappings;

    private final boolean errored;

    @Nullable
    private final Exception exception;

    public final Deque<GuiBook> guiStack = new ArrayDeque();

    public GuiBook currentGui = null;

    private BookContents(Book book, @Nullable Exception e) {
        this.book = book;
        this.errored = e != null;
        this.exception = e;
        this.categories = Collections.emptyMap();
        this.entries = Collections.emptyMap();
        this.recipeMappings = Collections.emptyMap();
        this.pamphletCategory = null;
    }

    public static BookContents empty(Book book, @Nullable Exception e) {
        return new BookContents(book, e);
    }

    public BookContents(Book book, ImmutableMap<ResourceLocation, BookCategory> categories, ImmutableMap<ResourceLocation, BookEntry> entries, ImmutableMap<ItemStackUtil.StackWrapper, Pair<BookEntry, Integer>> recipeMappings, @Nullable BookCategory pamphletCategory) {
        this.book = book;
        this.categories = categories;
        this.entries = entries;
        this.recipeMappings = recipeMappings;
        this.errored = false;
        this.exception = null;
        this.pamphletCategory = pamphletCategory;
    }

    public boolean isErrored() {
        return this.errored;
    }

    @Nullable
    public Exception getException() {
        return this.exception;
    }

    @Nullable
    public Pair<BookEntry, Integer> getEntryForStack(ItemStack stack) {
        return (Pair<BookEntry, Integer>) this.recipeMappings.get(ItemStackUtil.wrapStack(stack));
    }

    public GuiBook getCurrentGui() {
        if (this.currentGui == null) {
            this.currentGui = new GuiBookLanding(this.book);
        }
        return this.currentGui;
    }

    public void openLexiconGui(GuiBook gui, boolean push) {
        if (gui.canBeOpened()) {
            Minecraft mc = Minecraft.getInstance();
            if (push && mc.screen instanceof GuiBook guiBook && gui != mc.screen) {
                this.guiStack.push(guiBook);
            }
            mc.setScreen(gui);
            gui.onFirstOpened();
        }
    }

    @Override
    protected EntryDisplayState computeReadState() {
        Stream<EntryDisplayState> stream = this.categories.values().stream().filter(BookCategory::isRootCategory).map(AbstractReadStateHolder::getReadState);
        return mostImportantState(stream);
    }

    public final void checkValidCurrentEntry() {
        if (!this.getCurrentGui().canBeOpened()) {
            this.currentGui = null;
            this.guiStack.clear();
        }
    }

    public final void setTopEntry(ResourceLocation entryId, int page) {
        BookEntry entry = (BookEntry) this.entries.get(entryId);
        if (!entry.isLocked()) {
            GuiBook prevGui = this.getCurrentGui();
            int spread = page / 2;
            this.currentGui = new GuiBookEntry(this.book, entry, spread);
            if (prevGui instanceof GuiBookEntry currEntry && currEntry.getEntry() == entry && currEntry.getSpread() == spread) {
                return;
            }
            entry.getBook().getContents().guiStack.push(prevGui);
        }
    }
}
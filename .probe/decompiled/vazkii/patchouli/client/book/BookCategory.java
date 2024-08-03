package vazkii.patchouli.client.book;

import com.google.common.collect.Streams;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.common.base.PatchouliConfig;
import vazkii.patchouli.common.book.Book;

public final class BookCategory extends AbstractReadStateHolder implements Comparable<BookCategory> {

    private final ResourceLocation id;

    private final String name;

    private final String description;

    private final BookIcon icon;

    @Nullable
    private final String parent;

    private final String flag;

    private final int sortnum;

    private final boolean secret;

    private final Book book;

    @Nullable
    private BookCategory parentCategory;

    private final List<BookCategory> children = new ArrayList();

    private final List<BookEntry> entries = new ArrayList();

    private boolean locked;

    private boolean built;

    public BookCategory(JsonObject root, ResourceLocation id, Book book) {
        this.book = book;
        this.id = id;
        this.name = GsonHelper.getAsString(root, "name");
        this.description = GsonHelper.getAsString(root, "description");
        this.icon = BookIcon.from(GsonHelper.getAsString(root, "icon"));
        this.parent = GsonHelper.getAsString(root, "parent", null);
        this.flag = GsonHelper.getAsString(root, "flag", "");
        this.sortnum = GsonHelper.getAsInt(root, "sortnum", 0);
        this.secret = GsonHelper.getAsBoolean(root, "secret", false);
    }

    public MutableComponent getName() {
        return this.book.i18n ? Component.translatable(this.name) : Component.literal(this.name);
    }

    public String getDescription() {
        return this.description;
    }

    public BookIcon getIcon() {
        return this.icon;
    }

    public void addEntry(BookEntry entry) {
        this.entries.add(entry);
    }

    public void addChildCategory(BookCategory category) {
        this.children.add(category);
    }

    public List<BookEntry> getEntries() {
        return this.entries;
    }

    @Nullable
    public BookCategory getParentCategory() {
        return this.parentCategory;
    }

    public void updateLockStatus(boolean rootOnly) {
        if (!rootOnly || this.isRootCategory()) {
            this.children.forEach(c -> c.updateLockStatus(false));
            boolean wasLocked = this.locked;
            this.locked = !this.children.isEmpty() || !this.entries.isEmpty();
            Iterator var3 = this.children.iterator();
            label41: while (true) {
                if (var3.hasNext()) {
                    BookCategory c = (BookCategory) var3.next();
                    if (c.isLocked()) {
                        continue;
                    }
                    this.locked = false;
                    break;
                }
                var3 = this.entries.iterator();
                BookEntry e;
                do {
                    if (!var3.hasNext()) {
                        break label41;
                    }
                    e = (BookEntry) var3.next();
                } while (e.isLocked());
                this.locked = false;
                break;
            }
            if (!this.locked && wasLocked) {
                this.book.markUpdated();
            }
        }
    }

    public boolean isSecret() {
        return this.secret;
    }

    public boolean shouldHide() {
        return this.isSecret() && this.isLocked();
    }

    public boolean isLocked() {
        return this.getBook().advancementsEnabled() && this.locked;
    }

    public boolean isRootCategory() {
        return this.parent == null || this.parent.isEmpty();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public boolean canAdd() {
        return this.flag.isEmpty() || PatchouliConfig.getConfigFlag(this.flag);
    }

    public int compareTo(BookCategory o) {
        if (this.book.advancementsEnabled() && o.locked != this.locked) {
            return this.locked ? 1 : -1;
        } else {
            return this.sortnum != o.sortnum ? Integer.compare(this.sortnum, o.sortnum) : this.getName().getString().compareTo(o.getName().getString());
        }
    }

    public void build(BookContentsBuilder builder) {
        if (!this.built) {
            if (!this.isRootCategory()) {
                if (!this.parent.contains(":")) {
                    String hint = String.format("`%s:%s`", this.book.id.getNamespace(), this.parent);
                    throw new IllegalArgumentException("`parent` must be fully qualified (domain:name). Hint: Try " + hint);
                }
                BookCategory parentCat = builder.getCategory(new ResourceLocation(this.parent));
                if (parentCat == null) {
                    String msg = String.format("Category %s specifies parent %s, but it could not be found", this.id, this.parent);
                    throw new RuntimeException(msg);
                }
                parentCat.addChildCategory(this);
                this.parentCategory = parentCat;
            }
            this.built = true;
        }
    }

    public Book getBook() {
        return this.book;
    }

    @Override
    protected EntryDisplayState computeReadState() {
        Stream<EntryDisplayState> entryStream = this.entries.stream().filter(e -> !e.isLocked()).map(AbstractReadStateHolder::getReadState);
        Stream<EntryDisplayState> childrenStream = this.children.stream().map(AbstractReadStateHolder::getReadState);
        return mostImportantState(Streams.concat(new Stream[] { entryStream, childrenStream }));
    }

    @Override
    public void markReadStateDirty() {
        super.markReadStateDirty();
        if (this.parentCategory != null) {
            this.parentCategory.markReadStateDirty();
        } else {
            this.book.getContents().markReadStateDirty();
        }
    }
}
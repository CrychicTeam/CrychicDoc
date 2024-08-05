package vazkii.patchouli.client.book;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.base.ClientAdvancements;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.page.PageEmpty;
import vazkii.patchouli.client.book.page.PageQuest;
import vazkii.patchouli.common.base.PatchouliConfig;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.util.ItemStackUtil;
import vazkii.patchouli.common.util.SerializationUtil;

public final class BookEntry extends AbstractReadStateHolder implements Comparable<BookEntry> {

    private final String name;

    private final String flag;

    private final boolean priority;

    private final boolean secret;

    private final boolean readByDefault;

    private final BookPage[] pages;

    @Nullable
    private final ResourceLocation advancement;

    @Nullable
    private final ResourceLocation turnin;

    private final int sortnum;

    private final int entryColor;

    private final Map<String, Integer> extraRecipeMappings;

    private final ResourceLocation id;

    private final Book book;

    @Nullable
    private final String addedBy;

    private final ResourceLocation categoryId;

    private BookCategory category;

    private final BookIcon icon;

    private final List<BookPage> realPages = new ArrayList();

    private final List<ItemStackUtil.StackWrapper> relevantStacks = new LinkedList();

    private boolean locked;

    private boolean built;

    private static final List<BookPage> NO_PAGE = ImmutableList.of(new PageEmpty());

    public BookEntry(JsonObject root, ResourceLocation id, Book book, @Nullable String addedBy) {
        this.id = id;
        this.book = book;
        this.addedBy = addedBy;
        String categoryId = GsonHelper.getAsString(root, "category");
        if (categoryId.contains(":")) {
            this.categoryId = new ResourceLocation(categoryId);
            this.name = GsonHelper.getAsString(root, "name");
            this.flag = GsonHelper.getAsString(root, "flag", "");
            this.icon = BookIcon.from(GsonHelper.getAsString(root, "icon"));
            this.priority = GsonHelper.getAsBoolean(root, "priority", false);
            this.secret = GsonHelper.getAsBoolean(root, "secret", false);
            this.readByDefault = GsonHelper.getAsBoolean(root, "read_by_default", false);
            this.advancement = SerializationUtil.getAsResourceLocation(root, "advancement", null);
            this.turnin = SerializationUtil.getAsResourceLocation(root, "turnin", null);
            this.sortnum = GsonHelper.getAsInt(root, "sortnum", 0);
            String entryColor = GsonHelper.getAsString(root, "entry_color", null);
            if (entryColor != null) {
                this.entryColor = Integer.parseInt(entryColor, 16);
            } else {
                this.entryColor = book.textColor;
            }
            this.pages = (BookPage[]) ClientBookRegistry.INSTANCE.gson.fromJson(GsonHelper.getAsJsonArray(root, "pages"), BookPage[].class);
            JsonObject extraRecipeMap = GsonHelper.getAsJsonObject(root, "extra_recipe_mappings", null);
            if (extraRecipeMap == null) {
                this.extraRecipeMappings = Collections.emptyMap();
            } else {
                this.extraRecipeMappings = (Map<String, Integer>) ClientBookRegistry.INSTANCE.gson.fromJson(extraRecipeMap, (new TypeToken<Map<String, Integer>>() {
                }).getType());
            }
        } else {
            String hint = String.format("`%s:%s`", book.id.getNamespace(), categoryId);
            throw new IllegalArgumentException("`category` must be fully qualified (domain:name). Hint: Try " + hint);
        }
    }

    public MutableComponent getName() {
        return this.book.i18n ? Component.translatable(this.name) : Component.literal(this.name);
    }

    public List<BookPage> getPages() {
        List<BookPage> pages = !this.getBook().advancementsEnabled() ? this.realPages : (List) this.realPages.stream().filter(BookPage::isPageUnlocked).collect(Collectors.toList());
        return pages.isEmpty() ? NO_PAGE : pages;
    }

    public int getPageFromAnchor(String anchor) {
        List<BookPage> pages = this.getPages();
        for (int i = 0; i < pages.size(); i++) {
            BookPage page = (BookPage) pages.get(i);
            if (anchor.equals(page.anchor)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isPriority() {
        return this.priority;
    }

    public BookIcon getIcon() {
        return this.icon;
    }

    public void initCategory(ResourceLocation file, Function<ResourceLocation, BookCategory> categories) {
        this.category = (BookCategory) categories.apply(this.categoryId);
        if (this.category == null) {
            String msg = String.format("Entry in file %s does not have a valid category.", file);
            throw new RuntimeException(msg);
        } else {
            this.category.addEntry(this);
        }
    }

    public BookCategory getCategory() {
        return this.category;
    }

    public void updateLockStatus() {
        boolean currLocked = this.locked;
        this.locked = this.advancement != null && !ClientAdvancements.hasDone(this.advancement.toString());
        boolean dirty = false;
        if (!this.locked && currLocked) {
            dirty = true;
            this.book.markUpdated();
        }
        if (!dirty && !this.readStateDirty && this.getReadState() == EntryDisplayState.PENDING && ClientAdvancements.hasDone(this.turnin.toString())) {
            dirty = true;
        }
        if (dirty) {
            this.markReadStateDirty();
        }
    }

    public boolean isLocked() {
        return this.isSecret() ? this.locked : this.getBook().advancementsEnabled() && this.locked;
    }

    public boolean isSecret() {
        return this.secret;
    }

    public boolean shouldHide() {
        return this.isSecret() && this.isLocked();
    }

    public int getEntryColor() {
        return this.entryColor;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public boolean canAdd() {
        return this.flag.isEmpty() || PatchouliConfig.getConfigFlag(this.flag);
    }

    public boolean isFoundByQuery(String query) {
        if (this.getName().getString().toLowerCase().contains(query)) {
            return true;
        } else {
            for (ItemStackUtil.StackWrapper wrapper : this.relevantStacks) {
                if (wrapper.stack.getHoverName().getString().toLowerCase().contains(query)) {
                    return true;
                }
            }
            return false;
        }
    }

    public int compareTo(BookEntry o) {
        if (o.locked != this.locked) {
            return this.locked ? 1 : -1;
        } else {
            EntryDisplayState ourState = this.getReadState();
            EntryDisplayState otherState = o.getReadState();
            if (ourState != otherState) {
                return ourState.compareTo(otherState);
            } else if (o.priority != this.priority) {
                return this.priority ? -1 : 1;
            } else {
                int sort = this.sortnum - o.sortnum;
                return sort == 0 ? this.getName().getString().compareTo(o.getName().getString()) : sort;
            }
        }
    }

    public void build(Level level, BookContentsBuilder builder) {
        if (!this.built) {
            for (int i = 0; i < this.pages.length; i++) {
                if (this.pages[i].canAdd(this.book)) {
                    try {
                        this.pages[i].build(level, this, builder, i);
                        this.realPages.add(this.pages[i]);
                    } catch (Exception var10) {
                        throw new RuntimeException("Error while building entry %s page %d of book %s".formatted(this.id, i, this.book.id), var10);
                    }
                }
            }
            if (this.extraRecipeMappings != null) {
                Iterator var12 = this.extraRecipeMappings.entrySet().iterator();
                label56: while (true) {
                    String key;
                    List<ItemStack> stacks;
                    int pageNumber;
                    while (true) {
                        if (!var12.hasNext()) {
                            break label56;
                        }
                        Entry<String, Integer> entry = (Entry<String, Integer>) var12.next();
                        key = (String) entry.getKey();
                        pageNumber = (Integer) entry.getValue();
                        try {
                            stacks = ItemStackUtil.loadStackListFromString(key);
                            break;
                        } catch (Exception var11) {
                            PatchouliAPI.LOGGER.warn("Invalid extra recipe mapping: {} to page {} in entry {}: {}", key, pageNumber, this.id, var11.getMessage());
                        }
                    }
                    if (!stacks.isEmpty() && pageNumber < this.pages.length) {
                        for (ItemStack stack : stacks) {
                            this.addRelevantStack(builder, stack, pageNumber);
                        }
                    } else {
                        PatchouliAPI.LOGGER.warn("Invalid extra recipe mapping: {} to page {} in entry {}: Empty entry or page out of bounds", key, pageNumber, this.id);
                    }
                }
            }
            this.built = true;
        }
    }

    public void addRelevantStack(BookContentsBuilder builder, ItemStack stack, int page) {
        if (!stack.isEmpty()) {
            ItemStackUtil.StackWrapper wrapper = ItemStackUtil.wrapStack(stack);
            this.relevantStacks.add(wrapper);
            builder.addRecipeMapping(wrapper, this, page / 2);
        }
    }

    public Book getBook() {
        return this.book;
    }

    @Nullable
    public String getAddedBy() {
        return this.addedBy;
    }

    @Override
    protected EntryDisplayState computeReadState() {
        PersistentData.BookData data = PersistentData.data.getBookData(this.book);
        if (data != null && this.getId() != null && !this.readByDefault && !this.isLocked() && !data.viewedEntries.contains(this.getId())) {
            return EntryDisplayState.UNREAD;
        } else if (this.turnin != null && !ClientAdvancements.hasDone(this.turnin.toString())) {
            return EntryDisplayState.PENDING;
        } else {
            for (BookPage page : this.pages) {
                if (page instanceof PageQuest && ((PageQuest) page).isCompleted(this.book)) {
                    return EntryDisplayState.COMPLETED;
                }
            }
            return EntryDisplayState.NEUTRAL;
        }
    }

    @Override
    public void markReadStateDirty() {
        super.markReadStateDirty();
        this.getCategory().markReadStateDirty();
    }
}
package vazkii.patchouli.client.book;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.client.book.template.BookTemplate;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.util.ItemStackUtil;

public class BookContentsBuilder {

    public static final String DEFAULT_LANG = "en_us";

    private final Map<ResourceLocation, BookCategory> categories = new HashMap();

    private final Map<ResourceLocation, BookEntry> entries = new HashMap();

    private final Map<ResourceLocation, Supplier<BookTemplate>> templates = new HashMap();

    private final Map<ItemStackUtil.StackWrapper, Pair<BookEntry, Integer>> recipeMappings = new HashMap();

    private final Book book;

    private final boolean singleBookReload;

    private BookContentsBuilder(Book book, boolean singleBookReload) {
        this.book = book;
        this.singleBookReload = singleBookReload;
        this.templates.putAll(BookContents.addonTemplates);
    }

    @Nullable
    public BookCategory getCategory(ResourceLocation id) {
        return (BookCategory) this.categories.get(id);
    }

    @Nullable
    public BookEntry getEntry(ResourceLocation id) {
        return (BookEntry) this.entries.get(id);
    }

    @Nullable
    public Supplier<BookTemplate> getTemplate(ResourceLocation id) {
        return (Supplier<BookTemplate>) this.templates.get(id);
    }

    public void addRecipeMapping(ItemStackUtil.StackWrapper stack, BookEntry entry, int spread) {
        this.recipeMappings.put(stack, Pair.of(entry, spread));
    }

    public static BookContents loadAndBuildFor(Level level, Book book, boolean singleBookReload) {
        BookContentsBuilder builder = new BookContentsBuilder(book, singleBookReload);
        builder.loadFiles();
        return builder.build(level);
    }

    private void loadFiles() {
        this.load("categories", BookContentsBuilder::loadCategory, this.categories);
        this.load("entries", (b, l, id, file) -> loadEntry(b, l, id, file, this.categories::get), this.entries);
        this.load("templates", BookContentsBuilder::loadTemplate, this.templates);
    }

    private BookContents build(Level level) {
        this.categories.forEach((id, category) -> {
            try {
                category.build(this);
            } catch (Exception var4) {
                throw new RuntimeException("Error while building category " + id, var4);
            }
        });
        this.entries.values().forEach(entry -> {
            try {
                entry.build(level, this);
            } catch (Exception var4) {
                throw new RuntimeException("Error building entry %s of book %s".formatted(entry.getId(), this.book.id), var4);
            }
        });
        BookCategory pamphletCategory = null;
        if (this.book.isPamphlet) {
            if (this.categories.size() != 1) {
                throw new RuntimeException("Pamphlet %s should have exactly one category but instead there were %d".formatted(this.book.id, this.categories.size()));
            }
            pamphletCategory = (BookCategory) this.categories.values().iterator().next();
        }
        return new BookContents(this.book, ImmutableMap.copyOf(this.categories), ImmutableMap.copyOf(this.entries), ImmutableMap.copyOf(this.recipeMappings), pamphletCategory);
    }

    private <T> void load(String thing, BookContentsBuilder.LoadFunc<T> loader, Map<ResourceLocation, T> builder) {
        BookContentLoader contentLoader = this.getContentLoader();
        List<ResourceLocation> foundIds = new ArrayList();
        contentLoader.findFiles(this.book, thing, foundIds);
        for (ResourceLocation id : foundIds) {
            String filePath = String.format("%s/%s/%s/%s/%s.json", "patchouli_books", this.book.id.getPath(), "en_us", thing, id.getPath());
            T value = loader.load(this.book, contentLoader, id, new ResourceLocation(id.getNamespace(), filePath));
            if (value != null) {
                builder.put(id, value);
            }
        }
    }

    protected BookContentLoader getContentLoader() {
        if (this.book.isExternal) {
            return BookContentExternalLoader.INSTANCE;
        } else {
            return (BookContentLoader) (this.singleBookReload ? BookContentResourceDirectLoader.INSTANCE : BookContentResourceListenerLoader.INSTANCE);
        }
    }

    @Nullable
    private static BookCategory loadCategory(Book book, BookContentLoader loader, ResourceLocation id, ResourceLocation file) {
        BookContentLoader.LoadResult result = loadLocalizedJson(book, loader, file);
        BookCategory category = new BookCategory(result.json().getAsJsonObject(), id, book);
        return category.canAdd() ? category : null;
    }

    @Nullable
    private static BookEntry loadEntry(Book book, BookContentLoader loader, ResourceLocation id, ResourceLocation file, Function<ResourceLocation, BookCategory> categories) {
        BookContentLoader.LoadResult result = loadLocalizedJson(book, loader, file);
        BookEntry entry = new BookEntry(result.json().getAsJsonObject(), id, book, result.addedBy());
        if (entry.canAdd()) {
            entry.initCategory(file, categories);
            return entry;
        } else {
            return null;
        }
    }

    private static Supplier<BookTemplate> loadTemplate(Book book, BookContentLoader loader, ResourceLocation key, ResourceLocation res) {
        JsonElement json = loadLocalizedJson(book, loader, res).json();
        Supplier<BookTemplate> supplier = () -> (BookTemplate) ClientBookRegistry.INSTANCE.gson.fromJson(json, BookTemplate.class);
        BookTemplate template = (BookTemplate) supplier.get();
        if (template == null) {
            throw new IllegalArgumentException(res + " could not be instantiated by the supplier.");
        } else {
            return supplier;
        }
    }

    private static BookContentLoader.LoadResult loadLocalizedJson(Book book, BookContentLoader loader, ResourceLocation file) {
        ResourceLocation localizedFile = new ResourceLocation(file.getNamespace(), file.getPath().replaceAll("en_us", ClientBookRegistry.INSTANCE.currentLang));
        BookContentLoader.LoadResult input = loader.loadJson(book, localizedFile);
        if (input == null) {
            input = loader.loadJson(book, file);
            if (input == null) {
                throw new IllegalArgumentException(file + " does not exist.");
            }
        }
        return input;
    }

    private interface LoadFunc<T> {

        @Nullable
        T load(Book var1, BookContentLoader var2, ResourceLocation var3, ResourceLocation var4);
    }
}
package vazkii.patchouli.client.book;

import com.google.common.base.Stopwatch;
import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

public class BookContentResourceListenerLoader extends SimpleJsonResourceReloadListener implements BookContentLoader {

    public static final BookContentResourceListenerLoader INSTANCE = new BookContentResourceListenerLoader();

    private static final Pattern ID_READER = Pattern.compile("(?<bookId>[a-z0-9_.-]+)/(?<lang>[a-z0-9_.-]+)/(?<folder>[a-z0-9_.-]+)/(?<entryId>[a-z0-9/._-]+)");

    private Map<ResourceLocation, Map<ResourceLocation, JsonElement>> data;

    private BookContentResourceListenerLoader() {
        super(BookRegistry.GSON, "patchouli_books");
    }

    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
        Map<ResourceLocation, Map<ResourceLocation, JsonElement>> data = new HashMap();
        for (Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            ResourceLocation key = (ResourceLocation) entry.getKey();
            Matcher matcher = ID_READER.matcher(key.getPath());
            if (!matcher.matches()) {
                PatchouliAPI.LOGGER.trace("Ignored file {}", key);
            } else {
                ResourceLocation bookId = new ResourceLocation(key.getNamespace(), matcher.group("bookId"));
                ((Map) data.computeIfAbsent(bookId, id -> new HashMap())).put((ResourceLocation) entry.getKey(), (JsonElement) entry.getValue());
            }
        }
        int count = data.values().stream().mapToInt(Map::size).sum();
        PatchouliAPI.LOGGER.info("{} preloaded {} jsons", this.getClass().getSimpleName(), count);
        this.data = data;
    }

    @Override
    public void findFiles(Book book, String dir, List<ResourceLocation> list) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Map<ResourceLocation, JsonElement> map = (Map<ResourceLocation, JsonElement>) this.data.get(book.id);
        if (map != null) {
            for (ResourceLocation id : map.keySet()) {
                Matcher matcher = ID_READER.matcher(id.getPath());
                if (matcher.matches() && dir.equals(matcher.group("folder")) && "en_us".equals(matcher.group("lang"))) {
                    list.add(new ResourceLocation(id.getNamespace(), matcher.group("entryId")));
                }
            }
            PatchouliAPI.LOGGER.info("{}: Files found in {}", this.getClass().getSimpleName(), stopwatch.stop());
        }
    }

    @Nullable
    @Override
    public BookContentLoader.LoadResult loadJson(Book book, ResourceLocation file) {
        PatchouliAPI.LOGGER.trace("Loading {}", file);
        Map<ResourceLocation, JsonElement> map = (Map<ResourceLocation, JsonElement>) this.data.get(book.id);
        if (map == null) {
            return null;
        } else {
            String path = file.getPath();
            String relativizedPath = path.substring(0, path.length() - 5).split("/", 2)[1];
            JsonElement json = (JsonElement) map.get(new ResourceLocation(file.getNamespace(), relativizedPath));
            return json != null ? new BookContentLoader.LoadResult(json, null) : null;
        }
    }
}
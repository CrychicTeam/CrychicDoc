package vazkii.patchouli.common.book;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.ClientBookRegistry;
import vazkii.patchouli.common.base.PatchouliConfig;
import vazkii.patchouli.xplat.IXplatAbstractions;
import vazkii.patchouli.xplat.XplatModContainer;

public class BookRegistry {

    public static final BookRegistry INSTANCE = new BookRegistry();

    public static final String BOOKS_LOCATION = "patchouli_books";

    public final Map<ResourceLocation, Book> books = new HashMap();

    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).create();

    private BookRegistry() {
    }

    public void init() {
        Collection<XplatModContainer> mods = IXplatAbstractions.INSTANCE.getAllMods();
        Map<Pair<XplatModContainer, ResourceLocation>, String> foundBooks = new HashMap();
        mods.forEach(mod -> {
            String id = mod.getId();
            findFiles(mod, String.format("data/%s/%s", id, "patchouli_books"), x$0 -> Files.exists(x$0, new LinkOption[0]), (path, file) -> {
                if (Files.isRegularFile(file, new LinkOption[0]) && file.getFileName().toString().equals("book.json")) {
                    String fileStr = file.toString().replaceAll("\\\\", "/");
                    String relPath = fileStr.substring(fileStr.indexOf("patchouli_books") + "patchouli_books".length() + 1);
                    String bookName = relPath.substring(0, relPath.indexOf("/"));
                    if (bookName.contains("/")) {
                        PatchouliAPI.LOGGER.warn("Ignored book.json @ {}", file);
                        return true;
                    }
                    String assetPath = fileStr.substring(fileStr.indexOf("data/"));
                    ResourceLocation bookId = new ResourceLocation(id, bookName);
                    foundBooks.put(Pair.of(mod, bookId), assetPath);
                }
                return true;
            }, true, 2);
        });
        foundBooks.forEach((pair, file) -> {
            XplatModContainer mod = (XplatModContainer) pair.getLeft();
            ResourceLocation res = (ResourceLocation) pair.getRight();
            try {
                InputStream stream = Files.newInputStream(mod.getPath(file));
                try {
                    this.loadBook(mod, res, stream, false);
                } catch (Throwable var9) {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }
                    throw var9;
                }
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception var10) {
                PatchouliAPI.LOGGER.error("Failed to load book {} defined by mod {}, skipping", res, mod.getId(), var10);
            }
        });
        BookFolderLoader.findBooks();
        IXplatAbstractions.INSTANCE.signalBooksLoaded();
    }

    public void loadBook(XplatModContainer mod, ResourceLocation res, InputStream stream, boolean external) {
        Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        JsonObject tree = (JsonObject) GSON.fromJson(reader, JsonObject.class);
        this.books.put(res, new Book(tree, mod, res, external));
    }

    public void reloadContents(Level level) {
        PatchouliConfig.reloadBuiltinFlags();
        for (Book book : this.books.values()) {
            book.reloadContents(level, false);
        }
        ClientBookRegistry.INSTANCE.reloadLocks(false);
    }

    public static void findFiles(XplatModContainer mod, String base, Predicate<Path> rootFilter, BiFunction<Path, Path, Boolean> processor, boolean visitAllFiles) {
        findFiles(mod, base, rootFilter, processor, visitAllFiles, Integer.MAX_VALUE);
    }

    public static void findFiles(XplatModContainer mod, String base, Predicate<Path> rootFilter, BiFunction<Path, Path, Boolean> processor, boolean visitAllFiles, int maxDepth) {
        if (!mod.getId().equals("minecraft")) {
            try {
                for (Path root : mod.getRootPaths()) {
                    walk(root.resolve(base), rootFilter, processor, visitAllFiles, maxDepth);
                }
            } catch (IOException var8) {
                throw new UncheckedIOException(var8);
            }
        }
    }

    private static void walk(Path root, Predicate<Path> rootFilter, BiFunction<Path, Path, Boolean> processor, boolean visitAllFiles, int maxDepth) throws IOException {
        if (root != null && Files.exists(root, new LinkOption[0]) && rootFilter.test(root)) {
            if (processor != null) {
                Stream<Path> stream = Files.walk(root, maxDepth, new FileVisitOption[0]);
                label65: {
                    try {
                        Iterator<Path> itr = stream.iterator();
                        while (itr.hasNext()) {
                            boolean keepGoing = (Boolean) processor.apply(root, (Path) itr.next());
                            if (!visitAllFiles && !keepGoing) {
                                break label65;
                            }
                        }
                    } catch (Throwable var9) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (Throwable var8) {
                                var9.addSuppressed(var8);
                            }
                        }
                        throw var9;
                    }
                    if (stream != null) {
                        stream.close();
                    }
                    return;
                }
                if (stream != null) {
                    stream.close();
                }
            }
        }
    }
}
package vazkii.patchouli.client.book;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.book.Book;

public final class BookContentResourceDirectLoader implements BookContentLoader {

    public static final BookContentResourceDirectLoader INSTANCE = new BookContentResourceDirectLoader();

    private BookContentResourceDirectLoader() {
    }

    @Override
    public void findFiles(Book book, String dir, List<ResourceLocation> list) {
        String prefix = String.format("%s/%s/%s/%s", "patchouli_books", book.id.getPath(), "en_us", dir);
        Collection<ResourceLocation> files = Minecraft.getInstance().getResourceManager().listResources(prefix, p -> p.getPath().endsWith(".json")).keySet();
        files.stream().distinct().filter(file -> file.getNamespace().equals(book.id.getNamespace())).map(file -> {
            Preconditions.checkArgument(file.getPath().startsWith(prefix));
            Preconditions.checkArgument(file.getPath().endsWith(".json"));
            String newPath = file.getPath().substring(prefix.length(), file.getPath().length() - ".json".length());
            if (newPath.startsWith("/")) {
                newPath = newPath.substring(1);
            }
            return new ResourceLocation(file.getNamespace(), newPath);
        }).forEach(list::add);
    }

    @Nullable
    @Override
    public BookContentLoader.LoadResult loadJson(Book book, ResourceLocation file) {
        PatchouliAPI.LOGGER.debug("Loading {}", file);
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        try {
            Optional<Resource> resourceOpt = manager.m_213713_(file);
            if (resourceOpt.isPresent()) {
                Resource resource = (Resource) resourceOpt.get();
                return new BookContentLoader.LoadResult(BookContentLoader.streamToJson(resource.open()), computeAddedByText(resource.sourcePackId(), book));
            } else {
                return null;
            }
        } catch (IOException var6) {
            throw new UncheckedIOException(var6);
        }
    }

    @Nullable
    private static String computeAddedByText(String sourcePackId, Book book) {
        if (sourcePackId.startsWith("file/")) {
            sourcePackId = sourcePackId.substring(5);
        }
        return !sourcePackId.equals("mod_resources") && !sourcePackId.equals("fabric") ? sourcePackId : null;
    }
}
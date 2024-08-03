package vazkii.patchouli.client.book;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.FilenameUtils;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookFolderLoader;

public final class BookContentExternalLoader implements BookContentLoader {

    public static final BookContentExternalLoader INSTANCE = new BookContentExternalLoader();

    private BookContentExternalLoader() {
    }

    @Override
    public void findFiles(Book book, String dir, List<ResourceLocation> list) {
        File root = new File(BookFolderLoader.loadDir, book.id.getPath());
        File enUs = new File(root, "en_us");
        if (enUs.exists()) {
            File searchDir = new File(enUs, dir);
            if (searchDir.exists()) {
                this.crawl(searchDir, searchDir, list);
            }
        }
    }

    private void crawl(File realRoot, File root, List<ResourceLocation> list) {
        File[] files = root.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                this.crawl(realRoot, f, list);
            } else if (f.getName().endsWith(".json")) {
                list.add(this.relativize(realRoot, f));
            }
        }
    }

    private ResourceLocation relativize(File root, File f) {
        String rootPath = root.getAbsolutePath();
        String filePath = f.getAbsolutePath().substring(rootPath.length() + 1);
        String cleanPath = FilenameUtils.removeExtension(FilenameUtils.separatorsToUnix(filePath));
        return new ResourceLocation("patchouli", cleanPath);
    }

    @Override
    public BookContentLoader.LoadResult loadJson(Book book, ResourceLocation file) {
        try {
            String path = file.getPath().substring(BookFolderLoader.loadDir.getName().length());
            File targetFile = new File(BookFolderLoader.loadDir, path);
            return targetFile.exists() ? new BookContentLoader.LoadResult(BookContentLoader.streamToJson(new FileInputStream(targetFile)), null) : null;
        } catch (IOException var5) {
            PatchouliAPI.LOGGER.warn("Failed to load {}.", file, var5);
            return null;
        }
    }
}
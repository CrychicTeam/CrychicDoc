package vazkii.patchouli.common.book;

import java.io.File;
import java.io.FileInputStream;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.xplat.IXplatAbstractions;
import vazkii.patchouli.xplat.XplatModContainer;

public class BookFolderLoader {

    public static File loadDir;

    private static void setup() {
        loadDir = new File("patchouli_books");
        if (!loadDir.exists()) {
            loadDir.mkdir();
        } else if (!loadDir.isDirectory()) {
            throw new RuntimeException(loadDir.getAbsolutePath() + " is a file, not a folder, aborting. Please delete this file or move it elsewhere if it has important contents.");
        }
    }

    public static void findBooks() {
        if (loadDir == null) {
            setup();
        }
        XplatModContainer self = IXplatAbstractions.INSTANCE.getModContainer("patchouli");
        File[] subdirs = loadDir.listFiles(File::isDirectory);
        if (subdirs == null) {
            PatchouliAPI.LOGGER.warn("Failed to list external books in {}, not loading external books", loadDir.getAbsolutePath());
        } else {
            for (File dir : subdirs) {
                ResourceLocation res;
                try {
                    res = new ResourceLocation("patchouli", dir.getName());
                } catch (ResourceLocationException var14) {
                    PatchouliAPI.LOGGER.error("Invalid external book folder name {}, skipping", dir.getName(), var14);
                    continue;
                }
                File bookJson = new File(dir, "book.json");
                try {
                    FileInputStream stream = new FileInputStream(bookJson);
                    try {
                        BookRegistry.INSTANCE.loadBook(self, res, stream, true);
                    } catch (Throwable var12) {
                        try {
                            stream.close();
                        } catch (Throwable var11) {
                            var12.addSuppressed(var11);
                        }
                        throw var12;
                    }
                    stream.close();
                } catch (Exception var13) {
                    PatchouliAPI.LOGGER.error("Failed to load external book json from {}, skipping", bookJson, var13);
                }
            }
        }
    }
}
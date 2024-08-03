package org.embeddedt.modernfix.forge.load;

import java.nio.file.Path;
import java.util.IdentityHashMap;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;

public class ModResourcePackPathFixer {

    private static final IdentityHashMap<Path, IModFile> modFileByPath = new IdentityHashMap();

    public static synchronized IModFile getModFileByRootPath(Path path) {
        if (modFileByPath.size() == 0) {
            for (IModFileInfo info : ModList.get().getModFiles()) {
                modFileByPath.put(info.getFile().getFilePath(), info.getFile());
            }
        }
        return (IModFile) modFileByPath.get(path);
    }
}
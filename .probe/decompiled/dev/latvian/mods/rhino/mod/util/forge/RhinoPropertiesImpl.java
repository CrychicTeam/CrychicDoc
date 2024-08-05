package dev.latvian.mods.rhino.mod.util.forge;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class RhinoPropertiesImpl {

    public static Path getGameDir() {
        return FMLLoader.getGamePath();
    }

    public static boolean isDev() {
        return !FMLLoader.isProduction();
    }

    public static InputStream openResource(String path) throws Exception {
        return Files.newInputStream(ModList.get().getModFileById("rhino").getFile().findResource(new String[] { path }));
    }
}
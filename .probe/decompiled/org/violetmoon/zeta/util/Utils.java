package org.violetmoon.zeta.util;

import java.nio.file.Path;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

public class Utils {

    public static Path configDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static Path modsDir() {
        return FMLPaths.MODSDIR.get();
    }

    public static boolean isDevEnv() {
        return !FMLLoader.isProduction();
    }
}
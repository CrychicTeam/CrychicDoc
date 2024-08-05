package me.shedaniel.autoconfig.util.forge;

import java.nio.file.Path;
import net.minecraftforge.fml.loading.FMLPaths;

public class UtilsImpl {

    public static Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get();
    }
}
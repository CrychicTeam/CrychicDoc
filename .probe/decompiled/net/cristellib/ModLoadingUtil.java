package net.cristellib;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.cristellib.forge.ModLoadingUtilImpl;

public class ModLoadingUtil {

    @ExpectPlatform
    @Transformed
    public static boolean isModLoadedWithVersion(String modid, String minVersion) {
        return ModLoadingUtilImpl.isModLoadedWithVersion(modid, minVersion);
    }

    @ExpectPlatform
    @Transformed
    public static boolean isModLoaded(String modid) {
        return ModLoadingUtilImpl.isModLoaded(modid);
    }
}
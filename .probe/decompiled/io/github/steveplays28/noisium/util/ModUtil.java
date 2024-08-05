package io.github.steveplays28.noisium.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import io.github.steveplays28.noisium.util.forge.ModUtilImpl;

public abstract class ModUtil {

    @ExpectPlatform
    @Transformed
    public static boolean isModPresent(String id) {
        return ModUtilImpl.isModPresent(id);
    }
}
package com.craisinlord.integrated_api.utils;

import com.craisinlord.integrated_api.utils.forge.PlatformHooksImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import org.jetbrains.annotations.Contract;

public class PlatformHooks {

    @ExpectPlatform
    @Contract(pure = true)
    @Transformed
    public static boolean isModLoaded(String modid) {
        return PlatformHooksImpl.isModLoaded(modid);
    }

    @ExpectPlatform
    @Contract(pure = true)
    @Transformed
    public static boolean isDevEnvironment() {
        return PlatformHooksImpl.isDevEnvironment();
    }
}
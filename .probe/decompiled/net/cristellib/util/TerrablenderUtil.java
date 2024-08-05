package net.cristellib.util;

import net.cristellib.ModLoadingUtil;

public class TerrablenderUtil {

    private static boolean enableMixin = false;

    public static boolean mixinEnabled() {
        return enableMixin;
    }

    public static void setMixinEnabled(boolean bl) {
        enableMixin = bl;
    }

    public static boolean isModLoaded() {
        return ModLoadingUtil.isModLoadedWithVersion("terrablender", "3.0.0.170");
    }
}
package com.almostreliable.summoningrituals.util;

import net.minecraft.resources.ResourceLocation;

public final class Utils {

    private Utils() {
    }

    public static ResourceLocation getRL(String key) {
        return new ResourceLocation("summoningrituals", key);
    }

    public static <T> T cast(Object o) {
        return (T) o;
    }
}
package com.sihenzhang.crockpot.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class I18nUtils {

    private I18nUtils() {
    }

    public static MutableComponent createComponent(String prefix, String suffix) {
        return createComponent(prefix, "crockpot", suffix);
    }

    public static MutableComponent createComponent(String prefix, String suffix, Object... args) {
        return createComponent(prefix, "crockpot", suffix, args);
    }

    public static MutableComponent createComponent(String prefix, String modId, String suffix) {
        return Component.translatable(prefix + "." + modId + "." + suffix);
    }

    public static MutableComponent createComponent(String prefix, String modId, String suffix, Object... args) {
        return Component.translatable(prefix + "." + modId + "." + suffix, args);
    }

    public static MutableComponent createTooltipComponent(String suffix) {
        return createComponent("tooltip", suffix);
    }

    public static MutableComponent createTooltipComponent(String suffix, Object... args) {
        return createComponent("tooltip", suffix, args);
    }

    public static MutableComponent createIntegrationComponent(String modId, String suffix) {
        return createComponent("integration", modId + "." + suffix);
    }

    public static MutableComponent createIntegrationComponent(String modId, String suffix, Object... args) {
        return createComponent("integration", modId + "." + suffix, args);
    }
}
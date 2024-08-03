package de.keksuccino.fancymenu.platform;

import de.keksuccino.fancymenu.platform.services.IPlatformCompatibilityLayer;
import de.keksuccino.fancymenu.platform.services.IPlatformHelper;
import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static final IPlatformCompatibilityLayer COMPAT = load(IPlatformCompatibilityLayer.class);

    public static <T> T load(Class<T> clazz) {
        return (T) ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}
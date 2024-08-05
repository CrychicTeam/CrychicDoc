package com.almostreliable.morejs;

import java.util.ServiceLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoreJS {

    public static final Logger LOG = LogManager.getLogger("MoreJS/enchanting");

    public static final MoreJSPlatform PLATFORM = load();

    public static final String DISABLED_TAG = "morejs$disabled";

    static MoreJSPlatform load() {
        Class<MoreJSPlatform> clazz = MoreJSPlatform.class;
        MoreJSPlatform loadedService = (MoreJSPlatform) ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
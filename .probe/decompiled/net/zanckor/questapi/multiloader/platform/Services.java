package net.zanckor.questapi.multiloader.platform;

import java.util.ServiceLoader;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.multiloader.platform.services.IPlatformHelper;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static <T> T load(Class<T> clazz) {
        T loadedService = (T) ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        CommonMain.Constants.LOG.info("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
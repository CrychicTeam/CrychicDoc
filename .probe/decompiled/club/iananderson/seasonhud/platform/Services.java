package club.iananderson.seasonhud.platform;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.platform.services.IMinimapHelper;
import club.iananderson.seasonhud.platform.services.IPlatformHelper;
import club.iananderson.seasonhud.platform.services.ISeasonHelper;
import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static final ISeasonHelper SEASON = load(ISeasonHelper.class);

    public static final IMinimapHelper MINIMAP = load(IMinimapHelper.class);

    public static <T> T load(Class<T> clazz) {
        T loadedService = (T) ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Common.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
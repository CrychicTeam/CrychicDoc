package mezz.jei.common.platform;

import java.util.ServiceLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Services {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static <T> T load(Class<T> serviceClass) {
        T loadedService = (T) ServiceLoader.load(serviceClass).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + serviceClass.getName()));
        LOGGER.debug("Loaded {} for service {}", loadedService, serviceClass);
        return loadedService;
    }
}
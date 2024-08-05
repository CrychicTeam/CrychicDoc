package com.illusivesoulworks.polymorph.platform;

import com.illusivesoulworks.polymorph.PolymorphConstants;
import com.illusivesoulworks.polymorph.platform.services.IClientPlatform;
import com.illusivesoulworks.polymorph.platform.services.IIntegrationPlatform;
import com.illusivesoulworks.polymorph.platform.services.IPlatform;
import java.util.ServiceLoader;

public class Services {

    public static final IIntegrationPlatform INTEGRATION_PLATFORM = load(IIntegrationPlatform.class);

    public static final IClientPlatform CLIENT_PLATFORM = load(IClientPlatform.class);

    public static final IPlatform PLATFORM = load(IPlatform.class);

    public static <T> T load(Class<T> clazz) {
        T loadedService = (T) ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        PolymorphConstants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
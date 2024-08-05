package com.mrcrayfish.configured.impl.framework;

import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.api.IModConfigProvider;
import com.mrcrayfish.configured.api.ModContext;
import com.mrcrayfish.configured.platform.Services;
import com.mrcrayfish.framework.config.FrameworkConfigManager;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class FrameworkConfigProvider implements IModConfigProvider {

    @Override
    public Set<IModConfig> getConfigurationsForMod(ModContext context) {
        return !Services.PLATFORM.isModLoaded("framework") ? Collections.emptySet() : (Set) FrameworkConfigManager.getInstance().getConfigs().stream().filter(config -> config.getName().getNamespace().equals(context.modId())).map(FrameworkModConfig::new).collect(Collectors.toUnmodifiableSet());
    }
}
package com.mrcrayfish.configured.impl.jei;

import com.google.common.collect.ImmutableSet;
import com.mrcrayfish.configured.api.ConfigType;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.api.IModConfigProvider;
import com.mrcrayfish.configured.api.ModContext;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import mezz.jei.api.runtime.config.IJeiConfigManager;

public class JeiConfigProvider implements IModConfigProvider {

    @Override
    public Set<IModConfig> getConfigurationsForMod(ModContext context) {
        return (Set<IModConfig>) (context.modId().equals("jei") ? (Set) ConfiguredJeiPlugin.getJeiConfigManager().stream().map(IJeiConfigManager::getConfigFiles).flatMap(Collection::stream).map(file -> new JeiConfig("Client", ConfigType.CLIENT, file)).collect(Collectors.toUnmodifiableSet()) : ImmutableSet.of());
    }
}
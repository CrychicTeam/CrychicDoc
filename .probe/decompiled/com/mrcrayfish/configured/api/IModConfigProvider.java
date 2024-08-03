package com.mrcrayfish.configured.api;

import java.util.Set;

public interface IModConfigProvider {

    Set<IModConfig> getConfigurationsForMod(ModContext var1);
}
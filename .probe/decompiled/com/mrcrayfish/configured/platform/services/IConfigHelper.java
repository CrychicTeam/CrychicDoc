package com.mrcrayfish.configured.platform.services;

import com.mrcrayfish.configured.api.IModConfigProvider;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.LevelResource;

public interface IConfigHelper {

    LevelResource getServerConfigResource();

    Set<IModConfigProvider> getProviders();

    ResourceLocation getBackgroundTexture(String var1);
}
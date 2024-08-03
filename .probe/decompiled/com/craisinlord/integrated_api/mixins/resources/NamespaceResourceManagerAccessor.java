package com.craisinlord.integrated_api.mixins.resources;

import java.util.List;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ FallbackResourceManager.class })
public interface NamespaceResourceManagerAccessor {

    @Accessor("fallbacks")
    List<FallbackResourceManager.PackEntry> integratedapi_getFallbacks();
}
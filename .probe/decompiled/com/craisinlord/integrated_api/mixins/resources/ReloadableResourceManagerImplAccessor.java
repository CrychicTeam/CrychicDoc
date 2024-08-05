package com.craisinlord.integrated_api.mixins.resources;

import java.util.Map;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ MultiPackResourceManager.class })
public interface ReloadableResourceManagerImplAccessor {

    @Accessor("namespacedManagers")
    Map<String, FallbackResourceManager> integratedapi_getNamespacedManagers();
}
package org.embeddedt.modernfix.dynamicresources;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

public class DynamicModelProvider {

    private final Map<ResourceLocation, UnbakedModel> internalModels;

    private final Cache<ResourceLocation, Optional<UnbakedModel>> loadedModels = CacheBuilder.newBuilder().expireAfterAccess(3L, TimeUnit.MINUTES).maximumSize(1000L).concurrencyLevel(8).softValues().build();

    public DynamicModelProvider(Map<ResourceLocation, UnbakedModel> initialModels) {
        this.internalModels = initialModels;
    }

    public UnbakedModel getModel(ResourceLocation location) {
        try {
            return (UnbakedModel) ((Optional) this.loadedModels.get(location, () -> Optional.ofNullable(this.loadModel(location)))).orElse(null);
        } catch (ExecutionException var3) {
            throw new RuntimeException(var3.getCause());
        }
    }

    private UnbakedModel loadModel(ResourceLocation location) {
        return null;
    }
}
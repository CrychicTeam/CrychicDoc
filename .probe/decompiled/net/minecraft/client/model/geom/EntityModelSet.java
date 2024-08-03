package net.minecraft.client.model.geom;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class EntityModelSet implements ResourceManagerReloadListener {

    private Map<ModelLayerLocation, LayerDefinition> roots = ImmutableMap.of();

    public ModelPart bakeLayer(ModelLayerLocation modelLayerLocation0) {
        LayerDefinition $$1 = (LayerDefinition) this.roots.get(modelLayerLocation0);
        if ($$1 == null) {
            throw new IllegalArgumentException("No model for layer " + modelLayerLocation0);
        } else {
            return $$1.bakeRoot();
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager0) {
        this.roots = ImmutableMap.copyOf(LayerDefinitions.createRoots());
    }
}
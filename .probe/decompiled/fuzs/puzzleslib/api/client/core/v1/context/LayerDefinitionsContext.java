package fuzs.puzzleslib.api.client.core.v1.context;

import java.util.function.Supplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

@FunctionalInterface
public interface LayerDefinitionsContext {

    void registerLayerDefinition(ModelLayerLocation var1, Supplier<LayerDefinition> var2);
}
package org.violetmoon.zeta.client.event.load;

import java.util.function.Supplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.violetmoon.zeta.event.bus.IZetaLoadEvent;

public interface ZRegisterLayerDefinitions extends IZetaLoadEvent {

    void registerLayerDefinition(ModelLayerLocation var1, Supplier<LayerDefinition> var2);
}
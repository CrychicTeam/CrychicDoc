package dev.architectury.registry.client.level.entity;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.client.level.entity.forge.EntityModelLayerRegistryImpl;
import java.util.function.Supplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EntityModelLayerRegistry {

    @ExpectPlatform
    @Transformed
    public static void register(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        EntityModelLayerRegistryImpl.register(location, definition);
    }
}
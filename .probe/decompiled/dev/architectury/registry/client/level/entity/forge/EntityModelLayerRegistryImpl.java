package dev.architectury.registry.client.level.entity.forge;

import dev.architectury.platform.forge.EventBuses;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityModelLayerRegistryImpl {

    private static final Map<ModelLayerLocation, Supplier<LayerDefinition>> DEFINITIONS = new ConcurrentHashMap();

    public static void register(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        DEFINITIONS.put(location, definition);
    }

    @SubscribeEvent
    public static void event(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (Entry<ModelLayerLocation, Supplier<LayerDefinition>> entry : DEFINITIONS.entrySet()) {
            event.registerLayerDefinition((ModelLayerLocation) entry.getKey(), (Supplier<LayerDefinition>) entry.getValue());
        }
    }

    static {
        EventBuses.onRegistered("architectury", bus -> bus.register(EntityModelLayerRegistryImpl.class));
    }
}
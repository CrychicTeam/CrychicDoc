package dev.architectury.registry.client.level.entity.forge;

import dev.architectury.platform.forge.EventBuses;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityRendererRegistryImpl {

    private static final Map<Supplier<EntityType<?>>, EntityRendererProvider<?>> RENDERERS = new ConcurrentHashMap();

    public static <T extends Entity> void register(Supplier<? extends EntityType<? extends T>> type, EntityRendererProvider<T> factory) {
        RENDERERS.put(type, factory);
    }

    @SubscribeEvent
    public static void event(EntityRenderersEvent.RegisterRenderers event) {
        for (Entry<Supplier<EntityType<?>>, EntityRendererProvider<?>> entry : RENDERERS.entrySet()) {
            event.registerEntityRenderer((EntityType) ((Supplier) entry.getKey()).get(), (EntityRendererProvider) entry.getValue());
        }
    }

    static {
        EventBuses.onRegistered("architectury", bus -> bus.register(EntityRendererRegistryImpl.class));
    }
}
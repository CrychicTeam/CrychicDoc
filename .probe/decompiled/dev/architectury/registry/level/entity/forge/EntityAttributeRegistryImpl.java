package dev.architectury.registry.level.entity.forge;

import dev.architectury.platform.forge.EventBuses;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityAttributeRegistryImpl {

    private static final Map<Supplier<? extends EntityType<? extends LivingEntity>>, Supplier<AttributeSupplier.Builder>> ATTRIBUTES = new ConcurrentHashMap();

    public static void register(Supplier<? extends EntityType<? extends LivingEntity>> type, Supplier<AttributeSupplier.Builder> attribute) {
        ATTRIBUTES.put(type, attribute);
    }

    @SubscribeEvent
    public static void event(EntityAttributeCreationEvent event) {
        for (Entry<Supplier<? extends EntityType<? extends LivingEntity>>, Supplier<AttributeSupplier.Builder>> entry : ATTRIBUTES.entrySet()) {
            event.put((EntityType<? extends LivingEntity>) ((Supplier) entry.getKey()).get(), ((AttributeSupplier.Builder) ((Supplier) entry.getValue()).get()).build());
        }
    }

    static {
        EventBuses.onRegistered("architectury", bus -> bus.register(EntityAttributeRegistryImpl.class));
    }
}
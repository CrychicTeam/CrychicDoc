package dev.architectury.registry.level.entity;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.level.entity.forge.EntityAttributeRegistryImpl;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public final class EntityAttributeRegistry {

    private EntityAttributeRegistry() {
    }

    @ExpectPlatform
    @Transformed
    public static void register(Supplier<? extends EntityType<? extends LivingEntity>> type, Supplier<AttributeSupplier.Builder> attribute) {
        EntityAttributeRegistryImpl.register(type, attribute);
    }
}
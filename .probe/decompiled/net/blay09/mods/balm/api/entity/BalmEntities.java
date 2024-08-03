package net.blay09.mods.balm.api.entity;

import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public interface BalmEntities {

    <T extends Entity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation var1, EntityType.Builder<T> var2);

    <T extends LivingEntity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation var1, EntityType.Builder<T> var2, Supplier<AttributeSupplier.Builder> var3);

    @Deprecated
    default <T extends LivingEntity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder, AttributeSupplier.Builder attributeBuilder) {
        return this.registerEntity(identifier, typeBuilder, () -> attributeBuilder);
    }
}
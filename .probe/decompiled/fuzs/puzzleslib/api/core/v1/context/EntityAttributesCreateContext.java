package fuzs.puzzleslib.api.core.v1.context;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

@FunctionalInterface
public interface EntityAttributesCreateContext {

    void registerEntityAttributes(EntityType<? extends LivingEntity> var1, AttributeSupplier.Builder var2);
}
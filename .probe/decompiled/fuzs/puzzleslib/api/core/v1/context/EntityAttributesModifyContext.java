package fuzs.puzzleslib.api.core.v1.context;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

@FunctionalInterface
public interface EntityAttributesModifyContext {

    default void registerAttributeModification(EntityType<? extends LivingEntity> entityType, Attribute attribute) {
        this.registerAttributeModification(entityType, attribute, attribute.getDefaultValue());
    }

    void registerAttributeModification(EntityType<? extends LivingEntity> var1, Attribute var2, double var3);
}
package net.liopyu.entityjs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.liopyu.entityjs.builders.modification.ModifyEntityBuilder;
import net.liopyu.entityjs.builders.modification.ModifyLivingEntityBuilder;
import net.liopyu.entityjs.builders.modification.ModifyMobBuilder;
import net.liopyu.entityjs.builders.modification.ModifyPathfinderMobBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;

public class EntityModificationEventJS extends EventJS {

    public static final Map<EntityType<?>, EntityModificationEventJS> eventMap = new HashMap();

    public static final Map<EntityType<?>, Object> builderMap = new HashMap();

    private final Object builder;

    private final Entity entity;

    public EntityModificationEventJS(EntityType<?> entityType, Entity entity) {
        this.entity = entity;
        this.builder = this.determineModificationType(entityType, entity);
    }

    public static EntityModificationEventJS getOrCreate(EntityType<?> entityType, Entity entity) {
        if (!eventMap.containsKey(entityType)) {
            EntityModificationEventJS event = new EntityModificationEventJS(entityType, entity);
            eventMap.put(entityType, event);
            return event;
        } else {
            return (EntityModificationEventJS) eventMap.get(entityType);
        }
    }

    @HideFromJS
    public Object getBuilder() {
        return this.builder;
    }

    @Info(value = "Entity type modification event. Allows modification of methods for any existing entity. \n \nThis event determines the entity's type and uses the appropriate builder for modification. \n \nBuilders: \n    - ModifyPathfinderMobBuilder: For entities extending {@link PathfinderMob} \n    - ModifyMobBuilder: For entities extending {@link Mob} \n    - ModifyLivingEntityBuilder: For entities extending {@link LivingEntity} \n    - ModifyEntityBuilder: For entities extending {@link Entity} \n \nExample usage: \n```javascript\nEntityJSEvents.modifyEntity(event => {\n    event.modify(\"minecraft:zombie\", builder => {\n        builder.onRemovedFromWorld(entity => {\n            // Execute code when the zombie is removed from the world.\n        })\n    })\n})\n```\n", params = { @Param(name = "entityType", value = "The entity type to modify"), @Param(name = "modifyBuilder", value = "A consumer to modify the entity type.") })
    public void modify(EntityType<?> entityType, Consumer<? extends ModifyEntityBuilder> modifyBuilder) {
        Entity entity = this.entity;
        boolean entityTypeMatch = entityType == entity.getType();
        if (entityTypeMatch) {
            Object builder = getOrCreate(entityType, entity).getBuilder();
            if (builder instanceof ModifyPathfinderMobBuilder) {
                modifyBuilder.accept((ModifyPathfinderMobBuilder) builder);
            } else if (builder instanceof ModifyMobBuilder) {
                modifyBuilder.accept((ModifyMobBuilder) builder);
            } else if (builder instanceof ModifyLivingEntityBuilder) {
                modifyBuilder.accept((ModifyLivingEntityBuilder) builder);
            } else {
                if (!(builder instanceof ModifyEntityBuilder)) {
                    throw new IllegalArgumentException("Unsupported builder type or consumer type.");
                }
                modifyBuilder.accept((ModifyEntityBuilder) builder);
            }
        }
    }

    public ModifyEntityBuilder determineModificationType(EntityType<?> type, Entity entity) {
        if (entity instanceof PathfinderMob) {
            return new ModifyPathfinderMobBuilder(type);
        } else if (entity instanceof Mob) {
            return new ModifyMobBuilder(type);
        } else {
            return (ModifyEntityBuilder) (entity instanceof LivingEntity ? new ModifyLivingEntityBuilder(type) : new ModifyEntityBuilder(type));
        }
    }
}
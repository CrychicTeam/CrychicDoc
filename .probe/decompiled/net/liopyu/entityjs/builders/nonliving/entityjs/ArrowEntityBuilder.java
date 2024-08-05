package net.liopyu.entityjs.builders.nonliving.entityjs;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.liopyu.entityjs.builders.nonliving.BaseNonAnimatableEntityBuilder;
import net.liopyu.entityjs.builders.nonliving.NonAnimatableEntityTypeBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.IArrowEntityJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;

public abstract class ArrowEntityBuilder<T extends AbstractArrow & IArrowEntityJS> extends BaseNonAnimatableEntityBuilder<T> {

    public static final List<ArrowEntityBuilder<?>> thisList = new ArrayList();

    public transient Function<T, Object> textureLocation;

    public transient Consumer<AbstractArrow> tickDespawn;

    public transient Consumer<ContextUtils.ArrowEntityHitContext> onHitEntity;

    public transient Consumer<ContextUtils.ArrowBlockHitContext> onHitBlock;

    public transient Object defaultHitGroundSoundEvent;

    public transient Consumer<ContextUtils.ArrowLivingEntityContext> doPostHurtEffects;

    public transient Function<Entity, Object> canHitEntity;

    public transient Function<ContextUtils.ArrowPlayerContext, Object> tryPickup;

    public transient double setBaseDamage;

    public transient Function<Entity, Object> setDamageFunction;

    public transient Integer setKnockback;

    public transient Float setWaterInertia;

    public transient Consumer<ContextUtils.CollidingProjectileEntityContext> onEntityCollision;

    public ArrowEntityBuilder(ResourceLocation i) {
        super(i);
        thisList.add(this);
        this.textureLocation = t -> ((IArrowEntityJS) t).getArrowBuilder().newID("textures/entity/projectiles/", ".png");
        this.setBaseDamage = 2.0;
        this.setKnockback = 1;
    }

    @Override
    public EntityType<T> createObject() {
        return new NonAnimatableEntityTypeBuilder<T>(this).get();
    }

    @Info("Sets a callback function to be executed when the arrow\ncollides with an entity.\n\nExample usage:\n```javascript\narrowEntityBuilder.onEntityCollision(context => {\n    const { entity, target } = context\n    console.log(entity)\n});\n```\n")
    public ArrowEntityBuilder<T> onEntityCollision(Consumer<ContextUtils.CollidingProjectileEntityContext> consumer) {
        this.onEntityCollision = consumer;
        return this;
    }

    @Info("Sets a function to determine the texture resource for the entity.\nThe provided Function accepts a parameter of type T (the entity),\nallowing changing the texture based on information about the entity.\nThe default behavior returns <namespace>:textures/entity/projectiles/<path>.png.\n\nExample usage:\n```javascript\narrowEntityBuilder.textureResource(entity => {\n    // Define logic to determine the texture resource for the entity\n    // Use information about the entity provided by the context.\n    return \"kubejs:textures/entity/projectiles/arrow.png\" // Some ResourceLocation representing the texture resource;\n});\n```\n")
    public ArrowEntityBuilder<T> textureLocation(Function<T, Object> function) {
        this.textureLocation = entity -> {
            Object obj = function.apply(entity);
            if (obj instanceof String && !obj.toString().equals("undefined")) {
                return new ResourceLocation((String) obj);
            } else if (obj instanceof ResourceLocation) {
                return (ResourceLocation) obj;
            } else {
                EntityJSHelperClass.logWarningMessageOnce("Invalid texture resource in arrow builder: " + obj + "Defaulting to " + ((IArrowEntityJS) entity).getArrowBuilder().newID("textures/entity/projectiles/", ".png"));
                return ((IArrowEntityJS) entity).getArrowBuilder().newID("textures/entity/projectiles/", ".png");
            }
        };
        return this;
    }

    @Info("Sets a function to determine if a player can pick up the arrow entity.\n\n@param tryPickup The function to check if a player can pick up the arrow.\n\nExample usage:\n```javascript\narrowEntityBuilder.tryPickup(context => {\n    // Custom logic to determine if the player can pick up the arrow\n    // Return true if the player can pick up, false otherwise.\n});\n```\n")
    public ArrowEntityBuilder<T> tryPickup(Function<ContextUtils.ArrowPlayerContext, Object> function) {
        this.tryPickup = function;
        return this;
    }

    @Info("Sets the base damage value for the arrow entity.\n\n@param baseDamage The base damage value to be set.\n\nExample usage:\n```javascript\narrowEntityBuilder.setBaseDamage(8.0);\n```\n")
    public ArrowEntityBuilder<T> setBaseDamage(double baseDamage) {
        this.setBaseDamage = baseDamage;
        return this;
    }

    @Info("Sets the base damage value with a function for the arrow entity for more control.\n\n@param setDamageFunction Function which returns a double.\n\nExample usage:\n```javascript\narrowEntityBuilder.setBaseDamage(entity => {\n    return 10; //Some double based off entity context.\n});\n```\n")
    public ArrowEntityBuilder<T> setDamageFunction(Function<Entity, Object> baseDamage) {
        this.setDamageFunction = baseDamage;
        return this;
    }

    @Info("Sets the knockback value for the arrow entity when a bow has Punch Enchantment.\n\n@param setKnockback The knockback value of the Punch Enchantment to be set.\n\nExample usage:\n```javascript\narrowEntityBuilder.setKnockback(2);\n```\n")
    public ArrowEntityBuilder<T> setKnockback(int knockback) {
        this.setKnockback = knockback;
        return this;
    }

    @Info("Sets the water inertia value for the arrow entity.\n\n@param setWaterInertia The water inertia value to be set.\nDefaults to 0.6 for AbstractArrow\n\nExample usage:\n```javascript\narrowEntityBuilder.setWaterInertia(0.5);\n```\n")
    public ArrowEntityBuilder<T> setWaterInertia(float waterInertia) {
        this.setWaterInertia = waterInertia;
        return this;
    }

    @Info("Sets a consumer to perform additional effects after the arrow successfully hurts a living entity.\n\n@param doPostHurtEffects The consumer to perform additional effects.\n\nExample usage:\n```javascript\narrowEntityBuilder.doPostHurtEffects(context => {\n    // Custom logic to perform additional effects after the arrow hurts a living entity.\n});\n```\n")
    public ArrowEntityBuilder<T> doPostHurtEffects(Consumer<ContextUtils.ArrowLivingEntityContext> consumer) {
        this.doPostHurtEffects = consumer;
        return this;
    }

    @Info("Sets the default sound event played when the arrow hits the ground using a string representation.\n\n@param defaultHitGroundSoundEvent A string representing the ResourceLocation of the sound event.\n\nExample usage:\n```javascript\n// Example to set a custom sound event for the arrow hitting the ground.\narrowEntityBuilder.defaultHitGroundSoundEvent(\"minecraft:entity.arrow.hit\");\n```\n")
    public ArrowEntityBuilder<T> defaultHitGroundSoundEvent(Object sound) {
        if (sound instanceof String) {
            this.defaultHitGroundSoundEvent = new ResourceLocation((String) sound);
        } else if (sound instanceof ResourceLocation) {
            this.defaultHitGroundSoundEvent = (ResourceLocation) sound;
        } else {
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid value for defaultHitGroundSoundEvent. Value: " + sound + ". Must be a ResourceLocation or String. Example: \"minecraft:entity.arrow.hit\"");
        }
        return this;
    }

    @Info("Sets a consumer to be called during each tick to handle arrow entity despawn logic.\n\n@param tickDespawn The consumer to handle the arrow entity tick despawn logic.\n\nExample usage:\n```javascript\narrowEntityBuilder.tickDespawn(arrow => {\n    // Custom logic to handle arrow entity despawn during each tick\n});\n```\n")
    public ArrowEntityBuilder<T> tickDespawn(Consumer<AbstractArrow> consumer) {
        this.tickDespawn = consumer;
        return this;
    }

    @Info("Sets a consumer to be called when the arrow entity hits another entity.\n\n@param onHitEntity The consumer to handle the arrow entity hit context.\n\nExample usage:\n```javascript\narrowEntityBuilder.onHitEntity(context => {\n    // Custom logic to handle the arrow hitting another entity\n});\n```\n")
    public ArrowEntityBuilder<T> onHitEntity(Consumer<ContextUtils.ArrowEntityHitContext> consumer) {
        this.onHitEntity = consumer;
        return this;
    }

    @Info("Sets a consumer to be called when the arrow entity hits a block.\n\n@param onHitBlock The consumer to handle the arrow block hit context.\n\nExample usage:\n```javascript\narrowEntityBuilder.onHitBlock(context => {\n    // Custom logic to handle the arrow hitting a block\n});\n```\n")
    public ArrowEntityBuilder<T> onHitBlock(Consumer<ContextUtils.ArrowBlockHitContext> consumer) {
        this.onHitBlock = consumer;
        return this;
    }

    @Info("Sets a function to determine if the arrow entity can hit a specific entity.\n\n@param canHitEntity Function to check if the arrow can hit the entity.\n\nExample usage:\n```javascript\narrowEntityBuilder.canHitEntity(entity => {\n    // Custom logic to determine if the arrow can hit the specified entity\n    // Return true if the arrow can hit, false otherwise.\n});\n```\n")
    public ArrowEntityBuilder<T> canHitEntity(Function<Entity, Object> function) {
        this.canHitEntity = function;
        return this;
    }
}
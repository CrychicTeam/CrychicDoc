package net.liopyu.entityjs.builders.modification;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import java.util.function.Function;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class ModifyMobBuilder extends ModifyLivingEntityBuilder {

    public transient Consumer<ContextUtils.PlayerEntityContext> tickLeash;

    public transient Consumer<ContextUtils.TargetChangeContext> onTargetChanged;

    public transient Consumer<LivingEntity> ate;

    public transient Object setAmbientSound;

    public transient Function<ContextUtils.EntityItemStackContext, Object> canHoldItem;

    public transient Boolean shouldDespawnInPeaceful;

    public transient Function<Mob, Object> canPickUpLoot;

    public transient Boolean isPersistenceRequired;

    public transient Function<Mob, Object> meleeAttackRangeSqr;

    public transient Object ambientSoundInterval;

    public transient Function<ContextUtils.EntityDistanceToPlayerContext, Object> removeWhenFarAway;

    public transient Function<ContextUtils.PlayerEntityContext, Object> canBeLeashed;

    public transient Function<ContextUtils.EntityLevelContext, Object> createNavigation;

    public ModifyMobBuilder(EntityType<?> entity) {
        super(entity);
    }

    @Info("Sets a function to determine the PathNavigation of the entity.\n\n@param createNavigation A Function accepting an EntityLevelContext parameter\n\nExample usage:\n```javascript\nmodifyBuilder.createNavigation(context => {\n    const {entity, level} = context\n    return EntityJSUtils.createWallClimberNavigation(entity, level) // Return some path navigation\n});\n```\n")
    public ModifyMobBuilder createNavigation(Function<ContextUtils.EntityLevelContext, Object> createNavigation) {
        this.createNavigation = createNavigation;
        return this;
    }

    @Info("Sets a function to determine if the entity can be leashed.\n\n@param canBeLeashed A Function accepting a ContextUtils.PlayerEntityContext parameter\n\nExample usage:\n```javascript\nmodifyBuilder.canBeLeashed(context => {\n    return true // Return true if the entity can be leashed, false otherwise.\n});\n```\n")
    public ModifyMobBuilder canBeLeashed(Function<ContextUtils.PlayerEntityContext, Object> canBeLeashed) {
        this.canBeLeashed = canBeLeashed;
        return this;
    }

    @Info("Sets a predicate to determine if the entity should be removed when far away from the player.\n\n@param removeWhenFarAway A Function accepting a ContextUtils.EntityDistanceToPlayerContext parameter,\n                         defining the condition for the entity to be removed when far away.\n\nExample usage:\n```javascript\nmodifyBuilder.removeWhenFarAway(context => {\n    // Custom logic to determine if the entity should be removed when far away\n    // Return true if the entity should be removed based on the provided context.\n});\n```\n")
    public ModifyMobBuilder removeWhenFarAway(Function<ContextUtils.EntityDistanceToPlayerContext, Object> removeWhenFarAway) {
        this.removeWhenFarAway = removeWhenFarAway;
        return this;
    }

    @Info("Sets the interval in ticks between ambient sounds for the mob entity.\n\n@param ambientSoundInterval The interval in ticks between ambient sounds.\nDefaults to 120.\n\nExample usage:\n```javascript\nmodifyBuilder.ambientSoundInterval(100);\n```\n")
    public ModifyMobBuilder ambientSoundInterval(int ambientSoundInterval) {
        this.ambientSoundInterval = ambientSoundInterval;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity's target changes.\n\n@param setTarget A Consumer accepting a ContextUtils.TargetChangeContext parameter,\n                 defining the behavior to be executed when the entity's target changes.\n\nExample usage:\n```javascript\nmodifyBuilder.onTargetChanged(context => {\n    // Custom logic to handle the entity's target change\n    // Access information about the target change using the provided context.\n});\n```\n")
    public ModifyMobBuilder onTargetChanged(Consumer<ContextUtils.TargetChangeContext> setTarget) {
        this.onTargetChanged = setTarget;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity performs an eating action.\n\n@param ate A Consumer accepting a LivingEntity parameter,\n           defining the behavior to be executed when the entity eats.\n\nExample usage:\n```javascript\nmodifyBuilder.ate(entity => {\n    // Custom logic to handle the entity's eating action\n    // Access information about the entity using the provided parameter.\n});\n```\n")
    public ModifyMobBuilder ate(Consumer<LivingEntity> ate) {
        this.ate = ate;
        return this;
    }

    @Info("Sets the sound to play when the entity is ambient using either a string representation or a ResourceLocation object.\n\nExample usage:\n```javascript\nmodifyBuilder.setAmbientSound(\"minecraft:entity.zombie.ambient\");\n```\n")
    public ModifyMobBuilder setAmbientSound(Object ambientSound) {
        if (ambientSound instanceof String) {
            this.setAmbientSound = new ResourceLocation((String) ambientSound);
        } else if (ambientSound instanceof ResourceLocation resourceLocation) {
            this.setAmbientSound = resourceLocation;
        } else {
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid value for setAmbientSound. Value: " + ambientSound + ". Must be a ResourceLocation or String. Example: \"minecraft:entity.zombie.ambient\"");
            this.setAmbientSound = null;
        }
        return this;
    }

    @Info("Sets the function to determine whether the entity can hold an item.\n\n@param canHoldItem A Function accepting a {@link ContextUtils.EntityItemStackContext} parameter,\n                   defining the condition for the entity to hold an item.\n\nExample usage:\n```javascript\nmodifyBuilder.canHoldItem(context => {\n    // Custom logic to determine whether the entity can hold an item based on the provided context.\n    return true;\n});\n```\n")
    public ModifyMobBuilder canHoldItem(Function<ContextUtils.EntityItemStackContext, Object> canHoldItem) {
        this.canHoldItem = canHoldItem;
        return this;
    }

    @Info("Sets whether the entity should despawn in peaceful difficulty.\n\n@param shouldDespawnInPeaceful A boolean indicating whether the entity should despawn in peaceful difficulty.\n\nExample usage:\n```javascript\nmodifyBuilder.shouldDespawnInPeaceful(true);\n```\n")
    public ModifyMobBuilder shouldDespawnInPeaceful(boolean shouldDespawnInPeaceful) {
        this.shouldDespawnInPeaceful = shouldDespawnInPeaceful;
        return this;
    }

    @Info("Sets the function to determine whether the entity can pick up loot.\n\n@param canPickUpLoot A Function accepting a {@link Mob} parameter,\n                     defining the condition for the entity to pick up loot.\n\nExample usage:\n```javascript\nmodifyBuilder.canPickUpLoot(entity => {\n    // Custom logic to determine whether the entity can pick up loot based on the provided mob.\n    return true;\n});\n```\n")
    public ModifyMobBuilder canPickUpLoot(Function<Mob, Object> canPickUpLoot) {
        this.canPickUpLoot = canPickUpLoot;
        return this;
    }

    @Info("Sets whether persistence is required for the entity.\n\n@param isPersistenceRequired A boolean indicating whether persistence is required.\n\nExample usage:\n```javascript\nmodifyBuilder.isPersistenceRequired(true);\n```\n")
    public ModifyMobBuilder isPersistenceRequired(boolean isPersistenceRequired) {
        this.isPersistenceRequired = isPersistenceRequired;
        return this;
    }

    @Info("Sets the function to determine the squared melee attack range for the entity.\n\n@param meleeAttackRangeSqr A Function accepting a {@link Mob} parameter,\n                          defining the squared melee attack range based on the entity's state.\n                          Returns a 'Double' value representing the squared melee attack range.\nExample usage:\n```javascript\nmodifyBuilder.meleeAttackRangeSqr(entity => {\n    // Custom logic to calculate the squared melee attack range based on the provided mob.\n    return 2;\n});\n```\n")
    public ModifyMobBuilder meleeAttackRangeSqr(Function<Mob, Object> meleeAttackRangeSqr) {
        this.meleeAttackRangeSqr = meleeAttackRangeSqr;
        return this;
    }

    @Info("Sets the callback function to be executed when the entity ticks while leashed.\n\n@param consumer A Consumer accepting a {@link ContextUtils.PlayerEntityContext} parameter,\n                defining the behavior to be executed when the entity ticks while leashed.\n\nExample usage:\n```javascript\nmodifyBuilder.tickLeash(context => {\n    // Custom logic to handle the entity's behavior while leashed.\n    // Access information about the player and entity using the provided context.\n});\n```\n")
    public ModifyMobBuilder tickLeash(Consumer<ContextUtils.PlayerEntityContext> consumer) {
        this.tickLeash = consumer;
        return this;
    }
}
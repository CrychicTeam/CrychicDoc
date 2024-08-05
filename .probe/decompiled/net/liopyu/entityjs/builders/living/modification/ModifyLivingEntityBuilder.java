package net.liopyu.entityjs.builders.living.modification;

import dev.latvian.mods.kubejs.level.LevelEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.function.Consumer;
import java.util.function.Function;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class ModifyLivingEntityBuilder extends LevelEventJS {

    private final LivingEntity entity;

    public transient Consumer<ContextUtils.EntityHurtContext> modifyHurt;

    public transient boolean isPushable;

    public transient Function<LivingEntity, Object> shouldDropLoot;

    public transient Function<ContextUtils.PassengerEntityContext, Object> canAddPassenger;

    public transient Function<LivingEntity, Object> isAffectedByFluids;

    public transient boolean isAlwaysExperienceDropper;

    public transient Function<LivingEntity, Object> isImmobile;

    public transient Consumer<ContextUtils.LerpToContext> lerpTo;

    public transient Function<LivingEntity, Object> setBlockJumpFactor;

    public transient Function<LivingEntity, Object> blockSpeedFactor;

    public transient Float setSoundVolume;

    public transient Float setWaterSlowDown;

    public transient Object setSwimSound;

    public transient Function<LivingEntity, Object> isFlapping;

    public transient Object setDeathSound;

    public transient EntityType<?> getType;

    public transient Object mainArm;

    public transient Consumer<ContextUtils.AutoAttackContext> doAutoAttackOnTouch;

    public transient Function<ContextUtils.EntityPoseDimensionsContext, Object> setStandingEyeHeight;

    public transient Consumer<LivingEntity> onDecreaseAirSupply;

    public transient Consumer<ContextUtils.LivingEntityContext> onBlockedByShield;

    public transient Boolean repositionEntityAfterLoad;

    public transient Function<Entity, Object> nextStep;

    public transient Consumer<LivingEntity> onIncreaseAirSupply;

    public transient Function<ContextUtils.HurtContext, Object> setHurtSound;

    public transient Object setSwimSplashSound;

    public transient Function<ContextUtils.EntityTypeEntityContext, Object> canAttackType;

    public transient Function<LivingEntity, Object> scale;

    public transient Function<LivingEntity, Object> shouldDropExperience;

    public transient Function<LivingEntity, Object> experienceReward;

    public transient Consumer<ContextUtils.EntityEquipmentContext> onEquipItem;

    public transient Function<ContextUtils.VisualContext, Object> visibilityPercent;

    public transient Function<ContextUtils.LivingEntityContext, Object> canAttack;

    public transient Function<ContextUtils.OnEffectContext, Object> canBeAffected;

    public transient Function<LivingEntity, Object> invertedHealAndHarm;

    public transient Consumer<ContextUtils.OnEffectContext> onEffectAdded;

    public transient Consumer<ContextUtils.OnEffectContext> onEffectRemoved;

    public transient Consumer<ContextUtils.EntityHealContext> onLivingHeal;

    public transient Consumer<ContextUtils.EntityDamageContext> onHurt;

    public transient Consumer<ContextUtils.DeathContext> onDeath;

    public transient Consumer<ContextUtils.EntityLootContext> dropCustomDeathLoot;

    public transient LivingEntity.Fallsounds fallSounds;

    public transient Object smallFallSound;

    public transient Object largeFallSound;

    public transient Object eatingSound;

    public transient Function<LivingEntity, Object> onClimbable;

    public transient Boolean canBreatheUnderwater;

    public transient Consumer<ContextUtils.EntityFallDamageContext> onLivingFall;

    public transient Consumer<LivingEntity> onSprint;

    public transient Function<LivingEntity, Object> jumpBoostPower;

    public transient Function<ContextUtils.EntityFluidStateContext, Object> canStandOnFluid;

    public transient Function<LivingEntity, Object> isSensitiveToWater;

    public transient Consumer<LivingEntity> onStopRiding;

    public transient Consumer<LivingEntity> rideTick;

    public transient Consumer<ContextUtils.EntityItemEntityContext> onItemPickup;

    public transient Function<ContextUtils.LineOfSightContext, Object> hasLineOfSight;

    public transient Consumer<LivingEntity> onEnterCombat;

    public transient Consumer<LivingEntity> onLeaveCombat;

    public transient Function<LivingEntity, Object> isAffectedByPotions;

    public transient Function<LivingEntity, Object> isAttackable;

    public transient Function<ContextUtils.EntityItemLevelContext, Object> canTakeItem;

    public transient Function<LivingEntity, Object> isSleeping;

    public transient Consumer<ContextUtils.EntityBlockPosContext> onStartSleeping;

    public transient Consumer<LivingEntity> onStopSleeping;

    public transient Consumer<ContextUtils.EntityItemLevelContext> eat;

    public transient Function<ContextUtils.PlayerEntityContext, Object> shouldRiderFaceForward;

    public transient Function<LivingEntity, Object> canFreeze;

    public transient Function<LivingEntity, Object> isCurrentlyGlowing;

    public transient Function<LivingEntity, Object> canDisableShield;

    public transient Function<LivingEntity, Object> setMaxFallDistance;

    public transient Consumer<ContextUtils.MobInteractContext> onInteract;

    public transient Consumer<LivingEntity> onClientRemoval;

    public transient Consumer<LivingEntity> onAddedToWorld;

    public transient Consumer<LivingEntity> lavaHurt;

    public transient Consumer<LivingEntity> onFlap;

    public transient Function<LivingEntity, Object> dampensVibrations;

    public transient Consumer<ContextUtils.PlayerEntityContext> playerTouch;

    public transient Function<LivingEntity, Object> showVehicleHealth;

    public transient Consumer<ContextUtils.ThunderHitContext> thunderHit;

    public transient Function<ContextUtils.DamageContext, Object> isInvulnerableTo;

    public transient Function<LivingEntity, Object> canChangeDimensions;

    public transient Function<ContextUtils.CalculateFallDamageContext, Object> calculateFallDamage;

    public transient Function<ContextUtils.MayInteractContext, Object> mayInteract;

    public transient Function<ContextUtils.CanTrampleContext, Object> canTrample;

    public transient Consumer<LivingEntity> onRemovedFromWorld;

    public transient Consumer<LivingEntity> onLivingJump;

    public transient Consumer<LivingEntity> aiStep;

    public transient Consumer<AttributeSupplier.Builder> attributes;

    public transient MobType mobType;

    public transient Function<LivingEntity, Object> isFreezing;

    public transient Function<ContextUtils.CollidingEntityContext, Object> canCollideWith;

    public transient Consumer<ContextUtils.Vec3Context> travel;

    public transient Boolean canSteer;

    public transient boolean mountJumpingEnabled;

    public transient Consumer<LivingEntity> tickDeath;

    public transient Consumer<ContextUtils.LineOfSightContext> onHurtTarget;

    public transient Function<ContextUtils.LineOfSightContext, Object> isAlliedTo;

    public transient Consumer<ContextUtils.PositionRiderContext> positionRider;

    public transient Consumer<LivingEntity> tick;

    public ModifyLivingEntityBuilder(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public ModifyLivingEntityBuilder modifyHurt(Consumer<ContextUtils.EntityHurtContext> context) {
        this.modifyHurt = context;
        return this;
    }

    @Override
    public Level getLevel() {
        return this.entity.m_9236_();
    }

    @Info("Sets the block jump factor for the entity.\n\nExample usage:\n```javascript\nentityBuilder.setBlockJumpFactor(entity => {\n    //Set the jump factor for the entity through context\n    return 1 //some float value;\n});\n```\n")
    public ModifyLivingEntityBuilder setBlockJumpFactor(Function<LivingEntity, Object> blockJumpFactor) {
        this.setBlockJumpFactor = blockJumpFactor;
        return this;
    }

    @Info("Sets the water slowdown factor for the entity. Defaults to 0.8.\n\nExample usage:\n```javascript\nentityBuilder.setWaterSlowDown(0.6);\n```\n")
    public ModifyLivingEntityBuilder setWaterSlowDown(float slowdownFactor) {
        this.setWaterSlowDown = slowdownFactor;
        return this;
    }

    @Info("Sets the overall sound volume for the entity.\n\nExample usage:\n```javascript\nentityBuilder.setSoundVolume(0.5);\n```\n")
    public ModifyLivingEntityBuilder setSoundVolume(float volume) {
        this.setSoundVolume = volume;
        return this;
    }

    @Info("Sets a predicate to determine whether the entity should drop loot upon death.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose loot dropping behavior is being determined.\nIt returns a Boolean indicating whether the entity should drop loot.\n\nExample usage:\n```javascript\nentityBuilder.shouldDropLoot(entity => {\n    // Define logic to determine whether the entity should drop loot\n    // Use information about the LivingEntity provided by the context.\n    return // Some Boolean value indicating whether the entity should drop loot;\n});\n```\n")
    public ModifyLivingEntityBuilder shouldDropLoot(Function<LivingEntity, Object> b) {
        this.shouldDropLoot = b;
        return this;
    }

    @Info("Sets a callback function to be executed during the living entity's AI step.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nallowing customization of the AI behavior.\n\nExample usage:\n```javascript\nentityBuilder.aiStep(entity => {\n    // Custom logic to be executed during the living entity's AI step\n    // Access and modify information about the entity using the provided context.\n});\n```\n")
    public ModifyLivingEntityBuilder aiStep(Consumer<LivingEntity> aiStep) {
        this.aiStep = aiStep;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity jumps.\n\nExample usage:\n```javascript\nentityBuilder.onLivingJump(entity => {\n    // Custom logic to handle the entity's jump action\n});\n```\n")
    public ModifyLivingEntityBuilder onLivingJump(Consumer<LivingEntity> onJump) {
        this.onLivingJump = onJump;
        return this;
    }

    @HideFromJS
    public static MobCategory stringToMobCategory(String category) {
        return switch(category) {
            case "monster" ->
                MobCategory.MONSTER;
            case "creature" ->
                MobCategory.CREATURE;
            case "ambient" ->
                MobCategory.AMBIENT;
            case "water_creature" ->
                MobCategory.WATER_CREATURE;
            case "misc" ->
                MobCategory.MISC;
            default ->
                MobCategory.MISC;
        };
    }

    @Info("Sets whether the entity is pushable.\n\nExample usage:\n```javascript\nentityBuilder.isPushable(true);\n```\n")
    public ModifyLivingEntityBuilder isPushable(boolean b) {
        this.isPushable = b;
        return this;
    }

    @Info("Sets a predicate to determine if a passenger can be added to the entity.\n\n@param predicate The predicate to check if a passenger can be added.\n\nExample usage:\n```javascript\nentityBuilder.canAddPassenger(context => {\n    // Custom logic to determine if a passenger can be added to the entity\n    return true;\n});\n```\n")
    public ModifyLivingEntityBuilder canAddPassenger(Function<ContextUtils.PassengerEntityContext, Object> predicate) {
        this.canAddPassenger = predicate;
        return this;
    }

    @Info("Sets a predicate to determine whether the entity is affected by fluids.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose interaction with fluids is being determined.\nIt returns a Boolean indicating whether the entity is affected by fluids.\n\nExample usage:\n```javascript\nentityBuilder.isAffectedByFluids(entity => {\n    // Define logic to determine whether the entity is affected by fluids\n    // Use information about the LivingEntity provided by the context.\n    return // Some Boolean value indicating whether the entity is affected by fluids;\n});\n```\n")
    public ModifyLivingEntityBuilder isAffectedByFluids(Function<LivingEntity, Object> b) {
        this.isAffectedByFluids = b;
        return this;
    }

    @Info("Sets a predicate to determine whether the entity is immobile.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose immobility is being determined.\nIt returns a Boolean indicating whether the entity is immobile.\n\nExample usage:\n```javascript\nentityBuilder.isImmobile(entity => {\n    // Define logic to determine whether the entity is immobile\n    // Use information about the LivingEntity provided by the context.\n    return // Some Boolean value indicating whether the entity is immobile;\n});\n```\n")
    public ModifyLivingEntityBuilder isImmobile(Function<LivingEntity, Object> b) {
        this.isImmobile = b;
        return this;
    }

    @Info("Sets whether the entity is always considered as an experience dropper.\n\nExample usage:\n```javascript\nentityBuilder.isAlwaysExperienceDropper(true);\n```\n")
    public ModifyLivingEntityBuilder isAlwaysExperienceDropper(boolean b) {
        this.isAlwaysExperienceDropper = b;
        return this;
    }

    @Info("Sets a function to calculate fall damage for the entity.\nThe provided Function accepts a {@link ContextUtils.CalculateFallDamageContext} parameter,\nrepresenting the context of the fall damage calculation.\nIt returns an Integer representing the calculated fall damage.\n\nExample usage:\n```javascript\nentityBuilder.calculateFallDamage(context => {\n    // Define logic to calculate and return the fall damage for the entity\n    // Use information about the CalculateFallDamageContext provided by the context.\n    return // Some Integer value representing the calculated fall damage;\n});\n```\n")
    public ModifyLivingEntityBuilder calculateFallDamage(Function<ContextUtils.CalculateFallDamageContext, Object> calculation) {
        this.calculateFallDamage = calculation;
        return this;
    }

    @Info("Sets the death sound for the entity.\n\nExample usage:\n```javascript\nentityBuilder.setDeathSound(\"minecraft:entity.generic.death\");\n```\n")
    public ModifyLivingEntityBuilder setDeathSound(Object sound) {
        if (sound instanceof String) {
            this.setDeathSound = new ResourceLocation((String) sound);
        } else if (sound instanceof ResourceLocation) {
            this.setDeathSound = (ResourceLocation) sound;
        } else {
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid value for setDeathSound. Value: " + sound + ". Must be a ResourceLocation. Example: \"minecraft:entity.generic.death\"");
        }
        return this;
    }

    @Info("Sets the swim sound for the entity using a string representation.\n\nExample usage:\n```javascript\nentityBuilder.setSwimSound(\"minecraft:entity.generic.swim\");\n```\n")
    public ModifyLivingEntityBuilder setSwimSound(Object sound) {
        if (sound instanceof String) {
            this.setSwimSound = new ResourceLocation((String) sound);
        } else if (sound instanceof ResourceLocation) {
            this.setSwimSound = (ResourceLocation) sound;
        } else {
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid value for setSwimSound. Value: " + sound + ". Must be a ResourceLocation or String. Example: \"minecraft:entity.generic.swim\"");
            this.setSwimSound = new ResourceLocation("minecraft:entity.generic.swim");
        }
        return this;
    }

    @Info("Sets the swim splash sound for the entity using either a string representation or a ResourceLocation object.\n\nExample usage:\n```javascript\nentityBuilder.setSwimSplashSound(\"minecraft:entity.generic.splash\");\n```\n")
    public ModifyLivingEntityBuilder setSwimSplashSound(Object sound) {
        if (sound instanceof String) {
            this.setSwimSplashSound = new ResourceLocation((String) sound);
        } else if (sound instanceof ResourceLocation) {
            this.setSwimSplashSound = (ResourceLocation) sound;
        } else {
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid value for setSwimSplashSound. Value: " + sound + ". Must be a ResourceLocation or String. Example: \"minecraft:entity.generic.splash\"");
            this.setSwimSplashSound = new ResourceLocation("minecraft", "entity/generic/splash");
        }
        return this;
    }

    @Info("Sets a function to determine the block speed factor of the entity.\nThe provided Function accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose block speed factor is being determined.\nIt returns a Float representing the block speed factor.\n\nExample usage:\n```javascript\nentityBuilder.blockSpeedFactor(entity => {\n    // Define logic to calculate and return the block speed factor for the entity\n    // Use information about the LivingEntity provided by the context.\n    return // Some Float value representing the block speed factor;\n});\n```\n")
    public ModifyLivingEntityBuilder blockSpeedFactor(Function<LivingEntity, Object> callback) {
        this.blockSpeedFactor = callback;
        return this;
    }

    @Info("Sets a function to determine whether the entity is currently flapping.\nThe provided Function accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose flapping status is being determined.\nIt returns a Boolean indicating whether the entity is flapping.\n\nExample usage:\n```javascript\nentityBuilder.isFlapping(entity => {\n    // Define logic to determine whether the entity is currently flapping\n    // Use information about the LivingEntity provided by the context.\n    return // Some Boolean value indicating whether the entity is flapping;\n});\n```\n")
    public ModifyLivingEntityBuilder isFlapping(Function<LivingEntity, Object> b) {
        this.isFlapping = b;
        return this;
    }

    @Info("Sets a callback function to be executed during each tick of the entity.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is being ticked.\n\nExample usage:\n```javascript\nentityBuilder.tick(entity => {\n    // Define custom logic for handling during each tick of the entity\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder tick(Consumer<LivingEntity> tickCallback) {
        this.tick = tickCallback;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is added to the world.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is added to the world.\n\nExample usage:\n```javascript\nentityBuilder.onAddedToWorld(entity => {\n    // Define custom logic for handling when the entity is added to the world\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onAddedToWorld(Consumer<LivingEntity> onAddedToWorldCallback) {
        this.onAddedToWorld = onAddedToWorldCallback;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity automatically attacks on touch.\nThe provided Consumer accepts a {@link ContextUtils.AutoAttackContext} parameter,\nrepresenting the context of the auto-attack when the entity touches another entity.\n\nExample usage:\n```javascript\nentityBuilder.doAutoAttackOnTouch(context => {\n    // Define custom logic for handling when the entity automatically attacks on touch\n    // Use information about the AutoAttackContext provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder doAutoAttackOnTouch(Consumer<ContextUtils.AutoAttackContext> doAutoAttackOnTouch) {
        this.doAutoAttackOnTouch = doAutoAttackOnTouch;
        return this;
    }

    @Info("Sets a function to determine the standing eye height of the entity.\nThe provided Function accepts a {@link ContextUtils.EntityPoseDimensionsContext} parameter,\nrepresenting the context of the entity's pose and dimensions when standing.\nIt returns a Float representing the standing eye height.\n\nExample usage:\n```javascript\nentityBuilder.setStandingEyeHeight(context => {\n    // Define logic to calculate and return the standing eye height for the entity\n    // Use information about the EntityPoseDimensionsContext provided by the context.\n    return // Some Float value representing the standing eye height;\n});\n```\n")
    public ModifyLivingEntityBuilder setStandingEyeHeight(Function<ContextUtils.EntityPoseDimensionsContext, Object> setStandingEyeHeight) {
        this.setStandingEyeHeight = setStandingEyeHeight;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity's air supply decreases.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose air supply is being decreased.\n\nExample usage:\n```javascript\nentityBuilder.onDecreaseAirSupply(entity => {\n    // Define custom logic for handling when the entity's air supply decreases\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onDecreaseAirSupply(Consumer<LivingEntity> onDecreaseAirSupply) {
        this.onDecreaseAirSupply = onDecreaseAirSupply;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is blocked by a shield.\nThe provided Consumer accepts a {@link ContextUtils.LivingEntityContext} parameter,\nrepresenting the entity that is blocked by a shield.\n\nExample usage:\n```javascript\nentityBuilder.onBlockedByShield(context => {\n    // Define custom logic for handling when the entity is blocked by a shield\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onBlockedByShield(Consumer<ContextUtils.LivingEntityContext> onBlockedByShield) {
        this.onBlockedByShield = onBlockedByShield;
        return this;
    }

    @Info("Sets whether to reposition the entity after loading.\n\nExample usage:\n```javascript\nentityBuilder.repositionEntityAfterLoad(true);\n```\n")
    public ModifyLivingEntityBuilder repositionEntityAfterLoad(boolean customRepositionEntityAfterLoad) {
        this.repositionEntityAfterLoad = customRepositionEntityAfterLoad;
        return this;
    }

    @Info("Sets a function to determine the next step distance for the entity.\nThe provided Function accepts a {@link Entity} parameter,\nrepresenting the entity whose next step distance is being determined.\nIt returns a Float representing the next step distance.\n\nExample usage:\n```javascript\nentityBuilder.nextStep(entity => {\n    // Define logic to calculate and return the next step distance for the entity\n    // Use information about the Entity provided by the context.\n    return // Some Float value representing the next step distance;\n});\n```\n")
    public ModifyLivingEntityBuilder nextStep(Function<Entity, Object> nextStep) {
        this.nextStep = nextStep;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity's air supply increases.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose air supply is being increased.\n\nExample usage:\n```javascript\nentityBuilder.onIncreaseAirSupply(entity => {\n    // Define custom logic for handling when the entity's air supply increases\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onIncreaseAirSupply(Consumer<LivingEntity> onIncreaseAirSupply) {
        this.onIncreaseAirSupply = onIncreaseAirSupply;
        return this;
    }

    @Info("Sets a function to determine the custom hurt sound of the entity.\nThe provided Function accepts a {@link ContextUtils.HurtContext} parameter,\n```javascript\nentityBuilder.setHurtSound(context => {\n    // Custom logic to determine the hurt sound for the entity\n    // You can use information from the HurtContext to customize the sound based on the context\n    const { entity, damageSource } = context;\n    // Determine the hurt sound based on the type of damage source\n    switch (damageSource.getType()) {\n        case \"fire\":\n            return \"minecraft:entity.generic.burn\";\n        case \"fall\":\n            return \"minecraft:entity.generic.hurt\";\n        case \"drown\":\n            return \"minecraft:entity.generic.hurt\";\n        case \"explosion\":\n            return \"minecraft:entity.generic.explode\";\n        default:\n            return \"minecraft:entity.generic.explode\";\n    }\n})\n```\n")
    public ModifyLivingEntityBuilder setHurtSound(Function<ContextUtils.HurtContext, Object> sound) {
        this.setHurtSound = sound;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can attack a specific entity type.\nThe provided Predicate accepts a {@link ContextUtils.EntityTypeEntityContext} parameter,\nrepresenting the context of the entity attacking a specific entity type.\n\nExample usage:\n```javascript\nentityBuilder.canAttackType(context => {\n    // Define conditions to check if the entity can attack the specified entity type\n    // Use information about the EntityTypeEntityContext provided by the context.\n    return // Some boolean condition indicating if the entity can attack the specified entity type;\n});\n```\n")
    public ModifyLivingEntityBuilder canAttackType(Function<ContextUtils.EntityTypeEntityContext, Object> canAttackType) {
        this.canAttackType = canAttackType;
        return this;
    }

    @Info("Sets a function to determine the custom hitbox scale of the entity.\nThe provided Function accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose scale is being determined.\nIt returns a Float representing the custom scale.\n\nExample usage:\n```javascript\nentityBuilder.scale(entity => {\n    // Define logic to calculate and return the custom scale for the entity\n    // Use information about the LivingEntity provided by the context.\n    return // Some Float value;\n});\n```\n")
    public ModifyLivingEntityBuilder scale(Function<LivingEntity, Object> customScale) {
        this.scale = customScale;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity should drop experience upon death.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose experience drop is being determined.\n\nExample usage:\n```javascript\nentityBuilder.shouldDropExperience(entity => {\n    // Define conditions to check if the entity should drop experience upon death\n    // Use information about the LivingEntity provided by the context.\n    return // Some boolean condition indicating if the entity should drop experience;\n});\n```\n")
    public ModifyLivingEntityBuilder shouldDropExperience(Function<LivingEntity, Object> p) {
        this.shouldDropExperience = p;
        return this;
    }

    @Info("Sets a function to determine the experience reward for killing the entity.\nThe provided Function accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose experience reward is being determined.\nIt returns an Integer representing the experience reward.\n\nExample usage:\n```javascript\nentityBuilder.experienceReward(killedEntity => {\n    // Define logic to calculate and return the experience reward for the killedEntity\n    // Use information about the LivingEntity provided by the context.\n    return // Some Integer value representing the experience reward;\n});\n```\n")
    public ModifyLivingEntityBuilder experienceReward(Function<LivingEntity, Object> experienceReward) {
        this.experienceReward = experienceReward;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity equips an item.\nThe provided Consumer accepts a {@link ContextUtils.EntityEquipmentContext} parameter,\nrepresenting the context of the entity equipping an item.\n\nExample usage:\n```javascript\nentityBuilder.onEquipItem(context => {\n    // Define custom logic for handling when the entity equips an item\n    // Use information about the EntityEquipmentContext provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onEquipItem(Consumer<ContextUtils.EntityEquipmentContext> onEquipItem) {
        this.onEquipItem = onEquipItem;
        return this;
    }

    @Info("Sets a function to determine the visibility percentage of the entity.\nThe provided Function accepts a {@link ContextUtils.VisualContext} parameter,\nrepresenting both the entity whose visibility percentage is being determined\nand the the builder entity who is being looked at.\nIt returns a Double representing the visibility percentage.\n\nExample usage:\n```javascript\nentityBuilder.visibilityPercent(context => {\n    // Define logic to calculate and return the visibility percentage for the targetEntity\n    // Use information about the Entity provided by the context.\n    return // Some Double value representing the visibility percentage;\n});\n```\n")
    public ModifyLivingEntityBuilder visibilityPercent(Function<ContextUtils.VisualContext, Object> visibilityPercent) {
        this.visibilityPercent = visibilityPercent;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can attack another entity.\nThe provided Predicate accepts a {@link ContextUtils.LivingEntityContext} parameter,\nrepresenting the entity that may be attacked.\n\nExample usage:\n```javascript\nentityBuilder.canAttack(context => {\n    // Define conditions to check if the entity can attack the targetEntity\n    // Use information about the LivingEntity provided by the context.\n    return // Some boolean condition indicating if the entity can attack the targetEntity;\n});\n```\n")
    public ModifyLivingEntityBuilder canAttack(Function<ContextUtils.LivingEntityContext, Object> customCanAttack) {
        this.canAttack = customCanAttack;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can be affected by an effect.\nThe provided Predicate accepts a {@link ContextUtils.OnEffectContext} parameter,\nrepresenting the context of the effect that may affect the entity.\n\nExample usage:\n```javascript\nentityBuilder.canBeAffected(context => {\n    // Define conditions to check if the entity can be affected by the effect\n    // Use information about the OnEffectContext provided by the context.\n    return // Some boolean condition indicating if the entity can be affected by an effect;\n});\n```\n")
    public ModifyLivingEntityBuilder canBeAffected(Function<ContextUtils.OnEffectContext, Object> predicate) {
        this.canBeAffected = predicate;
        return this;
    }

    @Info("Sets a predicate to determine if the entity has inverted heal and harm behavior.\n\n@param invertedHealAndHarm The predicate to check for inverted heal and harm behavior.\n\nExample usage:\n```javascript\nentityBuilder.invertedHealAndHarm(entity => {\n    // Custom logic to determine if the entity has inverted heal and harm behavior\n    return true; // Replace with your custom boolean condition\n});\n```\n")
    public ModifyLivingEntityBuilder invertedHealAndHarm(Function<LivingEntity, Object> invertedHealAndHarm) {
        this.invertedHealAndHarm = invertedHealAndHarm;
        return this;
    }

    @Info("Sets a callback function to be executed when an effect is added to the entity.\nThe provided Consumer accepts a {@link ContextUtils.OnEffectContext} parameter,\nrepresenting the context of the effect being added to the entity.\n\nExample usage:\n```javascript\nentityBuilder.onEffectAdded(context => {\n    // Define custom logic for handling when an effect is added to the entity\n    // Use information about the OnEffectContext provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onEffectAdded(Consumer<ContextUtils.OnEffectContext> consumer) {
        this.onEffectAdded = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity receives healing.\nThe provided Consumer accepts a {@link ContextUtils.EntityHealContext} parameter,\nrepresenting the context of the entity receiving healing.\nVery similar to {@link ForgeEventFactory.onLivingHeal}\n\nExample usage:\n```javascript\nentityBuilder.onLivingHeal(context => {\n    // Define custom logic for handling when the entity receives healing\n    // Use information about the EntityHealContext provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onLivingHeal(Consumer<ContextUtils.EntityHealContext> callback) {
        this.onLivingHeal = callback;
        return this;
    }

    @Info("Sets a callback function to be executed when an effect is removed from the entity.\nThe provided Consumer accepts a {@link ContextUtils.OnEffectContext} parameter,\nrepresenting the context of the effect being removed from the entity.\n\nExample usage:\n```javascript\nentityBuilder.onEffectRemoved(context => {\n    // Define custom logic for handling when an effect is removed from the entity\n    // Use information about the OnEffectContext provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onEffectRemoved(Consumer<ContextUtils.OnEffectContext> consumer) {
        this.onEffectRemoved = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is hurt.\nThe provided Consumer accepts a {@link ContextUtils.EntityDamageContext} parameter,\nrepresenting the context of the entity being hurt.\n\nExample usage:\n```javascript\nentityBuilder.onHurt(context => {\n    // Define custom logic for handling when the entity is hurt\n    // Use information about the EntityDamageContext provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onHurt(Consumer<ContextUtils.EntityDamageContext> predicate) {
        this.onHurt = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity dies.\nThe provided Consumer accepts a {@link ContextUtils.DeathContext} parameter,\nrepresenting the context of the entity's death.\n\nExample usage:\n```javascript\nentityBuilder.onDeath(context => {\n    // Define custom logic for handling the entity's death\n    // Use information about the DeathContext provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onDeath(Consumer<ContextUtils.DeathContext> consumer) {
        this.onDeath = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity drops custom loot upon death.\nThe provided Consumer accepts a {@link ContextUtils.EntityLootContext} parameter,\nrepresenting the context of the entity's death and loot dropping.\n\nExample usage:\n```javascript\nentityBuilder.dropCustomDeathLoot(context => {\n    // Define custom logic for handling the entity dropping custom loot upon death\n    // Use information about the EntityLootContext provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder dropCustomDeathLoot(Consumer<ContextUtils.EntityLootContext> consumer) {
        this.dropCustomDeathLoot = consumer;
        return this;
    }

    @Info("Sets the sound resource locations for small and large falls of the entity using either string representations or ResourceLocation objects.\n\nExample usage:\n```javascript\nentityBuilder.fallSounds(\"minecraft:entity.generic.small_fall\",\n    \"minecraft:entity.generic.large_fall\");\n```\n")
    public ModifyLivingEntityBuilder fallSounds(Object smallFallSound, Object largeFallSound) {
        if (smallFallSound instanceof String) {
            this.smallFallSound = new ResourceLocation((String) smallFallSound);
        } else if (smallFallSound instanceof ResourceLocation) {
            this.smallFallSound = (ResourceLocation) smallFallSound;
        } else {
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid value for smallFallSound. Value: " + smallFallSound + ". Must be a ResourceLocation or String. Example: \"minecraft:entity.generic.small_fall\"");
            this.smallFallSound = new ResourceLocation("minecraft", "entity/generic/small_fall");
        }
        if (largeFallSound instanceof String) {
            this.largeFallSound = new ResourceLocation((String) largeFallSound);
        } else if (largeFallSound instanceof ResourceLocation) {
            this.largeFallSound = (ResourceLocation) largeFallSound;
        } else {
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid value for largeFallSound. Value: " + largeFallSound + ". Must be a ResourceLocation or String. Example: \"minecraft:entity.generic.large_fall\"");
            this.largeFallSound = new ResourceLocation("minecraft", "entity/generic/large_fall");
        }
        return this;
    }

    @Info("Sets the sound resource location for the entity's eating sound using either a string representation or a ResourceLocation object.\n\nExample usage:\n```javascript\nentityBuilder.eatingSound(\"minecraft:entity.zombie.ambient\");\n```\n")
    public ModifyLivingEntityBuilder eatingSound(Object sound) {
        if (sound instanceof String) {
            this.eatingSound = new ResourceLocation((String) sound);
        } else if (sound instanceof ResourceLocation) {
            this.eatingSound = (ResourceLocation) sound;
        } else {
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid value for eatingSound. Value: " + sound + ". Must be a ResourceLocation or String. Example: \"minecraft:entity.zombie.ambient\"");
            this.eatingSound = new ResourceLocation("minecraft", "entity/zombie/ambient");
        }
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is on a climbable surface.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may be checked for being on a climbable surface.\n\nExample usage:\n```javascript\nentityBuilder.onClimbable(entity => {\n    // Define conditions to check if the entity is on a climbable surface\n    // Use information about the LivingEntity provided by the context.\n    return // Some boolean condition indicating if the entity is on a climbable surface;\n});\n```\n")
    public ModifyLivingEntityBuilder onClimbable(Function<LivingEntity, Object> predicate) {
        this.onClimbable = predicate;
        return this;
    }

    @Info("Sets whether the entity can breathe underwater.\n\nExample usage:\n```javascript\nentityBuilder.canBreatheUnderwater(true);\n```\n")
    public ModifyLivingEntityBuilder canBreatheUnderwater(boolean canBreatheUnderwater) {
        this.canBreatheUnderwater = canBreatheUnderwater;
        return this;
    }

    @Info("Sets a callback function to be executed when the living entity falls and takes damage.\nThe provided Consumer accepts a {@link ContextUtils.EntityFallDamageContext} parameter,\nrepresenting the context of the entity falling and taking fall damage.\n\nExample usage:\n```javascript\nentityBuilder.onLivingFall(context => {\n    // Define custom logic for handling when the living entity falls and takes damage\n    // Use information about the EntityFallDamageContext provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onLivingFall(Consumer<ContextUtils.EntityFallDamageContext> c) {
        this.onLivingFall = c;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity starts sprinting.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that has started sprinting.\n\nExample usage:\n```javascript\nentityBuilder.onSprint(entity => {\n    // Define custom logic for handling when the entity starts sprinting\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onSprint(Consumer<LivingEntity> consumer) {
        this.onSprint = consumer;
        return this;
    }

    @Info("Sets the jump boost power for the entity.\n\nExample usage:\n```javascript\nentityBuilder.jumpBoostPower(entity => {\n    return //some float value\n});\n```\n")
    public ModifyLivingEntityBuilder jumpBoostPower(Function<LivingEntity, Object> jumpBoostPower) {
        this.jumpBoostPower = jumpBoostPower;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can stand on a fluid.\nThe provided Predicate accepts a {@link ContextUtils.EntityFluidStateContext} parameter,\nrepresenting the context of the entity potentially standing on a fluid.\n\nExample usage:\n```javascript\nentityBuilder.canStandOnFluid(context => {\n    // Define conditions for the entity to be able to stand on a fluid\n    // Use information about the EntityFluidStateContext provided by the context.\n    return // Some boolean condition indicating if the entity can stand on the fluid;\n});\n```\n")
    public ModifyLivingEntityBuilder canStandOnFluid(Function<ContextUtils.EntityFluidStateContext, Object> predicate) {
        this.canStandOnFluid = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is sensitive to water.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may be checked for sensitivity to water.\n\nExample usage:\n```javascript\nentityBuilder.isSensitiveToWater(entity => {\n    // Define conditions to check if the entity is sensitive to water\n    // Use information about the LivingEntity provided by the context.\n    return // Some boolean condition indicating if the entity is sensitive to water;\n});\n```\n")
    public ModifyLivingEntityBuilder isSensitiveToWater(Function<LivingEntity, Object> predicate) {
        this.isSensitiveToWater = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity stops riding.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that has stopped being ridden.\n\nExample usage:\n```javascript\nentityBuilder.onStopRiding(entity => {\n    // Define custom logic for handling when the entity stops being ridden\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onStopRiding(Consumer<LivingEntity> callback) {
        this.onStopRiding = callback;
        return this;
    }

    @Info("Sets a callback function to be executed during each tick when the entity is being ridden.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is being ridden.\n\nExample usage:\n```javascript\nentityBuilder.rideTick(entity => {\n    // Define custom logic for handling each tick when the entity is being ridden\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder rideTick(Consumer<LivingEntity> callback) {
        this.rideTick = callback;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity picks up an item.\nThe provided Consumer accepts a {@link ContextUtils.EntityItemEntityContext} parameter,\nrepresenting the context of the entity picking up an item with another entity.\n\nExample usage:\n```javascript\nentityBuilder.onItemPickup(context => {\n    // Define custom logic for handling the entity picking up an item\n    // Use information about the EntityItemEntityContext provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onItemPickup(Consumer<ContextUtils.EntityItemEntityContext> consumer) {
        this.onItemPickup = consumer;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity has line of sight to another entity.\nThe provided Function accepts a {@link LineOfSightContext} parameter,\nrepresenting the entity to check for line of sight.\n\nExample usage:\n```javascript\nentityBuilder.hasLineOfSight(context => {\n    // Define conditions to check if the entity has line of sight to the target entity\n    // Use information about the Entity provided by the context.\n    return // Some boolean condition indicating if there is line of sight;\n});\n```\n")
    public ModifyLivingEntityBuilder hasLineOfSight(Function<ContextUtils.LineOfSightContext, Object> f) {
        this.hasLineOfSight = f;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity enters combat.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that has entered combat.\n\nExample usage:\n```javascript\nentityBuilder.onEnterCombat(entity => {\n    // Define custom logic for handling the entity entering combat\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onEnterCombat(Consumer<LivingEntity> c) {
        this.onEnterCombat = c;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity leaves combat.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that has left combat.\n\nExample usage:\n```javascript\nentityBuilder.onLeaveCombat(entity => {\n    // Define custom logic for handling the entity leaving combat\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onLeaveCombat(Consumer<LivingEntity> runnable) {
        this.onLeaveCombat = runnable;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is affected by potions.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may be checked for its susceptibility to potions.\n\nExample usage:\n```javascript\nentityBuilder.isAffectedByPotions(entity => {\n    // Define conditions to check if the entity is affected by potions\n    // Use information about the LivingEntity provided by the context.\n    return // Some boolean condition indicating if the entity is affected by potions;\n});\n```\n")
    public ModifyLivingEntityBuilder isAffectedByPotions(Function<LivingEntity, Object> predicate) {
        this.isAffectedByPotions = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is attackable.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may be checked for its attackability.\n\nExample usage:\n```javascript\nentityBuilder.isAttackable(entity => {\n    // Define conditions to check if the entity is attackable\n    // Use information about the LivingEntity provided by the context.\n    return // Some boolean condition indicating if the entity is attackable;\n});\n```\n")
    public ModifyLivingEntityBuilder isAttackable(Function<LivingEntity, Object> predicate) {
        this.isAttackable = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can take an item.\nThe provided Predicate accepts a {@link ContextUtils.EntityItemLevelContext} parameter,\nrepresenting the context of the entity potentially taking an item.\n\nExample usage:\n```javascript\nentityBuilder.canTakeItem(context => {\n    // Define conditions for the entity to be able to take an item\n    // Use information about the EntityItemLevelContext provided by the context.\n    return // Some boolean condition indicating if the entity can take the item;\n});\n```\n")
    public ModifyLivingEntityBuilder canTakeItem(Function<ContextUtils.EntityItemLevelContext, Object> predicate) {
        this.canTakeItem = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is currently sleeping.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may be checked for its sleeping state.\n\nExample usage:\n```javascript\nentityBuilder.isSleeping(entity => {\n    // Define conditions to check if the entity is currently sleeping\n    // Use information about the LivingEntity provided by the context.\n    return // Some boolean condition indicating if the entity is sleeping;\n});\n```\n")
    public ModifyLivingEntityBuilder isSleeping(Function<LivingEntity, Object> supplier) {
        this.isSleeping = supplier;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity starts sleeping.\nThe provided Consumer accepts a {@link ContextUtils.EntityBlockPosContext} parameter,\nrepresenting the context of the entity starting to sleep at a specific block position.\n\nExample usage:\n```javascript\nentityBuilder.onStartSleeping(context => {\n    // Define custom logic for handling the entity starting to sleep\n    // Use information about the EntityBlockPosContext provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onStartSleeping(Consumer<ContextUtils.EntityBlockPosContext> consumer) {
        this.onStartSleeping = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity stops sleeping.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that has stopped sleeping.\n\nExample usage:\n```javascript\nentityBuilder.onStopSleeping(entity => {\n    // Define custom logic for handling the entity stopping sleeping\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onStopSleeping(Consumer<LivingEntity> runnable) {
        this.onStopSleeping = runnable;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity performs an eating action.\nThe provided Consumer accepts a {@link ContextUtils.EntityItemLevelContext} parameter,\nrepresenting the context of the entity's interaction with a specific item during eating.\n\nExample usage:\n```javascript\nentityBuilder.eat(context => {\n    // Custom logic to handle the entity's eating action\n    // Access information about the item being consumed using the provided context.\n});\n```\n")
    public ModifyLivingEntityBuilder eat(Consumer<ContextUtils.EntityItemLevelContext> function) {
        this.eat = function;
        return this;
    }

    @Info("Sets a predicate function to determine whether the rider of the entity should face forward.\nThe provided Predicate accepts a {@link ContextUtils.PlayerEntityContext} parameter,\nrepresenting the context of the player entity riding the main entity.\n\nExample usage:\n```javascript\nentityBuilder.shouldRiderFaceForward(context => {\n    // Define the conditions for the rider to face forward\n    // Use information about the player entity provided by the context.\n    return true //someBoolean;\n});\n```\n")
    public ModifyLivingEntityBuilder shouldRiderFaceForward(Function<ContextUtils.PlayerEntityContext, Object> predicate) {
        this.shouldRiderFaceForward = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can undergo freezing.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may be subjected to freezing.\n\nExample usage:\n```javascript\nentityBuilder.canFreeze(entity => {\n    // Define the conditions for the entity to be able to freeze\n    // Use information about the LivingEntity provided by the context.\n    return true //someBoolean;\n});\n```\n")
    public ModifyLivingEntityBuilder canFreeze(Function<LivingEntity, Object> predicate) {
        this.canFreeze = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is currently glowing.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may be checked for its glowing state.\n\nExample usage:\n```javascript\nentityBuilder.isCurrentlyGlowing(entity => {\n    // Define the conditions to check if the entity is currently glowing\n    // Use information about the LivingEntity provided by the context.\n    const isGlowing = // Some boolean condition to check if the entity is glowing;\n    return isGlowing;\n});\n```\n")
    public ModifyLivingEntityBuilder isCurrentlyGlowing(Function<LivingEntity, Object> predicate) {
        this.isCurrentlyGlowing = predicate;
        return this;
    }

    @Info("Sets a function to determine whether the entity can disable its target's shield.\nThe provided Predicate accepts a {@link LivingEntity} parameter.\n\nExample usage:\n```javascript\nentityBuilder.canDisableShield(entity => {\n    // Define the conditions to check if the entity can disable its shield\n    // Use information about the LivingEntity provided by the context.\n    return true;\n});\n```\n")
    public ModifyLivingEntityBuilder canDisableShield(Function<LivingEntity, Object> predicate) {
        this.canDisableShield = predicate;
        return this;
    }

    @Info("Sets a consumer to handle the interaction with the entity.\nThe provided Consumer accepts a {@link ContextUtils.MobInteractContext} parameter,\nrepresenting the context of the interaction\n\nExample usage:\n```javascript\nentityBuilder.onInteract(context => {\n    // Define custom logic for the interaction with the entity\n    // Use information about the MobInteractContext provided by the context.\n    if (context.player.isShiftKeyDown()) return\n    context.player.startRiding(context.entity);\n});\n```\n")
    public ModifyLivingEntityBuilder onInteract(Consumer<ContextUtils.MobInteractContext> c) {
        this.onInteract = c;
        return this;
    }

    @Info("Sets the minimum fall distance for the entity before taking damage.\n\nExample usage:\n```javascript\nentityBuilder.setMaxFallDistance(entity => {\n    // Define custom logic to determine the maximum fall distance\n    // Use information about the LivingEntity provided by the context.\n    return 3;\n});\n```\n")
    public ModifyLivingEntityBuilder setMaxFallDistance(Function<LivingEntity, Object> maxFallDistance) {
        this.setMaxFallDistance = maxFallDistance;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is removed on the client side.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is being removed on the client side.\n\nExample usage:\n```javascript\nentityBuilder.onClientRemoval(entity => {\n    // Define custom logic for handling the removal of the entity on the client side\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onClientRemoval(Consumer<LivingEntity> consumer) {
        this.onClientRemoval = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is hurt by lava.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is affected by lava.\n\nExample usage:\n```javascript\nentityBuilder.lavaHurt(entity => {\n    // Define custom logic for handling the entity being hurt by lava\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder lavaHurt(Consumer<LivingEntity> consumer) {
        this.lavaHurt = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity performs a flap action.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is flapping.\n\nExample usage:\n```javascript\nentityBuilder.onFlap(entity => {\n    // Define custom logic for handling the entity's flap action\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onFlap(Consumer<LivingEntity> consumer) {
        this.onFlap = consumer;
        return this;
    }

    @Info("Sets a predicate to determine whether the living entity dampens vibrations.\n\n@param predicate The predicate to determine whether the living entity dampens vibrations.\n\nThe predicate should take a LivingEntity as a parameter and return a boolean value indicating whether the living entity dampens vibrations.\n\nExample usage:\n```javascript\nModifyLivingEntityBuilder.dampensVibrations(entity => {\n    // Determine whether the living entity dampens vibrations\n    // Return true if the entity dampens vibrations, false otherwise\n});\n```\n")
    public ModifyLivingEntityBuilder dampensVibrations(Function<LivingEntity, Object> predicate) {
        this.dampensVibrations = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when a player interacts with the entity.\nThe provided Consumer accepts a {@link ContextUtils.PlayerEntityContext} parameter,\nrepresenting the context of the player's interaction with the entity.\n\nExample usage:\n```javascript\nentityBuilder.playerTouch(context => {\n    // Define custom logic for handling player interaction with the entity\n    // Use information about the PlayerEntityContext provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder playerTouch(Consumer<ContextUtils.PlayerEntityContext> consumer) {
        this.playerTouch = consumer;
        return this;
    }

    @Info("Sets a predicate to determine whether to show the vehicle health for the living entity.\n\n@param predicate The predicate to determine whether to show the vehicle health.\n\nThe predicate should take a LivingEntity as a parameter and return a boolean value indicating whether to show the vehicle health.\n\nExample usage:\n```javascript\nModifyLivingEntityBuilder.showVehicleHealth(entity => {\n    // Determine whether to show the vehicle health for the living entity\n    // Return true to show the vehicle health, false otherwise\n});\n```\n")
    public ModifyLivingEntityBuilder showVehicleHealth(Function<LivingEntity, Object> predicate) {
        this.showVehicleHealth = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is hit by thunder.\nThe provided Consumer accepts a {@link ContextUtils.ThunderHitContext} parameter,\nrepresenting the context of the entity being hit by thunder.\n\nExample usage:\n```javascript\nentityBuilder.thunderHit(context => {\n    // Define custom logic for handling the entity being hit by thunder\n    // Use information about the ThunderHitContext provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder thunderHit(Consumer<ContextUtils.ThunderHitContext> consumer) {
        this.thunderHit = consumer;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is invulnerable to a specific type of damage.\nThe provided Predicate accepts a {@link ContextUtils.DamageContext} parameter,\nrepresenting the context of the damage, and returns a boolean indicating invulnerability.\n\nExample usage:\n```javascript\nentityBuilder.isInvulnerableTo(context => {\n    // Define conditions for the entity to be invulnerable to the specific type of damage\n    // Use information about the DamageContext provided by the context.\n    return true // Some boolean condition indicating if the entity has invulnerability to the damage type;\n});\n```\n")
    public ModifyLivingEntityBuilder isInvulnerableTo(Function<ContextUtils.DamageContext, Object> predicate) {
        this.isInvulnerableTo = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can change dimensions.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may attempt to change dimensions.\n\nExample usage:\n```javascript\nentityBuilder.canChangeDimensions(entity => {\n    // Define the conditions for the entity to be able to change dimensions\n    // Use information about the LivingEntity provided by the context.\n    return false // Some boolean condition indicating if the entity can change dimensions;\n});\n```\n")
    public ModifyLivingEntityBuilder canChangeDimensions(Function<LivingEntity, Object> supplier) {
        this.canChangeDimensions = supplier;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity may interact with something.\nThe provided Predicate accepts a {@link ContextUtils.MayInteractContext} parameter,\nrepresenting the context of the potential interaction, and returns a boolean.\n\nExample usage:\n```javascript\nentityBuilder.mayInteract(context => {\n    // Define conditions for the entity to be allowed to interact\n    // Use information about the MayInteractContext provided by the context.\n    return false // Some boolean condition indicating if the entity may interact;\n});\n```\n")
    public ModifyLivingEntityBuilder mayInteract(Function<ContextUtils.MayInteractContext, Object> predicate) {
        this.mayInteract = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can trample or step on something.\nThe provided Predicate accepts a {@link ContextUtils.CanTrampleContext} parameter,\nrepresenting the context of the potential trampling action, and returns a boolean.\n\nExample usage:\n```javascript\nentityBuilder.canTrample(context => {\n    // Define conditions for the entity to be allowed to trample\n    // Use information about the CanTrampleContext provided by the context.\n    return false // Some boolean condition indicating if the entity can trample;\n});\n```\n")
    public ModifyLivingEntityBuilder canTrample(Function<ContextUtils.CanTrampleContext, Object> predicate) {
        this.canTrample = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is removed from the world.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is being removed from the world.\n\nExample usage:\n```javascript\nentityBuilder.onRemovedFromWorld(entity => {\n    // Define custom logic for handling the removal of the entity from the world\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public ModifyLivingEntityBuilder onRemovedFromWorld(Consumer<LivingEntity> consumer) {
        this.onRemovedFromWorld = consumer;
        return this;
    }

    @Info("Sets a consumer to handle custom lerping logic for the living entity.\n\n@param lerpTo The consumer to handle the custom lerping logic.\n\nThe consumer should take a LerpToContext as a parameter, providing information about the lerping operation, including the target position, yaw, pitch, increment count, teleport flag, and the entity itself.\n\nExample usage:\n```javascript\nModifyLivingEntityBuilder.lerpTo(context => {\n    // Custom lerping logic for the living entity\n    const { x, y, z, yaw, pitch, posRotationIncrements, teleport, entity } = context;\n    // Perform custom lerping operations using the provided context\n    // For example, you can smoothly move the entity from its current position to the target position\n    entity.setPositionAndRotation(x, y, z, yaw, pitch);\n});\n```\n")
    public ModifyLivingEntityBuilder lerpTo(Consumer<ContextUtils.LerpToContext> lerpTo) {
        this.lerpTo = lerpTo;
        return this;
    }

    @Info("Function determining if the entity is allied with a potential target.\n\nExample usage:\n```javascript\nentityBuilder.isAlliedTo(context => {\n    const {entity, target} = context\n    return target.type == 'minecraft:blaze'\n});\n```\n")
    public ModifyLivingEntityBuilder isAlliedTo(Function<ContextUtils.LineOfSightContext, Object> isAlliedTo) {
        this.isAlliedTo = isAlliedTo;
        return this;
    }

    @Info("@param onHurtTarget A Consumer to execute when the mob attacks its target\n\nExample usage:\n```javascript\nmobBuilder.onHurtTarget(context => {\n    const {entity, targetEntity} = context\n    //Execute code when the target is hurt\n});\n```\n")
    public ModifyLivingEntityBuilder onHurtTarget(Consumer<ContextUtils.LineOfSightContext> onHurtTarget) {
        this.onHurtTarget = onHurtTarget;
        return this;
    }

    @Info("Consumer overriding the tickDeath responsible to counting down\nthe ticks it takes to remove the entity when it dies.\n\nExample usage:\n```javascript\nentityBuilder.tickDeath(entity => {\n    // Override the tickDeath method in the entity\n});\n```\n")
    public ModifyLivingEntityBuilder tickDeath(Consumer<LivingEntity> tickDeath) {
        this.tickDeath = tickDeath;
        return this;
    }

    @Info("Boolean determining whether the entity can jump while mounted by a player.\n(Currently experimental jumping logic subject to change in the future)\nDefaults to false.\nExample usage:\n```javascript\nentityBuilder.mountJumpingEnabled(true);\n```\n")
    public ModifyLivingEntityBuilder mountJumpingEnabled(boolean mountJumpingEnabled) {
        this.mountJumpingEnabled = mountJumpingEnabled;
        return this;
    }

    @Info("Consumer determining travel logic for the entity.\n\nExample usage:\n```javascript\nentityBuilder.travel(context => {\n    const {entity, vec3} = context\n    // Use the vec3 and entity to determine the travel logic of the entity\n});\n```\n")
    public ModifyLivingEntityBuilder travel(Consumer<ContextUtils.Vec3Context> travel) {
        this.travel = travel;
        return this;
    }

    @Info("Boolean determining whether the passenger is able to steer the entity while riding.\nDefaults to true.\nExample usage:\n```javascript\nentityBuilder.canSteer(false);\n```\n")
    public ModifyLivingEntityBuilder canSteer(boolean canSteer) {
        this.canSteer = canSteer;
        return this;
    }

    @Info("Function determining if the entity may collide with another entity\nusing the ContextUtils.CollidingEntityContext which has this entity and the\none colliding with this entity.\n\nExample usage:\n```javascript\nentityBuilder.canCollideWith(context => {\n    return true //Some Boolean value determining whether the entity may collide with another\n});\n```\n")
    public ModifyLivingEntityBuilder canCollideWith(Function<ContextUtils.CollidingEntityContext, Object> canCollideWith) {
        this.canCollideWith = canCollideWith;
        return this;
    }

    @Info("Defines the Mob's Type\nExamples: 'undead', 'water', 'arthropod', 'undefined', 'illager'\n\nExample usage:\n```javascript\nentityBuilder.mobType('undead');\n```\n")
    public ModifyLivingEntityBuilder mobType(Object mt) {
        if (mt instanceof String string) {
            String var4 = string.toLowerCase();
            switch(var4) {
                case "undead":
                    this.mobType = MobType.UNDEAD;
                    break;
                case "arthropod":
                    this.mobType = MobType.ARTHROPOD;
                    break;
                case "undefined":
                    this.mobType = MobType.UNDEFINED;
                    break;
                case "illager":
                    this.mobType = MobType.ILLAGER;
                    break;
                case "water":
                    this.mobType = MobType.WATER;
                    break;
                default:
                    EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid value for mobType: " + mt + ". Example: \"undead\"");
            }
        } else if (mt instanceof MobType type) {
            this.mobType = type;
        } else {
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid value for mobType: " + mt + ". Example: \"undead\"");
        }
        return this;
    }

    @Info("Defines in what condition the entity will start freezing.\n\nExample usage:\n```javascript\nentityBuilder.isFreezing(entity => {\n    return true;\n});\n```\n")
    public ModifyLivingEntityBuilder isFreezing(Function<LivingEntity, Object> isFreezing) {
        this.isFreezing = isFreezing;
        return this;
    }

    @Info("@param positionRider A consumer determining the position of rider/riders.\n\n    Example usage:\n    ```javascript\n    entityBuilder.positionRider(context => {\n        const {entity, passenger, moveFunction} = context\n    });\n    ```\n")
    public ModifyLivingEntityBuilder positionRider(Consumer<ContextUtils.PositionRiderContext> builderConsumer) {
        this.positionRider = builderConsumer;
        return this;
    }
}
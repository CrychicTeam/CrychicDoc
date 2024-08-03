package net.liopyu.entityjs.builders.nonliving.entityjs;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import java.util.function.Function;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class PartBuilder<T extends LivingEntity> {

    public transient boolean isPickable = true;

    public transient Consumer<ContextUtils.LerpToContext> lerpTo;

    public transient Consumer<ContextUtils.EntityPlayerContext> playerTouch;

    public transient Function<ContextUtils.EntitySqrDistanceContext, Object> shouldRenderAtSqrDistance;

    public transient Consumer<Entity> tick;

    public transient Consumer<ContextUtils.MovementContext> move;

    public transient Function<Entity, Object> isAttackable = t -> true;

    public transient boolean isPushable;

    public transient Function<ContextUtils.EPassengerEntityContext, Object> canAddPassenger;

    public transient Function<Entity, Object> setBlockJumpFactor;

    public transient Function<Entity, Object> blockSpeedFactor;

    public transient Object setSwimSound;

    public transient Function<Entity, Object> isFlapping;

    public transient Boolean repositionEntityAfterLoad;

    public transient Function<Entity, Object> nextStep;

    public transient Object setSwimSplashSound;

    public transient Consumer<ContextUtils.EEntityFallDamageContext> onFall;

    public transient Consumer<Entity> onSprint;

    public transient Consumer<Entity> onStopRiding;

    public transient Consumer<Entity> rideTick;

    public transient Function<Entity, Object> canFreeze;

    public transient Function<Entity, Object> isCurrentlyGlowing;

    public transient Function<Entity, Object> setMaxFallDistance;

    public transient Consumer<Entity> onClientRemoval;

    public transient Consumer<Entity> onAddedToWorld;

    public transient Consumer<Entity> lavaHurt;

    public transient Consumer<Entity> onFlap;

    public transient Function<Entity, Object> dampensVibrations;

    public transient Function<Entity, Object> showVehicleHealth;

    public transient Consumer<ContextUtils.EThunderHitContext> thunderHit;

    public transient Function<ContextUtils.EDamageContext, Object> isInvulnerableTo;

    public transient Function<Entity, Object> canChangeDimensions;

    public transient Function<ContextUtils.EMayInteractContext, Object> mayInteract;

    public transient Function<ContextUtils.ECanTrampleContext, Object> canTrample;

    public transient Consumer<Entity> onRemovedFromWorld;

    public transient Function<Entity, Object> isFreezing;

    public transient Function<ContextUtils.ECollidingEntityContext, Object> canCollideWith;

    public transient Consumer<ContextUtils.PartHurtContext<T>> onPartHurt;

    public transient Consumer<ContextUtils.PositionRiderContext> positionRider;

    @Info("@param positionRider A consumer determining the position of rider/riders.\n\n    Example usage:\n    ```javascript\n    entityBuilder.positionRider(context => {\n        const {entity, passenger, moveFunction} = context\n    });\n    ```\n")
    public PartBuilder<T> positionRider(Consumer<ContextUtils.PositionRiderContext> builderConsumer) {
        this.positionRider = builderConsumer;
        return this;
    }

    @Info("Boolean determining if the part entity is pickable.\n\nExample usage:\n```javascript\nentityBuilder.isPickable(true)\n```\n")
    public PartBuilder<T> isPickable(boolean isPickable) {
        this.isPickable = isPickable;
        return this;
    }

    @Info("Sets a consumer to handle part entity hurt logic of the entity's parts.\n\n@param onPartHurt Consumer accepting a {@link ContextUtils.PartHurtContext<T>} parameter\n\nExample usage:\n```javascript\nentityBuilder.onPartHurt(context => {\n    const { entity, part, source, amount } = context\n    // Custom logic for determining how the parts of the entity should relay damage\n    // For example, hurt the parent entity twice the damage when this part is hit.\n    entity.attack(source, amount * 2)\n})\n```\n")
    public PartBuilder<T> onPartHurt(Consumer<ContextUtils.PartHurtContext<T>> onPartHurt) {
        this.onPartHurt = onPartHurt;
        return this;
    }

    @Info("Sets a consumer to handle lerping (linear interpolation) of the entity's position.\n\n@param lerpTo Consumer accepting a {@link ContextUtils.LerpToContext} parameter,\n                providing information and control over the lerping process.\n\nExample usage:\n```javascript\nentityBuilder.lerpTo(context => {\n    // Custom logic for lerping the entity's position\n    // Access information about the lerping process using the provided context.\n});\n```\n")
    public PartBuilder<T> lerpTo(Consumer<ContextUtils.LerpToContext> consumer) {
        this.lerpTo = consumer;
        return this;
    }

    @Info("Sets a function to determine whether the entity should render at a squared distance.\n\n@param shouldRenderAtSqrDistance Function accepting a {@link ContextUtils.EntitySqrDistanceContext} parameter,\n                 defining the conditions under which the entity should render.\n\nExample usage:\n```javascript\nentityBuilder.shouldRenderAtSqrDistance(context => {\n    // Custom logic to determine whether the entity should render\n    // Access information about the distance using the provided context.\n    return true;\n});\n```\n")
    public PartBuilder<T> shouldRenderAtSqrDistance(Function<ContextUtils.EntitySqrDistanceContext, Object> func) {
        this.shouldRenderAtSqrDistance = func;
        return this;
    }

    @Info("Sets a callback function to be executed when a player touches the entity.\nThe provided Consumer accepts a {@link ContextUtils.EntityPlayerContext} parameter,\nrepresenting the context of the player's interaction with the entity.\n\nExample usage:\n```javascript\nentityBuilder.playerTouch(context => {\n    // Custom logic to handle the player's touch interaction with the entity\n    // Access information about the interaction using the provided context.\n});\n```\n")
    public PartBuilder<T> playerTouch(Consumer<ContextUtils.EntityPlayerContext> consumer) {
        this.playerTouch = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity performs a movement action.\nThe provided Consumer accepts a {@link ContextUtils.MovementContext} parameter,\nrepresenting the context of the entity's movement.\n\nExample usage:\n```javascript\nentityBuilder.move(context => {\n    // Custom logic to handle the entity's movement action\n    // Access information about the movement using the provided context.\n});\n```\n")
    public PartBuilder<T> move(Consumer<ContextUtils.MovementContext> consumer) {
        this.move = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed on each tick for the entity.\n\n@param consumer A Consumer accepting a {@link Entity} parameter, defining the behavior to be executed on each tick.\n\nExample usage:\n```javascript\nentityBuilder.tick(entity => {\n    // Custom logic to be executed on each tick of the entity.\n    // Access information about the entity using the provided parameter.\n});\n```\n")
    public PartBuilder<T> tick(Consumer<Entity> consumer) {
        this.tick = consumer;
        return this;
    }

    @Info("Function determining if the entity may collide with another entity\nusing the ContextUtils.CollidingEntityContext which has this entity and the\none colliding with this entity.\n\nExample usage:\n```javascript\nentityBuilder.canCollideWith(context => {\n    return true //Some Boolean value determining whether the entity may collide with another\n});\n```\n")
    public PartBuilder<T> canCollideWith(Function<ContextUtils.ECollidingEntityContext, Object> canCollideWith) {
        this.canCollideWith = canCollideWith;
        return this;
    }

    @Info("Defines in what condition the entity will start freezing.\n\nExample usage:\n```javascript\nentityBuilder.isFreezing(entity => {\n    return true;\n});\n```\n")
    public PartBuilder<T> isFreezing(Function<Entity, Object> isFreezing) {
        this.isFreezing = isFreezing;
        return this;
    }

    @Info("Sets the block jump factor for the entity.\n\nExample usage:\n```javascript\nentityBuilder.setBlockJumpFactor(entity => {\n    //Set the jump factor for the entity through context\n    return 1 //some float value;\n});\n```\n")
    public PartBuilder<T> setBlockJumpFactor(Function<Entity, Object> blockJumpFactor) {
        this.setBlockJumpFactor = blockJumpFactor;
        return this;
    }

    @Info("Sets whether the entity is pushable.\n\nExample usage:\n```javascript\nentityBuilder.isPushable(true);\n```\n")
    public PartBuilder<T> isPushable(boolean b) {
        this.isPushable = b;
        return this;
    }

    @Info("Sets a predicate to determine if a passenger can be added to the entity.\n\n@param predicate The predicate to check if a passenger can be added.\n\nExample usage:\n```javascript\nentityBuilder.canAddPassenger(context => {\n    // Custom logic to determine if a passenger can be added to the entity\n    return true;\n});\n```\n")
    public PartBuilder<T> canAddPassenger(Function<ContextUtils.EPassengerEntityContext, Object> predicate) {
        this.canAddPassenger = predicate;
        return this;
    }

    @Info("Sets the swim sound for the entity using a string representation.\n\nExample usage:\n```javascript\nentityBuilder.setSwimSound(\"minecraft:entity.generic.swim\");\n```\n")
    public PartBuilder<T> setSwimSound(Object sound) {
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
    public PartBuilder<T> setSwimSplashSound(Object sound) {
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

    @Info("Sets a function to determine the block speed factor of the entity.\nThe provided Function accepts a {@link Entity} parameter,\nrepresenting the entity whose block speed factor is being determined.\nIt returns a Float representing the block speed factor.\n\nExample usage:\n```javascript\nentityBuilder.blockSpeedFactor(entity => {\n    // Define logic to calculate and return the block speed factor for the entity\n    // Use information about the Entity provided by the context.\n    return // Some Float value representing the block speed factor;\n});\n```\n")
    public PartBuilder<T> blockSpeedFactor(Function<Entity, Object> callback) {
        this.blockSpeedFactor = callback;
        return this;
    }

    @Info("Sets a function to determine whether the entity is currently flapping.\nThe provided Function accepts a {@link Entity} parameter,\nrepresenting the entity whose flapping status is being determined.\nIt returns a Boolean indicating whether the entity is flapping.\n\nExample usage:\n```javascript\nentityBuilder.isFlapping(entity => {\n    // Define logic to determine whether the entity is currently flapping\n    // Use information about the Entity provided by the context.\n    return // Some Boolean value indicating whether the entity is flapping;\n});\n```\n")
    public PartBuilder<T> isFlapping(Function<Entity, Object> b) {
        this.isFlapping = b;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is added to the world.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that is added to the world.\n\nExample usage:\n```javascript\nentityBuilder.onAddedToWorld(entity => {\n    // Define custom logic for handling when the entity is added to the world\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public PartBuilder<T> onAddedToWorld(Consumer<Entity> onAddedToWorldCallback) {
        this.onAddedToWorld = onAddedToWorldCallback;
        return this;
    }

    @Info("Sets whether to reposition the entity after loading.\n\nExample usage:\n```javascript\nentityBuilder.repositionEntityAfterLoad(true);\n```\n")
    public PartBuilder<T> repositionEntityAfterLoad(boolean customRepositionEntityAfterLoad) {
        this.repositionEntityAfterLoad = customRepositionEntityAfterLoad;
        return this;
    }

    @Info("Sets a function to determine the next step distance for the entity.\nThe provided Function accepts a {@link Entity} parameter,\nrepresenting the entity whose next step distance is being determined.\nIt returns a Float representing the next step distance.\n\nExample usage:\n```javascript\nentityBuilder.nextStep(entity => {\n    // Define logic to calculate and return the next step distance for the entity\n    // Use information about the Entity provided by the context.\n    return // Some Float value representing the next step distance;\n});\n```\n")
    public PartBuilder<T> nextStep(Function<Entity, Object> nextStep) {
        this.nextStep = nextStep;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity falls and takes damage.\nThe provided Consumer accepts a {@link ContextUtils.EEntityFallDamageContext} parameter,\nrepresenting the context of the entity falling and taking fall damage.\n\nExample usage:\n```javascript\nentityBuilder.onFall(context => {\n    // Define custom logic for handling when the entity falls and takes damage\n    // Use information about the EEntityFallDamageContext provided by the context.\n});\n```\n")
    public PartBuilder<T> onFall(Consumer<ContextUtils.EEntityFallDamageContext> c) {
        this.onFall = c;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity starts sprinting.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that has started sprinting.\n\nExample usage:\n```javascript\nentityBuilder.onSprint(entity => {\n    // Define custom logic for handling when the entity starts sprinting\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public PartBuilder<T> onSprint(Consumer<Entity> consumer) {
        this.onSprint = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity stops riding.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that has stopped being ridden.\n\nExample usage:\n```javascript\nentityBuilder.onStopRiding(entity => {\n    // Define custom logic for handling when the entity stops being ridden\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public PartBuilder<T> onStopRiding(Consumer<Entity> callback) {
        this.onStopRiding = callback;
        return this;
    }

    @Info("Sets a callback function to be executed during each tick when the entity is being ridden.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that is being ridden.\n\nExample usage:\n```javascript\nentityBuilder.rideTick(entity => {\n    // Define custom logic for handling each tick when the entity is being ridden\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public PartBuilder<T> rideTick(Consumer<Entity> callback) {
        this.rideTick = callback;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is attackable.\nThe provided Predicate accepts a {@link Entity} parameter,\nrepresenting the entity that may be checked for its attackability.\n\nExample usage:\n```javascript\nentityBuilder.isAttackable(entity => {\n    // Define conditions to check if the entity is attackable\n    // Use information about the Entity provided by the context.\n    return // Some boolean condition indicating if the entity is attackable;\n});\n```\n")
    public PartBuilder<T> isAttackable(Function<Entity, Object> predicate) {
        this.isAttackable = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can undergo freezing.\nThe provided Predicate accepts a {@link Entity} parameter,\nrepresenting the entity that may be subjected to freezing.\n\nExample usage:\n```javascript\nentityBuilder.canFreeze(entity => {\n    // Define the conditions for the entity to be able to freeze\n    // Use information about the Entity provided by the context.\n    return true //someBoolean;\n});\n```\n")
    public PartBuilder<T> canFreeze(Function<Entity, Object> predicate) {
        this.canFreeze = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is currently glowing.\nThe provided Predicate accepts a {@link Entity} parameter,\nrepresenting the entity that may be checked for its glowing state.\n\nExample usage:\n```javascript\nentityBuilder.isCurrentlyGlowing(entity => {\n    // Define the conditions to check if the entity is currently glowing\n    // Use information about the Entity provided by the context.\n    const isGlowing = // Some boolean condition to check if the entity is glowing;\n    return isGlowing;\n});\n```\n")
    public PartBuilder<T> isCurrentlyGlowing(Function<Entity, Object> predicate) {
        this.isCurrentlyGlowing = predicate;
        return this;
    }

    @Info("Sets the minimum fall distance for the entity before taking damage.\n\nExample usage:\n```javascript\nentityBuilder.setMaxFallDistance(entity => {\n    // Define custom logic to determine the maximum fall distance\n    // Use information about the Entity provided by the context.\n    return 3;\n});\n```\n")
    public PartBuilder<T> setMaxFallDistance(Function<Entity, Object> maxFallDistance) {
        this.setMaxFallDistance = maxFallDistance;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is removed on the client side.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that is being removed on the client side.\n\nExample usage:\n```javascript\nentityBuilder.onClientRemoval(entity => {\n    // Define custom logic for handling the removal of the entity on the client side\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public PartBuilder<T> onClientRemoval(Consumer<Entity> consumer) {
        this.onClientRemoval = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is hurt by lava.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that is affected by lava.\n\nExample usage:\n```javascript\nentityBuilder.lavaHurt(entity => {\n    // Define custom logic for handling the entity being hurt by lava\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public PartBuilder<T> lavaHurt(Consumer<Entity> consumer) {
        this.lavaHurt = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity performs a flap action.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that is flapping.\n\nExample usage:\n```javascript\nentityBuilder.onFlap(entity => {\n    // Define custom logic for handling the entity's flap action\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public PartBuilder<T> onFlap(Consumer<Entity> consumer) {
        this.onFlap = consumer;
        return this;
    }

    @Info("Sets a predicate to determine whether the living entity dampens vibrations.\n\n@param predicate The predicate to determine whether the living entity dampens vibrations.\n\nThe predicate should take a Entity as a parameter and return a boolean value indicating whether the living entity dampens vibrations.\n\nExample usage:\n```javascript\nbaseEntityBuilder.dampensVibrations(entity => {\n    // Determine whether the living entity dampens vibrations\n    // Return true if the entity dampens vibrations, false otherwise\n});\n```\n")
    public PartBuilder<T> dampensVibrations(Function<Entity, Object> predicate) {
        this.dampensVibrations = predicate;
        return this;
    }

    @Info("Sets a predicate to determine whether to show the vehicle health for the living entity.\n\n@param predicate The predicate to determine whether to show the vehicle health.\n\nThe predicate should take a Entity as a parameter and return a boolean value indicating whether to show the vehicle health.\n\nExample usage:\n```javascript\nbaseEntityBuilder.showVehicleHealth(entity => {\n    // Determine whether to show the vehicle health for the living entity\n    // Return true to show the vehicle health, false otherwise\n});\n```\n")
    public PartBuilder<T> showVehicleHealth(Function<Entity, Object> predicate) {
        this.showVehicleHealth = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is hit by thunder.\nThe provided Consumer accepts a {@link ContextUtils.ThunderHitContext} parameter,\nrepresenting the context of the entity being hit by thunder.\n\nExample usage:\n```javascript\nentityBuilder.thunderHit(context => {\n    // Define custom logic for handling the entity being hit by thunder\n    // Use information about the ThunderHitContext provided by the context.\n});\n```\n")
    public PartBuilder<T> thunderHit(Consumer<ContextUtils.EThunderHitContext> consumer) {
        this.thunderHit = consumer;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is invulnerable to a specific type of damage.\nThe provided Predicate accepts a {@link ContextUtils.DamageContext} parameter,\nrepresenting the context of the damage, and returns a boolean indicating invulnerability.\n\nExample usage:\n```javascript\nentityBuilder.isInvulnerableTo(context => {\n    // Define conditions for the entity to be invulnerable to the specific type of damage\n    // Use information about the DamageContext provided by the context.\n    return true // Some boolean condition indicating if the entity has invulnerability to the damage type;\n});\n```\n")
    public PartBuilder<T> isInvulnerableTo(Function<ContextUtils.EDamageContext, Object> predicate) {
        this.isInvulnerableTo = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can change dimensions.\nThe provided Predicate accepts a {@link Entity} parameter,\nrepresenting the entity that may attempt to change dimensions.\n\nExample usage:\n```javascript\nentityBuilder.canChangeDimensions(entity => {\n    // Define the conditions for the entity to be able to change dimensions\n    // Use information about the Entity provided by the context.\n    return false // Some boolean condition indicating if the entity can change dimensions;\n});\n```\n")
    public PartBuilder<T> canChangeDimensions(Function<Entity, Object> supplier) {
        this.canChangeDimensions = supplier;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity may interact with something.\nThe provided Predicate accepts a {@link ContextUtils.MayInteractContext} parameter,\nrepresenting the context of the potential interaction, and returns a boolean.\n\nExample usage:\n```javascript\nentityBuilder.mayInteract(context => {\n    // Define conditions for the entity to be allowed to interact\n    // Use information about the MayInteractContext provided by the context.\n    return false // Some boolean condition indicating if the entity may interact;\n});\n```\n")
    public PartBuilder<T> mayInteract(Function<ContextUtils.EMayInteractContext, Object> predicate) {
        this.mayInteract = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can trample or step on something.\nThe provided Predicate accepts a {@link ContextUtils.CanTrampleContext} parameter,\nrepresenting the context of the potential trampling action, and returns a boolean.\n\nExample usage:\n```javascript\nentityBuilder.canTrample(context => {\n    // Define conditions for the entity to be allowed to trample\n    // Use information about the CanTrampleContext provided by the context.\n    return false // Some boolean condition indicating if the entity can trample;\n});\n```\n")
    public PartBuilder<T> canTrample(Function<ContextUtils.ECanTrampleContext, Object> predicate) {
        this.canTrample = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is removed from the world.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that is being removed from the world.\n\nExample usage:\n```javascript\nentityBuilder.onRemovedFromWorld(entity => {\n    // Define custom logic for handling the removal of the entity from the world\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public PartBuilder<T> onRemovedFromWorld(Consumer<Entity> consumer) {
        this.onRemovedFromWorld = consumer;
        return this;
    }
}
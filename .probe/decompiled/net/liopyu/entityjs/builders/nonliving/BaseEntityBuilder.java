package net.liopyu.entityjs.builders.nonliving;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import net.liopyu.entityjs.entities.nonliving.entityjs.IAnimatableJSNL;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.core.keyframe.event.KeyFrameEvent;
import software.bernie.geckolib.core.keyframe.event.ParticleKeyframeEvent;
import software.bernie.geckolib.core.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.core.keyframe.event.data.KeyFrameData;
import software.bernie.geckolib.core.object.DataTicket;
import software.bernie.geckolib.core.object.PlayState;

public abstract class BaseEntityBuilder<T extends Entity & IAnimatableJSNL> extends BuilderBase<EntityType<T>> {

    public transient Consumer<ContextUtils.LerpToContext> lerpTo;

    public transient Consumer<ContextUtils.EntityPlayerContext> playerTouch;

    public transient Function<ContextUtils.EntitySqrDistanceContext, Object> shouldRenderAtSqrDistance;

    public transient Consumer<Entity> tick;

    public transient Consumer<ContextUtils.MovementContext> move;

    public transient Boolean isAttackable;

    public transient float width;

    public transient float height;

    public transient int clientTrackingRange;

    public transient int updateInterval;

    public transient MobCategory mobCategory;

    public transient Function<T, Object> modelResource;

    public transient Function<T, Object> textureResource;

    public transient Function<T, Object> animationResource;

    public transient BaseEntityBuilder.RenderType renderType;

    public final transient List<BaseEntityBuilder.AnimationControllerSupplier<T>> animationSuppliers;

    public static final List<BaseEntityBuilder<?>> thisList = new ArrayList();

    public transient Consumer<ContextUtils.NLRenderContext<T>> render;

    public transient boolean isPickable;

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

    public transient Consumer<ContextUtils.EntityHurtContext> onHurt;

    public transient boolean summonable;

    public transient boolean save;

    public transient boolean fireImmune;

    public transient ResourceLocation[] immuneTo;

    public transient boolean spawnFarFromPlayer;

    public transient float scaleHeight;

    public transient float scaleWidth;

    public transient Consumer<ContextUtils.ScaleModelRenderContextNL<T>> scaleModelForRender;

    public transient Consumer<ContextUtils.PositionRiderContext> positionRider;

    public BaseEntityBuilder(ResourceLocation i) {
        super(i);
        thisList.add(this);
        this.width = 1.0F;
        this.height = 1.0F;
        this.summonable = true;
        this.save = true;
        this.immuneTo = new ResourceLocation[0];
        this.fireImmune = false;
        this.spawnFarFromPlayer = false;
        this.clientTrackingRange = 5;
        this.updateInterval = 1;
        this.mobCategory = MobCategory.MISC;
        this.isAttackable = true;
        this.renderType = BaseEntityBuilder.RenderType.CUTOUT;
        this.animationSuppliers = new ArrayList();
        this.modelResource = t -> this.newID("geo/entity/", ".geo.json");
        this.textureResource = t -> this.newID("textures/entity/", ".png");
        this.animationResource = t -> this.newID("animations/entity/", ".animation.json");
        this.scaleHeight = 1.0F;
        this.scaleWidth = 1.0F;
    }

    @Info("Boolean determining if the part entity is pickable.\n\nExample usage:\n```javascript\nentityBuilder.isPickable(true)\n```\n")
    public BaseEntityBuilder<T> isPickable(boolean isPickable) {
        this.isPickable = isPickable;
        return this;
    }

    @Info("Function determining if the entity may collide with another entity\nusing the ContextUtils.CollidingEntityContext which has this entity and the\none colliding with this entity.\n\nExample usage:\n```javascript\nentityBuilder.canCollideWith(context => {\n    return true //Some Boolean value determining whether the entity may collide with another\n});\n```\n")
    public BaseEntityBuilder<T> canCollideWith(Function<ContextUtils.ECollidingEntityContext, Object> canCollideWith) {
        this.canCollideWith = canCollideWith;
        return this;
    }

    @Info("Defines in what condition the entity will start freezing.\n\nExample usage:\n```javascript\nentityBuilder.isFreezing(entity => {\n    return true;\n});\n```\n")
    public BaseEntityBuilder<T> isFreezing(Function<Entity, Object> isFreezing) {
        this.isFreezing = isFreezing;
        return this;
    }

    @Info("Sets the block jump factor for the entity.\n\nExample usage:\n```javascript\nentityBuilder.setBlockJumpFactor(entity => {\n    //Set the jump factor for the entity through context\n    return 1 //some float value;\n});\n```\n")
    public BaseEntityBuilder<T> setBlockJumpFactor(Function<Entity, Object> blockJumpFactor) {
        this.setBlockJumpFactor = blockJumpFactor;
        return this;
    }

    @Info("Sets whether the entity is pushable.\n\nExample usage:\n```javascript\nentityBuilder.isPushable(true);\n```\n")
    public BaseEntityBuilder<T> isPushable(boolean b) {
        this.isPushable = b;
        return this;
    }

    @Info("@param positionRider A consumer determining the position of rider/riders.\n\n    Example usage:\n    ```javascript\n    entityBuilder.positionRider(context => {\n        const {entity, passenger, moveFunction} = context\n    });\n    ```\n")
    public BaseEntityBuilder<T> positionRider(Consumer<ContextUtils.PositionRiderContext> builderConsumer) {
        this.positionRider = builderConsumer;
        return this;
    }

    @Info("Sets a predicate to determine if a passenger can be added to the entity.\n\n@param predicate The predicate to check if a passenger can be added.\n\nExample usage:\n```javascript\nentityBuilder.canAddPassenger(context => {\n    // Custom logic to determine if a passenger can be added to the entity\n    return true;\n});\n```\n")
    public BaseEntityBuilder<T> canAddPassenger(Function<ContextUtils.EPassengerEntityContext, Object> predicate) {
        this.canAddPassenger = predicate;
        return this;
    }

    @Info("Sets the swim sound for the entity using a string representation.\n\nExample usage:\n```javascript\nentityBuilder.setSwimSound(\"minecraft:entity.generic.swim\");\n```\n")
    public BaseEntityBuilder<T> setSwimSound(Object sound) {
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
    public BaseEntityBuilder<T> setSwimSplashSound(Object sound) {
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
    public BaseEntityBuilder<T> blockSpeedFactor(Function<Entity, Object> callback) {
        this.blockSpeedFactor = callback;
        return this;
    }

    @Info("Sets a function to determine whether the entity is currently flapping.\nThe provided Function accepts a {@link Entity} parameter,\nrepresenting the entity whose flapping status is being determined.\nIt returns a Boolean indicating whether the entity is flapping.\n\nExample usage:\n```javascript\nentityBuilder.isFlapping(entity => {\n    // Define logic to determine whether the entity is currently flapping\n    // Use information about the Entity provided by the context.\n    return // Some Boolean value indicating whether the entity is flapping;\n});\n```\n")
    public BaseEntityBuilder<T> isFlapping(Function<Entity, Object> b) {
        this.isFlapping = b;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is added to the world.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that is added to the world.\n\nExample usage:\n```javascript\nentityBuilder.onAddedToWorld(entity => {\n    // Define custom logic for handling when the entity is added to the world\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public BaseEntityBuilder<T> onAddedToWorld(Consumer<Entity> onAddedToWorldCallback) {
        this.onAddedToWorld = onAddedToWorldCallback;
        return this;
    }

    @Info("Sets whether to reposition the entity after loading.\n\nExample usage:\n```javascript\nentityBuilder.repositionEntityAfterLoad(true);\n```\n")
    public BaseEntityBuilder<T> repositionEntityAfterLoad(boolean customRepositionEntityAfterLoad) {
        this.repositionEntityAfterLoad = customRepositionEntityAfterLoad;
        return this;
    }

    @Info("Sets a function to determine the next step distance for the entity.\nThe provided Function accepts a {@link Entity} parameter,\nrepresenting the entity whose next step distance is being determined.\nIt returns a Float representing the next step distance.\n\nExample usage:\n```javascript\nentityBuilder.nextStep(entity => {\n    // Define logic to calculate and return the next step distance for the entity\n    // Use information about the Entity provided by the context.\n    return // Some Float value representing the next step distance;\n});\n```\n")
    public BaseEntityBuilder<T> nextStep(Function<Entity, Object> nextStep) {
        this.nextStep = nextStep;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity starts sprinting.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that has started sprinting.\n\nExample usage:\n```javascript\nentityBuilder.onSprint(entity => {\n    // Define custom logic for handling when the entity starts sprinting\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public BaseEntityBuilder<T> onSprint(Consumer<Entity> consumer) {
        this.onSprint = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity stops riding.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that has stopped being ridden.\n\nExample usage:\n```javascript\nentityBuilder.onStopRiding(entity => {\n    // Define custom logic for handling when the entity stops being ridden\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public BaseEntityBuilder<T> onStopRiding(Consumer<Entity> callback) {
        this.onStopRiding = callback;
        return this;
    }

    @Info("Sets a callback function to be executed during each tick when the entity is being ridden.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that is being ridden.\n\nExample usage:\n```javascript\nentityBuilder.rideTick(entity => {\n    // Define custom logic for handling each tick when the entity is being ridden\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public BaseEntityBuilder<T> rideTick(Consumer<Entity> callback) {
        this.rideTick = callback;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is attackable.\nThe provided Predicate accepts a {@link Entity} parameter,\nrepresenting the entity that may be checked for its attackability.\n\nExample usage:\n```javascript\nentityBuilder.isAttackable(entity => {\n    // Define conditions to check if the entity is attackable\n    // Use information about the Entity provided by the context.\n    return // Some boolean condition indicating if the entity is attackable;\n});\n```\n")
    public BaseEntityBuilder<T> isAttackable(Boolean predicate) {
        this.isAttackable = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can undergo freezing.\nThe provided Predicate accepts a {@link Entity} parameter,\nrepresenting the entity that may be subjected to freezing.\n\nExample usage:\n```javascript\nentityBuilder.canFreeze(entity => {\n    // Define the conditions for the entity to be able to freeze\n    // Use information about the Entity provided by the context.\n    return true //someBoolean;\n});\n```\n")
    public BaseEntityBuilder<T> canFreeze(Function<Entity, Object> predicate) {
        this.canFreeze = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is currently glowing.\nThe provided Predicate accepts a {@link Entity} parameter,\nrepresenting the entity that may be checked for its glowing state.\n\nExample usage:\n```javascript\nentityBuilder.isCurrentlyGlowing(entity => {\n    // Define the conditions to check if the entity is currently glowing\n    // Use information about the Entity provided by the context.\n    const isGlowing = // Some boolean condition to check if the entity is glowing;\n    return isGlowing;\n});\n```\n")
    public BaseEntityBuilder<T> isCurrentlyGlowing(Function<Entity, Object> predicate) {
        this.isCurrentlyGlowing = predicate;
        return this;
    }

    @Info("Sets the minimum fall distance for the entity before taking damage.\n\nExample usage:\n```javascript\nentityBuilder.setMaxFallDistance(entity => {\n    // Define custom logic to determine the maximum fall distance\n    // Use information about the Entity provided by the context.\n    return 3;\n});\n```\n")
    public BaseEntityBuilder<T> setMaxFallDistance(Function<Entity, Object> maxFallDistance) {
        this.setMaxFallDistance = maxFallDistance;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is removed on the client side.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that is being removed on the client side.\n\nExample usage:\n```javascript\nentityBuilder.onClientRemoval(entity => {\n    // Define custom logic for handling the removal of the entity on the client side\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public BaseEntityBuilder<T> onClientRemoval(Consumer<Entity> consumer) {
        this.onClientRemoval = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is hurt by lava.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that is affected by lava.\n\nExample usage:\n```javascript\nentityBuilder.lavaHurt(entity => {\n    // Define custom logic for handling the entity being hurt by lava\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public BaseEntityBuilder<T> lavaHurt(Consumer<Entity> consumer) {
        this.lavaHurt = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity performs a flap action.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that is flapping.\n\nExample usage:\n```javascript\nentityBuilder.onFlap(entity => {\n    // Define custom logic for handling the entity's flap action\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public BaseEntityBuilder<T> onFlap(Consumer<Entity> consumer) {
        this.onFlap = consumer;
        return this;
    }

    @Info("Sets a predicate to determine whether the living entity dampens vibrations.\n\n@param predicate The predicate to determine whether the living entity dampens vibrations.\n\nThe predicate should take a Entity as a parameter and return a boolean value indicating whether the living entity dampens vibrations.\n\nExample usage:\n```javascript\nbaseEntityBuilder.dampensVibrations(entity => {\n    // Determine whether the living entity dampens vibrations\n    // Return true if the entity dampens vibrations, false otherwise\n});\n```\n")
    public BaseEntityBuilder<T> dampensVibrations(Function<Entity, Object> predicate) {
        this.dampensVibrations = predicate;
        return this;
    }

    @Info("Sets a predicate to determine whether to show the vehicle health for the living entity.\n\n@param predicate The predicate to determine whether to show the vehicle health.\n\nThe predicate should take a Entity as a parameter and return a boolean value indicating whether to show the vehicle health.\n\nExample usage:\n```javascript\nbaseEntityBuilder.showVehicleHealth(entity => {\n    // Determine whether to show the vehicle health for the living entity\n    // Return true to show the vehicle health, false otherwise\n});\n```\n")
    public BaseEntityBuilder<T> showVehicleHealth(Function<Entity, Object> predicate) {
        this.showVehicleHealth = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is hit by thunder.\nThe provided Consumer accepts a {@link ContextUtils.ThunderHitContext} parameter,\nrepresenting the context of the entity being hit by thunder.\n\nExample usage:\n```javascript\nentityBuilder.thunderHit(context => {\n    // Define custom logic for handling the entity being hit by thunder\n    // Use information about the ThunderHitContext provided by the context.\n});\n```\n")
    public BaseEntityBuilder<T> thunderHit(Consumer<ContextUtils.EThunderHitContext> consumer) {
        this.thunderHit = consumer;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is invulnerable to a specific type of damage.\nThe provided Predicate accepts a {@link ContextUtils.DamageContext} parameter,\nrepresenting the context of the damage, and returns a boolean indicating invulnerability.\n\nExample usage:\n```javascript\nentityBuilder.isInvulnerableTo(context => {\n    // Define conditions for the entity to be invulnerable to the specific type of damage\n    // Use information about the DamageContext provided by the context.\n    return true // Some boolean condition indicating if the entity has invulnerability to the damage type;\n});\n```\n")
    public BaseEntityBuilder<T> isInvulnerableTo(Function<ContextUtils.EDamageContext, Object> predicate) {
        this.isInvulnerableTo = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can change dimensions.\nThe provided Predicate accepts a {@link Entity} parameter,\nrepresenting the entity that may attempt to change dimensions.\n\nExample usage:\n```javascript\nentityBuilder.canChangeDimensions(entity => {\n    // Define the conditions for the entity to be able to change dimensions\n    // Use information about the Entity provided by the context.\n    return false // Some boolean condition indicating if the entity can change dimensions;\n});\n```\n")
    public BaseEntityBuilder<T> canChangeDimensions(Function<Entity, Object> supplier) {
        this.canChangeDimensions = supplier;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity may interact with something.\nThe provided Predicate accepts a {@link ContextUtils.MayInteractContext} parameter,\nrepresenting the context of the potential interaction, and returns a boolean.\n\nExample usage:\n```javascript\nentityBuilder.mayInteract(context => {\n    // Define conditions for the entity to be allowed to interact\n    // Use information about the MayInteractContext provided by the context.\n    return false // Some boolean condition indicating if the entity may interact;\n});\n```\n")
    public BaseEntityBuilder<T> mayInteract(Function<ContextUtils.EMayInteractContext, Object> predicate) {
        this.mayInteract = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can trample or step on something.\nThe provided Predicate accepts a {@link ContextUtils.CanTrampleContext} parameter,\nrepresenting the context of the potential trampling action, and returns a boolean.\n\nExample usage:\n```javascript\nentityBuilder.canTrample(context => {\n    // Define conditions for the entity to be allowed to trample\n    // Use information about the CanTrampleContext provided by the context.\n    return false // Some boolean condition indicating if the entity can trample;\n});\n```\n")
    public BaseEntityBuilder<T> canTrample(Function<ContextUtils.ECanTrampleContext, Object> predicate) {
        this.canTrample = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is removed from the world.\nThe provided Consumer accepts a {@link Entity} parameter,\nrepresenting the entity that is being removed from the world.\n\nExample usage:\n```javascript\nentityBuilder.onRemovedFromWorld(entity => {\n    // Define custom logic for handling the removal of the entity from the world\n    // Use information about the Entity provided by the context.\n});\n```\n")
    public BaseEntityBuilder<T> onRemovedFromWorld(Consumer<Entity> consumer) {
        this.onRemovedFromWorld = consumer;
        return this;
    }

    @Info("Sets the scale of the model.\n\nExample usage:\n```javascript\nentityBuilder.modelSize(2,2);\n```\n")
    public BaseEntityBuilder<T> modelSize(float scaleHeight, float scaleWidth) {
        this.scaleHeight = scaleHeight;
        this.scaleWidth = scaleWidth;
        return this;
    }

    @Info("@param scaleModelForRender A Consumer to determing logic for model scaling and rendering\n    without affecting core logic such as hitbox sizing.\n\nExample usage:\n```javascript\nentityBuilder.scaleModelForRender(context => {\n    const { entity, widthScale, heightScale, poseStack, model, isReRender, partialTick, packedLight, packedOverlay } = context\n    poseStack.scale(0.5, 0.5, 0.5)\n});\n```\n")
    public BaseEntityBuilder<T> scaleModelForRender(Consumer<ContextUtils.ScaleModelRenderContextNL<T>> scaleModelForRender) {
        this.scaleModelForRender = scaleModelForRender;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity falls and takes damage.\nThe provided Consumer accepts a {@link ContextUtils.EEntityFallDamageContext} parameter,\nrepresenting the context of the entity falling and taking fall damage.\n\nExample usage:\n```javascript\nentityBuilder.onFall(context => {\n    // Define custom logic for handling when the entity falls and takes damage\n    // Use information about the EEntityFallDamageContext provided by the context.\n});\n```\n")
    public BaseEntityBuilder<T> onFall(Consumer<ContextUtils.EEntityFallDamageContext> c) {
        this.onFall = c;
        return this;
    }

    @Info("Sets the list of block names to which the entity is immune.\n\nExample usage:\n```javascript\nentityBuilder.immuneTo(\"minecraft:stone\", \"minecraft:dirt\");\n```\n")
    public BaseEntityBuilder<T> immuneTo(String... blockNames) {
        this.immuneTo = (ResourceLocation[]) Arrays.stream(blockNames).map(ResourceLocation::new).toArray(ResourceLocation[]::new);
        return this;
    }

    @Info("Sets whether the entity can spawn far from the player.\n\nExample usage:\n```javascript\nentityBuilder.canSpawnFarFromPlayer(true);\n```\n")
    public BaseEntityBuilder<T> canSpawnFarFromPlayer(boolean canSpawnFar) {
        this.spawnFarFromPlayer = canSpawnFar;
        return this;
    }

    @Info("Defines logic to render the entity.\n\nExample usage:\n```javascript\nentityBuilder.render(context => {\n    // Define logic to render the entity\n    context.poseStack.scale(0.5, 0.5, 0.5);\n});\n```\n")
    public BaseEntityBuilder<T> render(Consumer<ContextUtils.NLRenderContext<T>> render) {
        this.render = render;
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

    @Info("Sets whether the entity is summonable.\n\nExample usage:\n```javascript\nentityBuilder.setSummonable(true);\n```\n")
    public BaseEntityBuilder<T> setSummonable(boolean b) {
        this.summonable = b;
        return this;
    }

    @Info("Sets the mob category for the entity.\nAvailable options: 'monster', 'creature', 'ambient', 'water_creature', 'misc'.\nDefaults to 'misc'.\n\nExample usage:\n```javascript\nentityBuilder.mobCategory('monster');\n```\n")
    public BaseEntityBuilder<T> mobCategory(String category) {
        this.mobCategory = stringToMobCategory(category);
        return this;
    }

    @Info("Sets a function to determine the model resource for the entity.\nThe provided Function accepts a parameter of type T (the entity),\nallowing changing the model based on information about the entity.\nThe default behavior returns <namespace>:geo/entity/<path>.geo.json.\n\nExample usage:\n```javascript\nentityBuilder.modelResource(entity => {\n    // Define logic to determine the model resource for the entity\n    // Use information about the entity provided by the context.\n    return \"kubejs:geo/entity/wyrm.geo.json\" // Some ResourceLocation representing the model resource;\n});\n```\n")
    public BaseEntityBuilder<T> modelResource(Function<T, Object> function) {
        this.modelResource = entity -> {
            Object obj = function.apply(entity);
            if (obj instanceof String && !obj.toString().equals("undefined")) {
                return new ResourceLocation((String) obj);
            } else if (obj instanceof ResourceLocation) {
                return (ResourceLocation) obj;
            } else {
                EntityJSHelperClass.logWarningMessageOnce("Invalid model resource: " + obj + "Defaulting to " + ((IAnimatableJSNL) entity).getBuilder().newID("geo/entity/", ".geo.json"));
                return ((IAnimatableJSNL) entity).getBuilder().newID("geo/entity/", ".geo.json");
            }
        };
        return this;
    }

    @Info("Determines if the entity should serialize its data. Defaults to true.\n\nExample usage:\n```javascript\nentityBuilder.saves(false);\n```\n")
    public BaseEntityBuilder<T> saves(boolean shouldSave) {
        this.save = shouldSave;
        return this;
    }

    @Info("Sets whether the entity is immune to fire damage.\n\nExample usage:\n```javascript\nentityBuilder.fireImmune(true);\n```\n")
    public BaseEntityBuilder<T> fireImmune(boolean isFireImmune) {
        this.fireImmune = isFireImmune;
        return this;
    }

    @Info("Sets a function to determine the texture resource for the entity.\nThe provided Function accepts a parameter of type T (the entity),\nallowing changing the texture based on information about the entity.\nThe default behavior returns <namespace>:textures/entity/<path>.png.\n\nExample usage:\n```javascript\nentityBuilder.textureResource(entity => {\n    // Define logic to determine the texture resource for the entity\n    // Use information about the entity provided by the context.\n    return \"kubejs:textures/entity/wyrm.png\" // Some ResourceLocation representing the texture resource;\n});\n```\n")
    public BaseEntityBuilder<T> textureResource(Function<T, Object> function) {
        this.textureResource = entity -> {
            Object obj = function.apply(entity);
            if (obj instanceof String && !obj.toString().equals("undefined")) {
                return new ResourceLocation((String) obj);
            } else if (obj instanceof ResourceLocation) {
                return (ResourceLocation) obj;
            } else {
                EntityJSHelperClass.logWarningMessageOnce("Invalid texture resource: " + obj + "Defaulting to " + ((IAnimatableJSNL) entity).getBuilder().newID("textures/entity/", ".png"));
                return ((IAnimatableJSNL) entity).getBuilder().newID("textures/entity/", ".png");
            }
        };
        return this;
    }

    @Info("Sets a function to determine the animation resource for the entity.\nThe provided Function accepts a parameter of type T (the entity),\nallowing changing the animations based on information about the entity.\nThe default behavior returns <namespace>:animations/<path>.animation.json.\n\nExample usage:\n```javascript\nentityBuilder.animationResource(entity => {\n    // Define logic to determine the animation resource for the entity\n    // Use information about the entity provided by the context.\n    //return some ResourceLocation representing the animation resource;\n    return \"kubejs:animations/entity/wyrm.animation.json\" // Some ResourceLocation representing the animation resource;\n});\n```\n")
    public BaseEntityBuilder<T> animationResource(Function<T, Object> function) {
        this.animationResource = entity -> {
            Object obj = function.apply(entity);
            if (obj instanceof String && !obj.toString().equals("undefined")) {
                return new ResourceLocation((String) obj);
            } else if (obj instanceof ResourceLocation) {
                return (ResourceLocation) obj;
            } else {
                EntityJSHelperClass.logWarningMessageOnce("Invalid animation resource: " + obj + ". Defaulting to " + ((IAnimatableJSNL) entity).getBuilder().newID("animations/entity/", ".animation.json"));
                return ((IAnimatableJSNL) entity).getBuilder().newID("animations/entity/", ".animation.json");
            }
        };
        return this;
    }

    @Info("Sets the hit box of the entity type.\n\n@param width The width of the entity. Defaults to 0.5.\n@param height The height of the entity. Defaults to 0.5.\n\nExample usage:\n```javascript\nentityBuilder.sized(1.0f, 1.5f);\n```\n")
    public BaseEntityBuilder<T> sized(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    @Info("Sets the client tracking range. Defaults to 5.\n\n@param trackingRange The client tracking range.\n\nExample usage:\n```javascript\nentityBuilder.clientTrackingRange(8);\n```\n")
    public BaseEntityBuilder<T> clientTrackingRange(int trackingRange) {
        this.clientTrackingRange = trackingRange;
        return this;
    }

    @Info("Sets the update interval in ticks of the entity.\nDefaults to 1 tick.\n\n@param updateInterval The update interval in ticks.\n\nExample usage:\n```javascript\nentityBuilder.updateInterval(5);\n```\n")
    public BaseEntityBuilder<T> updateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
        return this;
    }

    @Info("Sets a consumer to handle lerping (linear interpolation) of the entity's position.\n\n@param lerpTo Consumer accepting a {@link ContextUtils.LerpToContext} parameter,\n                providing information and control over the lerping process.\n\nExample usage:\n```javascript\nentityBuilder.lerpTo(context => {\n    // Custom logic for lerping the entity's position\n    // Access information about the lerping process using the provided context.\n});\n```\n")
    public BaseEntityBuilder<T> lerpTo(Consumer<ContextUtils.LerpToContext> consumer) {
        this.lerpTo = consumer;
        return this;
    }

    @Info("Sets a function to determine whether the entity should render at a squared distance.\n\n@param shouldRenderAtSqrDistance Function accepting a {@link ContextUtils.EntitySqrDistanceContext} parameter,\n                 defining the conditions under which the entity should render.\n\nExample usage:\n```javascript\nentityBuilder.shouldRenderAtSqrDistance(context => {\n    // Custom logic to determine whether the entity should render\n    // Access information about the distance using the provided context.\n    return true;\n});\n```\n")
    public BaseEntityBuilder<T> shouldRenderAtSqrDistance(Function<ContextUtils.EntitySqrDistanceContext, Object> func) {
        this.shouldRenderAtSqrDistance = func;
        return this;
    }

    @Info("Sets whether the entity is attackable or not.\n\n@param isAttackable Boolean value indicating whether the entity is attackable.\n\nExample usage:\n```javascript\nentityBuilder.isAttackable(true);\n```\n")
    public BaseEntityBuilder<T> isAttackable(boolean b) {
        this.isAttackable = b;
        return this;
    }

    @Info("Sets a callback function to be executed when a player touches the entity.\nThe provided Consumer accepts a {@link ContextUtils.EntityPlayerContext} parameter,\nrepresenting the context of the player's interaction with the entity.\n\nExample usage:\n```javascript\nentityBuilder.playerTouch(context => {\n    // Custom logic to handle the player's touch interaction with the entity\n    // Access information about the interaction using the provided context.\n});\n```\n")
    public BaseEntityBuilder<T> playerTouch(Consumer<ContextUtils.EntityPlayerContext> consumer) {
        this.playerTouch = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity performs a movement action.\nThe provided Consumer accepts a {@link ContextUtils.MovementContext} parameter,\nrepresenting the context of the entity's movement.\n\nExample usage:\n```javascript\nentityBuilder.move(context => {\n    // Custom logic to handle the entity's movement action\n    // Access information about the movement using the provided context.\n});\n```\n")
    public BaseEntityBuilder<T> move(Consumer<ContextUtils.MovementContext> consumer) {
        this.move = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed on each tick for the entity.\n\n@param tick A Consumer accepting a {@link Entity} parameter, defining the behavior to be executed on each tick.\n\nExample usage:\n```javascript\nentityBuilder.tick(entity => {\n    // Custom logic to be executed on each tick of the entity.\n    // Access information about the entity using the provided parameter.\n});\n```\n")
    public BaseEntityBuilder<T> tick(Consumer<Entity> consumer) {
        this.tick = consumer;
        return this;
    }

    @Override
    public RegistryInfo getRegistryType() {
        return RegistryInfo.ENTITY_TYPE;
    }

    @Info("Adds an animation controller to the entity with the specified parameters.\n\n@param name The name of the animation controller.\n@param translationTicksLength The length of translation ticks for the animation.\n@param predicate The animation predicate defining the conditions for the animation to be played.\n\nExample usage:\n```javascript\nentityBuilder.addAnimationController('exampleController', 5, event => {\n    // Define conditions for the animation to be played based on the entity.\n    if (event.entity.hurtTime > 0) {\n        event.thenLoop('spawn');\n    } else {\n        event.thenPlayAndHold('idle');\n    }\n    return true; // Some boolean condition indicating if the animation should be played;\n});\n```\n")
    public BaseEntityBuilder<T> addAnimationController(String name, int translationTicksLength, BaseEntityBuilder.IAnimationPredicateJS<T> predicate) {
        return this.addKeyAnimationController(name, translationTicksLength, predicate, null, null, null);
    }

    @Info(value = "Adds a new AnimationController to the entity, with the ability to add event listeners", params = { @Param(name = "name", value = "The name of the controller"), @Param(name = "translationTicksLength", value = "How many ticks it takes to transition between different animations"), @Param(name = "predicate", value = "The predicate for the controller, determines if an animation should continue or not"), @Param(name = "soundListener", value = "A sound listener, used to execute actions when the json requests a sound to play. May be null"), @Param(name = "particleListener", value = "A particle listener, used to execute actions when the json requests a particle. May be null"), @Param(name = "instructionListener", value = "A custom instruction listener, used to execute actions based on arbitrary instructions provided by the json. May be null") })
    public BaseEntityBuilder<T> addKeyAnimationController(String name, int translationTicksLength, BaseEntityBuilder.IAnimationPredicateJS<T> predicate, @Nullable BaseEntityBuilder.ISoundListenerJS<T> soundListener, @Nullable BaseEntityBuilder.IParticleListenerJS<T> particleListener, @Nullable BaseEntityBuilder.ICustomInstructionListenerJS<T> instructionListener) {
        this.animationSuppliers.add(new BaseEntityBuilder.AnimationControllerSupplier<>(name, translationTicksLength, predicate, null, null, null, soundListener, particleListener, instructionListener));
        return this;
    }

    @Info("Sets the render type for the entity.\n\n@param type The render type to be set. Acceptable values are:\n             - \"solid\n             - \"cutout\"\n             - \"translucent\"\n             - RenderType.SOLID\n             - RenderType.CUTOUT\n             - RenderType.TRANSLUCENT\n\nExample usage:\n```javascript\nentityBuilder.setRenderType(\"translucent\");\n```\n")
    public BaseEntityBuilder<T> setRenderType(Object type) {
        if (type instanceof BaseEntityBuilder.RenderType) {
            this.renderType = (BaseEntityBuilder.RenderType) type;
        } else {
            if (!(type instanceof String typeString)) {
                throw new IllegalArgumentException("Invalid render type: " + type);
            }
            String var3 = typeString.toLowerCase();
            switch(var3) {
                case "solid":
                    this.renderType = BaseEntityBuilder.RenderType.SOLID;
                    break;
                case "cutout":
                    this.renderType = BaseEntityBuilder.RenderType.CUTOUT;
                    break;
                case "translucent":
                    this.renderType = BaseEntityBuilder.RenderType.TRANSLUCENT;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid render type string: " + typeString);
            }
        }
        return this;
    }

    public EntityType<T> createObject() {
        return new EntityTypeBuilder<T>(this).get();
    }

    @HideFromJS
    public abstract EntityType.EntityFactory<T> factory();

    @HideFromJS
    public abstract AttributeSupplier.Builder getAttributeBuilder();

    @Info(value = "Adds a triggerable AnimationController to the entity callable off the entity's methods anywhere.", params = { @Param(name = "name", value = "The name of the controller"), @Param(name = "translationTicksLength", value = "How many ticks it takes to transition between different animations"), @Param(name = "triggerableAnimationID", value = "The unique identifier of the triggerable animation(sets it apart from other triggerable animations)"), @Param(name = "triggerableAnimationName", value = "The name of the animation defined in the animations.json"), @Param(name = "loopType", value = "The loop type for the triggerable animation, either 'LOOP' or 'PLAY_ONCE' or 'HOLD_ON_LAST_FRAME' or 'DEFAULT'") })
    public BaseEntityBuilder<T> addTriggerableAnimationController(String name, int translationTicksLength, String triggerableAnimationName, String triggerableAnimationID, String loopType) {
        this.animationSuppliers.add(new BaseEntityBuilder.AnimationControllerSupplier<>(name, translationTicksLength, new BaseEntityBuilder.IAnimationPredicateJS<T>() {

            @Override
            public boolean test(BaseEntityBuilder.AnimationEventJS<T> event) {
                return true;
            }
        }, triggerableAnimationName, triggerableAnimationID, loopType, null, null, null));
        return this;
    }

    public static record AnimationControllerSupplier<E extends Entity & IAnimatableJSNL>(String name, int translationTicksLength, BaseEntityBuilder.IAnimationPredicateJS<E> predicate, String triggerableAnimationName, String triggerableAnimationID, Object loopType, @Nullable BaseEntityBuilder.ISoundListenerJS<E> soundListener, @Nullable BaseEntityBuilder.IParticleListenerJS<E> particleListener, @Nullable BaseEntityBuilder.ICustomInstructionListenerJS<E> instructionListener) {

        public AnimationController<E> get(E entity) {
            AnimationController<E> controller = new AnimationController<>(entity, this.name, this.translationTicksLength, this.predicate.toGecko());
            if (this.triggerableAnimationID != null) {
                Object type = EntityJSHelperClass.convertObjectToDesired(this.loopType, "looptype");
                controller.triggerableAnim(this.triggerableAnimationID, RawAnimation.begin().then(this.triggerableAnimationName, (Animation.LoopType) type));
            }
            if (this.soundListener != null) {
                controller.setSoundKeyframeHandler(event -> this.soundListener.playSound(new BaseEntityBuilder.SoundKeyFrameEventJS<>(event)));
            }
            if (this.particleListener != null) {
                controller.setParticleKeyframeHandler(event -> this.particleListener.summonParticle(new BaseEntityBuilder.ParticleKeyFrameEventJS<>(event)));
            }
            if (this.instructionListener != null) {
                controller.setCustomInstructionKeyframeHandler(event -> this.instructionListener.executeInstruction(new BaseEntityBuilder.CustomInstructionKeyframeEventJS<>(event)));
            }
            return controller;
        }
    }

    public static class AnimationEventJS<E extends Entity & IAnimatableJSNL> {

        private final List<RawAnimation.Stage> animationList = new ObjectArrayList();

        private final AnimationState<E> parent;

        public AnimationEventJS(AnimationState<E> parent) {
            this.parent = parent;
        }

        @Info("Returns the number of ticks the entity has been animating for")
        public double getAnimationTick() {
            return this.parent.getAnimationTick();
        }

        @Info("Returns the entity that is being animated")
        public E getEntity() {
            return this.parent.getAnimatable();
        }

        public float getLimbSwing() {
            return this.parent.getLimbSwing();
        }

        public float getLimbSwingAmount() {
            return this.parent.getLimbSwingAmount();
        }

        @Info("Returns a number, in the range [0, 1], how far through the tick it currently is")
        public float getPartialTick() {
            return this.parent.getPartialTick();
        }

        @Info("If the entity is moving")
        public boolean isMoving() {
            return this.parent.isMoving();
        }

        @Info("Returns the animation controller this event is part of")
        public AnimationController<E> getController() {
            return this.parent.getController();
        }

        @Info("Sets a triggerable animation with a specified loop type callable anywhere from the entity.\n\n@param animationName The name of the animation to be triggered, this is the animation named in the json.\n@param triggerableAnimationID The unique identifier for the triggerable animation.\n@param loopTypeEnum The loop type for the triggerable animation. Accepts 'LOOP', 'PLAY_ONCE', 'HOLD_ON_LAST_FRAME', or 'DEFAULT'.\n```javascript\n event.addTriggerableAnimation('spawn', 'spawning', 'default')\n ```\n")
        public PlayState addTriggerableAnimation(String animationName, String triggerableAnimationID, Object loopTypeEnum) {
            Object type = EntityJSHelperClass.convertObjectToDesired(loopTypeEnum, "looptype");
            this.parent.getController().triggerableAnim(triggerableAnimationID, RawAnimation.begin().then(animationName, (Animation.LoopType) type));
            return PlayState.CONTINUE;
        }

        @Info("Sets an animation to play defaulting to the animations.json file loop type")
        public PlayState thenPlay(String animationName) {
            this.parent.getController().setAnimation(RawAnimation.begin().then(animationName, Animation.LoopType.DEFAULT));
            return PlayState.CONTINUE;
        }

        @Info("Sets an animation to play in a loop")
        public PlayState thenLoop(String animationName) {
            this.parent.getController().setAnimation(RawAnimation.begin().thenLoop(animationName));
            return PlayState.CONTINUE;
        }

        @Info("Wait a certain amount of ticks before starting the next animation")
        public PlayState thenWait(int ticks) {
            this.parent.getController().setAnimation(RawAnimation.begin().thenWait(ticks));
            return PlayState.CONTINUE;
        }

        @Info("Sets an animation to play and hold on the last frame")
        public PlayState thenPlayAndHold(String animationName) {
            this.parent.getController().setAnimation(RawAnimation.begin().then(animationName, Animation.LoopType.HOLD_ON_LAST_FRAME));
            return PlayState.CONTINUE;
        }

        @Info("Sets an animation to play an x amount of times")
        public PlayState thenPlayXTimes(String animationName, int times) {
            for (int i = 0; i < times; i++) {
                this.parent.getController().setAnimation(RawAnimation.begin().then(animationName, i == times - 1 ? Animation.LoopType.DEFAULT : Animation.LoopType.PLAY_ONCE));
            }
            return PlayState.CONTINUE;
        }

        @Info("Adds an animation to the current animation list")
        public BaseEntityBuilder.AnimationEventJS<E> then(String animationName, Animation.LoopType loopType) {
            this.animationList.add(new RawAnimation.Stage(animationName, loopType));
            return this;
        }

        @Info("Returns any extra data that the event may have\n\nUsually used by armor animations to know what item is worn\n")
        public Map<DataTicket<?>, ?> getExtraData() {
            return this.parent.getExtraData();
        }
    }

    public static class CustomInstructionKeyframeEventJS<E extends Entity & IAnimatableJSNL> {

        @Info("A list of all the custom instructions. In blockbench, each line in the custom instruction box is a separate instruction.")
        public final String instructions;

        public CustomInstructionKeyframeEventJS(CustomInstructionKeyframeEvent<E> parent) {
            this.instructions = parent.getKeyframeData().getInstructions();
        }
    }

    @FunctionalInterface
    public interface IAnimationPredicateJS<E extends Entity & IAnimatableJSNL> {

        @Info(value = "Determines if an animation should continue for a given AnimationEvent. Return true to continue the current animation", params = { @Param(name = "event", value = "The AnimationEvent, provides values that can be used to determine if the animation should continue or not") })
        boolean test(BaseEntityBuilder.AnimationEventJS<E> event);

        default AnimationController.AnimationStateHandler<E> toGecko() {
            return event -> {
                if (event != null) {
                    BaseEntityBuilder.AnimationEventJS<E> animationEventJS = new BaseEntityBuilder.AnimationEventJS<>(event);
                    try {
                        if (animationEventJS == null) {
                            return PlayState.STOP;
                        }
                    } catch (Exception var4) {
                        ConsoleJS.STARTUP.error("Exception in IAnimationPredicateJS.toGecko()", var4);
                        return PlayState.STOP;
                    }
                    return this.test(animationEventJS) ? PlayState.CONTINUE : PlayState.STOP;
                } else {
                    ConsoleJS.STARTUP.error("AnimationEventJS was null in IAnimationPredicateJS.toGecko()");
                    return PlayState.STOP;
                }
            };
        }
    }

    @FunctionalInterface
    public interface ICustomInstructionListenerJS<E extends Entity & IAnimatableJSNL> {

        void executeInstruction(BaseEntityBuilder.CustomInstructionKeyframeEventJS<E> event);
    }

    @FunctionalInterface
    public interface IParticleListenerJS<E extends Entity & IAnimatableJSNL> {

        void summonParticle(BaseEntityBuilder.ParticleKeyFrameEventJS<E> event);
    }

    @FunctionalInterface
    public interface ISoundListenerJS<E extends Entity & IAnimatableJSNL> {

        void playSound(BaseEntityBuilder.SoundKeyFrameEventJS<E> event);
    }

    public static class KeyFrameEventJS<E extends Entity & IAnimatableJSNL, B extends KeyFrameData> {

        @Info("The amount of ticks that have passed in either the current transition or animation, depending on the controller's AnimationState")
        public final double animationTick;

        @Info("The entity being animated")
        public final E entity;

        @Info("The KeyFrame data")
        private final B eventKeyFrame;

        protected KeyFrameEventJS(KeyFrameEvent<E, B> parent) {
            this.animationTick = parent.getAnimationTick();
            this.entity = parent.getAnimatable();
            this.eventKeyFrame = parent.getKeyframeData();
        }
    }

    public static class ParticleKeyFrameEventJS<E extends Entity & IAnimatableJSNL> {

        public final String effect;

        public final String locator;

        public final String script;

        public ParticleKeyFrameEventJS(ParticleKeyframeEvent<E> parent) {
            this.effect = parent.getKeyframeData().getEffect();
            this.locator = parent.getKeyframeData().getLocator();
            this.script = parent.getKeyframeData().script();
        }
    }

    public static enum RenderType {

        SOLID, CUTOUT, TRANSLUCENT
    }

    public static class SoundKeyFrameEventJS<E extends Entity & IAnimatableJSNL> {

        @Info("The name of the sound to play")
        public final String sound;

        public SoundKeyFrameEventJS(SoundKeyframeEvent<E> parent) {
            this.sound = parent.getKeyframeData().getSound();
        }
    }
}
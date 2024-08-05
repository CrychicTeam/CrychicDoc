package net.liopyu.entityjs.builders.living;

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
import net.liopyu.entityjs.builders.nonliving.entityjs.PartBuilder;
import net.liopyu.entityjs.client.living.model.GeoLayerJSBuilder;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.liopyu.entityjs.events.BiomeSpawnsEventJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.liopyu.entityjs.util.implementation.EventBasedSpawnModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
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

public abstract class BaseLivingEntityBuilder<T extends LivingEntity & IAnimatableJS> extends BuilderBase<EntityType<T>> {

    public static final List<BaseLivingEntityBuilder<?>> thisList = new ArrayList();

    public transient float width;

    public transient float height;

    public transient boolean summonable;

    public transient boolean save;

    public transient boolean fireImmune;

    public transient boolean canSpawnFarFromPlayer;

    public transient ResourceLocation[] immuneTo;

    public transient boolean spawnFarFromPlayer;

    public transient int clientTrackingRange;

    public transient int updateInterval;

    public transient MobCategory mobCategory;

    public transient Function<T, Object> modelResource;

    public transient Function<T, Object> textureResource;

    public transient Function<T, Object> animationResource;

    public transient boolean isPushable;

    public final transient List<BaseLivingEntityBuilder.AnimationControllerSupplier<T>> animationSuppliers;

    public transient Function<LivingEntity, Object> shouldDropLoot;

    public transient Function<ContextUtils.PassengerEntityContext, Object> canAddPassenger;

    public transient Function<LivingEntity, Object> isAffectedByFluids;

    public transient boolean isAlwaysExperienceDropper;

    public transient Function<LivingEntity, Object> isImmobile;

    public transient Consumer<ContextUtils.LerpToContext> lerpTo;

    public transient Function<LivingEntity, Object> setBlockJumpFactor;

    public transient Function<LivingEntity, Object> blockSpeedFactor;

    public transient Float setSoundVolume;

    public transient Consumer<LivingEntity> tick;

    public transient Float setWaterSlowDown;

    public transient Object setSwimSound;

    public transient Function<LivingEntity, Object> isFlapping;

    public transient Object setDeathSound;

    public transient BaseLivingEntityBuilder.RenderType renderType;

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

    public SpawnPlacements.Type placementType;

    public Heightmap.Types heightMap;

    public SpawnPlacements.SpawnPredicate<? extends Entity> spawnPredicate;

    public static final List<BaseLivingEntityBuilder<?>> spawnList = new ArrayList();

    public static final List<EventBasedSpawnModifier.BiomeSpawn> biomeSpawnList = new ArrayList();

    public transient Consumer<ContextUtils.RenderContext<T>> render;

    public transient MobType mobType;

    public transient Function<LivingEntity, Object> isFreezing;

    public transient Function<ContextUtils.CollidingEntityContext, Object> canCollideWith;

    public transient Boolean defaultDeathPose;

    public transient Function<ContextUtils.Vec3Context, Object> travelVector;

    public transient Consumer<ContextUtils.Vec3Context> travel;

    public transient Boolean canSteer;

    public transient boolean mountJumpingEnabled;

    public transient Consumer<LivingEntity> tickDeath;

    public final List<ContextUtils.PartEntityParams<T>> partEntityParamsList = new ArrayList();

    public transient Consumer<ContextUtils.LineOfSightContext> onHurtTarget;

    public transient Function<ContextUtils.LineOfSightContext, Object> isAlliedTo;

    public transient float scaleHeight;

    public transient float scaleWidth;

    public transient Consumer<ContextUtils.ScaleModelRenderContext<T>> scaleModelForRender;

    public final List<GeoLayerJSBuilder<T>> layerList = new ArrayList();

    public transient Consumer<GeoLayerJSBuilder<T>> newGeoLayer;

    public transient Consumer<ContextUtils.PositionRiderContext> positionRider;

    public BaseLivingEntityBuilder(ResourceLocation i) {
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
        this.modelResource = t -> ((IAnimatableJS) t).getBuilder().newID("geo/entity/", ".geo.json");
        this.textureResource = t -> ((IAnimatableJS) t).getBuilder().newID("textures/entity/", ".png");
        this.animationResource = t -> ((IAnimatableJS) t).getBuilder().newID("animations/entity/", ".animation.json");
        this.isPushable = true;
        this.animationSuppliers = new ArrayList();
        this.isAlwaysExperienceDropper = false;
        this.setSoundVolume = 1.0F;
        this.setWaterSlowDown = 0.8F;
        this.repositionEntityAfterLoad = true;
        this.canBreatheUnderwater = false;
        this.renderType = BaseLivingEntityBuilder.RenderType.CUTOUT;
        this.mainArm = HumanoidArm.RIGHT;
        this.mobType = MobType.UNDEFINED;
        this.defaultDeathPose = true;
        this.canSteer = true;
        this.mountJumpingEnabled = true;
        this.scaleHeight = 1.0F;
        this.scaleWidth = 1.0F;
    }

    @Info("@param positionRider A consumer determining the position of rider/riders.\n\n    Example usage:\n    ```javascript\n    entityBuilder.positionRider(context => {\n        const {entity, passenger, moveFunction} = context\n    });\n    ```\n")
    public BaseLivingEntityBuilder<T> positionRider(Consumer<ContextUtils.PositionRiderContext> builderConsumer) {
        this.positionRider = builderConsumer;
        return this;
    }

    @Info("Adds an extra render layer to the mob.\n@param newGeoLayer The builder Consumer for the new render layer.\n\n    Example usage:\n    ```javascript\n    entityBuilder.newGeoLayer(builder => {\n        builder.textureResource(entity => {\n            return \"kubejs:textures/entity/sasuke.png\"\n        })\n    });\n    ```\n")
    public BaseLivingEntityBuilder<T> newGeoLayer(Consumer<GeoLayerJSBuilder<T>> builderConsumer) {
        GeoLayerJSBuilder<T> layerBuild = new GeoLayerJSBuilder<>(this);
        builderConsumer.accept(layerBuild);
        this.layerList.add(layerBuild);
        return this;
    }

    @Info(value = "Adds an extra hitbox to the mob. Aka part-entities.\nVanilla ticks extra hitboxes(for example the ender dragon's) with the\n.tickPart method which specifies which hitbox to move to the entity and\nits offset. This method is available off of the parent entity anywhere\nincluding non EntityJS callbacks. (Usually used in the entity's aiStep method)\nFor example: `entity.tickPart(\"head\", 0, 1, 0)`\n\nCreation of the hitbox:\n```javascript\nentityBuilder.addPartEntity(\"head\", 1, 2, builder => {\n    // Can also be null\n    builder.isPickable(true)\n});\n```\n", params = { @Param(name = "name", value = "The name of the part"), @Param(name = "width", value = "The width of the part"), @Param(name = "height", value = "The height of the part"), @Param(name = "builderConsumer", value = "The builder for the part, very similar to the normal builder callbacks") })
    public BaseLivingEntityBuilder<T> addPartEntity(String name, float width, float height, Consumer<PartBuilder<T>> builderConsumer) {
        PartBuilder<T> partBuilder = new PartBuilder<>();
        builderConsumer.accept(partBuilder);
        this.partEntityParamsList.add(new ContextUtils.PartEntityParams<>(name, width, height, partBuilder));
        return this;
    }

    @Info("Sets the scale of the model.\n\nExample usage:\n```javascript\nentityBuilder.modelSize(2,2);\n```\n")
    public BaseLivingEntityBuilder<T> modelSize(float scaleHeight, float scaleWidth) {
        this.scaleHeight = scaleHeight;
        this.scaleWidth = scaleWidth;
        return this;
    }

    @Info("@param scaleModelForRender A Consumer to determing logic for model scaling and rendering\n    without affecting core logic such as hitbox sizing.\n\nExample usage:\n```javascript\nentityBuilder.scaleModelForRender(context => {\n    const { entity, widthScale, heightScale, poseStack, model, isReRender, partialTick, packedLight, packedOverlay } = context\n    if (entity.isBaby()) {\n        poseStack.scale(0.5, 0.5, 0.5)\n    }\n});\n```\n")
    public BaseLivingEntityBuilder<T> scaleModelForRender(Consumer<ContextUtils.ScaleModelRenderContext<T>> scaleModelForRender) {
        this.scaleModelForRender = scaleModelForRender;
        return this;
    }

    @Info("Function determining if the entity is allied with a potential target.\n\nExample usage:\n```javascript\nentityBuilder.isAlliedTo(context => {\n    const {entity, target} = context\n    return target.type == 'minecraft:blaze'\n});\n```\n")
    public BaseLivingEntityBuilder<T> isAlliedTo(Function<ContextUtils.LineOfSightContext, Object> isAlliedTo) {
        this.isAlliedTo = isAlliedTo;
        return this;
    }

    @Info("@param onHurtTarget A Consumer to execute when the mob attacks its target\n\nExample usage:\n```javascript\nmobBuilder.onHurtTarget(context => {\n    const {entity, targetEntity} = context\n    //Execute code when the target is hurt\n});\n```\n")
    public BaseLivingEntityBuilder<T> onHurtTarget(Consumer<ContextUtils.LineOfSightContext> onHurtTarget) {
        this.onHurtTarget = onHurtTarget;
        return this;
    }

    @Info("Consumer overriding the tickDeath responsible to counting down\nthe ticks it takes to remove the entity when it dies.\n\nExample usage:\n```javascript\nentityBuilder.tickDeath(entity => {\n    // Override the tickDeath method in the entity\n});\n```\n")
    public BaseLivingEntityBuilder<T> tickDeath(Consumer<LivingEntity> tickDeath) {
        this.tickDeath = tickDeath;
        return this;
    }

    @Info("Boolean determining whether the entity can jump while mounted by a player.\n(Currently experimental jumping logic subject to change in the future)\nDefaults to false.\nExample usage:\n```javascript\nentityBuilder.mountJumpingEnabled(true);\n```\n")
    public BaseLivingEntityBuilder<T> mountJumpingEnabled(boolean mountJumpingEnabled) {
        this.mountJumpingEnabled = mountJumpingEnabled;
        return this;
    }

    @Info("Consumer determining travel logic for the entity.\n\nExample usage:\n```javascript\nentityBuilder.travel(context => {\n    const {entity, vec3} = context\n    // Use the vec3 and entity to determine the travel logic of the entity\n});\n```\n")
    public BaseLivingEntityBuilder<T> travel(Consumer<ContextUtils.Vec3Context> travel) {
        this.travel = travel;
        return this;
    }

    @Info("Boolean determining whether the passenger is able to steer the entity while riding.\nDefaults to true.\nExample usage:\n```javascript\nentityBuilder.canSteer(false);\n```\n")
    public BaseLivingEntityBuilder<T> canSteer(boolean canSteer) {
        this.canSteer = canSteer;
        return this;
    }

    @Info("Boolean determining if the entity will turn sideways on death.\nDefaults to true.\nExample usage:\n```javascript\nentityBuilder.defaultDeathPose(false);\n```\n")
    public BaseLivingEntityBuilder<T> defaultDeathPose(boolean defaultDeathPose) {
        this.defaultDeathPose = defaultDeathPose;
        return this;
    }

    @Info("Function determining if the entity may collide with another entity\nusing the ContextUtils.CollidingEntityContext which has this entity and the\none colliding with this entity.\n\nExample usage:\n```javascript\nentityBuilder.canCollideWith(context => {\n    return true //Some Boolean value determining whether the entity may collide with another\n});\n```\n")
    public BaseLivingEntityBuilder<T> canCollideWith(Function<ContextUtils.CollidingEntityContext, Object> canCollideWith) {
        this.canCollideWith = canCollideWith;
        return this;
    }

    @Info("Defines the Mob's Type\nExamples: 'undead', 'water', 'arthropod', 'undefined', 'illager'\n\nExample usage:\n```javascript\nentityBuilder.mobType('undead');\n```\n")
    public BaseLivingEntityBuilder<T> mobType(Object mt) {
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
    public BaseLivingEntityBuilder<T> isFreezing(Function<LivingEntity, Object> isFreezing) {
        this.isFreezing = isFreezing;
        return this;
    }

    @Info("Defines logic to render the entity.\n\nExample usage:\n```javascript\nentityBuilder.render(context => {\n    // Define logic to render the entity\n    if (context.entity.isBaby()) {\n        context.poseStack.scale(0.5, 0.5, 0.5);\n    }\n});\n```\n")
    public BaseLivingEntityBuilder<T> render(Consumer<ContextUtils.RenderContext<T>> render) {
        this.render = render;
        return this;
    }

    @Info("Sets the main arm of the entity. Defaults to 'right'.\n\n@param arm The main arm of the entity. Accepts values \"left\" or \"right\".\n\nExample usage:\n```javascript\nentityBuilder.mainArm(\"left\");\n```\n")
    public BaseLivingEntityBuilder<T> mainArm(Object arm) {
        if (arm instanceof HumanoidArm) {
            this.mainArm = (HumanoidArm) arm;
            return this;
        } else {
            if (arm instanceof String string) {
                String var3 = string.toLowerCase();
                switch(var3) {
                    case "left":
                        this.mainArm = HumanoidArm.LEFT;
                        break;
                    case "right":
                        this.mainArm = HumanoidArm.RIGHT;
                }
            } else {
                this.mainArm = HumanoidArm.RIGHT;
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid value for mainArm: " + arm + ". Example: \"left\"");
            }
            return this;
        }
    }

    @Info("Sets the hit box of the entity type.\n\n@param width The width of the entity, defaults to 1.\n@param height The height of the entity, defaults to 1.\n\nExample usage:\n```javascript\nentityBuilder.sized(2, 3);\n```\n")
    public BaseLivingEntityBuilder<T> sized(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    @Info("Determines if the entity should serialize its data. Defaults to true.\n\nExample usage:\n```javascript\nentityBuilder.saves(false);\n```\n")
    public BaseLivingEntityBuilder<T> saves(boolean shouldSave) {
        this.save = shouldSave;
        return this;
    }

    @Info("Sets whether the entity is immune to fire damage.\n\nExample usage:\n```javascript\nentityBuilder.fireImmune(true);\n```\n")
    public BaseLivingEntityBuilder<T> fireImmune(boolean isFireImmune) {
        this.fireImmune = isFireImmune;
        return this;
    }

    @Info("Sets a consumer to handle custom lerping logic for the living entity.\n\n@param lerpTo The consumer to handle the custom lerping logic.\n\nThe consumer should take a LerpToContext as a parameter, providing information about the lerping operation, including the target position, yaw, pitch, increment count, teleport flag, and the entity itself.\n\nExample usage:\n```javascript\nbaseLivingEntityBuilder.lerpTo(context => {\n    // Custom lerping logic for the living entity\n    const { x, y, z, yaw, pitch, posRotationIncrements, teleport, entity } = context;\n    // Perform custom lerping operations using the provided context\n    // For example, you can smoothly move the entity from its current position to the target position\n    entity.setPositionAndRotation(x, y, z, yaw, pitch);\n});\n```\n")
    public BaseLivingEntityBuilder<T> lerpTo(Consumer<ContextUtils.LerpToContext> lerpTo) {
        this.lerpTo = lerpTo;
        return this;
    }

    @Info("Sets the list of block names to which the entity is immune.\n\nExample usage:\n```javascript\nentityBuilder.immuneTo(\"minecraft:stone\", \"minecraft:dirt\");\n```\n")
    public BaseLivingEntityBuilder<T> immuneTo(String... blockNames) {
        this.immuneTo = (ResourceLocation[]) Arrays.stream(blockNames).map(ResourceLocation::new).toArray(ResourceLocation[]::new);
        return this;
    }

    @Info("Sets whether the entity can spawn far from the player.\n\nExample usage:\n```javascript\nentityBuilder.canSpawnFarFromPlayer(true);\n```\n")
    public BaseLivingEntityBuilder<T> canSpawnFarFromPlayer(boolean canSpawnFar) {
        this.spawnFarFromPlayer = canSpawnFar;
        return this;
    }

    @Info("Sets the block jump factor for the entity.\n\nExample usage:\n```javascript\nentityBuilder.setBlockJumpFactor(entity => {\n    //Set the jump factor for the entity through context\n    return 1 //some float value;\n});\n```\n")
    public BaseLivingEntityBuilder<T> setBlockJumpFactor(Function<LivingEntity, Object> blockJumpFactor) {
        this.setBlockJumpFactor = blockJumpFactor;
        return this;
    }

    @Info("Sets the water slowdown factor for the entity. Defaults to 0.8.\n\nExample usage:\n```javascript\nentityBuilder.setWaterSlowDown(0.6);\n```\n")
    public BaseLivingEntityBuilder<T> setWaterSlowDown(float slowdownFactor) {
        this.setWaterSlowDown = slowdownFactor;
        return this;
    }

    @Info("Sets the overall sound volume for the entity.\n\nExample usage:\n```javascript\nentityBuilder.setSoundVolume(0.5);\n```\n")
    public BaseLivingEntityBuilder<T> setSoundVolume(float volume) {
        this.setSoundVolume = volume;
        return this;
    }

    @Info("Sets a predicate to determine whether the entity should drop loot upon death.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose loot dropping behavior is being determined.\nIt returns a Boolean indicating whether the entity should drop loot.\n\nExample usage:\n```javascript\nentityBuilder.shouldDropLoot(entity => {\n    // Define logic to determine whether the entity should drop loot\n    // Use information about the LivingEntity provided by the context.\n    return // Some Boolean value indicating whether the entity should drop loot;\n});\n```\n")
    public BaseLivingEntityBuilder<T> shouldDropLoot(Function<LivingEntity, Object> b) {
        this.shouldDropLoot = b;
        return this;
    }

    @Info("Sets a callback function to be executed during the living entity's AI step.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nallowing customization of the AI behavior.\n\nExample usage:\n```javascript\nentityBuilder.aiStep(entity => {\n    // Custom logic to be executed during the living entity's AI step\n    // Access and modify information about the entity using the provided context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> aiStep(Consumer<LivingEntity> aiStep) {
        this.aiStep = aiStep;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity jumps.\n\nExample usage:\n```javascript\nentityBuilder.onLivingJump(entity => {\n    // Custom logic to handle the entity's jump action\n});\n```\n")
    public BaseLivingEntityBuilder<T> onLivingJump(Consumer<LivingEntity> onJump) {
        this.onLivingJump = onJump;
        return this;
    }

    @Info("Sets the client tracking range for the entity.\nDefaults to 5.\nExample usage:\n```javascript\nentityBuilder.clientTrackingRange(64); // Set the client tracking range to 64 blocks\n```\n")
    public BaseLivingEntityBuilder<T> clientTrackingRange(int i) {
        this.clientTrackingRange = i;
        return this;
    }

    @Info("Sets the update interval for the entity.\nDefaults to 1 tick.\nExample usage:\n```javascript\nentityBuilder.updateInterval(20); // Set the update interval to 20 ticks\n```\n")
    public BaseLivingEntityBuilder<T> updateInterval(int i) {
        this.updateInterval = i;
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

    @Info("Sets the mob category for the entity.\nAvailable options: 'monster', 'creature', 'ambient', 'water_creature', 'misc'.\nDefaults to 'misc'.\n\nExample usage:\n```javascript\nentityBuilder.mobCategory('monster');\n```\n")
    public BaseLivingEntityBuilder<T> mobCategory(String category) {
        this.mobCategory = stringToMobCategory(category);
        return this;
    }

    @Info("Sets a function to determine the model resource for the entity.\nThe provided Function accepts a parameter of type T (the entity),\nallowing changing the model based on information about the entity.\nThe default behavior returns <namespace>:geo/entity/<path>.geo.json.\n\nExample usage:\n```javascript\nentityBuilder.modelResource(entity => {\n    // Define logic to determine the model resource for the entity\n    // Use information about the entity provided by the context.\n    return \"kubejs:geo/entity/wyrm.geo.json\" // Some ResourceLocation representing the model resource;\n});\n```\n")
    public BaseLivingEntityBuilder<T> modelResource(Function<T, Object> function) {
        this.modelResource = entity -> {
            Object obj = function.apply(entity);
            if (obj instanceof String && !obj.toString().equals("undefined")) {
                return new ResourceLocation((String) obj);
            } else if (obj instanceof ResourceLocation) {
                return (ResourceLocation) obj;
            } else {
                EntityJSHelperClass.logWarningMessageOnce("Invalid model resource: " + obj + ". Defaulting to " + ((IAnimatableJS) entity).getBuilder().newID("geo/entity/", ".geo.json"));
                return ((IAnimatableJS) entity).getBuilder().newID("geo/entity/", ".geo.json");
            }
        };
        return this;
    }

    @Info("Sets a function to determine the texture resource for the entity.\nThe provided Function accepts a parameter of type T (the entity),\nallowing changing the texture based on information about the entity.\nThe default behavior returns <namespace>:textures/entity/<path>.png.\n\nExample usage:\n```javascript\nentityBuilder.textureResource(entity => {\n    // Define logic to determine the texture resource for the entity\n    // Use information about the entity provided by the context.\n    return \"kubejs:textures/entity/wyrm.png\" // Some ResourceLocation representing the texture resource;\n});\n```\n")
    public BaseLivingEntityBuilder<T> textureResource(Function<T, Object> function) {
        this.textureResource = entity -> {
            Object obj = function.apply(entity);
            if (obj instanceof String && !obj.toString().equals("undefined")) {
                return new ResourceLocation((String) obj);
            } else if (obj instanceof ResourceLocation) {
                return (ResourceLocation) obj;
            } else {
                EntityJSHelperClass.logWarningMessageOnce("Invalid texture resource: " + obj + ". Defaulting to " + ((IAnimatableJS) entity).getBuilder().newID("textures/entity/", ".png"));
                return ((IAnimatableJS) entity).getBuilder().newID("textures/entity/", ".png");
            }
        };
        return this;
    }

    @Info("Sets a function to determine the animation resource for the entity.\nThe provided Function accepts a parameter of type T (the entity),\nallowing changing the animations based on information about the entity.\nThe default behavior returns <namespace>:animations/<path>.animation.json.\n\nExample usage:\n```javascript\nentityBuilder.animationResource(entity => {\n    // Define logic to determine the animation resource for the entity\n    // Use information about the entity provided by the context.\n    //return some ResourceLocation representing the animation resource;\n    return \"kubejs:animations/entity/wyrm.animation.json\" // Some ResourceLocation representing the animation resource;\n});\n```\n")
    public BaseLivingEntityBuilder<T> animationResource(Function<T, Object> function) {
        this.animationResource = entity -> {
            Object obj = function.apply(entity);
            if (obj instanceof String && !obj.toString().equals("undefined")) {
                return new ResourceLocation((String) obj);
            } else if (obj instanceof ResourceLocation) {
                return (ResourceLocation) obj;
            } else {
                EntityJSHelperClass.logWarningMessageOnce("Invalid animation resource: " + obj + ". Defaulting to " + ((IAnimatableJS) entity).getBuilder().newID("animations/entity/", ".animation.json"));
                return ((IAnimatableJS) entity).getBuilder().newID("animations/entity/", ".animation.json");
            }
        };
        return this;
    }

    @Info("Sets whether the entity is pushable.\n\nExample usage:\n```javascript\nentityBuilder.isPushable(true);\n```\n")
    public BaseLivingEntityBuilder<T> isPushable(boolean b) {
        this.isPushable = b;
        return this;
    }

    @Info("Sets a predicate to determine if a passenger can be added to the entity.\n\n@param predicate The predicate to check if a passenger can be added.\n\nExample usage:\n```javascript\nentityBuilder.canAddPassenger(context => {\n    // Custom logic to determine if a passenger can be added to the entity\n    return true;\n});\n```\n")
    public BaseLivingEntityBuilder<T> canAddPassenger(Function<ContextUtils.PassengerEntityContext, Object> predicate) {
        this.canAddPassenger = predicate;
        return this;
    }

    @Info("Sets a predicate to determine whether the entity is affected by fluids.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose interaction with fluids is being determined.\nIt returns a Boolean indicating whether the entity is affected by fluids.\n\nExample usage:\n```javascript\nentityBuilder.isAffectedByFluids(entity => {\n    // Define logic to determine whether the entity is affected by fluids\n    // Use information about the LivingEntity provided by the context.\n    return // Some Boolean value indicating whether the entity is affected by fluids;\n});\n```\n")
    public BaseLivingEntityBuilder<T> isAffectedByFluids(Function<LivingEntity, Object> b) {
        this.isAffectedByFluids = b;
        return this;
    }

    @Info("Sets whether the entity is summonable.\n\nExample usage:\n```javascript\nentityBuilder.setSummonable(true);\n```\n")
    public BaseLivingEntityBuilder<T> setSummonable(boolean b) {
        this.summonable = b;
        return this;
    }

    @Info("Sets a predicate to determine whether the entity is immobile.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose immobility is being determined.\nIt returns a Boolean indicating whether the entity is immobile.\n\nExample usage:\n```javascript\nentityBuilder.isImmobile(entity => {\n    // Define logic to determine whether the entity is immobile\n    // Use information about the LivingEntity provided by the context.\n    return // Some Boolean value indicating whether the entity is immobile;\n});\n```\n")
    public BaseLivingEntityBuilder<T> isImmobile(Function<LivingEntity, Object> b) {
        this.isImmobile = b;
        return this;
    }

    @Info("Sets whether the entity is always considered as an experience dropper.\n\nExample usage:\n```javascript\nentityBuilder.isAlwaysExperienceDropper(true);\n```\n")
    public BaseLivingEntityBuilder<T> isAlwaysExperienceDropper(boolean b) {
        this.isAlwaysExperienceDropper = b;
        return this;
    }

    @Info("Sets a function to calculate fall damage for the entity.\nThe provided Function accepts a {@link ContextUtils.CalculateFallDamageContext} parameter,\nrepresenting the context of the fall damage calculation.\nIt returns an Integer representing the calculated fall damage.\n\nExample usage:\n```javascript\nentityBuilder.calculateFallDamage(context => {\n    // Define logic to calculate and return the fall damage for the entity\n    // Use information about the CalculateFallDamageContext provided by the context.\n    return // Some Integer value representing the calculated fall damage;\n});\n```\n")
    public BaseLivingEntityBuilder<T> calculateFallDamage(Function<ContextUtils.CalculateFallDamageContext, Object> calculation) {
        this.calculateFallDamage = calculation;
        return this;
    }

    @Info("Sets the death sound for the entity.\n\nExample usage:\n```javascript\nentityBuilder.setDeathSound(\"minecraft:entity.generic.death\");\n```\n")
    public BaseLivingEntityBuilder<T> setDeathSound(Object sound) {
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
    public BaseLivingEntityBuilder<T> setSwimSound(Object sound) {
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
    public BaseLivingEntityBuilder<T> setSwimSplashSound(Object sound) {
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
    public BaseLivingEntityBuilder<T> blockSpeedFactor(Function<LivingEntity, Object> callback) {
        this.blockSpeedFactor = callback;
        return this;
    }

    @Info("Sets a function to determine whether the entity is currently flapping.\nThe provided Function accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose flapping status is being determined.\nIt returns a Boolean indicating whether the entity is flapping.\n\nExample usage:\n```javascript\nentityBuilder.isFlapping(entity => {\n    // Define logic to determine whether the entity is currently flapping\n    // Use information about the LivingEntity provided by the context.\n    return // Some Boolean value indicating whether the entity is flapping;\n});\n```\n")
    public BaseLivingEntityBuilder<T> isFlapping(Function<LivingEntity, Object> b) {
        this.isFlapping = b;
        return this;
    }

    @Info("Sets a callback function to be executed during each tick of the entity.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is being ticked.\n\nExample usage:\n```javascript\nentityBuilder.tick(entity => {\n    // Define custom logic for handling during each tick of the entity\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> tick(Consumer<LivingEntity> tickCallback) {
        this.tick = tickCallback;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is added to the world.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is added to the world.\n\nExample usage:\n```javascript\nentityBuilder.onAddedToWorld(entity => {\n    // Define custom logic for handling when the entity is added to the world\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onAddedToWorld(Consumer<LivingEntity> onAddedToWorldCallback) {
        this.onAddedToWorld = onAddedToWorldCallback;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity automatically attacks on touch.\nThe provided Consumer accepts a {@link ContextUtils.AutoAttackContext} parameter,\nrepresenting the context of the auto-attack when the entity touches another entity.\n\nExample usage:\n```javascript\nentityBuilder.doAutoAttackOnTouch(context => {\n    // Define custom logic for handling when the entity automatically attacks on touch\n    // Use information about the AutoAttackContext provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> doAutoAttackOnTouch(Consumer<ContextUtils.AutoAttackContext> doAutoAttackOnTouch) {
        this.doAutoAttackOnTouch = doAutoAttackOnTouch;
        return this;
    }

    @Info("Sets a function to determine the standing eye height of the entity.\nThe provided Function accepts a {@link ContextUtils.EntityPoseDimensionsContext} parameter,\nrepresenting the context of the entity's pose and dimensions when standing.\nIt returns a Float representing the standing eye height.\n\nExample usage:\n```javascript\nentityBuilder.setStandingEyeHeight(context => {\n    // Define logic to calculate and return the standing eye height for the entity\n    // Use information about the EntityPoseDimensionsContext provided by the context.\n    return // Some Float value representing the standing eye height;\n});\n```\n")
    public BaseLivingEntityBuilder<T> setStandingEyeHeight(Function<ContextUtils.EntityPoseDimensionsContext, Object> setStandingEyeHeight) {
        this.setStandingEyeHeight = setStandingEyeHeight;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity's air supply decreases.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose air supply is being decreased.\n\nExample usage:\n```javascript\nentityBuilder.onDecreaseAirSupply(entity => {\n    // Define custom logic for handling when the entity's air supply decreases\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onDecreaseAirSupply(Consumer<LivingEntity> onDecreaseAirSupply) {
        this.onDecreaseAirSupply = onDecreaseAirSupply;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is blocked by a shield.\nThe provided Consumer accepts a {@link ContextUtils.LivingEntityContext} parameter,\nrepresenting the entity that is blocked by a shield.\n\nExample usage:\n```javascript\nentityBuilder.onBlockedByShield(context => {\n    // Define custom logic for handling when the entity is blocked by a shield\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onBlockedByShield(Consumer<ContextUtils.LivingEntityContext> onBlockedByShield) {
        this.onBlockedByShield = onBlockedByShield;
        return this;
    }

    @Info("Sets whether to reposition the entity after loading.\n\nExample usage:\n```javascript\nentityBuilder.repositionEntityAfterLoad(true);\n```\n")
    public BaseLivingEntityBuilder<T> repositionEntityAfterLoad(boolean customRepositionEntityAfterLoad) {
        this.repositionEntityAfterLoad = customRepositionEntityAfterLoad;
        return this;
    }

    @Info("Sets a function to determine the next step distance for the entity.\nThe provided Function accepts a {@link Entity} parameter,\nrepresenting the entity whose next step distance is being determined.\nIt returns a Float representing the next step distance.\n\nExample usage:\n```javascript\nentityBuilder.nextStep(entity => {\n    // Define logic to calculate and return the next step distance for the entity\n    // Use information about the Entity provided by the context.\n    return // Some Float value representing the next step distance;\n});\n```\n")
    public BaseLivingEntityBuilder<T> nextStep(Function<Entity, Object> nextStep) {
        this.nextStep = nextStep;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity's air supply increases.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose air supply is being increased.\n\nExample usage:\n```javascript\nentityBuilder.onIncreaseAirSupply(entity => {\n    // Define custom logic for handling when the entity's air supply increases\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onIncreaseAirSupply(Consumer<LivingEntity> onIncreaseAirSupply) {
        this.onIncreaseAirSupply = onIncreaseAirSupply;
        return this;
    }

    @Info("Sets a function to determine the custom hurt sound of the entity.\nThe provided Function accepts a {@link ContextUtils.HurtContext} parameter,\n```javascript\nentityBuilder.setHurtSound(context => {\n    // Custom logic to determine the hurt sound for the entity\n    // You can use information from the HurtContext to customize the sound based on the context\n    const { entity, damageSource } = context;\n    // Determine the hurt sound based on the type of damage source\n    switch (damageSource.getType()) {\n        case \"fire\":\n            return \"minecraft:entity.generic.burn\";\n        case \"fall\":\n            return \"minecraft:entity.generic.hurt\";\n        case \"drown\":\n            return \"minecraft:entity.generic.hurt\";\n        case \"explosion\":\n            return \"minecraft:entity.generic.explode\";\n        default:\n            return \"minecraft:entity.generic.explode\";\n    }\n})\n```\n")
    public BaseLivingEntityBuilder<T> setHurtSound(Function<ContextUtils.HurtContext, Object> sound) {
        this.setHurtSound = sound;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can attack a specific entity type.\nThe provided Predicate accepts a {@link ContextUtils.EntityTypeEntityContext} parameter,\nrepresenting the context of the entity attacking a specific entity type.\n\nExample usage:\n```javascript\nentityBuilder.canAttackType(context => {\n    // Define conditions to check if the entity can attack the specified entity type\n    // Use information about the EntityTypeEntityContext provided by the context.\n    return // Some boolean condition indicating if the entity can attack the specified entity type;\n});\n```\n")
    public BaseLivingEntityBuilder<T> canAttackType(Function<ContextUtils.EntityTypeEntityContext, Object> canAttackType) {
        this.canAttackType = canAttackType;
        return this;
    }

    @Info("Sets a function to determine the custom hitbox scale of the entity.\nThe provided Function accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose scale is being determined.\nIt returns a Float representing the custom scale.\n\nExample usage:\n```javascript\nentityBuilder.scale(entity => {\n    // Define logic to calculate and return the custom scale for the entity\n    // Use information about the LivingEntity provided by the context.\n    return // Some Float value;\n});\n```\n")
    public BaseLivingEntityBuilder<T> scale(Function<LivingEntity, Object> customScale) {
        this.scale = customScale;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity should drop experience upon death.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose experience drop is being determined.\n\nExample usage:\n```javascript\nentityBuilder.shouldDropExperience(entity => {\n    // Define conditions to check if the entity should drop experience upon death\n    // Use information about the LivingEntity provided by the context.\n    return // Some boolean condition indicating if the entity should drop experience;\n});\n```\n")
    public BaseLivingEntityBuilder<T> shouldDropExperience(Function<LivingEntity, Object> p) {
        this.shouldDropExperience = p;
        return this;
    }

    @Info("Sets a function to determine the experience reward for killing the entity.\nThe provided Function accepts a {@link LivingEntity} parameter,\nrepresenting the entity whose experience reward is being determined.\nIt returns an Integer representing the experience reward.\n\nExample usage:\n```javascript\nentityBuilder.experienceReward(killedEntity => {\n    // Define logic to calculate and return the experience reward for the killedEntity\n    // Use information about the LivingEntity provided by the context.\n    return // Some Integer value representing the experience reward;\n});\n```\n")
    public BaseLivingEntityBuilder<T> experienceReward(Function<LivingEntity, Object> experienceReward) {
        this.experienceReward = experienceReward;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity equips an item.\nThe provided Consumer accepts a {@link ContextUtils.EntityEquipmentContext} parameter,\nrepresenting the context of the entity equipping an item.\n\nExample usage:\n```javascript\nentityBuilder.onEquipItem(context => {\n    // Define custom logic for handling when the entity equips an item\n    // Use information about the EntityEquipmentContext provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onEquipItem(Consumer<ContextUtils.EntityEquipmentContext> onEquipItem) {
        this.onEquipItem = onEquipItem;
        return this;
    }

    @Info("Sets a function to determine the visibility percentage of the entity.\nThe provided Function accepts a {@link ContextUtils.VisualContext} parameter,\nrepresenting both the entity whose visibility percentage is being determined\nand the the builder entity who is being looked at.\nIt returns a Double representing the visibility percentage.\n\nExample usage:\n```javascript\nentityBuilder.visibilityPercent(context => {\n    // Define logic to calculate and return the visibility percentage for the targetEntity\n    // Use information about the Entity provided by the context.\n    return // Some Double value representing the visibility percentage;\n});\n```\n")
    public BaseLivingEntityBuilder<T> visibilityPercent(Function<ContextUtils.VisualContext, Object> visibilityPercent) {
        this.visibilityPercent = visibilityPercent;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can attack another entity.\nThe provided Predicate accepts a {@link ContextUtils.LivingEntityContext} parameter,\nrepresenting the entity that may be attacked.\n\nExample usage:\n```javascript\nentityBuilder.canAttack(context => {\n    // Define conditions to check if the entity can attack the targetEntity\n    // Use information about the LivingEntity provided by the context.\n    return // Some boolean condition indicating if the entity can attack the targetEntity;\n});\n```\n")
    public BaseLivingEntityBuilder<T> canAttack(Function<ContextUtils.LivingEntityContext, Object> customCanAttack) {
        this.canAttack = customCanAttack;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can be affected by an effect.\nThe provided Predicate accepts a {@link ContextUtils.OnEffectContext} parameter,\nrepresenting the context of the effect that may affect the entity.\n\nExample usage:\n```javascript\nentityBuilder.canBeAffected(context => {\n    // Define conditions to check if the entity can be affected by the effect\n    // Use information about the OnEffectContext provided by the context.\n    return // Some boolean condition indicating if the entity can be affected by an effect;\n});\n```\n")
    public BaseLivingEntityBuilder<T> canBeAffected(Function<ContextUtils.OnEffectContext, Object> predicate) {
        this.canBeAffected = predicate;
        return this;
    }

    @Info("Sets a predicate to determine if the entity has inverted heal and harm behavior.\n\n@param invertedHealAndHarm The predicate to check for inverted heal and harm behavior.\n\nExample usage:\n```javascript\nentityBuilder.invertedHealAndHarm(entity => {\n    // Custom logic to determine if the entity has inverted heal and harm behavior\n    return true; // Replace with your custom boolean condition\n});\n```\n")
    public BaseLivingEntityBuilder<T> invertedHealAndHarm(Function<LivingEntity, Object> invertedHealAndHarm) {
        this.invertedHealAndHarm = invertedHealAndHarm;
        return this;
    }

    @Info("Sets a callback function to be executed when an effect is added to the entity.\nThe provided Consumer accepts a {@link ContextUtils.OnEffectContext} parameter,\nrepresenting the context of the effect being added to the entity.\n\nExample usage:\n```javascript\nentityBuilder.onEffectAdded(context => {\n    // Define custom logic for handling when an effect is added to the entity\n    // Use information about the OnEffectContext provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onEffectAdded(Consumer<ContextUtils.OnEffectContext> consumer) {
        this.onEffectAdded = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity receives healing.\nThe provided Consumer accepts a {@link ContextUtils.EntityHealContext} parameter,\nrepresenting the context of the entity receiving healing.\nVery similar to {@link ForgeEventFactory.onLivingHeal}\n\nExample usage:\n```javascript\nentityBuilder.onLivingHeal(context => {\n    // Define custom logic for handling when the entity receives healing\n    // Use information about the EntityHealContext provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onLivingHeal(Consumer<ContextUtils.EntityHealContext> callback) {
        this.onLivingHeal = callback;
        return this;
    }

    @Info("Sets a callback function to be executed when an effect is removed from the entity.\nThe provided Consumer accepts a {@link ContextUtils.OnEffectContext} parameter,\nrepresenting the context of the effect being removed from the entity.\n\nExample usage:\n```javascript\nentityBuilder.onEffectRemoved(context => {\n    // Define custom logic for handling when an effect is removed from the entity\n    // Use information about the OnEffectContext provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onEffectRemoved(Consumer<ContextUtils.OnEffectContext> consumer) {
        this.onEffectRemoved = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is hurt.\nThe provided Consumer accepts a {@link ContextUtils.EntityDamageContext} parameter,\nrepresenting the context of the entity being hurt.\n\nExample usage:\n```javascript\nentityBuilder.onHurt(context => {\n    // Define custom logic for handling when the entity is hurt\n    // Use information about the EntityDamageContext provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onHurt(Consumer<ContextUtils.EntityDamageContext> predicate) {
        this.onHurt = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity dies.\nThe provided Consumer accepts a {@link ContextUtils.DeathContext} parameter,\nrepresenting the context of the entity's death.\n\nExample usage:\n```javascript\nentityBuilder.onDeath(context => {\n    // Define custom logic for handling the entity's death\n    // Use information about the DeathContext provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onDeath(Consumer<ContextUtils.DeathContext> consumer) {
        this.onDeath = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity drops custom loot upon death.\nThe provided Consumer accepts a {@link ContextUtils.EntityLootContext} parameter,\nrepresenting the context of the entity's death and loot dropping.\n\nExample usage:\n```javascript\nentityBuilder.dropCustomDeathLoot(context => {\n    // Define custom logic for handling the entity dropping custom loot upon death\n    // Use information about the EntityLootContext provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> dropCustomDeathLoot(Consumer<ContextUtils.EntityLootContext> consumer) {
        this.dropCustomDeathLoot = consumer;
        return this;
    }

    @Info("Sets the sound resource locations for small and large falls of the entity using either string representations or ResourceLocation objects.\n\nExample usage:\n```javascript\nentityBuilder.fallSounds(\"minecraft:entity.generic.small_fall\",\n    \"minecraft:entity.generic.large_fall\");\n```\n")
    public BaseLivingEntityBuilder<T> fallSounds(Object smallFallSound, Object largeFallSound) {
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
    public BaseLivingEntityBuilder<T> eatingSound(Object sound) {
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
    public BaseLivingEntityBuilder<T> onClimbable(Function<LivingEntity, Object> predicate) {
        this.onClimbable = predicate;
        return this;
    }

    @Info("Sets whether the entity can breathe underwater.\n\nExample usage:\n```javascript\nentityBuilder.canBreatheUnderwater(true);\n```\n")
    public BaseLivingEntityBuilder<T> canBreatheUnderwater(boolean canBreatheUnderwater) {
        this.canBreatheUnderwater = canBreatheUnderwater;
        return this;
    }

    @Info("Sets a callback function to be executed when the living entity falls and takes damage.\nThe provided Consumer accepts a {@link ContextUtils.EntityFallDamageContext} parameter,\nrepresenting the context of the entity falling and taking fall damage.\n\nExample usage:\n```javascript\nentityBuilder.onLivingFall(context => {\n    // Define custom logic for handling when the living entity falls and takes damage\n    // Use information about the EntityFallDamageContext provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onLivingFall(Consumer<ContextUtils.EntityFallDamageContext> c) {
        this.onLivingFall = c;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity starts sprinting.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that has started sprinting.\n\nExample usage:\n```javascript\nentityBuilder.onSprint(entity => {\n    // Define custom logic for handling when the entity starts sprinting\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onSprint(Consumer<LivingEntity> consumer) {
        this.onSprint = consumer;
        return this;
    }

    @Info("Sets the jump boost power for the entity.\n\nExample usage:\n```javascript\nentityBuilder.jumpBoostPower(entity => {\n    return //some float value\n});\n```\n")
    public BaseLivingEntityBuilder<T> jumpBoostPower(Function<LivingEntity, Object> jumpBoostPower) {
        this.jumpBoostPower = jumpBoostPower;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can stand on a fluid.\nThe provided Predicate accepts a {@link ContextUtils.EntityFluidStateContext} parameter,\nrepresenting the context of the entity potentially standing on a fluid.\n\nExample usage:\n```javascript\nentityBuilder.canStandOnFluid(context => {\n    // Define conditions for the entity to be able to stand on a fluid\n    // Use information about the EntityFluidStateContext provided by the context.\n    return // Some boolean condition indicating if the entity can stand on the fluid;\n});\n```\n")
    public BaseLivingEntityBuilder<T> canStandOnFluid(Function<ContextUtils.EntityFluidStateContext, Object> predicate) {
        this.canStandOnFluid = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is sensitive to water.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may be checked for sensitivity to water.\n\nExample usage:\n```javascript\nentityBuilder.isSensitiveToWater(entity => {\n    // Define conditions to check if the entity is sensitive to water\n    // Use information about the LivingEntity provided by the context.\n    return // Some boolean condition indicating if the entity is sensitive to water;\n});\n```\n")
    public BaseLivingEntityBuilder<T> isSensitiveToWater(Function<LivingEntity, Object> predicate) {
        this.isSensitiveToWater = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity stops riding.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that has stopped being ridden.\n\nExample usage:\n```javascript\nentityBuilder.onStopRiding(entity => {\n    // Define custom logic for handling when the entity stops being ridden\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onStopRiding(Consumer<LivingEntity> callback) {
        this.onStopRiding = callback;
        return this;
    }

    @Info("Sets a callback function to be executed during each tick when the entity is being ridden.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is being ridden.\n\nExample usage:\n```javascript\nentityBuilder.rideTick(entity => {\n    // Define custom logic for handling each tick when the entity is being ridden\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> rideTick(Consumer<LivingEntity> callback) {
        this.rideTick = callback;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity picks up an item.\nThe provided Consumer accepts a {@link ContextUtils.EntityItemEntityContext} parameter,\nrepresenting the context of the entity picking up an item with another entity.\n\nExample usage:\n```javascript\nentityBuilder.onItemPickup(context => {\n    // Define custom logic for handling the entity picking up an item\n    // Use information about the EntityItemEntityContext provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onItemPickup(Consumer<ContextUtils.EntityItemEntityContext> consumer) {
        this.onItemPickup = consumer;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity has line of sight to another entity.\nThe provided Function accepts a {@link LineOfSightContext} parameter,\nrepresenting the entity to check for line of sight.\n\nExample usage:\n```javascript\nentityBuilder.hasLineOfSight(context => {\n    // Define conditions to check if the entity has line of sight to the target entity\n    // Use information about the Entity provided by the context.\n    return // Some boolean condition indicating if there is line of sight;\n});\n```\n")
    public BaseLivingEntityBuilder<T> hasLineOfSight(Function<ContextUtils.LineOfSightContext, Object> f) {
        this.hasLineOfSight = f;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity enters combat.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that has entered combat.\n\nExample usage:\n```javascript\nentityBuilder.onEnterCombat(entity => {\n    // Define custom logic for handling the entity entering combat\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onEnterCombat(Consumer<LivingEntity> c) {
        this.onEnterCombat = c;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity leaves combat.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that has left combat.\n\nExample usage:\n```javascript\nentityBuilder.onLeaveCombat(entity => {\n    // Define custom logic for handling the entity leaving combat\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onLeaveCombat(Consumer<LivingEntity> runnable) {
        this.onLeaveCombat = runnable;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is affected by potions.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may be checked for its susceptibility to potions.\n\nExample usage:\n```javascript\nentityBuilder.isAffectedByPotions(entity => {\n    // Define conditions to check if the entity is affected by potions\n    // Use information about the LivingEntity provided by the context.\n    return // Some boolean condition indicating if the entity is affected by potions;\n});\n```\n")
    public BaseLivingEntityBuilder<T> isAffectedByPotions(Function<LivingEntity, Object> predicate) {
        this.isAffectedByPotions = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is attackable.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may be checked for its attackability.\n\nExample usage:\n```javascript\nentityBuilder.isAttackable(entity => {\n    // Define conditions to check if the entity is attackable\n    // Use information about the LivingEntity provided by the context.\n    return // Some boolean condition indicating if the entity is attackable;\n});\n```\n")
    public BaseLivingEntityBuilder<T> isAttackable(Function<LivingEntity, Object> predicate) {
        this.isAttackable = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can take an item.\nThe provided Predicate accepts a {@link ContextUtils.EntityItemLevelContext} parameter,\nrepresenting the context of the entity potentially taking an item.\n\nExample usage:\n```javascript\nentityBuilder.canTakeItem(context => {\n    // Define conditions for the entity to be able to take an item\n    // Use information about the EntityItemLevelContext provided by the context.\n    return // Some boolean condition indicating if the entity can take the item;\n});\n```\n")
    public BaseLivingEntityBuilder<T> canTakeItem(Function<ContextUtils.EntityItemLevelContext, Object> predicate) {
        this.canTakeItem = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is currently sleeping.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may be checked for its sleeping state.\n\nExample usage:\n```javascript\nentityBuilder.isSleeping(entity => {\n    // Define conditions to check if the entity is currently sleeping\n    // Use information about the LivingEntity provided by the context.\n    return // Some boolean condition indicating if the entity is sleeping;\n});\n```\n")
    public BaseLivingEntityBuilder<T> isSleeping(Function<LivingEntity, Object> supplier) {
        this.isSleeping = supplier;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity starts sleeping.\nThe provided Consumer accepts a {@link ContextUtils.EntityBlockPosContext} parameter,\nrepresenting the context of the entity starting to sleep at a specific block position.\n\nExample usage:\n```javascript\nentityBuilder.onStartSleeping(context => {\n    // Define custom logic for handling the entity starting to sleep\n    // Use information about the EntityBlockPosContext provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onStartSleeping(Consumer<ContextUtils.EntityBlockPosContext> consumer) {
        this.onStartSleeping = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity stops sleeping.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that has stopped sleeping.\n\nExample usage:\n```javascript\nentityBuilder.onStopSleeping(entity => {\n    // Define custom logic for handling the entity stopping sleeping\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onStopSleeping(Consumer<LivingEntity> runnable) {
        this.onStopSleeping = runnable;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity performs an eating action.\nThe provided Consumer accepts a {@link ContextUtils.EntityItemLevelContext} parameter,\nrepresenting the context of the entity's interaction with a specific item during eating.\n\nExample usage:\n```javascript\nentityBuilder.eat(context => {\n    // Custom logic to handle the entity's eating action\n    // Access information about the item being consumed using the provided context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> eat(Consumer<ContextUtils.EntityItemLevelContext> function) {
        this.eat = function;
        return this;
    }

    @Info("Sets a predicate function to determine whether the rider of the entity should face forward.\nThe provided Predicate accepts a {@link ContextUtils.PlayerEntityContext} parameter,\nrepresenting the context of the player entity riding the main entity.\n\nExample usage:\n```javascript\nentityBuilder.shouldRiderFaceForward(context => {\n    // Define the conditions for the rider to face forward\n    // Use information about the player entity provided by the context.\n    return true //someBoolean;\n});\n```\n")
    public BaseLivingEntityBuilder<T> shouldRiderFaceForward(Function<ContextUtils.PlayerEntityContext, Object> predicate) {
        this.shouldRiderFaceForward = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can undergo freezing.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may be subjected to freezing.\n\nExample usage:\n```javascript\nentityBuilder.canFreeze(entity => {\n    // Define the conditions for the entity to be able to freeze\n    // Use information about the LivingEntity provided by the context.\n    return true //someBoolean;\n});\n```\n")
    public BaseLivingEntityBuilder<T> canFreeze(Function<LivingEntity, Object> predicate) {
        this.canFreeze = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is currently glowing.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may be checked for its glowing state.\n\nExample usage:\n```javascript\nentityBuilder.isCurrentlyGlowing(entity => {\n    // Define the conditions to check if the entity is currently glowing\n    // Use information about the LivingEntity provided by the context.\n    const isGlowing = // Some boolean condition to check if the entity is glowing;\n    return isGlowing;\n});\n```\n")
    public BaseLivingEntityBuilder<T> isCurrentlyGlowing(Function<LivingEntity, Object> predicate) {
        this.isCurrentlyGlowing = predicate;
        return this;
    }

    @Info("Sets a function to determine whether the entity can disable its target's shield.\nThe provided Predicate accepts a {@link LivingEntity} parameter.\n\nExample usage:\n```javascript\nentityBuilder.canDisableShield(entity => {\n    // Define the conditions to check if the entity can disable its shield\n    // Use information about the LivingEntity provided by the context.\n    return true;\n});\n```\n")
    public BaseLivingEntityBuilder<T> canDisableShield(Function<LivingEntity, Object> predicate) {
        this.canDisableShield = predicate;
        return this;
    }

    @Info("Sets a consumer to handle the interaction with the entity.\nThe provided Consumer accepts a {@link ContextUtils.MobInteractContext} parameter,\nrepresenting the context of the interaction\n\nExample usage:\n```javascript\nentityBuilder.onInteract(context => {\n    // Define custom logic for the interaction with the entity\n    // Use information about the MobInteractContext provided by the context.\n    if (context.player.isShiftKeyDown()) return\n    context.player.startRiding(context.entity);\n});\n```\n")
    public BaseLivingEntityBuilder<T> onInteract(Consumer<ContextUtils.MobInteractContext> c) {
        this.onInteract = c;
        return this;
    }

    @Info("Sets the minimum fall distance for the entity before taking damage.\n\nExample usage:\n```javascript\nentityBuilder.setMaxFallDistance(entity => {\n    // Define custom logic to determine the maximum fall distance\n    // Use information about the LivingEntity provided by the context.\n    return 3;\n});\n```\n")
    public BaseLivingEntityBuilder<T> setMaxFallDistance(Function<LivingEntity, Object> maxFallDistance) {
        this.setMaxFallDistance = maxFallDistance;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is removed on the client side.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is being removed on the client side.\n\nExample usage:\n```javascript\nentityBuilder.onClientRemoval(entity => {\n    // Define custom logic for handling the removal of the entity on the client side\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onClientRemoval(Consumer<LivingEntity> consumer) {
        this.onClientRemoval = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is hurt by lava.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is affected by lava.\n\nExample usage:\n```javascript\nentityBuilder.lavaHurt(entity => {\n    // Define custom logic for handling the entity being hurt by lava\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> lavaHurt(Consumer<LivingEntity> consumer) {
        this.lavaHurt = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity performs a flap action.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is flapping.\n\nExample usage:\n```javascript\nentityBuilder.onFlap(entity => {\n    // Define custom logic for handling the entity's flap action\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onFlap(Consumer<LivingEntity> consumer) {
        this.onFlap = consumer;
        return this;
    }

    @Info("Sets a predicate to determine whether the living entity dampens vibrations.\n\n@param predicate The predicate to determine whether the living entity dampens vibrations.\n\nThe predicate should take a LivingEntity as a parameter and return a boolean value indicating whether the living entity dampens vibrations.\n\nExample usage:\n```javascript\nbaseLivingEntityBuilder.dampensVibrations(entity => {\n    // Determine whether the living entity dampens vibrations\n    // Return true if the entity dampens vibrations, false otherwise\n});\n```\n")
    public BaseLivingEntityBuilder<T> dampensVibrations(Function<LivingEntity, Object> predicate) {
        this.dampensVibrations = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when a player interacts with the entity.\nThe provided Consumer accepts a {@link ContextUtils.PlayerEntityContext} parameter,\nrepresenting the context of the player's interaction with the entity.\n\nExample usage:\n```javascript\nentityBuilder.playerTouch(context => {\n    // Define custom logic for handling player interaction with the entity\n    // Use information about the PlayerEntityContext provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> playerTouch(Consumer<ContextUtils.PlayerEntityContext> consumer) {
        this.playerTouch = consumer;
        return this;
    }

    @Info("Sets a predicate to determine whether to show the vehicle health for the living entity.\n\n@param predicate The predicate to determine whether to show the vehicle health.\n\nThe predicate should take a LivingEntity as a parameter and return a boolean value indicating whether to show the vehicle health.\n\nExample usage:\n```javascript\nbaseLivingEntityBuilder.showVehicleHealth(entity => {\n    // Determine whether to show the vehicle health for the living entity\n    // Return true to show the vehicle health, false otherwise\n});\n```\n")
    public BaseLivingEntityBuilder<T> showVehicleHealth(Function<LivingEntity, Object> predicate) {
        this.showVehicleHealth = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is hit by thunder.\nThe provided Consumer accepts a {@link ContextUtils.ThunderHitContext} parameter,\nrepresenting the context of the entity being hit by thunder.\n\nExample usage:\n```javascript\nentityBuilder.thunderHit(context => {\n    // Define custom logic for handling the entity being hit by thunder\n    // Use information about the ThunderHitContext provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> thunderHit(Consumer<ContextUtils.ThunderHitContext> consumer) {
        this.thunderHit = consumer;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity is invulnerable to a specific type of damage.\nThe provided Predicate accepts a {@link ContextUtils.DamageContext} parameter,\nrepresenting the context of the damage, and returns a boolean indicating invulnerability.\n\nExample usage:\n```javascript\nentityBuilder.isInvulnerableTo(context => {\n    // Define conditions for the entity to be invulnerable to the specific type of damage\n    // Use information about the DamageContext provided by the context.\n    return true // Some boolean condition indicating if the entity has invulnerability to the damage type;\n});\n```\n")
    public BaseLivingEntityBuilder<T> isInvulnerableTo(Function<ContextUtils.DamageContext, Object> predicate) {
        this.isInvulnerableTo = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can change dimensions.\nThe provided Predicate accepts a {@link LivingEntity} parameter,\nrepresenting the entity that may attempt to change dimensions.\n\nExample usage:\n```javascript\nentityBuilder.canChangeDimensions(entity => {\n    // Define the conditions for the entity to be able to change dimensions\n    // Use information about the LivingEntity provided by the context.\n    return false // Some boolean condition indicating if the entity can change dimensions;\n});\n```\n")
    public BaseLivingEntityBuilder<T> canChangeDimensions(Function<LivingEntity, Object> supplier) {
        this.canChangeDimensions = supplier;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity may interact with something.\nThe provided Predicate accepts a {@link ContextUtils.MayInteractContext} parameter,\nrepresenting the context of the potential interaction, and returns a boolean.\n\nExample usage:\n```javascript\nentityBuilder.mayInteract(context => {\n    // Define conditions for the entity to be allowed to interact\n    // Use information about the MayInteractContext provided by the context.\n    return false // Some boolean condition indicating if the entity may interact;\n});\n```\n")
    public BaseLivingEntityBuilder<T> mayInteract(Function<ContextUtils.MayInteractContext, Object> predicate) {
        this.mayInteract = predicate;
        return this;
    }

    @Info("Sets a predicate function to determine whether the entity can trample or step on something.\nThe provided Predicate accepts a {@link ContextUtils.CanTrampleContext} parameter,\nrepresenting the context of the potential trampling action, and returns a boolean.\n\nExample usage:\n```javascript\nentityBuilder.canTrample(context => {\n    // Define conditions for the entity to be allowed to trample\n    // Use information about the CanTrampleContext provided by the context.\n    return false // Some boolean condition indicating if the entity can trample;\n});\n```\n")
    public BaseLivingEntityBuilder<T> canTrample(Function<ContextUtils.CanTrampleContext, Object> predicate) {
        this.canTrample = predicate;
        return this;
    }

    @Info("Sets a callback function to be executed when the entity is removed from the world.\nThe provided Consumer accepts a {@link LivingEntity} parameter,\nrepresenting the entity that is being removed from the world.\n\nExample usage:\n```javascript\nentityBuilder.onRemovedFromWorld(entity => {\n    // Define custom logic for handling the removal of the entity from the world\n    // Use information about the LivingEntity provided by the context.\n});\n```\n")
    public BaseLivingEntityBuilder<T> onRemovedFromWorld(Consumer<LivingEntity> consumer) {
        this.onRemovedFromWorld = consumer;
        return this;
    }

    @Info(value = "Sets the spawn placement of the entity type\nentityBuilder.spawnPlacement('on_ground', 'world_surface', (entitypredicate, levelaccessor, spawntype, blockpos, randomsource) => {\n    if (levelaccessor.getLevel().getBiome(blockpos) == 'minecraft:plains') return true;\n    return false\n})\n", params = { @Param(name = "placementType", value = "The placement type of the spawn, accepts 'on_ground', 'in_water', 'no_restrictions', 'in_lava'"), @Param(name = "heightMap", value = "The height map used for the spawner"), @Param(name = "spawnPredicate", value = "The predicate that determines if the entity will spawn") })
    public BaseLivingEntityBuilder<T> spawnPlacement(SpawnPlacements.Type placementType, Heightmap.Types heightMap, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        spawnList.add(this);
        this.spawnPredicate = spawnPredicate;
        this.placementType = placementType;
        this.heightMap = heightMap;
        return this;
    }

    @Info(value = "Adds a spawner for this entity to the provided biome(s)", params = { @Param(name = "biomes", value = "A list of biomes that the entity should spawn in. If using a tag, only one value may be provided"), @Param(name = "weight", value = "The spawn weight the entity should have"), @Param(name = "minCount", value = "The minimum number of entities that can spawn at a time"), @Param(name = "maxCount", value = "The maximum number of entities that can spawn at a time") })
    public BaseLivingEntityBuilder<T> biomeSpawn(List<String> biomes, int weight, int minCount, int maxCount) {
        biomeSpawnList.add(new EventBasedSpawnModifier.BiomeSpawn(BiomeSpawnsEventJS.processBiomes(biomes), () -> new MobSpawnSettings.SpawnerData(this.get(), Weight.of(weight), minCount, maxCount)));
        return this;
    }

    @Info("Adds an animation controller to the entity with the specified parameters.\n\n@param name The name of the animation controller.\n@param translationTicksLength The length of translation ticks for the animation.\n@param predicate The animation predicate defining the conditions for the animation to be played.\n\nExample usage:\n```javascript\nentityBuilder.addAnimationController('exampleController', 5, event => {\n    // Define conditions for the animation to be played based on the entity.\n    if (event.entity.hurtTime > 0) {\n        event.thenLoop('spawn');\n    } else {\n        event.thenPlayAndHold('idle');\n    }\n    return true; // Some boolean condition indicating if the animation should be played;\n});\n```\n")
    public BaseLivingEntityBuilder<T> addAnimationController(String name, int translationTicksLength, BaseLivingEntityBuilder.IAnimationPredicateJS<T> predicate) {
        return this.addKeyAnimationController(name, translationTicksLength, predicate, null, null, null);
    }

    @Info(value = "Adds a new AnimationController to the entity, with the ability to add event listeners", params = { @Param(name = "name", value = "The name of the controller"), @Param(name = "translationTicksLength", value = "How many ticks it takes to transition between different animations"), @Param(name = "predicate", value = "The predicate for the controller, determines if an animation should continue or not"), @Param(name = "soundListener", value = "A sound listener, used to execute actions when the json requests a sound to play. May be null"), @Param(name = "particleListener", value = "A particle listener, used to execute actions when the json requests a particle. May be null"), @Param(name = "instructionListener", value = "A custom instruction listener, used to execute actions based on arbitrary instructions provided by the json. May be null") })
    public BaseLivingEntityBuilder<T> addKeyAnimationController(String name, int translationTicksLength, BaseLivingEntityBuilder.IAnimationPredicateJS<T> predicate, @Nullable BaseLivingEntityBuilder.ISoundListenerJS<T> soundListener, @Nullable BaseLivingEntityBuilder.IParticleListenerJS<T> particleListener, @Nullable BaseLivingEntityBuilder.ICustomInstructionListenerJS<T> instructionListener) {
        this.animationSuppliers.add(new BaseLivingEntityBuilder.AnimationControllerSupplier<>(name, translationTicksLength, predicate, null, null, null, soundListener, particleListener, instructionListener));
        return this;
    }

    @Info("Sets the render type for the entity.\n\n@param type The render type to be set. Acceptable values are:\n             - \"solid\n             - \"cutout\"\n             - \"translucent\"\n             - RenderType.SOLID\n             - RenderType.CUTOUT\n             - RenderType.TRANSLUCENT\n\nExample usage:\n```javascript\nentityBuilder.setRenderType(\"translucent\");\n```\n")
    public BaseLivingEntityBuilder<T> setRenderType(Object type) {
        if (type instanceof BaseLivingEntityBuilder.RenderType) {
            this.renderType = (BaseLivingEntityBuilder.RenderType) type;
        } else {
            if (!(type instanceof String typeString)) {
                throw new IllegalArgumentException("Invalid render type: " + type);
            }
            String var3 = typeString.toLowerCase();
            switch(var3) {
                case "solid":
                    this.renderType = BaseLivingEntityBuilder.RenderType.SOLID;
                    break;
                case "cutout":
                    this.renderType = BaseLivingEntityBuilder.RenderType.CUTOUT;
                    break;
                case "translucent":
                    this.renderType = BaseLivingEntityBuilder.RenderType.TRANSLUCENT;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid render type string: " + typeString);
            }
        }
        return this;
    }

    public EntityType<T> createObject() {
        return new LivingEntityTypeBuilderJS<T>(this).get();
    }

    @HideFromJS
    public abstract EntityType.EntityFactory<T> factory();

    @HideFromJS
    public abstract AttributeSupplier.Builder getAttributeBuilder();

    @HideFromJS
    @Override
    public RegistryInfo getRegistryType() {
        return RegistryInfo.ENTITY_TYPE;
    }

    @Info(value = "Adds a triggerable AnimationController to the entity callable off the entity's methods anywhere.", params = { @Param(name = "name", value = "The name of the controller"), @Param(name = "translationTicksLength", value = "How many ticks it takes to transition between different animations"), @Param(name = "triggerableAnimationID", value = "The unique identifier of the triggerable animation(sets it apart from other triggerable animations)"), @Param(name = "triggerableAnimationName", value = "The name of the animation defined in the animations.json"), @Param(name = "loopType", value = "The loop type for the triggerable animation, either 'LOOP' or 'PLAY_ONCE' or 'HOLD_ON_LAST_FRAME' or 'DEFAULT'") })
    public BaseLivingEntityBuilder<T> addTriggerableAnimationController(String name, int translationTicksLength, String triggerableAnimationName, String triggerableAnimationID, String loopType) {
        this.animationSuppliers.add(new BaseLivingEntityBuilder.AnimationControllerSupplier<>(name, translationTicksLength, new BaseLivingEntityBuilder.IAnimationPredicateJS<T>() {

            @Override
            public boolean test(BaseLivingEntityBuilder.AnimationEventJS<T> event) {
                return true;
            }
        }, triggerableAnimationName, triggerableAnimationID, loopType, null, null, null));
        return this;
    }

    public static record AnimationControllerSupplier<E extends LivingEntity & IAnimatableJS>(String name, int translationTicksLength, BaseLivingEntityBuilder.IAnimationPredicateJS<E> predicate, String triggerableAnimationName, String triggerableAnimationID, Object loopType, @Nullable BaseLivingEntityBuilder.ISoundListenerJS<E> soundListener, @Nullable BaseLivingEntityBuilder.IParticleListenerJS<E> particleListener, @Nullable BaseLivingEntityBuilder.ICustomInstructionListenerJS<E> instructionListener) {

        public AnimationController<E> get(E entity) {
            AnimationController<E> controller = new AnimationController<>(entity, this.name, this.translationTicksLength, this.predicate.toGecko());
            if (this.triggerableAnimationID != null) {
                Object type = EntityJSHelperClass.convertObjectToDesired(this.loopType, "looptype");
                controller.triggerableAnim(this.triggerableAnimationID, RawAnimation.begin().then(this.triggerableAnimationName, (Animation.LoopType) type));
            }
            if (this.soundListener != null) {
                controller.setSoundKeyframeHandler(event -> this.soundListener.playSound(new BaseLivingEntityBuilder.SoundKeyFrameEventJS<>(event)));
            }
            if (this.particleListener != null) {
                controller.setParticleKeyframeHandler(event -> this.particleListener.summonParticle(new BaseLivingEntityBuilder.ParticleKeyFrameEventJS<>(event)));
            }
            if (this.instructionListener != null) {
                controller.setCustomInstructionKeyframeHandler(event -> this.instructionListener.executeInstruction(new BaseLivingEntityBuilder.CustomInstructionKeyframeEventJS<>(event)));
            }
            return controller;
        }
    }

    public static class AnimationEventJS<E extends LivingEntity & IAnimatableJS> {

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
        public BaseLivingEntityBuilder.AnimationEventJS<E> then(String animationName, Animation.LoopType loopType) {
            this.animationList.add(new RawAnimation.Stage(animationName, loopType));
            return this;
        }

        @Info("Returns any extra data that the event may have\n\nUsually used by armor animations to know what item is worn\n")
        public Map<DataTicket<?>, ?> getExtraData() {
            return this.parent.getExtraData();
        }
    }

    public static class CustomInstructionKeyframeEventJS<E extends LivingEntity & IAnimatableJS> {

        @Info("A list of all the custom instructions. In blockbench, each line in the custom instruction box is a separate instruction.")
        public final String instructions;

        public CustomInstructionKeyframeEventJS(CustomInstructionKeyframeEvent<E> parent) {
            this.instructions = parent.getKeyframeData().getInstructions();
        }
    }

    @FunctionalInterface
    public interface IAnimationPredicateJS<E extends LivingEntity & IAnimatableJS> {

        @Info(value = "Determines if an animation should continue for a given AnimationEvent. Return true to continue the current animation", params = { @Param(name = "event", value = "The AnimationEvent, provides values that can be used to determine if the animation should continue or not") })
        boolean test(BaseLivingEntityBuilder.AnimationEventJS<E> event);

        default AnimationController.AnimationStateHandler<E> toGecko() {
            return event -> {
                if (event != null) {
                    BaseLivingEntityBuilder.AnimationEventJS<E> animationEventJS = new BaseLivingEntityBuilder.AnimationEventJS<>(event);
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
    public interface ICustomInstructionListenerJS<E extends LivingEntity & IAnimatableJS> {

        void executeInstruction(BaseLivingEntityBuilder.CustomInstructionKeyframeEventJS<E> event);
    }

    @FunctionalInterface
    public interface IParticleListenerJS<E extends LivingEntity & IAnimatableJS> {

        void summonParticle(BaseLivingEntityBuilder.ParticleKeyFrameEventJS<E> event);
    }

    @FunctionalInterface
    public interface ISoundListenerJS<E extends LivingEntity & IAnimatableJS> {

        void playSound(BaseLivingEntityBuilder.SoundKeyFrameEventJS<E> event);
    }

    public static class KeyFrameEventJS<E extends LivingEntity & IAnimatableJS, B extends KeyFrameData> {

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

    public static class ParticleKeyFrameEventJS<E extends LivingEntity & IAnimatableJS> {

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

    public static class SoundKeyFrameEventJS<E extends LivingEntity & IAnimatableJS> {

        @Info("The name of the sound to play")
        public final String sound;

        public SoundKeyFrameEventJS(SoundKeyframeEvent<E> parent) {
            this.sound = parent.getKeyframeData().getSound();
        }
    }
}
package net.liopyu.entityjs.entities.living.entityjs;

import com.mojang.serialization.Dynamic;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.liopyu.entityjs.builders.living.entityjs.BaseLivingEntityJSBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.PartEntityJS;
import net.liopyu.entityjs.events.BuildBrainEventJS;
import net.liopyu.entityjs.events.BuildBrainProviderEventJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.liopyu.entityjs.util.EventHandlers;
import net.liopyu.entityjs.util.ModKeybinds;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BaseLivingEntityJS extends LivingEntity implements IAnimatableJS {

    private final AnimatableInstanceCache getAnimatableInstanceCache;

    private final BaseLivingEntityJSBuilder builder;

    public final PartEntityJS<?>[] partEntities;

    private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);

    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);

    protected boolean thisJumping = false;

    public String entityName() {
        return this.m_6095_().toString();
    }

    public BaseLivingEntityJS(BaseLivingEntityJSBuilder builder, EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.builder = builder;
        this.getAnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
        List<PartEntityJS<?>> tempPartEntities = new ArrayList();
        for (ContextUtils.PartEntityParams<BaseLivingEntityJS> params : builder.partEntityParamsList) {
            PartEntityJS<BaseLivingEntityJS> partEntity = new PartEntityJS<>(this, params.name, params.width, params.height, params.builder);
            tempPartEntities.add(partEntity);
        }
        this.partEntities = (PartEntityJS<?>[]) tempPartEntities.toArray(new PartEntityJS[0]);
    }

    @Override
    public void setId(int entityId) {
        super.m_20234_(entityId);
        for (int i = 0; i < this.partEntities.length; i++) {
            PartEntityJS<?> partEntity = this.partEntities[i];
            if (partEntity != null) {
                partEntity.m_20234_(entityId + i + 1);
            }
        }
    }

    public void tickPart(String partName, double offsetX, double offsetY, double offsetZ) {
        double x = this.m_20185_();
        double y = this.m_20186_();
        double z = this.m_20189_();
        for (PartEntityJS<?> partEntity : this.partEntities) {
            if (partEntity.name.equals(partName)) {
                partEntity.movePart(x + offsetX, y + offsetY, z + offsetZ, partEntity.m_146908_(), partEntity.m_146909_());
                return;
            }
        }
        EntityJSHelperClass.logWarningMessageOnce("Part with name " + partName + " not found for entity: " + this.entityName());
    }

    public boolean isMultipartEntity() {
        return this.partEntities != null;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
    }

    public PartEntity<?>[] getParts() {
        return (PartEntity<?>[]) Objects.requireNonNullElseGet(this.partEntities, () -> new PartEntity[0]);
    }

    @Override
    public BaseLivingEntityBuilder<?> getBuilder() {
        return this.builder;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.getAnimatableInstanceCache;
    }

    @Override
    public Brain.Provider<?> brainProvider() {
        if (EventHandlers.buildBrainProvider.hasListeners()) {
            BuildBrainProviderEventJS<BaseLivingEntityJS> event = new BuildBrainProviderEventJS<>();
            EventHandlers.buildBrainProvider.post(event, this.getTypeId());
            return event.provide();
        } else {
            return super.brainProvider();
        }
    }

    @Override
    protected Brain<BaseLivingEntityJS> makeBrain(Dynamic<?> dynamic0) {
        if (EventHandlers.buildBrain.hasListeners()) {
            Brain<BaseLivingEntityJS> brain = UtilsJS.cast(this.brainProvider().makeBrain(dynamic0));
            EventHandlers.buildBrain.post(new BuildBrainEventJS<>(brain), this.getTypeId());
            return brain;
        } else {
            return UtilsJS.cast(super.makeBrain(dynamic0));
        }
    }

    public void onJump() {
        if (this.builder.onLivingJump != null) {
            this.builder.onLivingJump.accept(this);
        }
    }

    public void jump() {
        double jumpPower = (double) (this.m_6118_() + this.getJumpBoostPower());
        Vec3 currentVelocity = this.m_20184_();
        this.m_20334_(currentVelocity.x, jumpPower, currentVelocity.z);
        this.f_19812_ = true;
        if (this.m_20142_()) {
            float yawRadians = this.m_146908_() * (float) (Math.PI / 180.0);
            this.m_20256_(this.m_20184_().add(-Math.sin((double) yawRadians) * 0.2, 0.0, Math.cos((double) yawRadians) * 0.2));
        }
        this.f_19812_ = true;
        this.onJump();
        ForgeHooks.onLivingJump(this);
    }

    public boolean shouldJump() {
        BlockPos forwardPos = this.m_20183_().relative(this.m_6350_());
        return this.m_9236_().loadedAndEntityCanStandOn(forwardPos, this) && (double) this.getStepHeight() < this.m_9236_().getBlockState(forwardPos).m_60808_(this.m_9236_(), forwardPos).max(Direction.Axis.Y);
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (this.builder.onInteract != null) {
            ContextUtils.MobInteractContext context = new ContextUtils.MobInteractContext(this, pPlayer, pHand);
            this.builder.onInteract.accept(context);
        }
        return super.m_6096_(pPlayer, pHand);
    }

    @Override
    public HumanoidArm getMainArm() {
        return this.builder.mainArm != null ? (HumanoidArm) this.builder.mainArm : HumanoidArm.RIGHT;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.builder.aiStep != null) {
            this.builder.aiStep.accept(this);
        }
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (this.builder.isAlliedTo != null) {
            ContextUtils.LineOfSightContext context = new ContextUtils.LineOfSightContext(pEntity, this);
            Object obj = this.builder.isAlliedTo.apply(context);
            if (obj instanceof Boolean b) {
                return b;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isAlliedTo from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_7307_(pEntity));
        }
        return super.m_7307_(pEntity);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if (this.builder != null && this.builder.onHurtTarget != null) {
            ContextUtils.LineOfSightContext context = new ContextUtils.LineOfSightContext(pEntity, this);
            EntityJSHelperClass.consumerCallback(this.builder.onHurtTarget, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHurtTarget.");
        }
        return super.doHurtTarget(pEntity);
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        LivingEntity livingentity = this.getControllingPassenger();
        if (this.m_6084_() && this.m_20160_() && this.builder.canSteer && livingentity != null) {
            if (this.getControllingPassenger() instanceof Player && this.builder.mountJumpingEnabled) {
                if (this.ableToJump()) {
                    this.setThisJumping(true);
                }
                if (this.thisJumping) {
                    this.setThisJumping(false);
                    double jumpPower = (double) (this.m_6118_() + this.getJumpBoostPower());
                    Vec3 currentVelocity = this.m_20184_();
                    double newVelocityX = currentVelocity.x;
                    double newVelocityY = currentVelocity.y + jumpPower;
                    double newVelocityZ = currentVelocity.z;
                    this.m_20334_(newVelocityX, newVelocityY, newVelocityZ);
                    this.onJump();
                    ForgeHooks.onLivingJump(this);
                }
            }
            LivingEntity passenger = this.getControllingPassenger();
            this.f_19859_ = this.m_146908_();
            this.f_19860_ = this.m_146909_();
            this.m_146922_(passenger.m_146908_());
            this.m_146926_(passenger.m_146909_() * 0.5F);
            this.m_19915_(this.m_146908_(), this.m_146909_());
            this.f_20883_ = this.m_146908_();
            this.f_20885_ = this.f_20883_;
            float x = passenger.xxa * 0.5F;
            float z = passenger.zza;
            if (z <= 0.0F) {
                z *= 0.25F;
            }
            this.m_7910_((float) this.m_21133_(Attributes.MOVEMENT_SPEED));
            super.travel(new Vec3((double) x, pTravelVector.y, (double) z));
        } else {
            super.travel(pTravelVector);
        }
        if (this.builder.travel != null) {
            ContextUtils.Vec3Context context = new ContextUtils.Vec3Context(pTravelVector, this);
            EntityJSHelperClass.consumerCallback(this.builder.travel, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: travel.");
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.builder.tick != null && !this.m_9236_().isClientSide()) {
            EntityJSHelperClass.consumerCallback(this.builder.tick, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: tick.");
        }
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.builder.onAddedToWorld != null && !this.m_9236_().isClientSide()) {
            EntityJSHelperClass.consumerCallback(this.builder.onAddedToWorld, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onAddedToWorld.");
        }
    }

    @Override
    protected void doAutoAttackOnTouch(@NotNull LivingEntity target) {
        super.doAutoAttackOnTouch(target);
        if (this.builder.doAutoAttackOnTouch != null) {
            ContextUtils.AutoAttackContext context = new ContextUtils.AutoAttackContext(this, target);
            EntityJSHelperClass.consumerCallback(this.builder.doAutoAttackOnTouch, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: doAutoAttackOnTouch.");
        }
    }

    @Override
    protected int decreaseAirSupply(int int0) {
        if (this.builder.onDecreaseAirSupply != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onDecreaseAirSupply, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onDecreaseAirSupply.");
        }
        return super.decreaseAirSupply(int0);
    }

    @Override
    protected int increaseAirSupply(int int0) {
        if (this.builder.onIncreaseAirSupply != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onIncreaseAirSupply, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onIncreaseAirSupply.");
        }
        return super.increaseAirSupply(int0);
    }

    @Override
    protected void blockedByShield(@NotNull LivingEntity livingEntity0) {
        super.blockedByShield(livingEntity0);
        if (this.builder.onBlockedByShield != null) {
            ContextUtils.LivingEntityContext context = new ContextUtils.LivingEntityContext(this, livingEntity0);
            EntityJSHelperClass.consumerCallback(this.builder.onBlockedByShield, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onDecreaseAirSupply.");
        }
    }

    @Override
    public void onEquipItem(EquipmentSlot slot, ItemStack previous, ItemStack current) {
        super.onEquipItem(slot, previous, current);
        if (this.builder.onEquipItem != null) {
            ContextUtils.EntityEquipmentContext context = new ContextUtils.EntityEquipmentContext(slot, previous, current, this);
            EntityJSHelperClass.consumerCallback(this.builder.onEquipItem, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onEquipItem.");
        }
    }

    @Override
    public void onEffectAdded(@NotNull MobEffectInstance effectInstance, @Nullable Entity entity) {
        if (this.builder.onEffectAdded != null) {
            ContextUtils.OnEffectContext context = new ContextUtils.OnEffectContext(effectInstance, this);
            EntityJSHelperClass.consumerCallback(this.builder.onEffectAdded, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onEffectAdded.");
        } else {
            super.onEffectAdded(effectInstance, entity);
        }
    }

    @Override
    protected void onEffectRemoved(@NotNull MobEffectInstance effectInstance) {
        if (this.builder.onEffectRemoved != null) {
            ContextUtils.OnEffectContext context = new ContextUtils.OnEffectContext(effectInstance, this);
            EntityJSHelperClass.consumerCallback(this.builder.onEffectRemoved, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onEffectRemoved.");
        } else {
            super.onEffectRemoved(effectInstance);
        }
    }

    @Override
    public void heal(float amount) {
        super.heal(amount);
        if (this.builder.onLivingHeal != null) {
            ContextUtils.EntityHealContext context = new ContextUtils.EntityHealContext(this, amount);
            EntityJSHelperClass.consumerCallback(this.builder.onLivingHeal, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onLivingHeal.");
        }
    }

    @Override
    public void die(@NotNull DamageSource damageSource) {
        super.die(damageSource);
        if (this.builder.onDeath != null) {
            ContextUtils.DeathContext context = new ContextUtils.DeathContext(this, damageSource);
            EntityJSHelperClass.consumerCallback(this.builder.onDeath, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onDeath.");
        }
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource damageSource, int lootingMultiplier, boolean allowDrops) {
        if (this.builder.dropCustomDeathLoot != null) {
            ContextUtils.EntityLootContext context = new ContextUtils.EntityLootContext(damageSource, lootingMultiplier, allowDrops, this);
            EntityJSHelperClass.consumerCallback(this.builder.dropCustomDeathLoot, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: dropCustomDeathLoot.");
        } else {
            super.dropCustomDeathLoot(damageSource, lootingMultiplier, allowDrops);
        }
    }

    @Override
    protected void onFlap() {
        if (this.builder.onFlap != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onFlap, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onFlap.");
        }
        super.m_142043_();
    }

    public boolean ableToJump() {
        return ModKeybinds.mount_jump.isDown() && this.m_20096_();
    }

    public void setThisJumping(boolean value) {
        this.thisJumping = value;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        LivingEntity var10000;
        if (this.m_146895_() instanceof LivingEntity entity) {
            var10000 = entity;
        } else {
            var10000 = null;
        }
        return var10000;
    }

    @Info("Calls a triggerable animation to be played anywhere.\n")
    public void triggerAnimation(String controllerName, String animName) {
        this.triggerAnim(controllerName, animName);
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        if (this.builder.canCollideWith != null) {
            ContextUtils.CollidingEntityContext context = new ContextUtils.CollidingEntityContext(this, pEntity);
            Object obj = this.builder.canCollideWith.apply(context);
            if (obj instanceof Boolean b) {
                return b;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canCollideWith from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_7337_(pEntity));
        }
        return super.m_7337_(pEntity);
    }

    @Override
    protected float getSoundVolume() {
        return (Float) Objects.requireNonNullElseGet(this.builder.setSoundVolume, () -> super.getSoundVolume());
    }

    @Override
    protected float getWaterSlowDown() {
        return (Float) Objects.requireNonNullElseGet(this.builder.setWaterSlowDown, () -> super.getWaterSlowDown());
    }

    @Override
    protected float getBlockJumpFactor() {
        if (this.builder.setBlockJumpFactor == null) {
            return super.m_20098_();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.setBlockJumpFactor.apply(this), "float");
            if (obj != null) {
                return (Float) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for setBlockJumpFactor from entity: " + this.entityName() + ". Value: " + this.builder.setBlockJumpFactor.apply(this) + ". Must be a float. Defaulting to " + super.m_20098_());
                return super.m_20098_();
            }
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        if (this.builder != null && this.builder.setStandingEyeHeight != null) {
            ContextUtils.EntityPoseDimensionsContext context = new ContextUtils.EntityPoseDimensionsContext(pPose, pDimensions, this);
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.setStandingEyeHeight.apply(context), "float");
            if (obj != null) {
                return (Float) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for setStandingEyeHeight from entity: " + this.entityName() + ". Value: " + this.builder.setStandingEyeHeight.apply(context) + ". Must be a float. Defaulting to " + super.getStandingEyeHeight(pPose, pDimensions));
                return super.getStandingEyeHeight(pPose, pDimensions);
            }
        } else {
            return super.getStandingEyeHeight(pPose, pDimensions);
        }
    }

    @Override
    public boolean isPushable() {
        return this.builder.isPushable;
    }

    @Override
    protected float getBlockSpeedFactor() {
        if (this.builder.blockSpeedFactor == null) {
            return super.getBlockSpeedFactor();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.blockSpeedFactor.apply(this), "float");
            if (this.builder.blockSpeedFactor == null) {
                return super.getBlockSpeedFactor();
            } else if (obj != null) {
                return (Float) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for blockSpeedFactor from entity: " + this.builder.get() + ". Value: " + this.builder.blockSpeedFactor.apply(this) + ". Must be a float, defaulting to " + super.getBlockSpeedFactor());
                return super.getBlockSpeedFactor();
            }
        }
    }

    @Override
    protected void positionRider(Entity pPassenger, Entity.MoveFunction pCallback) {
        if (this.builder.positionRider != null) {
            ContextUtils.PositionRiderContext context = new ContextUtils.PositionRiderContext(this, pPassenger, pCallback);
            EntityJSHelperClass.consumerCallback(this.builder.positionRider, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: positionRider.");
        } else {
            super.m_19956_(pPassenger, pCallback);
        }
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity entity) {
        if (this.builder.canAddPassenger == null) {
            return super.m_7310_(entity);
        } else {
            ContextUtils.PassengerEntityContext context = new ContextUtils.PassengerEntityContext(entity, this);
            Object obj = this.builder.canAddPassenger.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canAddPassenger from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean, defaulting to " + super.m_7310_(entity));
                return super.m_7310_(entity);
            }
        }
    }

    @Override
    protected boolean shouldDropLoot() {
        if (this.builder.shouldDropLoot != null) {
            Object obj = this.builder.shouldDropLoot.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for shouldDropLoot from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean, defaulting to " + super.shouldDropLoot());
        }
        return super.shouldDropLoot();
    }

    @Override
    protected boolean isAffectedByFluids() {
        if (this.builder.isAffectedByFluids != null) {
            Object obj = this.builder.isAffectedByFluids.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isAffectedByFluids from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.isAffectedByFluids());
        }
        return super.isAffectedByFluids();
    }

    @Override
    protected boolean isAlwaysExperienceDropper() {
        return this.builder.isAlwaysExperienceDropper;
    }

    @Override
    protected boolean isImmobile() {
        if (this.builder.isImmobile != null) {
            Object obj = this.builder.isImmobile.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isImmobile from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.isImmobile());
        }
        return super.isImmobile();
    }

    @Override
    protected boolean isFlapping() {
        if (this.builder.isFlapping != null) {
            Object obj = this.builder.isFlapping.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isFlapping from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_142039_());
        }
        return super.m_142039_();
    }

    @Override
    public int calculateFallDamage(float fallDistance, float pDamageMultiplier) {
        if (this.builder.calculateFallDamage == null) {
            return super.calculateFallDamage(fallDistance, pDamageMultiplier);
        } else {
            ContextUtils.CalculateFallDamageContext context = new ContextUtils.CalculateFallDamageContext(fallDistance, pDamageMultiplier, this);
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.calculateFallDamage.apply(context), "integer");
            if (obj != null) {
                return (Integer) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for calculateFallDamage from entity: " + this.entityName() + ". Value: " + this.builder.calculateFallDamage.apply(context) + ". Must be an int, defaulting to " + super.calculateFallDamage(fallDistance, pDamageMultiplier));
                return super.calculateFallDamage(fallDistance, pDamageMultiplier);
            }
        }
    }

    @Override
    protected boolean repositionEntityAfterLoad() {
        return (Boolean) Objects.requireNonNullElseGet(this.builder.repositionEntityAfterLoad, () -> super.m_6093_());
    }

    @Override
    protected float nextStep() {
        if (this.builder.nextStep != null) {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.nextStep.apply(this), "float");
            if (obj != null) {
                return (Float) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for nextStep from entity: " + this.entityName() + ". Value: " + this.builder.nextStep.apply(this) + ". Must be a float, defaulting to " + super.m_6059_());
        }
        return super.m_6059_();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource0) {
        if (this.builder.setHurtSound == null) {
            return super.getHurtSound(damageSource0);
        } else {
            ContextUtils.HurtContext context = new ContextUtils.HurtContext(this, damageSource0);
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.setHurtSound.apply(context), "resourcelocation");
            if (obj != null) {
                return (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) obj));
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for setHurtSound from entity: " + this.entityName() + ". Value: " + this.builder.setHurtSound.apply(context) + ". Must be a ResourceLocation or String. Defaulting to \"minecraft:entity.generic.hurt\"");
                return super.getHurtSound(damageSource0);
            }
        }
    }

    @Override
    protected SoundEvent getSwimSplashSound() {
        return this.builder.setSwimSplashSound == null ? super.m_5509_() : (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.setSwimSplashSound));
    }

    @Override
    protected SoundEvent getSwimSound() {
        return this.builder.setSwimSound == null ? super.m_5501_() : (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.setSwimSound));
    }

    @Override
    public boolean canAttackType(@NotNull EntityType<?> entityType) {
        if (this.builder.canAttackType != null) {
            ContextUtils.EntityTypeEntityContext context = new ContextUtils.EntityTypeEntityContext(this, entityType);
            Object obj = this.builder.canAttackType.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canAttackType from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.canAttackType(entityType));
        }
        return super.canAttackType(entityType);
    }

    @Override
    public float getScale() {
        if (this.builder.scale == null) {
            return super.getScale();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.scale.apply(this), "float");
            if (obj != null) {
                return (Float) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for scale from entity: " + this.entityName() + ". Value: " + this.builder.scale.apply(this) + ". Must be a float. Defaulting to " + super.getScale());
                return super.getScale();
            }
        }
    }

    @Override
    public boolean shouldDropExperience() {
        if (this.builder.shouldDropExperience != null) {
            Object obj = this.builder.shouldDropExperience.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for shouldDropExperience from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.shouldDropExperience());
        }
        return super.shouldDropExperience();
    }

    @Override
    public double getVisibilityPercent(@Nullable Entity entity0) {
        if (this.builder.visibilityPercent != null) {
            ContextUtils.VisualContext context = new ContextUtils.VisualContext(entity0, this);
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.visibilityPercent.apply(context), "double");
            if (obj != null) {
                return (Double) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for visibilityPercent from entity: " + this.entityName() + ". Value: " + this.builder.visibilityPercent.apply(context) + ". Must be a double. Defaulting to " + super.getVisibilityPercent(entity0));
                return super.getVisibilityPercent(entity0);
            }
        } else {
            return super.getVisibilityPercent(entity0);
        }
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity entity) {
        if (this.builder.canAttack != null) {
            ContextUtils.LivingEntityContext context = new ContextUtils.LivingEntityContext(this, entity);
            Object obj = this.builder.canAttack.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj && super.canAttack(entity);
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canAttack from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.canAttack(entity));
        }
        return super.canAttack(entity);
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance effectInstance) {
        if (this.builder.canBeAffected == null) {
            return super.canBeAffected(effectInstance);
        } else {
            ContextUtils.OnEffectContext context = new ContextUtils.OnEffectContext(effectInstance, this);
            Object result = this.builder.canBeAffected.apply(context);
            if (result instanceof Boolean) {
                return (Boolean) result;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canBeAffected from entity: " + this.entityName() + ". Value: " + result + ". Must be a boolean. Defaulting to " + super.canBeAffected(effectInstance));
                return super.canBeAffected(effectInstance);
            }
        }
    }

    @Override
    public boolean isInvertedHealAndHarm() {
        if (this.builder.invertedHealAndHarm == null) {
            return super.isInvertedHealAndHarm();
        } else {
            Object obj = this.builder.invertedHealAndHarm.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for invertedHealAndHarm from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.isInvertedHealAndHarm());
                return super.isInvertedHealAndHarm();
            }
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.builder.setDeathSound == null ? super.getDeathSound() : (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.setDeathSound));
    }

    @NotNull
    @Override
    public LivingEntity.Fallsounds getFallSounds() {
        return this.builder.fallSounds != null ? new LivingEntity.Fallsounds((SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.smallFallSound)), (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.largeFallSound))) : super.getFallSounds();
    }

    @NotNull
    @Override
    public SoundEvent getEatingSound(@NotNull ItemStack itemStack) {
        return this.builder.eatingSound != null ? (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.eatingSound)) : super.getEatingSound(itemStack);
    }

    @Override
    public boolean onClimbable() {
        if (this.builder.onClimbable == null) {
            return super.onClimbable();
        } else {
            Object obj = this.builder.onClimbable.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for onClimbable from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to super.onClimbable(): " + super.onClimbable());
                return super.onClimbable();
            }
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return (Boolean) Objects.requireNonNullElseGet(this.builder.canBreatheUnderwater, () -> super.canBreatheUnderwater());
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, @NotNull DamageSource damageSource) {
        if (this.builder.onLivingFall != null) {
            ContextUtils.EntityFallDamageContext context = new ContextUtils.EntityFallDamageContext(this, damageMultiplier, distance, damageSource);
            EntityJSHelperClass.consumerCallback(this.builder.onLivingFall, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onLivingFall.");
        }
        return super.causeFallDamage(distance, damageMultiplier, damageSource);
    }

    @Override
    public void setSprinting(boolean sprinting) {
        if (this.builder.onSprint != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onSprint, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onSprint.");
        }
        super.setSprinting(sprinting);
    }

    @Override
    public float getJumpBoostPower() {
        if (this.builder.jumpBoostPower == null) {
            return super.getJumpBoostPower();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.jumpBoostPower.apply(this), "float");
            if (obj != null) {
                return (Float) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for jumpBoostPower from entity: " + this.entityName() + ". Value: " + this.builder.jumpBoostPower.apply(this) + ". Must be a float. Defaulting to " + super.getJumpBoostPower());
                return super.getJumpBoostPower();
            }
        }
    }

    @Override
    public boolean canStandOnFluid(@NotNull FluidState fluidState) {
        if (this.builder.canStandOnFluid != null) {
            ContextUtils.EntityFluidStateContext context = new ContextUtils.EntityFluidStateContext(this, fluidState);
            Object obj = this.builder.canStandOnFluid.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canStandOnFluid from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.canStandOnFluid(fluidState));
        }
        return super.canStandOnFluid(fluidState);
    }

    @Override
    public boolean isSensitiveToWater() {
        if (this.builder.isSensitiveToWater != null) {
            Object obj = this.builder.isSensitiveToWater.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isSensitiveToWater from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.isSensitiveToWater());
        }
        return super.isSensitiveToWater();
    }

    @Override
    public void stopRiding() {
        super.stopRiding();
        if (this.builder.onStopRiding != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onStopRiding, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onStopRiding.");
        }
    }

    @Override
    public void rideTick() {
        super.rideTick();
        if (this.builder.rideTick != null) {
            EntityJSHelperClass.consumerCallback(this.builder.rideTick, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: rideTick.");
        }
    }

    @Override
    public void onItemPickup(@NotNull ItemEntity itemEntity0) {
        super.onItemPickup(itemEntity0);
        if (this.builder.onItemPickup != null) {
            ContextUtils.EntityItemEntityContext context = new ContextUtils.EntityItemEntityContext(this, itemEntity0);
            EntityJSHelperClass.consumerCallback(this.builder.onItemPickup, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onItemPickup.");
        }
    }

    @Override
    public boolean hasLineOfSight(@NotNull Entity entity) {
        if (this.builder.hasLineOfSight != null) {
            ContextUtils.LineOfSightContext context = new ContextUtils.LineOfSightContext(entity, this);
            Object obj = this.builder.hasLineOfSight.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for hasLineOfSight from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.hasLineOfSight(entity));
        }
        return super.hasLineOfSight(entity);
    }

    @Override
    public void onEnterCombat() {
        if (this.builder.onEnterCombat != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onEnterCombat, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onEnterCombat.");
        } else {
            super.onEnterCombat();
        }
    }

    @Override
    public void onLeaveCombat() {
        if (this.builder.onLeaveCombat != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onLeaveCombat, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onLeaveCombat.");
        }
        super.onLeaveCombat();
    }

    @Override
    public boolean isAffectedByPotions() {
        if (this.builder.isAffectedByPotions != null) {
            Object obj = this.builder.isAffectedByPotions.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isAffectedByPotions from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.isAffectedByPotions());
        }
        return super.isAffectedByPotions();
    }

    @Override
    public boolean attackable() {
        if (this.builder.isAttackable != null) {
            Object obj = this.builder.isAttackable.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isAttackable from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.attackable());
        }
        return super.attackable();
    }

    @Override
    public boolean canTakeItem(@NotNull ItemStack itemStack) {
        if (this.builder.canTakeItem != null) {
            ContextUtils.EntityItemLevelContext context = new ContextUtils.EntityItemLevelContext(this, itemStack, this.m_9236_());
            Object obj = this.builder.canTakeItem.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canTakeItem from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.canTakeItem(itemStack));
        }
        return super.canTakeItem(itemStack);
    }

    @Override
    public boolean isSleeping() {
        if (this.builder.isSleeping != null) {
            Object obj = this.builder.isSleeping.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isSleeping from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.isSleeping());
        }
        return super.isSleeping();
    }

    @Override
    public void startSleeping(@NotNull BlockPos blockPos) {
        if (this.builder.onStartSleeping != null) {
            ContextUtils.EntityBlockPosContext context = new ContextUtils.EntityBlockPosContext(this, blockPos);
            EntityJSHelperClass.consumerCallback(this.builder.onStartSleeping, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onStartSleeping.");
        }
        super.startSleeping(blockPos);
    }

    @Override
    public void stopSleeping() {
        if (this.builder.onStopSleeping != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onStopSleeping, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onStopSleeping.");
        }
        super.stopSleeping();
    }

    @NotNull
    @Override
    public ItemStack eat(@NotNull Level level, @NotNull ItemStack itemStack) {
        if (this.builder.eat != null) {
            ContextUtils.EntityItemLevelContext context = new ContextUtils.EntityItemLevelContext(this, itemStack, level);
            EntityJSHelperClass.consumerCallback(this.builder.eat, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: eat.");
            return itemStack;
        } else {
            return super.eat(level, itemStack);
        }
    }

    public boolean shouldRiderFaceForward(@NotNull Player player) {
        if (this.builder.shouldRiderFaceForward != null) {
            ContextUtils.PlayerEntityContext context = new ContextUtils.PlayerEntityContext(player, this);
            Object obj = this.builder.shouldRiderFaceForward.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for shouldRiderFaceForward from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.shouldRiderFaceForward(player));
        }
        return super.shouldRiderFaceForward(player);
    }

    @Override
    public boolean canFreeze() {
        if (this.builder.canFreeze != null) {
            Object obj = this.builder.canFreeze.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canFreeze from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.canFreeze());
        }
        return super.canFreeze();
    }

    @Override
    public boolean isFreezing() {
        if (this.builder.isFreezing != null) {
            Object obj = this.builder.isFreezing.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isFreezing from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_203117_());
        }
        return super.m_203117_();
    }

    @Override
    public boolean isCurrentlyGlowing() {
        if (this.builder.isCurrentlyGlowing != null && !this.m_9236_().isClientSide()) {
            Object obj = this.builder.isCurrentlyGlowing.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isCurrentlyGlowing from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.isCurrentlyGlowing());
        }
        return super.isCurrentlyGlowing();
    }

    @Override
    public boolean canDisableShield() {
        if (this.builder.canDisableShield != null) {
            Object obj = this.builder.canDisableShield.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canDisableShield from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.canDisableShield());
        }
        return super.canDisableShield();
    }

    @Override
    public void onClientRemoval() {
        if (this.builder.onClientRemoval != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onClientRemoval, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onClientRemoval.");
        }
        super.m_142036_();
    }

    @Override
    public void actuallyHurt(DamageSource pDamageSource, float pDamageAmount) {
        if (this.builder.onHurt != null) {
            ContextUtils.EntityDamageContext context = new ContextUtils.EntityDamageContext(pDamageSource, pDamageAmount, this);
            EntityJSHelperClass.consumerCallback(this.builder.onHurt, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHurt.");
        }
        super.actuallyHurt(pDamageSource, pDamageAmount);
    }

    @Override
    public void lavaHurt() {
        if (this.builder.lavaHurt != null) {
            EntityJSHelperClass.consumerCallback(this.builder.lavaHurt, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: lavaHurt.");
        }
        super.m_20093_();
    }

    @Override
    public int getExperienceReward() {
        if (this.builder.experienceReward != null) {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.experienceReward.apply(this), "integer");
            if (obj != null) {
                return (Integer) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for experienceReward from entity: " + this.entityName() + ". Value: " + this.builder.experienceReward.apply(this) + ". Must be an integer. Defaulting to " + super.getExperienceReward());
        }
        return super.getExperienceReward();
    }

    @Override
    public boolean dampensVibrations() {
        if (this.builder.dampensVibrations != null) {
            Object obj = this.builder.dampensVibrations.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for dampensVibrations from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_213854_());
        }
        return super.m_213854_();
    }

    @Override
    public void playerTouch(Player player0) {
        if (this.builder.playerTouch != null) {
            ContextUtils.PlayerEntityContext context = new ContextUtils.PlayerEntityContext(player0, this);
            EntityJSHelperClass.consumerCallback(this.builder.playerTouch, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: playerTouch.");
        }
    }

    @Override
    public boolean showVehicleHealth() {
        if (this.builder.showVehicleHealth != null) {
            Object obj = this.builder.showVehicleHealth.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for showVehicleHealth from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_20152_());
        }
        return super.m_20152_();
    }

    @Override
    public void thunderHit(ServerLevel serverLevel0, LightningBolt lightningBolt1) {
        if (this.builder.thunderHit != null) {
            super.m_8038_(serverLevel0, lightningBolt1);
            ContextUtils.ThunderHitContext context = new ContextUtils.ThunderHitContext(serverLevel0, lightningBolt1, this);
            EntityJSHelperClass.consumerCallback(this.builder.thunderHit, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: thunderHit.");
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource0) {
        if (this.builder.isInvulnerableTo != null) {
            ContextUtils.DamageContext context = new ContextUtils.DamageContext(this, damageSource0);
            Object obj = this.builder.isInvulnerableTo.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isInvulnerableTo from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_6673_(damageSource0));
        }
        return super.m_6673_(damageSource0);
    }

    @Override
    public boolean canChangeDimensions() {
        if (this.builder.canChangeDimensions != null) {
            Object obj = this.builder.canChangeDimensions.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canChangeDimensions from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.canChangeDimensions());
        }
        return super.canChangeDimensions();
    }

    @Override
    public boolean mayInteract(@NotNull Level level0, @NotNull BlockPos blockPos1) {
        if (this.builder.mayInteract != null) {
            ContextUtils.MayInteractContext context = new ContextUtils.MayInteractContext(level0, blockPos1, this);
            Object obj = this.builder.mayInteract.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for mayInteract from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_142265_(level0, blockPos1));
        }
        return super.m_142265_(level0, blockPos1);
    }

    public boolean canTrample(@NotNull BlockState state, @NotNull BlockPos pos, float fallDistance) {
        if (this.builder.canTrample != null) {
            ContextUtils.CanTrampleContext context = new ContextUtils.CanTrampleContext(state, pos, fallDistance, this);
            Object obj = this.builder.canTrample.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canTrample from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.canTrample(state, pos, fallDistance));
        }
        return super.canTrample(state, pos, fallDistance);
    }

    public void onRemovedFromWorld() {
        if (this.builder != null && this.builder.onRemovedFromWorld != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onRemovedFromWorld, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onRemovedFromWorld.");
        }
        super.onRemovedFromWorld();
    }

    @Override
    public int getMaxFallDistance() {
        if (this.builder.setMaxFallDistance == null) {
            return super.m_6056_();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.setMaxFallDistance.apply(this), "integer");
            if (obj != null) {
                return (Integer) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for setMaxFallDistance from entity: " + this.entityName() + ". Value: " + this.builder.setMaxFallDistance.apply(this) + ". Must be an integer. Defaulting to " + super.m_6056_());
                return super.m_6056_();
            }
        }
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        super.lerpTo(x, y, z, yaw, pitch, posRotationIncrements, teleport);
        if (this.builder.lerpTo != null) {
            ContextUtils.LerpToContext context = new ContextUtils.LerpToContext(x, y, z, yaw, pitch, posRotationIncrements, teleport, this);
            EntityJSHelperClass.consumerCallback(this.builder.lerpTo, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: lerpTo.");
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return this.armorItems;
    }

    @Override
    public Iterable<ItemStack> getHandSlots() {
        return this.handItems;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return switch(slot.getType()) {
            case HAND ->
                (ItemStack) this.handItems.get(slot.getIndex());
            case ARMOR ->
                (ItemStack) this.armorItems.get(slot.getIndex());
        };
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        this.m_181122_(stack);
        switch(slot.getType()) {
            case HAND:
                this.onEquipItem(slot, this.handItems.set(slot.getIndex(), stack), stack);
                break;
            case ARMOR:
                this.onEquipItem(slot, this.armorItems.set(slot.getIndex(), stack), stack);
        }
    }
}
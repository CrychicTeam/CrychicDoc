package net.liopyu.entityjs.mixin;

import com.mojang.serialization.Dynamic;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.Objects;
import net.liopyu.entityjs.builders.modification.ModifyLivingEntityBuilder;
import net.liopyu.entityjs.events.BuildBrainEventJS;
import net.liopyu.entityjs.events.BuildBrainProviderEventJS;
import net.liopyu.entityjs.events.EntityModificationEventJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.liopyu.entityjs.util.EventHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { LivingEntity.class }, remap = false)
public abstract class LivingEntityMixin {

    @Unique
    private Object entityJs$entityObject = this;

    @Unique
    public Object entityJs$builder;

    @Unique
    private LivingEntity entityJs$getLivingEntity() {
        return (LivingEntity) this.entityJs$entityObject;
    }

    @Unique
    private String entityJs$entityName() {
        return this.entityJs$getLivingEntity().m_6095_().toString();
    }

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void entityjs$onEntityInit(EntityType<?> pEntityType, Level pLevel, CallbackInfo ci) {
        EntityType<?> entityType = this.entityJs$getLivingEntity().m_6095_();
        if (EventHandlers.modifyEntity.hasListeners()) {
            EntityModificationEventJS eventJS = EntityModificationEventJS.getOrCreate(entityType, this.entityJs$getLivingEntity());
            EventHandlers.modifyEntity.post(eventJS);
            this.entityJs$builder = eventJS.getBuilder();
        }
    }

    public String entityJs$getTypeId() {
        return ((ResourceLocation) Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(this.entityJs$getLivingEntity().m_6095_()))).toString();
    }

    @Inject(method = { "brainProvider" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void brainProvider(CallbackInfoReturnable<Brain.Provider<?>> cir) {
        if (EventHandlers.buildBrainProvider.hasListeners()) {
            BuildBrainProviderEventJS<?> event = new BuildBrainProviderEventJS();
            EventHandlers.buildBrainProvider.post(event, this.entityJs$getTypeId());
            cir.setReturnValue(event.provide());
        }
    }

    @Inject(method = { "makeBrain" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void makeBrain(Dynamic<?> pDynamic, CallbackInfoReturnable<Brain<?>> cir) {
        if (EventHandlers.buildBrain.hasListeners()) {
            Brain<?> brain = UtilsJS.cast(this.entityJs$getLivingEntity().brainProvider().makeBrain(pDynamic));
            EventHandlers.buildBrain.post(new BuildBrainEventJS<>(brain), this.entityJs$getTypeId());
            cir.setReturnValue(brain);
        }
    }

    @Inject(method = { "getMobType" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void getMobType(CallbackInfoReturnable<MobType> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && this.entityJs$builder != null && builder.mobType != null) {
            cir.setReturnValue(builder.mobType);
        }
    }

    @Inject(method = { "tickDeath" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    protected void tickDeath(CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.tickDeath != null) {
            EntityJSHelperClass.consumerCallback(builder.tickDeath, this.entityJs$getLivingEntity(), "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: tickDeath.");
        }
    }

    @Inject(method = { "aiStep" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false)
    private void entityjs$aiStep(CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.aiStep != null) {
            builder.aiStep.accept(this.entityJs$getLivingEntity());
        }
    }

    @Inject(method = { "doHurtTarget" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false)
    private void entityjs$isAlliedTo(Entity pTarget, CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && this.entityJs$builder != null && builder.onHurtTarget != null) {
            ContextUtils.LineOfSightContext context = new ContextUtils.LineOfSightContext(this.entityJs$getLivingEntity(), this.entityJs$getLivingEntity());
            EntityJSHelperClass.consumerCallback(builder.onHurtTarget, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onHurtTarget.");
        }
    }

    @Inject(method = { "travel" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false)
    private void entityjs$travel(Vec3 pTravelVector, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.travel != null) {
            ContextUtils.Vec3Context context = new ContextUtils.Vec3Context(pTravelVector, this.entityJs$getLivingEntity());
            EntityJSHelperClass.consumerCallback(builder.travel, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: travel.");
        }
    }

    @Inject(method = { "doAutoAttackOnTouch" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false)
    private void entityjs$doAutoAttackOnTouch(LivingEntity pTarget, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.doAutoAttackOnTouch != null) {
            ContextUtils.AutoAttackContext context = new ContextUtils.AutoAttackContext(this.entityJs$getLivingEntity(), pTarget);
            EntityJSHelperClass.consumerCallback(builder.doAutoAttackOnTouch, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: doAutoAttackOnTouch.");
        }
    }

    @Inject(method = { "decreaseAirSupply" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false)
    private void entityjs$decreaseAirSupply(int pCurrentAir, CallbackInfoReturnable<Integer> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onDecreaseAirSupply != null) {
            EntityJSHelperClass.consumerCallback(builder.onDecreaseAirSupply, this.entityJs$getLivingEntity(), "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onDecreaseAirSupply.");
        }
    }

    @Inject(method = { "increaseAirSupply" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false)
    private void entityjs$increaseAirSupply(int pCurrentAir, CallbackInfoReturnable<Integer> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onIncreaseAirSupply != null) {
            EntityJSHelperClass.consumerCallback(builder.onIncreaseAirSupply, this.entityJs$getLivingEntity(), "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onIncreaseAirSupply.");
        }
    }

    @Inject(method = { "blockedByShield" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false)
    private void entityjs$blockedByShield(LivingEntity pDefender, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onBlockedByShield != null) {
            ContextUtils.LivingEntityContext context = new ContextUtils.LivingEntityContext(this.entityJs$getLivingEntity(), pDefender);
            EntityJSHelperClass.consumerCallback(builder.onBlockedByShield, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onDecreaseAirSupply.");
        }
    }

    @Inject(method = { "onEquipItem" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false)
    private void entityjs$onEquipItem(EquipmentSlot pSlot, ItemStack pOldItem, ItemStack pNewItem, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onEquipItem != null) {
            ContextUtils.EntityEquipmentContext context = new ContextUtils.EntityEquipmentContext(pSlot, pOldItem, pNewItem, this.entityJs$getLivingEntity());
            EntityJSHelperClass.consumerCallback(builder.onEquipItem, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onEquipItem.");
        }
    }

    @Inject(method = { "onEffectAdded" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false)
    private void entityjs$onEffectAdded(MobEffectInstance pEffectInstance, Entity pEntity, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onEffectAdded != null) {
            ContextUtils.OnEffectContext context = new ContextUtils.OnEffectContext(pEffectInstance, this.entityJs$getLivingEntity());
            EntityJSHelperClass.consumerCallback(builder.onEffectAdded, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onEffectAdded.");
        }
    }

    @Inject(method = { "onEffectRemoved" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false)
    private void entityjs$onEffectRemoved(MobEffectInstance pEffectInstance, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onEffectRemoved != null) {
            ContextUtils.OnEffectContext context = new ContextUtils.OnEffectContext(pEffectInstance, this.entityJs$getLivingEntity());
            EntityJSHelperClass.consumerCallback(builder.onEffectRemoved, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onEffectRemoved.");
        }
    }

    @Inject(method = { "heal" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false)
    private void entityjs$heal(float pHealAmount, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onLivingHeal != null) {
            ContextUtils.EntityHealContext context = new ContextUtils.EntityHealContext(this.entityJs$getLivingEntity(), pHealAmount);
            EntityJSHelperClass.consumerCallback(builder.onLivingHeal, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onLivingHeal.");
        }
    }

    @Inject(method = { "die" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false)
    private void entityjs$die(DamageSource pDamageSource, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onDeath != null) {
            ContextUtils.DeathContext context = new ContextUtils.DeathContext(this.entityJs$getLivingEntity(), pDamageSource);
            EntityJSHelperClass.consumerCallback(builder.onDeath, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onDeath.");
        }
    }

    @Inject(method = { "dropCustomDeathLoot" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false)
    private void entityjs$dropCustomDeathLoot(DamageSource pDamageSource, int pLooting, boolean pHitByPlayer, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.dropCustomDeathLoot != null) {
            ContextUtils.EntityLootContext context = new ContextUtils.EntityLootContext(pDamageSource, pLooting, pHitByPlayer, this.entityJs$getLivingEntity());
            EntityJSHelperClass.consumerCallback(builder.dropCustomDeathLoot, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: dropCustomDeathLoot.");
        }
    }

    @Inject(method = { "getSoundVolume" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$getSoundVolume(CallbackInfoReturnable<Float> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.setSoundVolume != null) {
            cir.setReturnValue(builder.setSoundVolume);
        }
    }

    @Inject(method = { "getWaterSlowDown" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$getWaterSlowDown(CallbackInfoReturnable<Float> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.setWaterSlowDown != null) {
            cir.setReturnValue(builder.setWaterSlowDown);
        }
    }

    @Inject(method = { "getStandingEyeHeight" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions, CallbackInfoReturnable<Float> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder) {
            if (builder.setStandingEyeHeight == null) {
                return;
            }
            ContextUtils.EntityPoseDimensionsContext context = new ContextUtils.EntityPoseDimensionsContext(pPose, pDimensions, this.entityJs$getLivingEntity());
            Object obj = EntityJSHelperClass.convertObjectToDesired(builder.setStandingEyeHeight.apply(context), "float");
            if (obj != null) {
                cir.setReturnValue((Float) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for setStandingEyeHeight from entity: " + this.entityJs$entityName() + ". Value: " + builder.setStandingEyeHeight.apply(context) + ". Must be a float. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "isPushable" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$isPushable(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.isPushable != null) {
            cir.setReturnValue(builder.isPushable);
        }
    }

    @Inject(method = { "getBlockSpeedFactor" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$getBlockSpeedFactor(CallbackInfoReturnable<Float> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder) {
            if (builder.blockSpeedFactor == null) {
                return;
            }
            Object obj = EntityJSHelperClass.convertObjectToDesired(builder.blockSpeedFactor.apply(this.entityJs$getLivingEntity()), "float");
            if (obj != null) {
                cir.setReturnValue((Float) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for blockSpeedFactor from entity: " + this.entityJs$getLivingEntity().m_6095_() + ". Value: " + builder.blockSpeedFactor.apply(this.entityJs$getLivingEntity()) + ". Must be a float, defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "shouldDropLoot" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$shouldDropLoot(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.shouldDropLoot != null) {
            Object obj = builder.shouldDropLoot.apply(this.entityJs$getLivingEntity());
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for shouldDropLoot from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean, defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "isAffectedByFluids" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$isAffectedByFluids(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.isAffectedByFluids != null) {
            Object obj = builder.isAffectedByFluids.apply(this.entityJs$getLivingEntity());
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isAffectedByFluids from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "isAlwaysExperienceDropper" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$isAlwaysExperienceDropper(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.isAlwaysExperienceDropper != null) {
            cir.setReturnValue(builder.isAlwaysExperienceDropper);
        }
    }

    @Inject(method = { "isImmobile" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$isImmobile(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.isImmobile != null) {
            Object obj = builder.isImmobile.apply(this.entityJs$getLivingEntity());
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isImmobile from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "calculateFallDamage" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$calculateFallDamage(float pFallDistance, float pDamageMultiplier, CallbackInfoReturnable<Integer> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder) {
            if (builder.calculateFallDamage == null) {
                return;
            }
            ContextUtils.CalculateFallDamageContext context = new ContextUtils.CalculateFallDamageContext(pFallDistance, pDamageMultiplier, this.entityJs$getLivingEntity());
            Object obj = EntityJSHelperClass.convertObjectToDesired(builder.calculateFallDamage.apply(context), "integer");
            if (obj != null) {
                cir.setReturnValue((Integer) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for calculateFallDamage from entity: " + this.entityJs$entityName() + ". Value: " + builder.calculateFallDamage.apply(context) + ". Must be an int, defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "getHurtSound" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$getHurtSound(DamageSource pDamageSource, CallbackInfoReturnable<SoundEvent> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder) {
            if (builder.setHurtSound == null) {
                return;
            }
            ContextUtils.HurtContext context = new ContextUtils.HurtContext(this.entityJs$getLivingEntity(), pDamageSource);
            Object obj = EntityJSHelperClass.convertObjectToDesired(builder.setHurtSound.apply(context), "resourcelocation");
            if (obj != null) {
                cir.setReturnValue(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) obj));
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for setHurtSound from entity: " + this.entityJs$entityName() + ". Value: " + builder.setHurtSound.apply(context) + ". Must be a ResourceLocation or String. Defaulting to \"minecraft:entity.generic.hurt\"");
            }
        }
    }

    @Inject(method = { "canAttackType" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$canAttackType(EntityType<?> pEntityType, CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.canAttackType != null) {
            ContextUtils.EntityTypeEntityContext context = new ContextUtils.EntityTypeEntityContext(this.entityJs$getLivingEntity(), pEntityType);
            Object obj = builder.canAttackType.apply(context);
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canAttackType from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "getScale" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$getScale(CallbackInfoReturnable<Float> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder) {
            if (builder.scale == null) {
                return;
            }
            Object obj = EntityJSHelperClass.convertObjectToDesired(builder.scale.apply(this.entityJs$getLivingEntity()), "float");
            if (obj != null) {
                cir.setReturnValue((Float) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for scale from entity: " + this.entityJs$entityName() + ". Value: " + builder.scale.apply(this.entityJs$getLivingEntity()) + ". Must be a float. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "shouldDropExperience" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$shouldDropExperience(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.shouldDropExperience != null) {
            Object obj = builder.shouldDropExperience.apply(this.entityJs$getLivingEntity());
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for shouldDropExperience from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "getVisibilityPercent" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$getVisibilityPercent(Entity pLookingEntity, CallbackInfoReturnable<Double> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.visibilityPercent != null) {
            ContextUtils.VisualContext context = new ContextUtils.VisualContext(pLookingEntity, this.entityJs$getLivingEntity());
            Object obj = EntityJSHelperClass.convertObjectToDesired(builder.visibilityPercent.apply(context), "double");
            if (obj != null) {
                cir.setReturnValue((Double) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for visibilityPercent from entity: " + this.entityJs$entityName() + ". Value: " + builder.visibilityPercent.apply(context) + ". Must be a double. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$canAttack(LivingEntity pTarget, CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.canAttack != null) {
            ContextUtils.LivingEntityContext context = new ContextUtils.LivingEntityContext(this.entityJs$getLivingEntity(), pTarget);
            Object obj = builder.canAttack.apply(context);
            if (obj instanceof Boolean b) {
                boolean bool = b && (Boolean) cir.getReturnValue();
                cir.setReturnValue(bool);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canAttack from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "canBeAffected" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$canBeAffected(MobEffectInstance pEffectInstance, CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder) {
            if (builder.canBeAffected == null) {
                return;
            }
            ContextUtils.OnEffectContext context = new ContextUtils.OnEffectContext(pEffectInstance, this.entityJs$getLivingEntity());
            Object result = builder.canBeAffected.apply(context);
            if (result instanceof Boolean) {
                cir.setReturnValue((Boolean) result);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canBeAffected from entity: " + this.entityJs$entityName() + ". Value: " + result + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "isInvertedHealAndHarm" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$isInvertedHealAndHarm(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder) {
            if (builder.invertedHealAndHarm == null) {
                return;
            }
            Object obj = builder.invertedHealAndHarm.apply(this.entityJs$getLivingEntity());
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for invertedHealAndHarm from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "getDeathSound" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$getDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder) {
            if (builder.setDeathSound == null) {
                return;
            }
            cir.setReturnValue((SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) builder.setDeathSound)));
        }
    }

    @Inject(method = { "getFallSounds" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$getFallSounds(CallbackInfoReturnable<LivingEntity.Fallsounds> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.fallSounds != null) {
            cir.setReturnValue(new LivingEntity.Fallsounds((SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) builder.smallFallSound)), (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) builder.largeFallSound))));
        }
    }

    @Inject(method = { "getEatingSound" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$getEatingSound(ItemStack pStack, CallbackInfoReturnable<SoundEvent> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.eatingSound != null) {
            cir.setReturnValue((SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) builder.eatingSound)));
        }
    }

    @Inject(method = { "onClimbable" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$onClimbable(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder) {
            if (builder.onClimbable == null) {
                return;
            }
            Object obj = builder.onClimbable.apply(this.entityJs$getLivingEntity());
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for onClimbable from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to super.onClimbable(): " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "canBreatheUnderwater" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$canBreatheUnderwater(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.canBreatheUnderwater != null) {
            cir.setReturnValue(builder.canBreatheUnderwater);
        }
    }

    @Inject(method = { "getJumpBoostPower" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$getJumpBoostPower(CallbackInfoReturnable<Float> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder) {
            if (builder.jumpBoostPower == null) {
                return;
            }
            Object obj = EntityJSHelperClass.convertObjectToDesired(builder.jumpBoostPower.apply(this.entityJs$getLivingEntity()), "float");
            if (obj != null) {
                cir.setReturnValue((Float) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for jumpBoostPower from entity: " + this.entityJs$entityName() + ". Value: " + builder.jumpBoostPower.apply(this.entityJs$getLivingEntity()) + ". Must be a float. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "canStandOnFluid" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$canStandOnFluid(FluidState pFluidState, CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.canStandOnFluid != null) {
            ContextUtils.EntityFluidStateContext context = new ContextUtils.EntityFluidStateContext(this.entityJs$getLivingEntity(), pFluidState);
            Object obj = EntityJSHelperClass.convertObjectToDesired(builder.canStandOnFluid.apply(context), "boolean");
            if (obj != null) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canStandOnFluid from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
            cir.setReturnValue(false);
        }
    }

    @Inject(method = { "isSensitiveToWater" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$isSensitiveToWater(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.isSensitiveToWater != null) {
            Object obj = EntityJSHelperClass.convertObjectToDesired(builder.isSensitiveToWater.apply(this.entityJs$getLivingEntity()), "boolean");
            if (obj != null) {
                cir.setReturnValue((Boolean) obj);
                return;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isSensitiveToWater from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
        }
    }

    @Inject(method = { "onItemPickup" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$onItemPickup(ItemEntity pItemEntity, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onItemPickup != null) {
            ContextUtils.EntityItemEntityContext context = new ContextUtils.EntityItemEntityContext(this.entityJs$getLivingEntity(), pItemEntity);
            EntityJSHelperClass.consumerCallback(builder.onItemPickup, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onItemPickup.");
        }
    }

    @Inject(method = { "hasLineOfSight" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$hasLineOfSight(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.hasLineOfSight != null) {
            ContextUtils.LineOfSightContext context = new ContextUtils.LineOfSightContext(pEntity, this.entityJs$getLivingEntity());
            Object obj = builder.hasLineOfSight.apply(context);
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for hasLineOfSight from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "onEnterCombat" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$onEnterCombat(CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onEnterCombat != null) {
            EntityJSHelperClass.consumerCallback(builder.onEnterCombat, this.entityJs$getLivingEntity(), "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onEnterCombat.");
        }
    }

    @Inject(method = { "onLeaveCombat" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$onLeaveCombat(CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onLeaveCombat != null) {
            EntityJSHelperClass.consumerCallback(builder.onLeaveCombat, this.entityJs$getLivingEntity(), "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onLeaveCombat.");
        }
    }

    @Inject(method = { "isAffectedByPotions" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$isAffectedByPotions(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.isAffectedByPotions != null) {
            Object obj = builder.isAffectedByPotions.apply(this.entityJs$getLivingEntity());
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isAffectedByPotions from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "attackable" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$attackable(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.isAttackableFunction != null) {
            Object obj = builder.isAttackableFunction.apply(this.entityJs$getLivingEntity());
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isAttackable from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "canTakeItem" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$canTakeItem(ItemStack pStack, CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.canTakeItem != null) {
            ContextUtils.EntityItemLevelContext context = new ContextUtils.EntityItemLevelContext(this.entityJs$getLivingEntity(), pStack, this.entityJs$getLivingEntity().m_9236_());
            Object obj = builder.canTakeItem.apply(context);
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canTakeItem from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "isSleeping" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$isSleeping(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && this.entityJs$builder != null && builder.isSleeping != null) {
            Object obj = builder.isSleeping.apply(this.entityJs$getLivingEntity());
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isSleeping from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "startSleeping" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$startSleeping(BlockPos pPos, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onStartSleeping != null) {
            ContextUtils.EntityBlockPosContext context = new ContextUtils.EntityBlockPosContext(this.entityJs$getLivingEntity(), pPos);
            EntityJSHelperClass.consumerCallback(builder.onStartSleeping, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onStartSleeping.");
        }
    }

    @Inject(method = { "stopSleeping" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$stopSleeping(CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onStopSleeping != null) {
            EntityJSHelperClass.consumerCallback(builder.onStopSleeping, this.entityJs$getLivingEntity(), "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onStopSleeping.");
        }
    }

    @Inject(method = { "eat" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$eat(Level pLevel, ItemStack pFood, CallbackInfoReturnable<ItemStack> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.eat != null) {
            ContextUtils.EntityItemLevelContext context = new ContextUtils.EntityItemLevelContext(this.entityJs$getLivingEntity(), pFood, pLevel);
            EntityJSHelperClass.consumerCallback(builder.eat, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: eat.");
        }
    }

    @Inject(method = { "shouldRiderFaceForward" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$shouldRiderFaceForward(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.shouldRiderFaceForward != null) {
            ContextUtils.PlayerEntityContext context = new ContextUtils.PlayerEntityContext(player, this.entityJs$getLivingEntity());
            Object obj = builder.shouldRiderFaceForward.apply(context);
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for shouldRiderFaceForward from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "canFreeze" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$canFreeze(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.canFreeze != null) {
            Object obj = builder.canFreeze.apply(this.entityJs$getLivingEntity());
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canFreeze from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "isCurrentlyGlowing" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$isCurrentlyGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && this.entityJs$builder != null && builder.isCurrentlyGlowing != null && !this.entityJs$getLivingEntity().m_9236_().isClientSide()) {
            Object obj = builder.isCurrentlyGlowing.apply(this.entityJs$getLivingEntity());
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isCurrentlyGlowing from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "canDisableShield" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$canDisableShield(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.canDisableShield != null) {
            Object obj = builder.canDisableShield.apply(this.entityJs$getLivingEntity());
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canDisableShield from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "actuallyHurt" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$actuallyHurt(DamageSource pDamageSource, float pDamageAmount, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.onHurt != null) {
            ContextUtils.EntityDamageContext context = new ContextUtils.EntityDamageContext(pDamageSource, pDamageAmount, this.entityJs$getLivingEntity());
            EntityJSHelperClass.consumerCallback(builder.onHurt, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onHurt.");
        }
    }

    @Inject(method = { "getExperienceReward" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$getExperienceReward(CallbackInfoReturnable<Integer> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.experienceReward != null) {
            Object obj = EntityJSHelperClass.convertObjectToDesired(builder.experienceReward.apply(this.entityJs$getLivingEntity()), "integer");
            if (obj != null) {
                cir.setReturnValue((Integer) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for experienceReward from entity: " + this.entityJs$entityName() + ". Value: " + builder.experienceReward.apply(this.entityJs$getLivingEntity()) + ". Must be an integer. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "canChangeDimensions" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$canChangeDimensions(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.canChangeDimensions != null) {
            Object obj = builder.canChangeDimensions.apply(this.entityJs$getLivingEntity());
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canChangeDimensions from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "lerpTo" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    private void entityjs$lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyLivingEntityBuilder builder && builder.lerpTo != null) {
            ContextUtils.LerpToContext context = new ContextUtils.LerpToContext(pX, pY, pZ, pYaw, pPitch, pPosRotationIncrements, pTeleport, this.entityJs$getLivingEntity());
            EntityJSHelperClass.consumerCallback(builder.lerpTo, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: lerpTo.");
        }
    }
}
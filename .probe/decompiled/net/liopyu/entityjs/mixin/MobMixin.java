package net.liopyu.entityjs.mixin;

import net.liopyu.entityjs.builders.modification.ModifyMobBuilder;
import net.liopyu.entityjs.events.EntityModificationEventJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.liopyu.entityjs.util.EventHandlers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { Mob.class }, remap = false)
public class MobMixin {

    @Unique
    private Object entityJs$builder;

    @Unique
    private Object entityJs$entityObject = this;

    @Unique
    private Mob entityJs$getLivingEntity() {
        return (Mob) this.entityJs$entityObject;
    }

    @Unique
    private String entityJs$entityName() {
        return this.entityJs$getLivingEntity().m_6095_().toString();
    }

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void entityjs$onMobInit(EntityType<?> pEntityType, Level pLevel, CallbackInfo ci) {
        EntityType<?> entityType = this.entityJs$getLivingEntity().m_6095_();
        if (EventHandlers.modifyEntity.hasListeners()) {
            EntityModificationEventJS eventJS = EntityModificationEventJS.getOrCreate(entityType, this.entityJs$getLivingEntity());
            EventHandlers.modifyEntity.post(eventJS);
            this.entityJs$builder = eventJS.getBuilder();
        }
    }

    @Inject(method = { "getControllingPassenger" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void getControllingPassenger(CallbackInfoReturnable<LivingEntity> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder && builder.controlledByFirstPassenger != null) {
            if (!builder.controlledByFirstPassenger) {
                return;
            }
            LivingEntity var10000;
            if (this.entityJs$getLivingEntity().m_146895_() instanceof LivingEntity entity) {
                var10000 = entity;
            } else {
                var10000 = null;
            }
            cir.setReturnValue(var10000);
        }
    }

    @Inject(method = { "mobInteract" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void mobInteract(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder && builder.onInteract != null) {
            ContextUtils.MobInteractContext context = new ContextUtils.MobInteractContext(this.entityJs$getLivingEntity(), pPlayer, pHand);
            EntityJSHelperClass.consumerCallback(builder.onInteract, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onInteract.");
        }
    }

    @Inject(method = { "doHurtTarget" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void doHurtTarget(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder && this.entityJs$builder != null && builder.onHurtTarget != null) {
            ContextUtils.LineOfSightContext context = new ContextUtils.LineOfSightContext(pEntity, this.entityJs$getLivingEntity());
            EntityJSHelperClass.consumerCallback(builder.onHurtTarget, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onHurtTarget.");
        }
    }

    @Inject(method = { "tickLeash" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    protected void tickLeash(CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder && builder.tickLeash != null) {
            Player $$0 = (Player) this.entityJs$getLivingEntity().getLeashHolder();
            ContextUtils.PlayerEntityContext context = new ContextUtils.PlayerEntityContext($$0, this.entityJs$getLivingEntity());
            EntityJSHelperClass.consumerCallback(builder.tickLeash, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: tickLeash.");
        }
    }

    @Inject(method = { "setTarget" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void setTarget(LivingEntity pTarget, CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder && builder.onTargetChanged != null) {
            ContextUtils.TargetChangeContext context = new ContextUtils.TargetChangeContext(pTarget, this.entityJs$getLivingEntity());
            EntityJSHelperClass.consumerCallback(builder.onTargetChanged, context, "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: onTargetChanged.");
        }
    }

    @Inject(method = { "ate" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void ate(CallbackInfo ci) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder && builder.ate != null) {
            EntityJSHelperClass.consumerCallback(builder.ate, this.entityJs$getLivingEntity(), "[EntityJS]: Error in " + this.entityJs$entityName() + "builder for field: ate.");
        }
    }

    @Inject(method = { "createNavigation" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    protected void createNavigation(Level pLevel, CallbackInfoReturnable<PathNavigation> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder) {
            if (this.entityJs$builder == null || builder.createNavigation == null) {
                return;
            }
            ContextUtils.EntityLevelContext context = new ContextUtils.EntityLevelContext(pLevel, this.entityJs$getLivingEntity());
            Object obj = builder.createNavigation.apply(context);
            if (obj instanceof PathNavigation p) {
                cir.setReturnValue(p);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for createNavigation from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be PathNavigation. Defaulting to super method.");
            }
        }
    }

    @Inject(method = { "canBeLeashed" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void canBeLeashed(Player pPlayer, CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder && builder.canBeLeashed != null) {
            ContextUtils.PlayerEntityContext context = new ContextUtils.PlayerEntityContext(pPlayer, this.entityJs$getLivingEntity());
            Object obj = builder.canBeLeashed.apply(context);
            if (obj instanceof Boolean b) {
                cir.setReturnValue(b);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canBeLeashed from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "getMainArm" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void getMainArm(CallbackInfoReturnable<HumanoidArm> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder && builder.mainArm != null) {
            cir.setReturnValue((HumanoidArm) builder.mainArm);
        }
    }

    @Inject(method = { "getAmbientSound" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    protected void getAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder && builder.setAmbientSound != null) {
            cir.setReturnValue(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) builder.setAmbientSound));
        }
    }

    @Inject(method = { "canHoldItem" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void canHoldItem(ItemStack pStack, CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder && builder.canHoldItem != null) {
            ContextUtils.EntityItemStackContext context = new ContextUtils.EntityItemStackContext(pStack, this.entityJs$getLivingEntity());
            Object obj = builder.canHoldItem.apply(context);
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canHoldItem from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "shouldDespawnInPeaceful" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    protected void shouldDespawnInPeaceful(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder) {
            if (builder.shouldDespawnInPeaceful == null) {
                return;
            }
            cir.setReturnValue(builder.shouldDespawnInPeaceful);
        }
    }

    @Inject(method = { "isPersistenceRequired" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void isPersistenceRequired(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder) {
            if (builder.isPersistenceRequired == null) {
                return;
            }
            cir.setReturnValue(builder.isPersistenceRequired);
        }
    }

    @Inject(method = { "getMeleeAttackRangeSqr" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void getMeleeAttackRangeSqr(LivingEntity pEntity, CallbackInfoReturnable<Double> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder && builder.meleeAttackRangeSqr != null) {
            Object obj = EntityJSHelperClass.convertObjectToDesired(builder.meleeAttackRangeSqr.apply(this.entityJs$getLivingEntity()), "double");
            if (obj != null) {
                cir.setReturnValue((Double) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for meleeAttackRangeSqr from entity: " + this.entityJs$entityName() + ". Value: " + builder.meleeAttackRangeSqr.apply(this.entityJs$getLivingEntity()) + ". Must be a double. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "getAmbientSoundInterval" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void getAmbientSoundInterval(CallbackInfoReturnable<Integer> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder && builder.ambientSoundInterval != null) {
            cir.setReturnValue((Integer) builder.ambientSoundInterval);
        }
    }

    @Inject(method = { "removeWhenFarAway" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void removeWhenFarAway(double pDistanceToClosestPlayer, CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyMobBuilder builder) {
            if (builder.removeWhenFarAway == null) {
                return;
            }
            ContextUtils.EntityDistanceToPlayerContext context = new ContextUtils.EntityDistanceToPlayerContext(pDistanceToClosestPlayer, this.entityJs$getLivingEntity());
            Object obj = builder.removeWhenFarAway.apply(context);
            if (obj instanceof Boolean) {
                cir.setReturnValue((Boolean) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for removeWhenFarAway from entity: " + this.entityJs$entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }
}
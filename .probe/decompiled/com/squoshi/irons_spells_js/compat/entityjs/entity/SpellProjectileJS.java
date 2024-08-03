package com.squoshi.irons_spells_js.compat.entityjs.entity;

import com.squoshi.irons_spells_js.compat.entityjs.entity.builder.SpellProjectileJSBuilder;
import com.squoshi.irons_spells_js.util.ISSKJSUtils;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import java.util.Objects;
import java.util.Optional;
import net.liopyu.entityjs.builders.nonliving.entityjs.ProjectileEntityBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.IProjectileEntityJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class SpellProjectileJS extends AbstractMagicProjectile implements IProjectileEntityJS, AntiMagicSusceptible {

    public SpellProjectileJSBuilder builder;

    private float damage;

    private float speed = 0.0F;

    public SpellProjectileJS(SpellProjectileJSBuilder builder, EntityType<? extends AbstractMagicProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.builder = builder;
    }

    public SpellProjectileJS(EntityType<? extends AbstractMagicProjectile> entityType, Level levelIn, LivingEntity shooter) {
        super(entityType, levelIn);
        this.m_5602_(shooter);
    }

    public ProjectileEntityBuilder<?> getProjectileBuilder() {
        return this.builder;
    }

    @Override
    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    public float getDamage() {
        return this.damage;
    }

    @Override
    public void trailParticles() {
        if (this.builder.trailParticles != null) {
            this.builder.trailParticles.accept(this);
        }
    }

    @Override
    public void impactParticles(double v, double v1, double v2) {
        if (this.builder.impactParticles != null) {
            SpellProjectileJS.ImpactParticleContext context = new SpellProjectileJS.ImpactParticleContext(this, v, v1, v2);
            this.builder.impactParticles.accept(context);
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return this.builder.setImpactSound != null ? Optional.ofNullable(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.setImpactSound)) : Optional.empty();
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        if (this.builder.onAntiMagic != null) {
            ISSKJSUtils.safeCallback(this.builder.onAntiMagic, new SpellProjectileJS.OnAntiMagicContext(playerMagicData, this), "Error while calling onAntiMagic");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("Damage", this.damage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.damage = pCompound.getFloat("Damage");
    }

    public String entityName() {
        return this.m_6095_().toString();
    }

    @Override
    public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        super.m_37251_(pShooter, pX, pY, pZ, pVelocity, pInaccuracy);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.builder != null && this.builder.onHurt != null) {
            ContextUtils.EntityHurtContext context = new ContextUtils.EntityHurtContext(this, pSource, pAmount);
            EntityJSHelperClass.consumerCallback(this.builder.onHurt, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHurt.");
        }
        return super.m_6469_(pSource, pAmount);
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        super.m_6453_(x, y, z, yaw, pitch, posRotationIncrements, teleport);
        if (this.builder != null && this.builder.lerpTo != null) {
            ContextUtils.LerpToContext context = new ContextUtils.LerpToContext(x, y, z, yaw, pitch, posRotationIncrements, teleport, this);
            EntityJSHelperClass.consumerCallback(this.builder.lerpTo, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: lerpTo.");
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.builder != null && this.builder.tick != null) {
            EntityJSHelperClass.consumerCallback(this.builder.tick, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: tick.");
        }
    }

    @Override
    public void move(MoverType pType, Vec3 pPos) {
        super.m_6478_(pType, pPos);
        if (this.builder != null && this.builder.move != null) {
            ContextUtils.MovementContext context = new ContextUtils.MovementContext(pType, pPos, this);
            EntityJSHelperClass.consumerCallback(this.builder.move, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: move.");
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (this.builder != null && this.builder.playerTouch != null) {
            ContextUtils.EntityPlayerContext context = new ContextUtils.EntityPlayerContext(player, this);
            EntityJSHelperClass.consumerCallback(this.builder.playerTouch, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: playerTouch.");
        } else {
            super.m_6123_(player);
        }
    }

    public void onRemovedFromWorld() {
        if (this.builder != null && this.builder.onRemovedFromWorld != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onRemovedFromWorld, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onRemovedFromWorld.");
        }
        super.onRemovedFromWorld();
    }

    @Override
    public void thunderHit(ServerLevel serverLevel0, LightningBolt lightningBolt1) {
        if (this.builder != null && this.builder.thunderHit != null) {
            super.m_8038_(serverLevel0, lightningBolt1);
            ContextUtils.EThunderHitContext context = new ContextUtils.EThunderHitContext(serverLevel0, lightningBolt1, this);
            EntityJSHelperClass.consumerCallback(this.builder.thunderHit, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: thunderHit.");
        }
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        if (this.builder != null && this.builder.onFall != null) {
            ContextUtils.EEntityFallDamageContext context = new ContextUtils.EEntityFallDamageContext(this, pMultiplier, pFallDistance, pSource);
            EntityJSHelperClass.consumerCallback(this.builder.onFall, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onLivingFall.");
        }
        return super.m_142535_(pFallDistance, pMultiplier, pSource);
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.builder != null && this.builder.onAddedToWorld != null && !this.m_9236_().isClientSide()) {
            EntityJSHelperClass.consumerCallback(this.builder.onAddedToWorld, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onAddedToWorld.");
        }
    }

    @Override
    public void setSprinting(boolean sprinting) {
        if (this.builder != null && this.builder.onSprint != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onSprint, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onSprint.");
        }
        super.m_6858_(sprinting);
    }

    @Override
    public void stopRiding() {
        super.m_8127_();
        if (this.builder != null && this.builder.onStopRiding != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onStopRiding, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onStopRiding.");
        }
    }

    @Override
    public void rideTick() {
        super.m_6083_();
        if (this.builder != null && this.builder.rideTick != null) {
            EntityJSHelperClass.consumerCallback(this.builder.rideTick, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: rideTick.");
        }
    }

    @Override
    public void onClientRemoval() {
        if (this.builder != null && this.builder.onClientRemoval != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onClientRemoval, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onClientRemoval.");
        }
        super.m_142036_();
    }

    @Override
    public void lavaHurt() {
        if (this.builder != null && this.builder.lavaHurt != null) {
            EntityJSHelperClass.consumerCallback(this.builder.lavaHurt, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: lavaHurt.");
        }
        super.m_20093_();
    }

    @Override
    protected void onFlap() {
        if (this.builder != null && this.builder.onFlap != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onFlap, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onFlap.");
        }
        super.m_142043_();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        if (this.builder != null && this.builder.shouldRenderAtSqrDistance != null) {
            ContextUtils.EntitySqrDistanceContext context = new ContextUtils.EntitySqrDistanceContext(distance, this);
            Object obj = this.builder.shouldRenderAtSqrDistance.apply(context);
            if (obj instanceof Boolean b) {
                return b;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid shouldRenderAtSqrDistance for arrow builder: " + obj + ". Must be a boolean. Defaulting to super method: " + super.m_6783_(distance));
        }
        return super.m_6783_(distance);
    }

    @Override
    public boolean isAttackable() {
        return this.builder.isAttackable != null ? this.builder.isAttackable : super.m_6097_();
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public void push(Entity pEntity) {
        if (this.builder.onEntityCollision != null) {
            ContextUtils.CollidingProjectileEntityContext context = new ContextUtils.CollidingProjectileEntityContext(this, pEntity);
            EntityJSHelperClass.consumerCallback(this.builder.onEntityCollision, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onEntityCollision.");
        }
        if (this.builder.isPushable) {
            super.m_7334_(pEntity);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.builder != null && this.builder.onHitEntity != null) {
            ContextUtils.ProjectileEntityHitContext context = new ContextUtils.ProjectileEntityHitContext(result, this);
            EntityJSHelperClass.consumerCallback(this.builder.onHitEntity, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHitEntity.");
        } else {
            super.onHitEntity(result);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.m_8060_(result);
        if (this.builder != null && this.builder.onHitBlock != null) {
            ContextUtils.ProjectileBlockHitContext context = new ContextUtils.ProjectileBlockHitContext(result, this);
            EntityJSHelperClass.consumerCallback(this.builder.onHitBlock, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHitBlock.");
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (this.builder != null && this.builder.canHitEntity != null) {
            Object obj = this.builder.canHitEntity.apply(entity);
            if (obj instanceof Boolean b) {
                return super.canHitEntity(entity) && b;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid canHitEntity for arrow builder: " + obj + ". Must be a boolean. Defaulting to super method: " + super.canHitEntity(entity));
        }
        return entity != this.m_19749_() && super.canHitEntity(entity);
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

    @Override
    public boolean canCollideWith(Entity pEntity) {
        if (this.builder.canCollideWith != null) {
            ContextUtils.ECollidingEntityContext context = new ContextUtils.ECollidingEntityContext(this, pEntity);
            Object obj = this.builder.canCollideWith.apply(context);
            if (obj instanceof Boolean b) {
                return b;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canCollideWith from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_7337_(pEntity));
        }
        return super.m_7337_(pEntity);
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
    protected float getBlockSpeedFactor() {
        if (this.builder.blockSpeedFactor == null) {
            return super.m_6041_();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.blockSpeedFactor.apply(this), "float");
            if (this.builder.blockSpeedFactor == null) {
                return super.m_6041_();
            } else if (obj != null) {
                return (Float) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for blockSpeedFactor from entity: " + this.entityName() + ". Value: " + this.builder.blockSpeedFactor.apply(this) + ". Must be a float, defaulting to " + super.m_6041_());
                return super.m_6041_();
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
            ContextUtils.EPassengerEntityContext context = new ContextUtils.EPassengerEntityContext(entity, this);
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

    @Override
    protected SoundEvent getSwimSplashSound() {
        return this.builder.setSwimSplashSound == null ? super.m_5509_() : (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.setSwimSplashSound));
    }

    @Override
    protected SoundEvent getSwimSound() {
        return this.builder.setSwimSound == null ? super.m_5501_() : (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.setSwimSound));
    }

    @Override
    public boolean canFreeze() {
        if (this.builder.canFreeze != null) {
            Object obj = this.builder.canFreeze.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canFreeze from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_142079_());
        }
        return super.m_142079_();
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
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isCurrentlyGlowing from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_142038_());
        }
        return super.m_142038_();
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
    public boolean isInvulnerableTo(DamageSource damageSource0) {
        if (this.builder.isInvulnerableTo != null) {
            ContextUtils.EDamageContext context = new ContextUtils.EDamageContext(this, damageSource0);
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
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canChangeDimensions from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_6072_());
        }
        return super.m_6072_();
    }

    @Override
    public boolean mayInteract(@NotNull Level level0, @NotNull BlockPos blockPos1) {
        if (this.builder.mayInteract != null) {
            ContextUtils.EMayInteractContext context = new ContextUtils.EMayInteractContext(level0, blockPos1, this);
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
            ContextUtils.ECanTrampleContext context = new ContextUtils.ECanTrampleContext(state, pos, fallDistance, this);
            Object obj = this.builder.canTrample.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canTrample from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.canTrample(state, pos, fallDistance));
        }
        return super.canTrample(state, pos, fallDistance);
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

    public static class ImpactParticleContext {

        public final SpellProjectileJS entity;

        public final double x;

        public final double y;

        public final double z;

        public ImpactParticleContext(SpellProjectileJS entity, double x, double y, double z) {
            this.entity = entity;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static record OnAntiMagicContext(MagicData getMagicData, Entity getEntity) {
    }
}
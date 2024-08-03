package net.liopyu.entityjs.entities.nonliving.entityjs;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.liopyu.entityjs.builders.nonliving.entityjs.ArrowEntityBuilder;
import net.liopyu.entityjs.builders.nonliving.entityjs.ArrowEntityJSBuilder;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ArrowEntityJS extends AbstractArrow implements IArrowEntityJS {

    public final ArrowEntityJSBuilder builder;

    @NotNull
    protected ItemStack pickUpStack;

    private double baseDamage;

    private int knockback;

    @Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;

    @Nullable
    private List<Entity> piercedAndKilledEntities;

    private EntityJSHelperClass.EntityMovementTracker movementTracker;

    private boolean isMoving;

    public ArrowEntityJS(ArrowEntityJSBuilder builder, EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.builder = builder;
        this.pickUpStack = ItemStack.EMPTY;
        this.baseDamage = builder.setBaseDamage;
        this.movementTracker = new EntityJSHelperClass.EntityMovementTracker();
    }

    public ArrowEntityJS(Level level, LivingEntity shooter, ArrowEntityJSBuilder builder) {
        super((EntityType<? extends AbstractArrow>) builder.get(), shooter, level);
        this.builder = builder;
        this.pickUpStack = ItemStack.EMPTY;
        this.baseDamage = builder.setBaseDamage;
        this.movementTracker = new EntityJSHelperClass.EntityMovementTracker();
    }

    public ArrowEntityBuilder<?> getArrowBuilder() {
        return this.builder;
    }

    @Override
    public void setPickUpItem(ItemStack stack) {
        this.pickUpStack = stack;
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.pickUpStack;
    }

    public String entityName() {
        return this.m_6095_().toString();
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
    protected void tickDespawn() {
        if (this.builder.tickDespawn != null) {
            EntityJSHelperClass.consumerCallback(this.builder.tickDespawn, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: tickDespawn.");
        } else {
            super.tickDespawn();
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        if (this.builder.doPostHurtEffects != null) {
            ContextUtils.ArrowLivingEntityContext context = new ContextUtils.ArrowLivingEntityContext(this, target);
            EntityJSHelperClass.consumerCallback(this.builder.doPostHurtEffects, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: doPostHurtEffects.");
        } else {
            super.doPostHurtEffects(target);
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return this.builder != null && this.builder.defaultHitGroundSoundEvent != null ? (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.defaultHitGroundSoundEvent)) : super.getDefaultHitGroundSoundEvent();
    }

    @Override
    protected float getWaterInertia() {
        return this.builder.setWaterInertia != null ? this.builder.setWaterInertia : super.getWaterInertia();
    }

    @Override
    protected boolean tryPickup(Player player) {
        if (this.builder.tryPickup == null) {
            return super.tryPickup(player);
        } else {
            ContextUtils.ArrowPlayerContext context = new ContextUtils.ArrowPlayerContext(player, this);
            Object obj = this.builder.tryPickup.apply(context);
            if (obj instanceof Boolean b) {
                return b;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid value for tryPickup from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.tryPickup(player));
                return super.tryPickup(player);
            }
        }
    }

    private void resetPiercedEntities() {
        if (this.piercedAndKilledEntities != null) {
            this.piercedAndKilledEntities.clear();
        }
        if (this.piercingIgnoreEntityIds != null) {
            this.piercingIgnoreEntityIds.clear();
        }
    }

    public double setDamageFunction() {
        if (this.builder.setDamageFunction != null) {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.setDamageFunction.apply(this), "double");
            if (obj != null) {
                return (Double) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid setDamageFunction for arrow builder: " + this.builder.setDamageFunction.apply(this) + ". Must be a double. Defaulting to super method: 0");
                return 0.0;
            }
        } else {
            return 0.0;
        }
    }

    @Override
    public void setBaseDamage(double pBaseDamage) {
        this.baseDamage = pBaseDamage + this.builder.setBaseDamage + this.setDamageFunction();
    }

    @Override
    public double getBaseDamage() {
        return this.baseDamage;
    }

    @Override
    public void setKnockback(int pKnockback) {
        if (this.builder.setKnockback != null) {
            this.knockback = this.builder.setKnockback + pKnockback;
        } else {
            this.knockback = pKnockback;
        }
    }

    @Override
    public int getKnockback() {
        return this.knockback;
    }

    @Override
    public void setEnchantmentEffectsFromEntity(LivingEntity pShooter, float pVelocity) {
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, pShooter);
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, pShooter);
        this.setBaseDamage((double) (pVelocity * 2.0F) + this.f_19796_.triangle((double) this.m_9236_().m_46791_().getId() * 0.11, 0.57425));
        if (i > 0) {
            this.setBaseDamage(this.getBaseDamage() + (double) i * 0.5 + 0.5);
        }
        if (j > 0) {
            this.setKnockback(j);
        }
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAMING_ARROWS, pShooter) > 0) {
            this.m_20254_(100);
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.builder.onHurt != null) {
            ContextUtils.EntityHurtContext context = new ContextUtils.EntityHurtContext(this, pSource, pAmount);
            EntityJSHelperClass.consumerCallback(this.builder.onHurt, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHurt.");
        }
        return super.m_6469_(pSource, pAmount);
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
    public void tick() {
        super.tick();
        this.isMoving = this.movementTracker.isMoving(this);
        if (this.builder.tick != null) {
            EntityJSHelperClass.consumerCallback(this.builder.tick, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: tick.");
        }
    }

    @Override
    public void move(MoverType pType, Vec3 pPos) {
        super.move(pType, pPos);
        if (this.builder.move != null) {
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
            super.playerTouch(player);
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
        if (this.builder.thunderHit != null) {
            super.m_8038_(serverLevel0, lightningBolt1);
            ContextUtils.EThunderHitContext context = new ContextUtils.EThunderHitContext(serverLevel0, lightningBolt1, this);
            EntityJSHelperClass.consumerCallback(this.builder.thunderHit, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: thunderHit.");
        }
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, @NotNull DamageSource damageSource) {
        if (this.builder.onFall != null) {
            ContextUtils.EEntityFallDamageContext context = new ContextUtils.EEntityFallDamageContext(this, damageMultiplier, distance, damageSource);
            EntityJSHelperClass.consumerCallback(this.builder.onFall, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onLivingFall.");
        }
        return super.m_142535_(distance, damageMultiplier, damageSource);
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.builder.onAddedToWorld != null && !this.m_9236_().isClientSide()) {
            EntityJSHelperClass.consumerCallback(this.builder.onAddedToWorld, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onAddedToWorld.");
        }
    }

    @Override
    public void setSprinting(boolean sprinting) {
        if (this.builder.onSprint != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onSprint, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onSprint.");
        }
        super.m_6858_(sprinting);
    }

    @Override
    public void stopRiding() {
        super.m_8127_();
        if (this.builder.onStopRiding != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onStopRiding, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onStopRiding.");
        }
    }

    @Override
    public void rideTick() {
        super.m_6083_();
        if (this.builder.rideTick != null) {
            EntityJSHelperClass.consumerCallback(this.builder.rideTick, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: rideTick.");
        }
    }

    @Override
    public void onClientRemoval() {
        if (this.builder.onClientRemoval != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onClientRemoval, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onClientRemoval.");
        }
        super.m_142036_();
    }

    @Override
    public void lavaHurt() {
        if (this.builder.lavaHurt != null) {
            EntityJSHelperClass.consumerCallback(this.builder.lavaHurt, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: lavaHurt.");
        }
        super.m_20093_();
    }

    @Override
    protected void onFlap() {
        if (this.builder.onFlap != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onFlap, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onFlap.");
        }
        super.m_142043_();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        if (this.builder.shouldRenderAtSqrDistance != null) {
            ContextUtils.EntitySqrDistanceContext context = new ContextUtils.EntitySqrDistanceContext(distance, this);
            Object obj = this.builder.shouldRenderAtSqrDistance.apply(context);
            if (obj instanceof Boolean b) {
                return b;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid shouldRenderAtSqrDistance for arrow builder: " + obj + ". Must be a boolean. Defaulting to super method: " + super.shouldRenderAtSqrDistance(distance));
        }
        return super.shouldRenderAtSqrDistance(distance);
    }

    public boolean isMoving() {
        return this.isMoving;
    }

    @Override
    public boolean isAttackable() {
        return this.builder.isAttackable != null ? this.builder.isAttackable : super.isAttackable();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        float f = (float) this.m_20184_().length();
        int i = Mth.ceil(Mth.clamp((double) f * this.baseDamage, 0.0, 2.147483647E9));
        if (this.m_36796_() > 0) {
            if (this.piercingIgnoreEntityIds == null) {
                this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }
            if (this.piercedAndKilledEntities == null) {
                this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
            }
            if (this.piercingIgnoreEntityIds.size() >= this.m_36796_() + 1) {
                this.m_146870_();
                return;
            }
            this.piercingIgnoreEntityIds.add(entity.getId());
        }
        if (this.m_36792_()) {
            long j = (long) this.f_19796_.nextInt(i / 2 + 2);
            i = (int) Math.min(j + (long) i, 2147483647L);
        }
        Entity entity1 = this.m_19749_();
        DamageSource damagesource;
        if (entity1 == null) {
            damagesource = this.m_269291_().arrow(this, this);
        } else {
            damagesource = this.m_269291_().arrow(this, entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity) entity1).setLastHurtMob(entity);
            }
        }
        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int k = entity.getRemainingFireTicks();
        if (this.m_6060_() && !flag) {
            entity.setSecondsOnFire(5);
        }
        if (entity.hurt(damagesource, (float) i)) {
            if (flag) {
                return;
            }
            if (entity instanceof LivingEntity livingentity) {
                if (!this.m_9236_().isClientSide && this.m_36796_() <= 0) {
                    livingentity.setArrowCount(livingentity.getArrowCount() + 1);
                }
                if (this.knockback > 0) {
                    double d0 = Math.max(0.0, 1.0 - livingentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    Vec3 vec3 = this.m_20184_().multiply(1.0, 0.0, 1.0).normalize().scale((double) this.knockback * 0.6 * d0);
                    if (vec3.lengthSqr() > 0.0) {
                        livingentity.m_5997_(vec3.x, 0.1, vec3.z);
                    }
                }
                if (!this.m_9236_().isClientSide && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity);
                }
                this.doPostHurtEffects(livingentity);
                if (entity1 != null && livingentity != entity1 && livingentity instanceof Player && entity1 instanceof ServerPlayer && !this.m_20067_()) {
                    ((ServerPlayer) entity1).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }
                if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(livingentity);
                }
                if (!this.m_9236_().isClientSide && entity1 instanceof ServerPlayer serverplayer) {
                    if (this.piercedAndKilledEntities != null && this.m_36795_()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayer, this.piercedAndKilledEntities);
                    } else if (!entity.isAlive() && this.m_36795_()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayer, Arrays.asList(entity));
                    }
                }
            }
            this.m_5496_(this.getDefaultHitGroundSoundEvent(), 1.0F, 1.2F / (this.f_19796_.nextFloat() * 0.2F + 0.9F));
            if (this.m_36796_() <= 0) {
                this.m_146870_();
            }
        } else {
            entity.setRemainingFireTicks(k);
            this.m_20256_(this.m_20184_().scale(-0.1));
            this.m_146922_(this.m_146908_() + 180.0F);
            this.f_19859_ += 180.0F;
            if (!this.m_9236_().isClientSide && this.m_20184_().lengthSqr() < 1.0E-7) {
                if (this.f_36705_ == AbstractArrow.Pickup.ALLOWED) {
                    this.m_5552_(this.getPickupItem(), 0.1F);
                }
                this.m_146870_();
            }
        }
        if (this.builder != null && this.builder.onHitEntity != null) {
            ContextUtils.ArrowEntityHitContext context = new ContextUtils.ArrowEntityHitContext(pResult, this);
            EntityJSHelperClass.consumerCallback(this.builder.onHitEntity, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHitEntity.");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putDouble("damage", this.baseDamage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("damage", 99)) {
            this.baseDamage = pCompound.getDouble("damage");
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.m_36740_(this.getDefaultHitGroundSoundEvent());
        this.resetPiercedEntities();
        if (this.builder != null && this.builder.onHitBlock != null) {
            ContextUtils.ArrowBlockHitContext context = new ContextUtils.ArrowBlockHitContext(result, this);
            EntityJSHelperClass.consumerCallback(this.builder.onHitBlock, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHitBlock.");
        }
        super.onHitBlock(result);
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
        return super.canHitEntity(entity);
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
}
package yesman.epicfight.world.capabilities.entitypatch;

import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.ServerAnimator;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPPlayAnimation;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.TargetIndicatorCheckEvent;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

public abstract class LivingEntityPatch<T extends LivingEntity> extends HurtableEntityPatch<T> {

    public static final EntityDataAccessor<Float> STUN_SHIELD = new EntityDataAccessor<>(251, EntityDataSerializers.FLOAT);

    public static final EntityDataAccessor<Float> MAX_STUN_SHIELD = new EntityDataAccessor<>(252, EntityDataSerializers.FLOAT);

    public static final EntityDataAccessor<Integer> EXECUTION_RESISTANCE = new EntityDataAccessor<>(254, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Boolean> AIRBORNE = new EntityDataAccessor<>(250, EntityDataSerializers.BOOLEAN);

    private ItemStack tempOffhandHolder = ItemStack.EMPTY;

    private AttackResult.ResultType lastResultType;

    private float lastDealDamage;

    protected Entity lastTryHurtEntity;

    protected LivingEntity grapplingTarget;

    protected Armature armature;

    protected EntityState state = EntityState.DEFAULT_STATE;

    protected Animator animator;

    protected Vec3 lastAttackPosition;

    protected EpicFightDamageSource epicFightDamageSource;

    protected boolean isLastAttackSuccess;

    public LivingMotion currentLivingMotion = LivingMotions.IDLE;

    public LivingMotion currentCompositeMotion = LivingMotions.IDLE;

    public void onConstructed(T entityIn) {
        super.onConstructed(entityIn);
        this.armature = Armatures.getArmatureFor(this);
        this.animator = EpicFightMod.getAnimator(this);
        this.animator.init();
        this.original.m_20088_().define(STUN_SHIELD, 0.0F);
        this.original.m_20088_().define(MAX_STUN_SHIELD, 0.0F);
        this.original.m_20088_().define(EXECUTION_RESISTANCE, 1);
        this.original.m_20088_().define(AIRBORNE, false);
    }

    @Override
    public void onJoinWorld(T entityIn, EntityJoinLevelEvent event) {
        super.onJoinWorld(entityIn, event);
        this.initAttributes();
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void initAnimator(ClientAnimator var1);

    public abstract void updateMotion(boolean var1);

    public Armature getArmature() {
        return this.armature;
    }

    protected void initAttributes() {
        this.original.getAttribute(EpicFightAttributes.WEIGHT.get()).setBaseValue(this.original.getAttribute(Attributes.MAX_HEALTH).getBaseValue() * 2.0);
        this.original.getAttribute(EpicFightAttributes.MAX_STRIKES.get()).setBaseValue(1.0);
        this.original.getAttribute(EpicFightAttributes.ARMOR_NEGATION.get()).setBaseValue(0.0);
        this.original.getAttribute(EpicFightAttributes.IMPACT.get()).setBaseValue(0.5);
    }

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        this.animator.tick();
        super.tick(event);
        if (this.original.deathTime == 19) {
            this.aboutToDeath();
        }
        if (this.original.f_19861_) {
            this.setAirborneState(false);
        }
    }

    public void onFall(LivingFallEvent event) {
        if (!this.getOriginal().m_9236_().isClientSide() && (this.isAirborneState() || this.getOriginal().m_9236_().getGameRules().getBoolean(EpicFightGamerules.HAS_FALL_ANIMATION) && event.getDamageMultiplier() > 0.0F && !this.getEntityState().inaction()) && (this.isAirborneState() || event.getDistance() > 5.0F)) {
            StaticAnimation fallAnimation = this.<Animator>getAnimator().getLivingAnimation(LivingMotions.LANDING_RECOVERY, this.getHitAnimation(StunType.FALL));
            if (fallAnimation != null) {
                this.playAnimationSynchronized(fallAnimation, 0.0F);
            }
        }
        this.setAirborneState(false);
    }

    @Override
    public void onDeath(LivingDeathEvent event) {
        this.<Animator>getAnimator().playDeathAnimation();
        this.currentLivingMotion = LivingMotions.DEATH;
    }

    public void updateEntityState() {
        this.state = this.animator.getEntityState();
    }

    public void cancelAnyAction() {
        this.original.stopUsingItem();
        ForgeEventFactory.onUseItemStop(this.original, this.original.getUseItem(), this.original.getUseItemRemainingTicks());
    }

    public CapabilityItem getHoldingItemCapability(InteractionHand hand) {
        return EpicFightCapabilities.getItemStackCapability(this.original.getItemInHand(hand));
    }

    public CapabilityItem getAdvancedHoldingItemCapability(InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return this.getHoldingItemCapability(hand);
        } else {
            return this.isOffhandItemValid() ? this.getHoldingItemCapability(hand) : CapabilityItem.EMPTY;
        }
    }

    public EpicFightDamageSource getDamageSource(StaticAnimation animation, InteractionHand hand) {
        EpicFightDamageSources damageSources = EpicFightDamageSources.of(this.original.m_9236_());
        EpicFightDamageSource damagesource = damageSources.mobAttack(this.original).setAnimation(animation);
        damagesource.setImpact(this.getImpact(hand));
        damagesource.setArmorNegation(this.getArmorNegation(hand));
        damagesource.setHurtItem(this.original.getItemInHand(hand));
        return damagesource;
    }

    public AttackResult tryHurt(DamageSource damageSource, float amount) {
        return AttackResult.of(this.getEntityState().attackResult(damageSource), amount);
    }

    public AttackResult tryHarm(Entity target, EpicFightDamageSource damagesource, float amount) {
        LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(target, LivingEntityPatch.class);
        return entitypatch != null ? entitypatch.tryHurt(damagesource, amount) : AttackResult.success(amount);
    }

    @Nullable
    public EpicFightDamageSource getEpicFightDamageSource() {
        return this.epicFightDamageSource;
    }

    protected void setOffhandDamage(boolean execute) {
        if (execute) {
            ItemStack mainHandItem = this.getOriginal().getMainHandItem();
            ItemStack offHandItem = this.isOffhandItemValid() ? this.getOriginal().getOffhandItem() : ItemStack.EMPTY;
            if (!this.isOffhandItemValid()) {
                this.tempOffhandHolder = this.getOriginal().getOffhandItem();
            }
            this.getOriginal().setItemInHand(InteractionHand.MAIN_HAND, offHandItem);
            this.getOriginal().setItemInHand(InteractionHand.OFF_HAND, mainHandItem);
            AttributeInstance damageAttributeInstance = this.original.getAttribute(Attributes.ATTACK_DAMAGE);
            Collection<AttributeModifier> modifiers = this.isOffhandItemValid() ? offHandItem.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE) : null;
            mainHandItem.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE).forEach(damageAttributeInstance::m_22130_);
            if (modifiers != null) {
                modifiers.forEach(damageAttributeInstance::m_22118_);
            }
        }
    }

    protected void recoverMainhandDamage(boolean execute) {
        if (execute) {
            ItemStack mainHandItem = this.tempOffhandHolder.isEmpty() ? this.getOriginal().getMainHandItem() : this.tempOffhandHolder;
            ItemStack offHandItem = this.getOriginal().getOffhandItem();
            this.getOriginal().setItemInHand(InteractionHand.MAIN_HAND, offHandItem);
            this.getOriginal().setItemInHand(InteractionHand.OFF_HAND, mainHandItem);
            if (!this.tempOffhandHolder.isEmpty()) {
                this.tempOffhandHolder = ItemStack.EMPTY;
            }
            AttributeInstance damageAttributeInstance = this.original.getAttribute(Attributes.ATTACK_DAMAGE);
            Collection<AttributeModifier> modifiers = mainHandItem.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE);
            if (modifiers != null) {
                modifiers.forEach(damageAttributeInstance::m_22130_);
            }
            offHandItem.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE).forEach(damageAttributeInstance::m_22118_);
        }
    }

    public void setLastAttackResult(AttackResult attackResult) {
        this.lastResultType = attackResult.resultType;
        this.lastDealDamage = attackResult.damage;
    }

    public void setLastAttackEntity(Entity tryHurtEntity) {
        this.lastTryHurtEntity = tryHurtEntity;
    }

    protected boolean checkLastAttackSuccess(Entity target) {
        boolean success = target.is(this.lastTryHurtEntity);
        this.lastTryHurtEntity = null;
        if (success && !this.isLastAttackSuccess) {
            this.setLastAttackSuccess(true);
        }
        return success;
    }

    public AttackResult attack(EpicFightDamageSource damageSource, Entity target, InteractionHand hand) {
        return this.checkLastAttackSuccess(target) ? new AttackResult(this.lastResultType, this.lastDealDamage) : AttackResult.missed(0.0F);
    }

    public float getModifiedBaseDamage(float baseDamage) {
        return baseDamage;
    }

    public boolean onDrop(LivingDropsEvent event) {
        return false;
    }

    public void gatherDamageDealt(EpicFightDamageSource source, float amount) {
    }

    @Override
    public float getStunShield() {
        return this.original.m_20088_().get(STUN_SHIELD);
    }

    @Override
    public void setStunShield(float value) {
        value = Math.max(value, 0.0F);
        value = Math.min(value, this.getMaxStunShield());
        this.original.m_20088_().set(STUN_SHIELD, value);
    }

    public float getMaxStunShield() {
        return this.original.m_20088_().get(MAX_STUN_SHIELD);
    }

    public void setMaxStunShield(float value) {
        value = Math.max(value, 0.0F);
        this.original.m_20088_().set(MAX_STUN_SHIELD, value);
    }

    public int getExecutionResistance() {
        return this.original.m_20088_().get(EXECUTION_RESISTANCE);
    }

    public void setExecutionResistance(int value) {
        int maxExecutionResistance = (int) this.original.getAttributeValue(EpicFightAttributes.MAX_EXECUTION_RESISTANCE.get());
        value = Math.min(maxExecutionResistance, value);
        this.original.m_20088_().set(EXECUTION_RESISTANCE, value);
    }

    @Override
    public float getWeight() {
        return (float) this.original.getAttributeValue(EpicFightAttributes.WEIGHT.get());
    }

    public void rotateTo(float degree, float limit, boolean syncPrevRot) {
        LivingEntity entity = this.getOriginal();
        float yRot = Mth.wrapDegrees(entity.m_146908_());
        float amount = Mth.clamp(Mth.wrapDegrees(degree - yRot), -limit, limit);
        float f1 = yRot + amount;
        if (syncPrevRot) {
            entity.f_19859_ = f1;
            entity.yHeadRotO = f1;
            entity.yBodyRotO = f1;
        }
        entity.m_146922_(f1);
        entity.yHeadRot = f1;
        entity.yBodyRot = f1;
    }

    public void rotateTo(Entity target, float limit, boolean syncPrevRot) {
        Vec3 playerPosition = this.original.m_20182_();
        Vec3 targetPosition = target.position();
        float yaw = (float) MathUtils.getYRotOfVector(targetPosition.subtract(playerPosition));
        this.rotateTo(yaw, limit, syncPrevRot);
    }

    public LivingEntity getTarget() {
        return this.original.getLastHurtMob();
    }

    public float getAttackDirectionPitch() {
        float partialTicks = EpicFightMod.isPhysicalClient() ? Minecraft.getInstance().getFrameTime() : 1.0F;
        float pitch = -this.getOriginal().m_5686_(partialTicks);
        float correct = pitch > 0.0F ? 0.03333F * (float) Math.pow((double) pitch, 2.0) : -0.03333F * (float) Math.pow((double) pitch, 2.0);
        return Mth.clamp(correct, -30.0F, 30.0F);
    }

    public float getCameraXRot() {
        return this.original.m_146909_();
    }

    public float getCameraYRot() {
        return this.original.m_146908_();
    }

    @OnlyIn(Dist.CLIENT)
    public OpenMatrix4f getHeadMatrix(float partialTicks) {
        float f2;
        if (this.state.inaction()) {
            f2 = 0.0F;
        } else {
            float f = MathUtils.lerpBetween(this.original.yBodyRotO, this.original.yBodyRot, partialTicks);
            float f1 = MathUtils.lerpBetween(this.original.yHeadRotO, this.original.yHeadRot, partialTicks);
            f2 = f1 - f;
            if (this.original.m_20202_() != null) {
                if (f2 > 45.0F) {
                    f2 = 45.0F;
                } else if (f2 < -45.0F) {
                    f2 = -45.0F;
                }
            }
        }
        return MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, this.original.f_19860_, this.original.m_146909_(), f2, f2, partialTicks, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public OpenMatrix4f getModelMatrix(float partialTicks) {
        float scale = this.original.isBaby() ? 0.5F : 1.0F;
        float prevYRot;
        float yRot;
        if (this.original.m_20202_() instanceof LivingEntity ridingEntity) {
            prevYRot = ridingEntity.yBodyRotO;
            yRot = ridingEntity.yBodyRot;
        } else {
            prevYRot = this.isLogicalClient() ? this.original.yBodyRotO : this.original.m_146908_();
            yRot = this.isLogicalClient() ? this.original.yBodyRot : this.original.m_146908_();
        }
        return MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, prevYRot, yRot, partialTicks, scale, scale, scale);
    }

    public void reserveAnimation(StaticAnimation animation) {
        this.animator.reserveAnimation(animation);
        EpicFightNetworkManager.sendToAllPlayerTrackingThisEntity(new SPPlayAnimation(animation, this.original.m_19879_(), 0.0F), this.original);
    }

    public void playAnimationSynchronized(StaticAnimation animation, float convertTimeModifier) {
        this.playAnimationSynchronized(animation, convertTimeModifier, SPPlayAnimation::new);
    }

    public void playAnimationSynchronized(StaticAnimation animation, float convertTimeModifier, LivingEntityPatch.AnimationPacketProvider packetProvider) {
        this.animator.playAnimation(animation, convertTimeModifier);
        EpicFightNetworkManager.sendToAllPlayerTrackingThisEntity(packetProvider.get(animation, convertTimeModifier, this), this.original);
    }

    protected void playReboundAnimation() {
        this.getClientAnimator().playReboundAnimation();
    }

    public void resetSize(EntityDimensions size) {
        EntityDimensions entitysize = this.original.f_19815_;
        this.original.f_19815_ = size;
        if (size.width < entitysize.width) {
            double d0 = (double) size.width / 2.0;
            this.original.m_20011_(new AABB(this.original.m_20185_() - d0, this.original.m_20186_(), this.original.m_20189_() - d0, this.original.m_20185_() + d0, this.original.m_20186_() + (double) size.height, this.original.m_20189_() + d0));
        } else {
            AABB axisalignedbb = this.original.m_20191_();
            this.original.m_20011_(new AABB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double) size.width, axisalignedbb.minY + (double) size.height, axisalignedbb.minZ + (double) size.width));
            if (size.width > entitysize.width && !this.original.m_9236_().isClientSide()) {
                float f = entitysize.width - size.width;
                this.original.m_6478_(MoverType.SELF, new Vec3((double) f, 0.0, (double) f));
            }
        }
    }

    @Override
    public boolean applyStun(StunType stunType, float stunTime) {
        this.original.xxa = 0.0F;
        this.original.yya = 0.0F;
        this.original.zza = 0.0F;
        this.original.m_20334_(0.0, 0.0, 0.0);
        this.cancelKnockback = true;
        StaticAnimation hitAnimation = this.getHitAnimation(stunType);
        if (hitAnimation != null) {
            this.playAnimationSynchronized(hitAnimation, stunType.hasFixedStunTime() ? 0.0F : stunTime);
            return true;
        } else {
            return false;
        }
    }

    public void correctRotation() {
    }

    public void updateHeldItem(CapabilityItem fromCap, CapabilityItem toCap, ItemStack from, ItemStack to, InteractionHand hand) {
    }

    public void updateArmor(CapabilityItem fromCap, CapabilityItem toCap, EquipmentSlot slotType) {
    }

    public void onAttackBlocked(DamageSource damageSource, LivingEntityPatch<?> opponent) {
    }

    public void onMount(boolean isMountOrDismount, Entity ridingEntity) {
    }

    public void notifyGrapplingWarning() {
    }

    public void onDodgeSuccess(DamageSource damageSource) {
    }

    @Override
    public boolean isStunned() {
        return this.getEntityState().hurt();
    }

    public <A extends Animator> A getAnimator() {
        return (A) this.animator;
    }

    @OnlyIn(Dist.CLIENT)
    public ClientAnimator getClientAnimator() {
        return this.getAnimator();
    }

    public ServerAnimator getServerAnimator() {
        return this.getAnimator();
    }

    public abstract StaticAnimation getHitAnimation(StunType var1);

    public void aboutToDeath() {
    }

    public SoundEvent getWeaponHitSound(InteractionHand hand) {
        return this.getAdvancedHoldingItemCapability(hand).getHitSound();
    }

    public SoundEvent getSwingSound(InteractionHand hand) {
        return this.getAdvancedHoldingItemCapability(hand).getSmashingSound();
    }

    public HitParticleType getWeaponHitParticle(InteractionHand hand) {
        return this.getAdvancedHoldingItemCapability(hand).getHitParticle();
    }

    public Collider getColliderMatching(InteractionHand hand) {
        return this.getAdvancedHoldingItemCapability(hand).getWeaponCollider();
    }

    public int getMaxStrikes(InteractionHand hand) {
        return (int) (hand == InteractionHand.MAIN_HAND ? this.original.getAttributeValue(EpicFightAttributes.MAX_STRIKES.get()) : (this.isOffhandItemValid() ? this.original.getAttributeValue(EpicFightAttributes.OFFHAND_MAX_STRIKES.get()) : this.original.getAttribute(EpicFightAttributes.MAX_STRIKES.get()).getBaseValue()));
    }

    public float getArmorNegation(InteractionHand hand) {
        return (float) (hand == InteractionHand.MAIN_HAND ? this.original.getAttributeValue(EpicFightAttributes.ARMOR_NEGATION.get()) : (this.isOffhandItemValid() ? this.original.getAttributeValue(EpicFightAttributes.OFFHAND_ARMOR_NEGATION.get()) : this.original.getAttribute(EpicFightAttributes.ARMOR_NEGATION.get()).getBaseValue()));
    }

    public float getImpact(InteractionHand hand) {
        int i = 0;
        float impact;
        if (hand == InteractionHand.MAIN_HAND) {
            impact = (float) this.original.getAttributeValue(EpicFightAttributes.IMPACT.get());
            i = this.getOriginal().getMainHandItem().getEnchantmentLevel(Enchantments.KNOCKBACK);
        } else if (this.isOffhandItemValid()) {
            impact = (float) this.original.getAttributeValue(EpicFightAttributes.OFFHAND_IMPACT.get());
            i = this.getOriginal().getOffhandItem().getEnchantmentLevel(Enchantments.KNOCKBACK);
        } else {
            impact = (float) this.original.getAttribute(EpicFightAttributes.IMPACT.get()).getBaseValue();
        }
        return impact * (1.0F + (float) i * 0.12F);
    }

    public ItemStack getValidItemInHand(InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return this.original.getItemInHand(hand);
        } else {
            return this.isOffhandItemValid() ? this.original.getItemInHand(hand) : ItemStack.EMPTY;
        }
    }

    public boolean isOffhandItemValid() {
        return this.getHoldingItemCapability(InteractionHand.MAIN_HAND).checkOffhandValid(this);
    }

    public boolean isTeammate(Entity entityIn) {
        if (this.original.m_20202_() != null && this.original.m_20202_().equals(entityIn)) {
            return true;
        } else {
            return this.isRideOrBeingRidden(entityIn) ? true : this.original.m_7307_(entityIn) && this.original.m_5647_() != null && !this.original.m_5647_().isAllowFriendlyFire();
        }
    }

    public boolean canPush(Entity entity) {
        LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
        if (entitypatch != null) {
            EntityState state = entitypatch.getEntityState();
            if (state.inaction()) {
                return false;
            }
        }
        EntityState thisState = this.getEntityState();
        return !thisState.inaction() && !entity.is(this.grapplingTarget);
    }

    public LivingEntity getGrapplingTarget() {
        return this.grapplingTarget;
    }

    public void setGrapplingTarget(LivingEntity grapplingTarget) {
        this.grapplingTarget = grapplingTarget;
    }

    public Vec3 getLastAttackPosition() {
        return this.lastAttackPosition;
    }

    public void setLastAttackPosition() {
        this.lastAttackPosition = this.original.m_20182_();
    }

    private boolean isRideOrBeingRidden(Entity entityIn) {
        LivingEntity orgEntity = this.getOriginal();
        for (Entity passanger : orgEntity.m_20197_()) {
            if (passanger.equals(entityIn)) {
                return true;
            }
        }
        for (Entity passangerx : entityIn.getPassengers()) {
            if (passangerx.equals(orgEntity)) {
                return true;
            }
        }
        return false;
    }

    public void setAirborneState(boolean airborne) {
        this.original.m_20088_().set(AIRBORNE, airborne);
    }

    public boolean isAirborneState() {
        return this.original.m_20088_().get(AIRBORNE);
    }

    public void setLastAttackSuccess(boolean setter) {
        this.isLastAttackSuccess = setter;
    }

    public boolean isLastAttackSuccess() {
        return this.isLastAttackSuccess;
    }

    public boolean shouldMoveOnCurrentSide(ActionAnimation actionAnimation) {
        return !this.isLogicalClient();
    }

    public boolean isFirstPerson() {
        return false;
    }

    @Override
    public boolean overrideRender() {
        return true;
    }

    public boolean shouldBlockMoving() {
        return false;
    }

    public float getYRotLimit() {
        return 20.0F;
    }

    public double getXOld() {
        return this.original.f_19790_;
    }

    public double getYOld() {
        return this.original.f_19791_;
    }

    public double getZOld() {
        return this.original.f_19792_;
    }

    @Override
    public EntityState getEntityState() {
        return this.state;
    }

    public InteractionHand getAttackingHand() {
        Pair<AnimationPlayer, AttackAnimation> layerInfo = this.<Animator>getAnimator().findFor(AttackAnimation.class);
        return layerInfo != null ? ((AttackAnimation) layerInfo.getSecond()).getPhaseByTime(((AnimationPlayer) layerInfo.getFirst()).getElapsedTime()).hand : null;
    }

    public LivingMotion getCurrentLivingMotion() {
        return this.currentLivingMotion;
    }

    public List<LivingEntity> getCurrenltyAttackedEntities() {
        return this.<Animator>getAnimator().getAnimationVariables(AttackAnimation.HIT_ENTITIES);
    }

    public List<LivingEntity> getCurrenltyHurtEntities() {
        return this.<Animator>getAnimator().getAnimationVariables(AttackAnimation.HURT_ENTITIES);
    }

    public void removeHurtEntities() {
        this.<Animator>getAnimator().getAnimationVariables(AttackAnimation.HIT_ENTITIES).clear();
        this.<Animator>getAnimator().getAnimationVariables(AttackAnimation.HURT_ENTITIES).clear();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean flashTargetIndicator(LocalPlayerPatch playerpatch) {
        TargetIndicatorCheckEvent event = new TargetIndicatorCheckEvent(playerpatch, this);
        playerpatch.getEventListener().triggerEvents(PlayerEventListener.EventType.TARGET_INDICATOR_ALERT_CHECK_EVENT, event);
        return event.isCanceled();
    }

    @FunctionalInterface
    public interface AnimationPacketProvider {

        SPPlayAnimation get(StaticAnimation var1, float var2, LivingEntityPatch<?> var3);
    }
}
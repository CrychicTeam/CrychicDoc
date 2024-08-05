package com.nameless.indestructible.world.capability;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.nameless.indestructible.api.animation.types.AnimationEvent;
import com.nameless.indestructible.api.animation.types.CustomGuardAnimation;
import com.nameless.indestructible.data.AdvancedMobpatchReloader;
import com.nameless.indestructible.gameasset.GuardAnimations;
import com.nameless.indestructible.world.ai.goal.AdvancedChasingGoal;
import com.nameless.indestructible.world.ai.goal.AdvancedCombatGoal;
import com.nameless.indestructible.world.ai.goal.GuardGoal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPChangeLivingMotion;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.entitypatch.Faction;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;
import yesman.epicfight.world.damagesource.EpicFightDamageType;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.ai.brain.BrainRecomposer;
import yesman.epicfight.world.entity.ai.brain.task.AnimatedCombatBehavior;
import yesman.epicfight.world.entity.ai.brain.task.BackUpIfTooCloseStopInaction;
import yesman.epicfight.world.entity.ai.brain.task.MoveToTargetSinkStopInaction;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

public class AdvancedCustomHumanoidMobPatch<T extends PathfinderMob> extends HumanoidMobPatch<T> {

    private final AdvancedMobpatchReloader.AdvancedCustomHumanoidMobPatchProvider provider;

    private final Map<WeaponCategory, Map<Style, Pair<StaticAnimation, Pair<Float, Boolean>>>> guardMotions;

    private static final EntityDataAccessor<Float> STAMINA = new EntityDataAccessor<>(253, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> ATTACK_SPEED = new EntityDataAccessor<>(177, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> IS_BLOCKING = new EntityDataAccessor<>(178, EntityDataSerializers.BOOLEAN);

    private final int regenStaminaStandbyTime;

    private final float regenStaminaMultiply;

    private final float maxStamina;

    private final boolean hasStunReduction;

    private final float maxStunShield;

    private final int reganShieldStandbyTime;

    private final float reganShieldMultiply;

    private final float staminaLoseMultiply;

    private int block_tick;

    private boolean cancel_block;

    private int tickSinceLastAction;

    private int tickSinceBreakShield;

    private AdvancedCustomHumanoidMobPatch.CounterMotion counterMotion;

    private final float guardRadius;

    private AdvancedCustomHumanoidMobPatch.DamageSourceModifier damageSourceModifier = null;

    private final List<AnimationEvent.TimeStampedEvent> timeEvents = Lists.newArrayList();

    private final List<AnimationEvent.HitEvent> hitEvents = Lists.newArrayList();

    private final List<AnimationEvent.ConditionalEvent> stunEvents = Lists.newArrayList();

    private int phase;

    private float strafingForward;

    private float strafingClockwise;

    private int strafingTime;

    private int inactionTime;

    private int convertTick;

    private boolean isRunning;

    public AdvancedCustomHumanoidMobPatch(Faction faction, AdvancedMobpatchReloader.AdvancedCustomHumanoidMobPatchProvider provider) {
        super(faction);
        this.provider = provider;
        this.regenStaminaStandbyTime = this.provider.getRegenStaminaStandbyTime();
        this.regenStaminaMultiply = this.provider.getRegenStaminaMultiply();
        this.maxStamina = this.provider.getMaxStamina();
        this.hasStunReduction = this.provider.hasStunReduction();
        this.maxStunShield = this.provider.getMaxStunShield();
        this.reganShieldStandbyTime = this.provider.getReganShieldStandbyTime();
        this.reganShieldMultiply = this.provider.getReganShieldMultiply();
        this.staminaLoseMultiply = this.provider.getStaminaLoseMultiply();
        this.weaponLivingMotions = this.provider.getHumanoidWeaponMotions();
        this.weaponAttackMotions = this.provider.getHumanoidCombatBehaviors();
        this.guardMotions = this.provider.getGuardMotions();
        this.guardRadius = this.provider.getGuardRadius();
    }

    public void onConstructed(T entityIn) {
        super.onConstructed(entityIn);
        entityIn.m_20088_().define(STAMINA, 0.0F);
        entityIn.m_20088_().define(ATTACK_SPEED, 1.0F);
        entityIn.m_20088_().define(IS_BLOCKING, false);
    }

    public void onJoinWorld(T entityIn, EntityJoinLevelEvent event) {
        super.onJoinWorld(entityIn, event);
        this.tickSinceLastAction = 0;
        this.tickSinceBreakShield = 0;
        this.block_tick = 30;
        this.setStamina(this.getMaxStamina());
        this.setAttackSpeed(1.0F);
        this.setPhase(0);
        if (this.maxStunShield > 0.0F) {
            this.setMaxStunShield(this.maxStunShield);
            this.setStunShield(this.maxStunShield);
        }
        if (!this.isLogicalClient()) {
            this.initStunEvent(this.provider);
            this.resetMotion();
        }
    }

    @Override
    public void serverTick(LivingEvent.LivingTickEvent event) {
        if (this.hasStunReduction) {
            super.serverTick(event);
        }
        if (!this.state.inaction() && this.isBlocking()) {
            this.tickSinceLastAction++;
        }
        float stamina = this.getStamina();
        float maxStamina = this.getMaxStamina();
        if (stamina < maxStamina) {
            if (this.tickSinceLastAction > this.regenStaminaStandbyTime) {
                float staminaFactor = 1.0F + (float) Math.pow((double) (stamina / (maxStamina - stamina * 0.5F)), 2.0);
                this.setStamina(stamina + maxStamina * 0.01F * this.regenStaminaMultiply * staminaFactor);
            } else {
                this.setStamina(stamina + 0.0015F * this.regenStaminaMultiply * maxStamina);
            }
        }
        if (maxStamina < stamina) {
            this.setStamina(maxStamina);
        }
        if (this.maxStunShield > 0.0F) {
            float stunShield = this.getStunShield();
            float maxStunShield = this.getMaxStunShield();
            if (stunShield > 0.0F) {
                if (stunShield < maxStunShield && !this.getEntityState().hurt() && !this.getEntityState().knockDown()) {
                    this.setStunShield(stunShield + 0.0015F * this.reganShieldMultiply * maxStunShield);
                }
                if (this.tickSinceBreakShield > 0) {
                    this.tickSinceBreakShield = 0;
                }
            }
            if (stunShield == 0.0F) {
                this.tickSinceBreakShield++;
                if (this.tickSinceBreakShield > this.reganShieldStandbyTime) {
                    this.setStunShield(this.getMaxStunShield());
                }
            }
            if (stunShield > maxStunShield) {
                this.setStunShield(this.getMaxStunShield());
            }
        }
    }

    @Override
    protected void clientTick(LivingEvent.LivingTickEvent event) {
        boolean shouldRunning = this.original.f_267362_.speed() >= 0.7F;
        if (shouldRunning != this.isRunning) {
            this.convertTick++;
            if (this.convertTick > 4) {
                this.isRunning = shouldRunning;
            }
        } else {
            this.convertTick = 0;
        }
    }

    public boolean hasTimeEvent() {
        return !this.timeEvents.isEmpty();
    }

    public List<AnimationEvent.TimeStampedEvent> getTimeEventList() {
        return this.timeEvents;
    }

    public void addEvent(AnimationEvent.TimeStampedEvent event) {
        this.timeEvents.add(event);
    }

    public boolean hasHitEvent() {
        return !this.hitEvents.isEmpty();
    }

    public List<AnimationEvent.HitEvent> getHitEventList() {
        return this.hitEvents;
    }

    public void addEvent(AnimationEvent.HitEvent event) {
        this.hitEvents.add(event);
    }

    private void initStunEvent(AdvancedMobpatchReloader.AdvancedCustomHumanoidMobPatchProvider provider) {
        this.stunEvents.clear();
        if (!provider.getStunEvent().isEmpty()) {
            this.stunEvents.addAll(provider.getStunEvent());
        }
    }

    public void resetMotion() {
        if (this.hasTimeEvent()) {
            this.timeEvents.clear();
        }
        if (this.hasHitEvent()) {
            this.hitEvents.clear();
        }
        if (this.damageSourceModifier != null) {
            this.damageSourceModifier = null;
        }
    }

    public void setBlockTick(int value) {
        this.block_tick = value;
    }

    public int getBlockTick() {
        return this.block_tick;
    }

    public void setBlocking(boolean blocking) {
        this.original.m_20088_().set(IS_BLOCKING, blocking);
    }

    public boolean isBlocking() {
        return this.original.m_20088_().get(IS_BLOCKING);
    }

    public void cancelBlock(boolean setCancel) {
        this.cancel_block = setCancel;
    }

    public float getMaxStamina() {
        return this.maxStamina;
    }

    public float getStamina() {
        return this.getMaxStamina() == 0.0F ? 0.0F : Math.max(0.0F, this.original.m_20088_().get(STAMINA));
    }

    public void setStamina(float value) {
        float f1 = Math.max(Math.min(value, this.getMaxStamina()), 0.0F);
        this.original.m_20088_().set(STAMINA, f1);
    }

    public float getAttackSpeed() {
        return this.original.m_20088_().get(ATTACK_SPEED);
    }

    public void setAttackSpeed(float value) {
        this.original.m_20088_().set(ATTACK_SPEED, Math.abs(value));
    }

    public void setCounterMotion(StaticAnimation counter, float cost, float chance, float speed) {
        this.counterMotion = new AdvancedCustomHumanoidMobPatch.CounterMotion(counter, cost, chance, speed);
    }

    public StaticAnimation getCounter() {
        return this.counterMotion.counter;
    }

    public float getCounterChance() {
        return this.counterMotion.chance;
    }

    public float getCounterStamina() {
        return this.counterMotion.cost;
    }

    public float getCounterSpeed() {
        return this.counterMotion.speed;
    }

    public void resetActionTick() {
        this.tickSinceLastAction = 0;
    }

    public int getTickSinceLastAction() {
        return this.tickSinceLastAction;
    }

    public void setDamageSourceModifier(AdvancedCustomHumanoidMobPatch.DamageSourceModifier damageSourceModifier) {
        this.damageSourceModifier = damageSourceModifier;
    }

    public int getPhase() {
        return this.phase;
    }

    public void setPhase(int phase) {
        this.phase = Math.min(Math.max(0, phase), 20);
    }

    public int getStrafingTime() {
        return this.strafingTime;
    }

    public void setStrafingTime(int time) {
        this.strafingTime = time;
    }

    public float getStrafingForward() {
        return this.strafingForward;
    }

    public float getStrafingClockwise() {
        return this.strafingClockwise;
    }

    public int getInactionTime() {
        return this.inactionTime;
    }

    public void setInactionTime(int time) {
        this.inactionTime = time;
    }

    public void setStrafingDirection(float forward, float clockwise) {
        this.strafingForward = forward;
        this.strafingClockwise = clockwise;
    }

    @Override
    public void setAIAsInfantry(boolean holdingRanedWeapon) {
        boolean isUsingBrain = this.getOriginal().m_6274_().availableBehaviorsByPriority.size() > 0;
        if (isUsingBrain) {
            if (!holdingRanedWeapon) {
                CombatBehaviors.Builder<HumanoidMobPatch<?>> builder = this.getHoldingItemWeaponMotionBuilder();
                if (builder != null) {
                    BrainRecomposer.replaceBehaviors(this.original.m_6274_(), Activity.FIGHT, MeleeAttack.class, new AnimatedCombatBehavior<>(this, builder.build(this)));
                }
                BrainRecomposer.replaceBehaviors(this.original.m_6274_(), Activity.FIGHT, OneShot.class, BehaviorBuilder.triggerIf(entity -> entity.m_21093_(is -> is.getItem() instanceof CrossbowItem), BackUpIfTooCloseStopInaction.create(5, 0.75F)));
                BrainRecomposer.replaceBehaviors(this.original.m_6274_(), Activity.CORE, MoveToTargetSink.class, new MoveToTargetSinkStopInaction());
            }
        } else if (!holdingRanedWeapon) {
            CombatBehaviors.Builder<HumanoidMobPatch<?>> builder = this.getHoldingItemWeaponMotionBuilder();
            if (builder != null) {
                this.original.f_21345_.addGoal(0, new AdvancedCombatGoal<>(this, builder.build(this)));
                this.original.f_21345_.addGoal(0, new GuardGoal<>(this, this.guardRadius));
                this.original.f_21345_.addGoal(1, new AdvancedChasingGoal<>(this, this.getOriginal(), this.provider.getChasingSpeed(), true, 0.0));
            }
        }
    }

    @Override
    protected void setWeaponMotions() {
        if (this.weaponAttackMotions == null) {
            super.setWeaponMotions();
        }
    }

    @Override
    protected void initAttributes() {
        this.original.m_21051_(EpicFightAttributes.WEIGHT.get()).setBaseValue((Double) this.provider.getAttributeValues().get(EpicFightAttributes.WEIGHT.get()));
        this.original.m_21051_(EpicFightAttributes.MAX_STRIKES.get()).setBaseValue((Double) this.provider.getAttributeValues().get(EpicFightAttributes.MAX_STRIKES.get()));
        this.original.m_21051_(EpicFightAttributes.ARMOR_NEGATION.get()).setBaseValue((Double) this.provider.getAttributeValues().get(EpicFightAttributes.ARMOR_NEGATION.get()));
        this.original.m_21051_(EpicFightAttributes.IMPACT.get()).setBaseValue((Double) this.provider.getAttributeValues().get(EpicFightAttributes.IMPACT.get()));
        this.original.m_21051_(EpicFightAttributes.OFFHAND_IMPACT.get()).setBaseValue(0.5);
        this.original.m_21051_(EpicFightAttributes.OFFHAND_ARMOR_NEGATION.get()).setBaseValue(0.0);
        this.original.m_21051_(EpicFightAttributes.OFFHAND_ATTACK_SPEED.get()).setBaseValue(1.2F);
        this.original.m_21051_(EpicFightAttributes.OFFHAND_MAX_STRIKES.get()).setBaseValue(1.0);
        if (this.provider.getAttributeValues().containsKey(Attributes.ATTACK_DAMAGE)) {
            this.original.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue((Double) this.provider.getAttributeValues().get(Attributes.ATTACK_DAMAGE));
        }
    }

    @Override
    public void initAnimator(ClientAnimator clientAnimator) {
        for (Pair<LivingMotion, StaticAnimation> pair : this.provider.getDefaultAnimations()) {
            clientAnimator.addLivingAnimation((LivingMotion) pair.getFirst(), (StaticAnimation) pair.getSecond());
        }
        clientAnimator.setCurrentMotionsAsDefault();
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        if (this.original.m_21223_() <= 0.0F) {
            this.currentLivingMotion = LivingMotions.DEATH;
        } else if (this.state.inaction() && considerInaction) {
            this.currentLivingMotion = LivingMotions.IDLE;
        } else if (this.original.m_20202_() != null) {
            this.currentLivingMotion = LivingMotions.MOUNT;
        } else if (this.original.m_20184_().y < -0.55F) {
            this.currentLivingMotion = LivingMotions.FALL;
        } else if (this.original.f_267362_.speed() > 0.01F) {
            if (this.isRunning) {
                this.currentLivingMotion = LivingMotions.CHASE;
            } else {
                this.currentLivingMotion = LivingMotions.WALK;
            }
        } else {
            this.currentLivingMotion = LivingMotions.IDLE;
        }
        this.currentCompositeMotion = this.currentLivingMotion;
        if (this.original.m_6117_()) {
            CapabilityItem activeItem = this.getHoldingItemCapability(this.original.m_7655_());
            UseAnim useAnim = this.original.m_21120_(this.original.m_7655_()).getUseAnimation();
            UseAnim secondUseAnim = activeItem.getUseAnimation(this);
            if (useAnim == UseAnim.BLOCK || secondUseAnim == UseAnim.BLOCK) {
                if (activeItem.getWeaponCategory() == CapabilityItem.WeaponCategories.SHIELD) {
                    this.currentCompositeMotion = LivingMotions.BLOCK_SHIELD;
                } else {
                    this.currentCompositeMotion = LivingMotions.BLOCK;
                }
            } else if (useAnim == UseAnim.BOW || useAnim == UseAnim.SPEAR) {
                this.currentCompositeMotion = LivingMotions.AIM;
            } else if (useAnim == UseAnim.CROSSBOW) {
                this.currentCompositeMotion = LivingMotions.RELOAD;
            } else {
                this.currentCompositeMotion = this.currentLivingMotion;
            }
        } else {
            if (this.isBlocking()) {
                this.currentCompositeMotion = LivingMotions.BLOCK;
            } else if (CrossbowItem.isCharged(this.original.m_21205_())) {
                this.currentCompositeMotion = LivingMotions.AIM;
            } else if (this.getClientAnimator().getCompositeLayer(Layer.Priority.MIDDLE).animationPlayer.getAnimation().isReboundAnimation()) {
                this.currentCompositeMotion = LivingMotions.NONE;
            } else if (this.original.f_20911_ && this.original.m_21257_().isEmpty()) {
                this.currentCompositeMotion = LivingMotions.DIGGING;
            } else {
                this.currentCompositeMotion = this.currentLivingMotion;
            }
            if (this.getClientAnimator().isAiming() && this.currentCompositeMotion != LivingMotions.AIM) {
                this.playReboundAnimation();
            }
        }
    }

    @Override
    public AttackResult attack(EpicFightDamageSource damageSource, Entity target, InteractionHand hand) {
        AttackResult result = super.attack(damageSource, target, hand);
        if (result.resultType.dealtDamage() && this.hasHitEvent()) {
            for (AnimationEvent.HitEvent event : this.getHitEventList()) {
                event.testAndExecute(this, target);
            }
        }
        return result;
    }

    @Override
    public StaticAnimation getHitAnimation(StunType stunType) {
        this.setAttackSpeed(1.0F);
        this.resetActionTick();
        this.resetMotion();
        return (StaticAnimation) this.provider.getStunAnimations().get(stunType);
    }

    @Override
    public OpenMatrix4f getModelMatrix(float partialTicks) {
        float scale = this.provider.getScale();
        return super.getModelMatrix(partialTicks).scale(scale, scale, scale);
    }

    @Override
    public void setStunReductionOnHit(StunType stunType) {
        if (this.hasStunReduction) {
            super.setStunReductionOnHit(stunType);
        }
    }

    @Override
    public float getStunReduction() {
        return this.hasStunReduction ? super.getStunReduction() : 0.0F;
    }

    @Override
    public void modifyLivingMotionByCurrentItem() {
        this.<Animator>getAnimator().resetLivingAnimations();
        CapabilityItem mainhandCap = this.getHoldingItemCapability(InteractionHand.MAIN_HAND);
        CapabilityItem offhandCap = this.getAdvancedHoldingItemCapability(InteractionHand.OFF_HAND);
        Map<LivingMotion, StaticAnimation> motionModifier = Maps.newHashMap();
        motionModifier.putAll(offhandCap.getLivingMotionModifier(this, InteractionHand.OFF_HAND));
        motionModifier.putAll(mainhandCap.getLivingMotionModifier(this, InteractionHand.MAIN_HAND));
        for (Entry<LivingMotion, StaticAnimation> entry : motionModifier.entrySet()) {
            this.<Animator>getAnimator().addLivingAnimation((LivingMotion) entry.getKey(), (StaticAnimation) entry.getValue());
        }
        if (this.weaponLivingMotions != null && this.weaponLivingMotions.containsKey(mainhandCap.getWeaponCategory())) {
            Map<Style, Set<Pair<LivingMotion, StaticAnimation>>> mapByStyle = (Map<Style, Set<Pair<LivingMotion, StaticAnimation>>>) this.weaponLivingMotions.get(mainhandCap.getWeaponCategory());
            Style style = mainhandCap.getStyle(this);
            if (mapByStyle.containsKey(style) || mapByStyle.containsKey(CapabilityItem.Styles.COMMON)) {
                for (Pair<LivingMotion, StaticAnimation> pair : (Set) mapByStyle.getOrDefault(style, (Set) mapByStyle.get(CapabilityItem.Styles.COMMON))) {
                    this.animator.addLivingAnimation((LivingMotion) pair.getFirst(), (StaticAnimation) pair.getSecond());
                }
            }
        }
        if (this.guardMotions != null && this.guardMotions.containsKey(mainhandCap.getWeaponCategory())) {
            Map<Style, Pair<StaticAnimation, Pair<Float, Boolean>>> motionByStyle = (Map<Style, Pair<StaticAnimation, Pair<Float, Boolean>>>) this.guardMotions.get(mainhandCap.getWeaponCategory());
            Style style = mainhandCap.getStyle(this);
            if (motionByStyle.containsKey(style) || motionByStyle.containsKey(CapabilityItem.Styles.COMMON)) {
                StaticAnimation guard = (StaticAnimation) ((Pair) motionByStyle.getOrDefault(style, (Pair) motionByStyle.get(CapabilityItem.Styles.COMMON))).getFirst();
                this.animator.addLivingAnimation(LivingMotions.BLOCK, guard);
            }
        }
        SPChangeLivingMotion msg = new SPChangeLivingMotion(this.original.m_19879_());
        msg.putEntries(this.<Animator>getAnimator().getLivingAnimationEntrySet());
        EpicFightNetworkManager.sendToAllPlayerTrackingThisEntity(msg, this.original);
    }

    private Pair<StaticAnimation, Pair<Float, Boolean>> getCurrentGuardMotion() {
        CapabilityItem itemCap = this.getHoldingItemCapability(InteractionHand.MAIN_HAND);
        if (this.guardMotions != null && itemCap != null) {
            Style style = itemCap.getStyle(this);
            Map<Style, Pair<StaticAnimation, Pair<Float, Boolean>>> mapByStyle = (Map<Style, Pair<StaticAnimation, Pair<Float, Boolean>>>) this.guardMotions.get(itemCap.getWeaponCategory());
            if (mapByStyle != null && (mapByStyle.containsKey(style) || mapByStyle.containsKey(CapabilityItem.Styles.COMMON))) {
                return (Pair<StaticAnimation, Pair<Float, Boolean>>) mapByStyle.getOrDefault(style, (Pair) mapByStyle.get(CapabilityItem.Styles.COMMON));
            }
        }
        return Pair.of(GuardAnimations.MOB_LONGSWORD_GUARD, Pair.of(1.0F, false));
    }

    public Float getStaminaCostMultiply() {
        return (Float) ((Pair) this.getCurrentGuardMotion().getSecond()).getFirst();
    }

    public CustomGuardAnimation getGuardAnimation() {
        Object var2 = this.getCurrentGuardMotion().getFirst();
        return var2 instanceof CustomGuardAnimation ? (CustomGuardAnimation) var2 : (CustomGuardAnimation) GuardAnimations.MOB_LONGSWORD_GUARD;
    }

    public boolean canBlockProjectile() {
        return (Boolean) ((Pair) this.getCurrentGuardMotion().getSecond()).getSecond();
    }

    @Override
    public AttackResult tryHurt(DamageSource damageSource, float amount) {
        AttackResult result = AttackResult.of(this.getEntityState().attackResult(damageSource), amount);
        if (result.resultType.dealtDamage()) {
            result = this.tryProcess(damageSource, amount);
        }
        return result;
    }

    private boolean isBlockableSource(DamageSource damageSource) {
        return !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR) && (!damageSource.is(DamageTypeTags.IS_PROJECTILE) || this.canBlockProjectile()) && !damageSource.is(DamageTypeTags.IS_EXPLOSION) && !damageSource.is(DamageTypes.MAGIC) && !damageSource.is(DamageTypeTags.IS_FIRE);
    }

    private AttackResult tryProcess(DamageSource damageSource, float amount) {
        if (this.isBlocking()) {
            CustomGuardAnimation animation = this.getGuardAnimation();
            StaticAnimation success = animation.successAnimation != null ? EpicFightMod.getInstance().animationManager.findAnimationByPath(animation.successAnimation) : Animations.SWORD_GUARD_HIT;
            boolean isFront = false;
            Vec3 sourceLocation = damageSource.getSourcePosition();
            if (sourceLocation != null) {
                Vec3 viewVector = this.getOriginal().m_20252_(1.0F);
                Vec3 toSourceLocation = sourceLocation.subtract(this.getOriginal().m_20182_()).normalize();
                if (toSourceLocation.dot(viewVector) > 0.0) {
                    isFront = true;
                }
            }
            if (this.isBlockableSource(damageSource) && isFront) {
                float impact;
                if (damageSource instanceof EpicFightDamageSource efDamageSource) {
                    impact = amount / 4.0F * (1.0F + efDamageSource.getImpact() / 2.0F);
                    if (efDamageSource.is(EpicFightDamageType.GUARD_PUNCTURE)) {
                        impact = Float.MAX_VALUE;
                    }
                } else {
                    impact = amount / 3.0F;
                }
                float knockback = 0.25F + Math.min(impact * 0.1F, 1.0F);
                if (damageSource.getDirectEntity() instanceof LivingEntity targetEntity) {
                    knockback += (float) EnchantmentHelper.getKnockbackBonus(targetEntity) * 0.1F;
                }
                float stamina = this.getStamina() - impact * this.getStaminaCostMultiply();
                this.setStamina(stamina);
                EpicFightParticles.HIT_BLUNT.get().spawnParticleWithArgument((ServerLevel) this.getOriginal().m_9236_(), HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, this.getOriginal(), damageSource.getDirectEntity());
                if (!(stamina >= 0.0F)) {
                    this.setBlocking(false);
                    this.applyStun(StunType.NEUTRALIZE, 2.0F);
                    this.setStamina(this.getMaxStamina());
                    return new AttackResult(AttackResult.ResultType.SUCCESS, amount / 2.0F);
                }
                float counter_cost = this.getCounterStamina();
                RandomSource random = this.getOriginal().m_217043_();
                if (random.nextFloat() < this.getCounterChance() && stamina >= counter_cost) {
                    this.getOriginal().m_7292_(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), 25));
                    this.setAttackSpeed(this.getCounterSpeed());
                    this.playAnimationSynchronized(this.getCounter(), 0.0F);
                    this.playSound(EpicFightSounds.CLASH.get(), -0.05F, 0.1F);
                    this.knockBackEntity(damageSource.getDirectEntity().position(), 0.1F);
                    if (this.cancel_block) {
                        this.setBlocking(false);
                    }
                    this.setStamina(this.getStamina() - counter_cost);
                } else {
                    this.playAnimationSynchronized(success, 0.1F);
                    this.playSound(animation.isShield ? SoundEvents.SHIELD_BLOCK : EpicFightSounds.CLASH.get(), -0.05F, 0.1F);
                    this.knockBackEntity(damageSource.getDirectEntity().position(), knockback);
                }
                return new AttackResult(AttackResult.ResultType.BLOCKED, amount);
            }
        }
        if (damageSource instanceof EpicFightDamageSource efDamageSourcex && this.staminaLoseMultiply > 0.0F && this.getStunShield() <= 0.0F) {
            DynamicAnimation animationx = this.<Animator>getAnimator().getPlayerFor(null).getAnimation();
            if (animationx != Animations.BIPED_COMMON_NEUTRALIZED && animationx != Animations.GREATSWORD_GUARD_BREAK) {
                this.setStamina(this.getStamina() - efDamageSourcex.getImpact() * this.staminaLoseMultiply);
            }
            if (this.getStamina() < efDamageSourcex.getImpact()) {
                efDamageSourcex.setStunType(StunType.NEUTRALIZE);
                this.setStamina(this.getMaxStamina());
            }
        }
        return new AttackResult(AttackResult.ResultType.SUCCESS, amount);
    }

    @Override
    public EpicFightDamageSource getDamageSource(StaticAnimation animation, InteractionHand hand) {
        EpicFightDamageSources damageSources = EpicFightDamageSources.of(this.original.m_9236_());
        EpicFightDamageSource damagesource = damageSources.mobAttack(this.original).setAnimation(animation);
        damagesource.setImpact(this.getImpact(hand));
        damagesource.setArmorNegation(this.getArmorNegation(hand));
        damagesource.setHurtItem(this.original.m_21120_(hand));
        if (this.damageSourceModifier != null) {
            damagesource.setImpact(this.getImpact(hand) * this.damageSourceModifier.impact());
            damagesource.setArmorNegation(Math.min(100.0F, this.getArmorNegation(hand) * this.damageSourceModifier.armor_negation()));
        }
        return damagesource;
    }

    @Override
    public void onDeath(LivingDeathEvent event) {
        this.resetMotion();
        this.setBlocking(false);
        this.<Animator>getAnimator().playDeathAnimation();
        this.currentLivingMotion = LivingMotions.DEATH;
    }

    @Override
    public float getModifiedBaseDamage(float baseDamage) {
        if (this.damageSourceModifier != null) {
            baseDamage *= this.damageSourceModifier.damage();
        }
        return baseDamage;
    }

    @Override
    public boolean applyStun(StunType stunType, float time) {
        DynamicAnimation animation = this.<Animator>getAnimator().getPlayerFor(null).getAnimation();
        if (animation == Animations.BIPED_COMMON_NEUTRALIZED || animation == Animations.GREATSWORD_GUARD_BREAK) {
            stunType = stunType == StunType.KNOCKDOWN ? stunType : StunType.NONE;
        }
        if (!this.stunEvents.isEmpty() && this.getHitAnimation(stunType) != null) {
            for (AnimationEvent.ConditionalEvent event : this.stunEvents) {
                event.testAndExecute(this, stunType.ordinal());
            }
        }
        return super.applyStun(stunType, time);
    }

    @Override
    public void knockBackEntity(Vec3 sourceLocation, float power) {
        DynamicAnimation animation = this.<Animator>getAnimator().getPlayerFor(null).getAnimation();
        if (animation != Animations.BIPED_COMMON_NEUTRALIZED && animation != Animations.GREATSWORD_GUARD_BREAK) {
            super.knockBackEntity(sourceLocation, power);
        }
    }

    public static record CounterMotion(StaticAnimation counter, float cost, float chance, float speed) {
    }

    public static record CustomAnimationMotion(StaticAnimation animation, float convertTime, float speed, float stamina) {
    }

    public static record DamageSourceModifier(float damage, float impact, float armor_negation) {
    }
}
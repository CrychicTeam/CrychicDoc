package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.network.IClientEventEntity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.SpellBarrageGoal;
import io.redspace.ironsspellbooks.network.ClientboundEntityEvent;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class DeadKingBoss extends AbstractSpellCastingMob implements Enemy, IAnimatedAttacker, IClientEventEntity {

    public static final byte STOP_MUSIC = 0;

    public static final byte START_MUSIC = 1;

    private final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(this.m_5446_(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true).setCreateWorldFog(true);

    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(DeadKingBoss.class, EntityDataSerializers.INT);

    private int transitionAnimationTime = 139;

    private boolean isCloseToGround;

    public boolean isMeleeing;

    private final RawAnimation phase_transition_animation = RawAnimation.begin().thenPlay("dead_king_die");

    private final RawAnimation melee = RawAnimation.begin().thenPlay("dead_king_melee");

    private final RawAnimation slam = RawAnimation.begin().thenPlay("dead_king_slam");

    private final AnimationController<DeadKingBoss> transitionController = new AnimationController<>(this, "dead_king_transition", 0, this::transitionPredicate);

    private final AnimationController<DeadKingBoss> meleeController = new AnimationController<>(this, "dead_king_animations", 0, this::meleePredicate);

    RawAnimation animationToPlay = null;

    @Override
    public void handleClientEvent(byte eventId) {
        switch(eventId) {
            case 0:
                DeadKingMusicManager.stop(this);
                break;
            case 1:
                DeadKingMusicManager.createOrResumeInstance(this);
        }
    }

    public DeadKingBoss(Level pLevel) {
        this(EntityRegistry.DEAD_KING.get(), pLevel);
        this.m_21530_();
    }

    public DeadKingBoss(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.m_21530_();
        this.f_21364_ = 60;
        this.f_21365_ = this.createLookControl();
        this.f_21342_ = this.createMoveControl();
    }

    private DeadKingAnimatedWarlockAttackGoal getCombatGoal() {
        return (DeadKingAnimatedWarlockAttackGoal) new DeadKingAnimatedWarlockAttackGoal(this, 1.0, 55, 85, 4.0F).setSpellQuality(0.3F, 0.5F).setSpells(List.of(SpellRegistry.RAY_OF_SIPHONING_SPELL.get(), SpellRegistry.BLOOD_SLASH_SPELL.get(), SpellRegistry.BLOOD_SLASH_SPELL.get(), SpellRegistry.WITHER_SKULL_SPELL.get(), SpellRegistry.WITHER_SKULL_SPELL.get(), SpellRegistry.WITHER_SKULL_SPELL.get(), SpellRegistry.FANG_STRIKE_SPELL.get(), SpellRegistry.FANG_STRIKE_SPELL.get(), SpellRegistry.POISON_ARROW_SPELL.get(), SpellRegistry.POISON_ARROW_SPELL.get(), SpellRegistry.BLIGHT_SPELL.get(), SpellRegistry.ACID_ORB_SPELL.get()), List.of(SpellRegistry.FANG_WARD_SPELL.get(), SpellRegistry.BLOOD_STEP_SPELL.get()), List.of(), List.of()).setMeleeBias(0.8F, 0.8F).setAllowFleeing(false);
    }

    @Override
    protected void registerGoals() {
        this.setFirstPhaseGoals();
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Villager.class, true));
        this.f_21346_.addGoal(5, new NearestAttackableTargetGoal(this, AbstractIllager.class, true));
    }

    protected void setFirstPhaseGoals() {
        this.f_21345_.getRunningGoals().forEach(WrappedGoal::m_8041_);
        this.f_21345_.removeAllGoals(x -> true);
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new DeadKingBoss.DeadKingBarrageGoal(this, SpellRegistry.WITHER_SKULL_SPELL.get(), 3, 4, 70, 140, 3));
        this.f_21345_.addGoal(2, new DeadKingBoss.DeadKingBarrageGoal(this, SpellRegistry.RAISE_DEAD_SPELL.get(), 3, 5, 600, 900, 1));
        this.f_21345_.addGoal(3, new DeadKingBoss.DeadKingBarrageGoal(this, SpellRegistry.BLOOD_STEP_SPELL.get(), 1, 1, 100, 180, 1));
        this.f_21345_.addGoal(4, this.getCombatGoal().setSingleUseSpell(SpellRegistry.RAISE_DEAD_SPELL.get(), 10, 50, 8, 8));
        this.f_21345_.addGoal(5, new PatrolNearLocationGoal(this, 32.0F, 0.9F));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    protected void setFinalPhaseGoals() {
        this.f_21345_.getRunningGoals().forEach(WrappedGoal::m_8041_);
        this.f_21345_.removeAllGoals(x -> true);
        this.f_21345_.addGoal(1, new DeadKingBoss.DeadKingBarrageGoal(this, SpellRegistry.WITHER_SKULL_SPELL.get(), 5, 5, 60, 140, 4));
        this.f_21345_.addGoal(2, new DeadKingBoss.DeadKingBarrageGoal(this, SpellRegistry.SUMMON_VEX_SPELL.get(), 3, 5, 400, 600, 1));
        this.f_21345_.addGoal(3, new DeadKingBoss.DeadKingBarrageGoal(this, SpellRegistry.BLOOD_STEP_SPELL.get(), 1, 1, 100, 180, 1));
        this.f_21345_.addGoal(4, this.getCombatGoal().setIsFlying().setSingleUseSpell(SpellRegistry.BLAZE_STORM_SPELL.get(), 10, 30, 10, 10));
        this.f_21345_.addGoal(5, new PatrolNearLocationGoal(this, 32.0F, 0.9F));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.hasUsedSingleAttack = false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundRegistry.DEAD_KING_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.DEAD_KING_DEATH.get();
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId != 3) {
            super.m_7822_(pId);
        }
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    public boolean isPushable() {
        return !this.isPhaseTransitioning();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        return pSpawnData;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.m_8061_(EquipmentSlot.OFFHAND, new ItemStack(ItemRegistry.BLOOD_STAFF.get()));
        this.m_21409_(EquipmentSlot.OFFHAND, 0.0F);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (super.m_7307_(pEntity)) {
            return true;
        } else {
            if (pEntity instanceof MagicSummon summon && summon.getSummoner() == this) {
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean isInvertedHealAndHarm() {
        return true;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void tick() {
        if (this.isPhase(DeadKingBoss.Phases.FinalPhase)) {
            this.m_20242_(true);
            if (this.f_19797_ % 10 == 0) {
                this.isCloseToGround = Utils.raycastForBlock(this.f_19853_, this.m_20182_(), this.m_20182_().subtract(0.0, 2.5, 0.0), ClipContext.Fluid.ANY).getType() == HitResult.Type.BLOCK;
            }
            Vec3 woosh = new Vec3((double) Mth.sin((float) (this.f_19797_ * 5) * (float) (Math.PI / 180.0)), ((double) Mth.cos((float) (this.f_19797_ * 3 + 986741) * (float) (Math.PI / 180.0)) + (this.isCloseToGround ? 0.05 : -0.185)) * 0.5, (double) Mth.sin((float) (this.f_19797_ * 1 + 465) * (float) (Math.PI / 180.0)));
            if (this.m_5448_() == null) {
                woosh = woosh.scale(0.25);
            }
            this.m_20256_(this.m_20184_().add(woosh.scale(0.0085F)));
        }
        super.m_8119_();
        if (this.f_19853_.isClientSide) {
            if (this.isPhase(DeadKingBoss.Phases.FinalPhase) && !this.m_20145_()) {
                float radius = 0.35F;
                for (int i = 0; i < 5; i++) {
                    Vec3 random = this.m_20182_().add(new Vec3((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * radius), (double) (1.0F + (this.f_19796_.nextFloat() * 2.0F - 1.0F) * radius), (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * radius)));
                    this.f_19853_.addParticle(ParticleTypes.SMOKE, random.x, random.y, random.z, 0.0, -0.1, 0.0);
                }
            }
        } else {
            float halfHealth = this.m_21233_() / 2.0F;
            if (this.isPhase(DeadKingBoss.Phases.FirstPhase)) {
                this.bossEvent.setProgress((this.m_21223_() - halfHealth) / (this.m_21233_() - halfHealth));
                if (this.m_21223_() <= halfHealth) {
                    this.setPhase(DeadKingBoss.Phases.Transitioning);
                    Player player = this.f_19853_.m_45930_(this, 16.0);
                    if (player != null) {
                        this.m_21391_(player, 360.0F, 360.0F);
                    }
                    if (!this.m_21224_()) {
                        this.m_21153_(halfHealth);
                    }
                    this.m_216990_(SoundRegistry.DEAD_KING_FAKE_DEATH.get());
                    this.m_20331_(true);
                }
            } else if (this.isPhase(DeadKingBoss.Phases.Transitioning)) {
                if (--this.transitionAnimationTime <= 0) {
                    this.setPhase(DeadKingBoss.Phases.FinalPhase);
                    MagicManager.spawnParticles(this.f_19853_, ParticleHelper.FIRE, this.m_20182_().x, this.m_20182_().y + 2.5, this.m_20182_().z, 80, 0.2, 0.2, 0.2, 0.25, true);
                    this.setFinalPhaseGoals();
                    this.m_20242_(true);
                    this.m_216990_(SoundRegistry.DEAD_KING_EXPLODE.get());
                    this.f_19853_.getEntities(this, this.m_20191_().inflate(5.0), entity -> entity instanceof LivingEntity && entity.isPickable() && entity.distanceToSqr(this.m_20182_()) < 25.0).forEach(x$0 -> super.m_7327_(x$0));
                    this.m_20331_(false);
                }
            } else if (this.isPhase(DeadKingBoss.Phases.FinalPhase)) {
                this.bossEvent.setProgress(this.m_21223_() / (this.m_21233_() - halfHealth));
            }
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return pDimensions.height * 0.95F;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    public boolean isPhase(DeadKingBoss.Phases phase) {
        return phase.value == this.getPhase();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return pSource == this.f_19853_.damageSources().lava() ? false : super.m_6469_(pSource, pAmount);
    }

    @Override
    protected boolean isImmobile() {
        return this.isPhase(DeadKingBoss.Phases.Transitioning) || super.m_6107_();
    }

    public boolean isPhaseTransitioning() {
        return this.isPhase(DeadKingBoss.Phases.Transitioning);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer pPlayer) {
        super.m_6457_(pPlayer);
        this.bossEvent.addPlayer(pPlayer);
        Messages.sendToPlayer(new ClientboundEntityEvent(this, (byte) 1), pPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer pPlayer) {
        super.m_6452_(pPlayer);
        this.bossEvent.removePlayer(pPlayer);
        Messages.sendToPlayer(new ClientboundEntityEvent(this, (byte) 0), pPlayer);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 10.0).add(AttributeRegistry.SPELL_POWER.get(), 1.15).add(Attributes.ARMOR, 15.0).add(AttributeRegistry.SPELL_RESIST.get(), 1.0).add(Attributes.MAX_HEALTH, 400.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.8).add(Attributes.ATTACK_KNOCKBACK, 0.6).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.155);
    }

    @Override
    public void setCustomName(@Nullable Component pName) {
        super.m_6593_(pName);
        this.bossEvent.setName(this.m_5446_());
    }

    private void setPhase(int phase) {
        this.f_19804_.set(PHASE, phase);
    }

    private void setPhase(DeadKingBoss.Phases phase) {
        this.setPhase(phase.value);
    }

    public int getPhase() {
        return this.f_19804_.get(PHASE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("phase", this.getPhase());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (this.m_8077_()) {
            this.bossEvent.setName(this.m_5446_());
        }
        this.setPhase(pCompound.getInt("phase"));
        if (this.isPhase(DeadKingBoss.Phases.FinalPhase)) {
            this.setFinalPhaseGoals();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(PHASE, 0);
    }

    @Override
    public void playAnimation(String animationId) {
        try {
            DeadKingBoss.AttackType attackType = DeadKingBoss.AttackType.valueOf(animationId);
            this.animationToPlay = RawAnimation.begin().thenPlay(attackType.data.animationId);
        } catch (Exception var3) {
            IronsSpellbooks.LOGGER.error("Entity {} Failed to play animation: {}", this, animationId);
        }
    }

    private PlayState meleePredicate(AnimationState<DeadKingBoss> animationEvent) {
        AnimationController<DeadKingBoss> controller = animationEvent.getController();
        if (this.animationToPlay != null) {
            controller.forceAnimationReset();
            controller.setAnimation(this.animationToPlay);
            this.animationToPlay = null;
        }
        return this.transitionController.getAnimationState() == AnimationController.State.STOPPED ? PlayState.CONTINUE : PlayState.STOP;
    }

    private PlayState transitionPredicate(AnimationState animationEvent) {
        AnimationController controller = animationEvent.getController();
        if (this.isPhaseTransitioning()) {
            controller.setAnimation(this.phase_transition_animation);
            return PlayState.CONTINUE;
        } else {
            return PlayState.STOP;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(this.transitionController);
        controllerRegistrar.add(this.meleeController);
        super.registerControllers(controllerRegistrar);
    }

    @Override
    public boolean shouldAlwaysAnimateHead() {
        return !this.isPhaseTransitioning();
    }

    @Override
    public boolean bobBodyWhileWalking() {
        return this.isPhase(DeadKingBoss.Phases.FirstPhase);
    }

    @Override
    public boolean isAnimating() {
        return this.transitionController.getAnimationState() != AnimationController.State.STOPPED || this.meleeController.getAnimationState() != AnimationController.State.STOPPED || super.isAnimating();
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        this.f_19853_.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundRegistry.DEAD_KING_HIT.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
        return super.m_7327_(pEntity);
    }

    @Override
    public boolean shouldAlwaysAnimateLegs() {
        return this.isPhase(DeadKingBoss.Phases.FirstPhase);
    }

    @Override
    protected LookControl createLookControl() {
        return new LookControl(this) {

            @Override
            protected float rotateTowards(float pFrom, float pTo, float pMaxDelta) {
                return super.rotateTowards(pFrom, pTo, pMaxDelta * 2.5F);
            }

            @Override
            protected boolean resetXRotOnTick() {
                return !DeadKingBoss.this.isCasting();
            }
        };
    }

    protected MoveControl createMoveControl() {
        return new MoveControl(this) {

            @Override
            protected float rotlerp(float pSourceAngle, float pTargetAngle, float pMaximumChange) {
                double d0 = this.f_24975_ - this.f_24974_.m_20185_();
                double d1 = this.f_24977_ - this.f_24974_.m_20189_();
                return d0 * d0 + d1 * d1 < 0.5 ? pSourceAngle : super.rotlerp(pSourceAngle, pTargetAngle, pMaximumChange * 0.25F);
            }
        };
    }

    public static enum AttackType {

        DOUBLE_SWING(51, "dead_king_double_swing", 16, 36), SLAM(48, "dead_king_slam", 30);

        public final AttackAnimationData data;

        private AttackType(int lengthInTicks, String animationId, int... attackTimestamps) {
            this.data = new AttackAnimationData(lengthInTicks, animationId, attackTimestamps);
        }
    }

    private class DeadKingBarrageGoal extends SpellBarrageGoal {

        public DeadKingBarrageGoal(IMagicEntity abstractSpellCastingMob, AbstractSpell spell, int minLevel, int maxLevel, int pAttackIntervalMin, int pAttackIntervalMax, int projectileCount) {
            super(abstractSpellCastingMob, spell, minLevel, maxLevel, pAttackIntervalMin, pAttackIntervalMax, projectileCount);
        }

        @Override
        public boolean canUse() {
            return !DeadKingBoss.this.isMeleeing && super.canUse();
        }
    }

    public static enum Phases {

        FirstPhase(0), Transitioning(1), FinalPhase(2);

        final int value;

        private Phases(int value) {
            this.value = value;
        }
    }
}
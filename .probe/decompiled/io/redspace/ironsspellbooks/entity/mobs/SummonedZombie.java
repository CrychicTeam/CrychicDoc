package io.redspace.ironsspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericCopyOwnerTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericFollowOwnerGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericOwnerHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericOwnerHurtTargetGoal;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SummonedZombie extends Zombie implements MagicSummon, GeoAnimatable {

    private static final EntityDataAccessor<Boolean> DATA_IS_ANIMATING_RISE = SynchedEntityData.defineId(SummonedZombie.class, EntityDataSerializers.BOOLEAN);

    protected LivingEntity cachedSummoner;

    protected UUID summonerUUID;

    private int riseAnimTime = 80;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SummonedZombie(EntityType<? extends Zombie> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_21364_ = 0;
    }

    public SummonedZombie(Level level, LivingEntity owner, boolean playRiseAnimation) {
        this(EntityRegistry.SUMMONED_ZOMBIE.get(), level);
        this.setSummoner(owner);
        if (playRiseAnimation) {
            this.triggerRiseAnimation();
        }
    }

    @Override
    public void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new MeleeAttackGoal(this, 1.2F, true));
        this.f_21345_.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 0.9F, 15.0F, 5.0F, false, 25.0F));
        this.f_21345_.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.f_21346_.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.f_21346_.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.f_21346_.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.f_21346_.addGoal(4, new GenericHurtByTargetGoal(this, entity -> entity == this.getSummoner()).setAlertOthers());
    }

    @Override
    public boolean isPreventingPlayerRest(Player pPlayer) {
        return !this.isAlliedTo(pPlayer);
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_IS_ANIMATING_RISE, false);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = Utils.random;
        this.m_213945_(randomsource, pDifficulty);
        if (randomsource.nextDouble() < 0.25) {
            this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }
        return pSpawnData;
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return super.m_7307_(pEntity) || this.isAlliedHelper(pEntity);
    }

    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(this.m_9236_(), this.cachedSummoner, this.summonerUUID);
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.summonerUUID = owner.m_20148_();
            this.cachedSummoner = owner;
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.m_6667_(pDamageSource);
    }

    public void onRemovedFromWorld() {
        this.onRemovedHelper(this, MobEffectRegistry.RAISE_DEAD_TIMER.get());
        super.onRemovedFromWorld();
    }

    @Override
    public void remove(Entity.RemovalReason pReason) {
        super.m_142687_(pReason);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        return Utils.doMeleeAttack(this, pEntity, SpellRegistry.RAISE_DEAD_SPELL.get().getDamageSource(this, this.getSummoner()));
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || !this.isAnimatingRise() && !this.shouldIgnoreDamage(pSource) ? super.hurt(pSource, pAmount) : false;
    }

    @Override
    public void tick() {
        if (this.isAnimatingRise()) {
            if (this.m_9236_().isClientSide) {
                this.clientDiggingParticles(this);
            }
            if (--this.riseAnimTime < 0) {
                this.f_19804_.set(DATA_IS_ANIMATING_RISE, false);
                this.m_146926_(0.0F);
                this.m_146867_();
            }
        } else {
            super.tick();
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public void onUnSummon() {
        if (!this.m_9236_().isClientSide) {
            MagicManager.spawnParticles(this.m_9236_(), ParticleTypes.POOF, this.m_20185_(), this.m_20186_(), this.m_20189_(), 25, 0.4, 0.8, 0.4, 0.03, false);
            this.m_146870_();
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.summonerUUID = OwnerHelper.deserializeOwner(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        OwnerHelper.serializeOwner(compoundTag, this.summonerUUID);
    }

    protected void clientDiggingParticles(LivingEntity livingEntity) {
        RandomSource randomsource = livingEntity.getRandom();
        BlockState blockstate = livingEntity.m_20075_();
        if (blockstate.m_60799_() != RenderShape.INVISIBLE) {
            for (int i = 0; i < 15; i++) {
                double d0 = livingEntity.m_20185_() + (double) Mth.randomBetween(randomsource, -0.5F, 0.5F);
                double d1 = livingEntity.m_20186_();
                double d2 = livingEntity.m_20189_() + (double) Mth.randomBetween(randomsource, -0.5F, 0.5F);
                livingEntity.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0.0, 0.0, 0.0);
            }
        }
    }

    public boolean isAnimatingRise() {
        return this.f_19804_.get(DATA_IS_ANIMATING_RISE);
    }

    public void triggerRiseAnimation() {
        this.f_19804_.set(DATA_IS_ANIMATING_RISE, true);
    }

    @Override
    public boolean isPushable() {
        return super.m_6094_() && !this.isAnimatingRise();
    }

    @Override
    protected boolean isImmobile() {
        return super.m_6107_() || this.isAnimatingRise();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "rise", 0, this::risePredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object o) {
        return (double) this.f_19797_;
    }

    private PlayState risePredicate(AnimationState event) {
        if (!this.isAnimatingRise()) {
            return PlayState.STOP;
        } else {
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                String animation = new String[] { "rise_from_ground_01", "rise_from_ground_02", "rise_from_ground_03", "rise_from_ground_04" }[this.f_19796_.nextIntBetweenInclusive(0, 3)];
                event.getController().setAnimation(RawAnimation.begin().thenPlay(animation));
            }
            return PlayState.CONTINUE;
        }
    }
}
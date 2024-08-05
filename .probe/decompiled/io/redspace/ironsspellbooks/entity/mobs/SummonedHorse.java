package io.redspace.ironsspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericFollowOwnerGoal;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SummonedHorse extends AbstractHorse implements MagicSummon {

    protected LivingEntity cachedSummoner;

    public SummonedHorse(EntityType<? extends AbstractHorse> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SummonedHorse(Level pLevel) {
        this(EntityRegistry.SPECTRAL_STEED.get(), pLevel);
    }

    public SummonedHorse(Level pLevel, LivingEntity owner) {
        this(pLevel);
        this.m_30586_(owner.m_20148_());
        this.setSummoner(owner);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new GenericFollowOwnerGoal(this, this::getSummoner, 0.8F, 12.0F, 4.0F, false, 32.0F));
        this.f_21345_.addGoal(3, new PanicGoal(this, 0.9F));
        this.f_21345_.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void openCustomInventoryScreen(Player pPlayer) {
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.JUMP_STRENGTH, 1.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.35);
    }

    @Override
    public void tick() {
        this.spawnParticles();
        super.tick();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        super.m_7515_();
        return SoundEvents.HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        super.m_5592_();
        return SoundEvents.HORSE_DEATH;
    }

    @Override
    public void onUnSummon() {
        if (!this.f_19853_.isClientSide) {
            MagicManager.spawnParticles(this.f_19853_, ParticleTypes.POOF, this.m_20185_(), this.m_20186_(), this.m_20189_(), 25, 0.4, 0.8, 0.4, 0.03, false);
            this.m_146870_();
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return this.shouldIgnoreDamage(pSource) ? false : super.hurt(pSource, pAmount);
    }

    @Nullable
    @Override
    protected SoundEvent getEatingSound() {
        return SoundEvents.HORSE_EAT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.HORSE_HURT;
    }

    @Override
    protected SoundEvent getAngrySound() {
        super.getAngrySound();
        return SoundEvents.HORSE_ANGRY;
    }

    public void spawnParticles() {
        if (this.f_19853_.isClientSide && Utils.random.nextFloat() < 0.25F) {
            float radius = 0.75F;
            Vec3 vec = new Vec3((double) (this.f_19796_.nextFloat() * 2.0F * radius - radius), (double) (this.f_19796_.nextFloat() * 2.0F * radius - radius), (double) (this.f_19796_.nextFloat() * 2.0F * radius - radius));
            this.f_19853_.addParticle(ParticleTypes.ENCHANT, this.m_20185_() + vec.x, this.m_20186_() + vec.y + 1.0, this.m_20189_() + vec.z, vec.x * 0.01F, 0.08 + vec.y * 0.01F, vec.z * 0.01F);
        }
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (this.m_20160_()) {
            return super.mobInteract(pPlayer, pHand);
        } else {
            if (pPlayer == this.getSummoner()) {
                this.m_6835_(pPlayer);
            } else {
                this.m_7564_();
            }
            return InteractionResult.sidedSuccess(this.f_19853_.isClientSide);
        }
    }

    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(this.f_19853_, this.cachedSummoner, this.m_21805_());
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.m_30586_(owner.m_20148_());
            this.cachedSummoner = owner;
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.m_6667_(pDamageSource);
    }

    public void onRemovedFromWorld() {
        this.onRemovedHelper(this, MobEffectRegistry.SUMMON_HORSE_TIMER.get());
        super.onRemovedFromWorld();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.m_30586_(OwnerHelper.deserializeOwner(compoundTag));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        OwnerHelper.serializeOwner(compoundTag, this.m_21805_());
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    @Override
    protected boolean canParent() {
        return false;
    }

    @Override
    public boolean isSaddled() {
        return true;
    }

    @Override
    public boolean isTamed() {
        return true;
    }
}
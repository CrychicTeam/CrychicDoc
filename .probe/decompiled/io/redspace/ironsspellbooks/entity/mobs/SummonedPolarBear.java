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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SummonedPolarBear extends PolarBear implements MagicSummon {

    protected LivingEntity cachedSummoner;

    protected UUID summonerUUID;

    public SummonedPolarBear(EntityType<? extends PolarBear> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_21364_ = 0;
    }

    public SummonedPolarBear(Level pLevel, LivingEntity owner) {
        this(EntityRegistry.SUMMONED_POLAR_BEAR.get(), pLevel);
        this.setSummoner(owner);
    }

    public float getStepHeight() {
        return 1.0F;
    }

    @Override
    public void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new SummonedPolarBear.PolarBearMeleeAttackGoal());
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
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (this.m_20160_()) {
            return super.m_6071_(pPlayer, pHand);
        } else {
            if (pPlayer == this.getSummoner()) {
                this.doPlayerRide(pPlayer);
            }
            return InteractionResult.sidedSuccess(this.f_19853_.isClientSide);
        }
    }

    protected void doPlayerRide(Player pPlayer) {
        this.m_29567_(false);
        if (!this.f_19853_.isClientSide) {
            pPlayer.m_146922_(this.m_146908_());
            pPlayer.m_146926_(this.m_146909_());
            pPlayer.m_20329_(this);
        }
    }

    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(this.f_19853_, this.cachedSummoner, this.summonerUUID);
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
        this.onRemovedHelper(this, MobEffectRegistry.POLAR_BEAR_TIMER.get());
        super.onRemovedFromWorld();
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

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        return Utils.doMeleeAttack(this, pEntity, SpellRegistry.SUMMON_POLAR_BEAR_SPELL.get().getDamageSource(this, this.getSummoner()));
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return super.m_7307_(pEntity) || this.isAlliedHelper(pEntity);
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
        return this.shouldIgnoreDamage(pSource) ? false : super.m_6469_(pSource, pAmount);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, 6.0);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.m_146895_();
        if (entity instanceof Mob) {
            return (Mob) entity;
        } else {
            entity = this.m_146895_();
            return entity instanceof Player ? (Player) entity : null;
        }
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec0) {
        super.m_274498_(player, vec0);
        this.f_19859_ = this.m_146908_();
        this.m_146922_(player.m_146908_());
        this.m_146926_(player.m_146909_());
        this.m_19915_(this.m_146908_(), this.m_146909_());
        this.f_20883_ = this.f_19859_;
        this.f_20885_ = this.m_146908_();
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 vec0) {
        float f = player.f_20900_ * 0.5F;
        float f1 = player.f_20902_;
        if (f1 <= 0.0F) {
            f1 *= 0.25F;
        }
        if (this.m_20069_()) {
            f *= 0.3F;
            f1 *= 0.3F;
        }
        return new Vec3((double) f, 0.0, (double) f1);
    }

    @Override
    protected float getRiddenSpeed(Player player0) {
        return (float) this.m_21133_(Attributes.MOVEMENT_SPEED) * 0.8F;
    }

    class PolarBearMeleeAttackGoal extends MeleeAttackGoal {

        public PolarBearMeleeAttackGoal() {
            super(SummonedPolarBear.this, 1.25, true);
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            double d0 = this.m_6639_(pEnemy);
            if (pDistToEnemySqr <= d0 && this.m_25564_()) {
                this.m_25563_();
                this.f_25540_.m_7327_(pEnemy);
                SummonedPolarBear.this.m_29567_(false);
            } else if (pDistToEnemySqr <= d0 * 2.0) {
                if (this.m_25564_()) {
                    SummonedPolarBear.this.m_29567_(false);
                    this.m_25563_();
                }
                if (this.m_25565_() <= 10) {
                    SummonedPolarBear.this.m_29567_(true);
                    SummonedPolarBear.this.m_29561_();
                }
            } else {
                this.m_25563_();
                SummonedPolarBear.this.m_29567_(false);
            }
        }

        @Override
        public void stop() {
            SummonedPolarBear.this.m_29567_(false);
            super.stop();
        }
    }
}
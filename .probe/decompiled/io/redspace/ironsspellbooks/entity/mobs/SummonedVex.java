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
import java.util.EnumSet;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SummonedVex extends Vex implements MagicSummon {

    protected LivingEntity cachedSummoner;

    protected UUID summonerUUID;

    public SummonedVex(EntityType<? extends Vex> pEntityType, Level pLevel) {
        super(EntityRegistry.SUMMONED_VEX.get(), pLevel);
        this.f_21364_ = 0;
    }

    public SummonedVex(Level pLevel, LivingEntity owner) {
        this(EntityRegistry.SUMMONED_VEX.get(), pLevel);
        this.setSummoner(owner);
    }

    @Override
    public void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(4, new SummonedVex.VexChargeAttackGoal());
        this.f_21345_.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 0.65F, 35.0F, 10.0F, true, 50.0F));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.f_21345_.addGoal(16, new SummonedVex.VexRandomMoveGoal());
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
    public boolean doHurtTarget(Entity pEntity) {
        return Utils.doMeleeAttack(this, pEntity, SpellRegistry.SUMMON_VEX_SPELL.get().getDamageSource(this, this.getSummoner()));
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return this.shouldIgnoreDamage(pSource) ? false : super.m_6469_(pSource, pAmount);
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
        this.onRemovedHelper(this, MobEffectRegistry.VEX_TIMER.get());
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
    public boolean isAlliedTo(Entity pEntity) {
        return super.m_7307_(pEntity) || this.isAlliedHelper(pEntity);
    }

    @Override
    public void onUnSummon() {
        if (!this.m_9236_().isClientSide) {
            MagicManager.spawnParticles(this.m_9236_(), ParticleTypes.POOF, this.m_20185_(), this.m_20186_(), this.m_20189_(), 25, 0.4, 0.8, 0.4, 0.03, false);
            this.m_146870_();
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    class VexChargeAttackGoal extends Goal {

        public VexChargeAttackGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = SummonedVex.this.m_5448_();
            return livingentity != null && livingentity.isAlive() && !SummonedVex.this.m_21566_().hasWanted() && SummonedVex.this.f_19796_.nextInt(m_186073_(7)) == 0 ? SummonedVex.this.m_20280_(livingentity) > 4.0 : false;
        }

        @Override
        public boolean canContinueToUse() {
            return SummonedVex.this.m_21566_().hasWanted() && SummonedVex.this.m_34028_() && SummonedVex.this.m_5448_() != null && SummonedVex.this.m_5448_().isAlive();
        }

        @Override
        public void start() {
            LivingEntity livingentity = SummonedVex.this.m_5448_();
            if (livingentity != null) {
                Vec3 vec3 = livingentity.m_146892_();
                SummonedVex.this.f_21342_.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0);
            }
            SummonedVex.this.m_34042_(true);
            SummonedVex.this.m_5496_(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
        }

        @Override
        public void stop() {
            SummonedVex.this.m_34042_(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = SummonedVex.this.m_5448_();
            if (livingentity != null) {
                if (SummonedVex.this.m_20191_().intersects(livingentity.m_20191_())) {
                    SummonedVex.this.doHurtTarget(livingentity);
                    SummonedVex.this.m_34042_(false);
                } else {
                    double d0 = SummonedVex.this.m_20280_(livingentity);
                    if (d0 < 9.0) {
                        Vec3 vec3 = livingentity.m_146892_();
                        SummonedVex.this.f_21342_.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0);
                    }
                }
            }
        }
    }

    class VexRandomMoveGoal extends Goal {

        public VexRandomMoveGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !SummonedVex.this.m_21566_().hasWanted() && SummonedVex.this.f_19796_.nextInt(m_186073_(7)) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            BlockPos blockpos = SummonedVex.this.m_34027_();
            if (blockpos == null) {
                blockpos = SummonedVex.this.m_20183_();
            }
            for (int i = 0; i < 3; i++) {
                BlockPos blockpos1 = blockpos.offset(SummonedVex.this.f_19796_.nextInt(15) - 7, SummonedVex.this.f_19796_.nextInt(11) - 5, SummonedVex.this.f_19796_.nextInt(15) - 7);
                if (SummonedVex.this.m_9236_().m_46859_(blockpos1)) {
                    SummonedVex.this.f_21342_.setWantedPosition((double) blockpos1.m_123341_() + 0.5, (double) blockpos1.m_123342_() + 0.5, (double) blockpos1.m_123343_() + 0.5, 0.25);
                    if (SummonedVex.this.m_5448_() == null) {
                        SummonedVex.this.m_21563_().setLookAt((double) blockpos1.m_123341_() + 0.5, (double) blockpos1.m_123342_() + 0.5, (double) blockpos1.m_123343_() + 0.5, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }
    }
}
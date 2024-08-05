package io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class NeutralWizard extends AbstractSpellCastingMob implements NeutralMob {

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private int remainingPersistentAngerTime;

    @Nullable
    private UUID persistentAngerTarget;

    private int lastAngerLevelUpdate;

    private int angerLevel;

    protected NeutralWizard(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.f_19796_));
    }

    @Override
    public void setRemainingPersistentAngerTime(int pTime) {
        this.remainingPersistentAngerTime = pTime;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        this.m_21678_(pCompound);
        pCompound.putInt("AngerLevel", this.angerLevel);
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.m_147285_(this.f_19853_, pCompound);
        this.angerLevel = pCompound.getInt("AngerLevel");
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (!this.f_19853_.isClientSide) {
            this.m_21666_((ServerLevel) this.f_19853_, true);
        }
        if (this.angerLevel > 0 && this.lastAngerLevelUpdate + 400 < this.f_19797_) {
            this.angerLevel--;
            this.lastAngerLevelUpdate = this.f_19797_;
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof Player player && !player.isCreative()) {
            this.increaseAngerLevel((int) Math.ceil((double) pAmount));
        }
        return super.m_6469_(pSource, pAmount);
    }

    public void increaseAngerLevel(int levels) {
        if (!this.f_19853_.isClientSide && this.angerLevel < this.getAngerThreshold()) {
            MagicManager.spawnParticles(this.f_19853_, ParticleTypes.ANGRY_VILLAGER, this.m_20185_(), this.m_20186_() + 1.25, this.m_20189_(), 15, 0.3, 0.2, 0.3, 0.0, false);
            this.getAngerSound().ifPresent(sound -> this.m_5496_(sound, this.m_6121_(), this.m_6100_()));
        }
        this.angerLevel = Math.min(this.angerLevel + levels, 10);
        this.lastAngerLevelUpdate = this.f_19797_;
    }

    public Optional<SoundEvent> getAngerSound() {
        return Optional.empty();
    }

    public int getAngerThreshold() {
        return 2;
    }

    public boolean isHostileTowards(LivingEntity entity) {
        return this.isAngryAt(entity) && this.angerLevel >= this.getAngerThreshold();
    }

    @Override
    public boolean isAngryAt(LivingEntity pTarget) {
        return this.angerLevel > 0 && NeutralMob.super.isAngryAt(pTarget);
    }

    public boolean guardsBlocks() {
        return true;
    }
}
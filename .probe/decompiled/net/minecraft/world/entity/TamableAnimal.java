package net.minecraft.world.entity;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;

public abstract class TamableAnimal extends Animal implements OwnableEntity {

    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TamableAnimal.class, EntityDataSerializers.BYTE);

    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(TamableAnimal.class, EntityDataSerializers.OPTIONAL_UUID);

    private boolean orderedToSit;

    protected TamableAnimal(EntityType<? extends TamableAnimal> entityTypeExtendsTamableAnimal0, Level level1) {
        super(entityTypeExtendsTamableAnimal0, level1);
        this.reassessTameGoals();
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_FLAGS_ID, (byte) 0);
        this.f_19804_.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        if (this.getOwnerUUID() != null) {
            compoundTag0.putUUID("Owner", this.getOwnerUUID());
        }
        compoundTag0.putBoolean("Sitting", this.orderedToSit);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        UUID $$1;
        if (compoundTag0.hasUUID("Owner")) {
            $$1 = compoundTag0.getUUID("Owner");
        } else {
            String $$2 = compoundTag0.getString("Owner");
            $$1 = OldUsersConverter.convertMobOwnerIfNecessary(this.m_20194_(), $$2);
        }
        if ($$1 != null) {
            try {
                this.setOwnerUUID($$1);
                this.setTame(true);
            } catch (Throwable var4) {
                this.setTame(false);
            }
        }
        this.orderedToSit = compoundTag0.getBoolean("Sitting");
        this.setInSittingPose(this.orderedToSit);
    }

    @Override
    public boolean canBeLeashed(Player player0) {
        return !this.m_21523_();
    }

    protected void spawnTamingParticles(boolean boolean0) {
        ParticleOptions $$1 = ParticleTypes.HEART;
        if (!boolean0) {
            $$1 = ParticleTypes.SMOKE;
        }
        for (int $$2 = 0; $$2 < 7; $$2++) {
            double $$3 = this.f_19796_.nextGaussian() * 0.02;
            double $$4 = this.f_19796_.nextGaussian() * 0.02;
            double $$5 = this.f_19796_.nextGaussian() * 0.02;
            this.m_9236_().addParticle($$1, this.m_20208_(1.0), this.m_20187_() + 0.5, this.m_20262_(1.0), $$3, $$4, $$5);
        }
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 7) {
            this.spawnTamingParticles(true);
        } else if (byte0 == 6) {
            this.spawnTamingParticles(false);
        } else {
            super.handleEntityEvent(byte0);
        }
    }

    public boolean isTame() {
        return (this.f_19804_.get(DATA_FLAGS_ID) & 4) != 0;
    }

    public void setTame(boolean boolean0) {
        byte $$1 = this.f_19804_.get(DATA_FLAGS_ID);
        if (boolean0) {
            this.f_19804_.set(DATA_FLAGS_ID, (byte) ($$1 | 4));
        } else {
            this.f_19804_.set(DATA_FLAGS_ID, (byte) ($$1 & -5));
        }
        this.reassessTameGoals();
    }

    protected void reassessTameGoals() {
    }

    public boolean isInSittingPose() {
        return (this.f_19804_.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setInSittingPose(boolean boolean0) {
        byte $$1 = this.f_19804_.get(DATA_FLAGS_ID);
        if (boolean0) {
            this.f_19804_.set(DATA_FLAGS_ID, (byte) ($$1 | 1));
        } else {
            this.f_19804_.set(DATA_FLAGS_ID, (byte) ($$1 & -2));
        }
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return (UUID) this.f_19804_.get(DATA_OWNERUUID_ID).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID uUID0) {
        this.f_19804_.set(DATA_OWNERUUID_ID, Optional.ofNullable(uUID0));
    }

    public void tame(Player player0) {
        this.setTame(true);
        this.setOwnerUUID(player0.m_20148_());
        if (player0 instanceof ServerPlayer) {
            CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer) player0, this);
        }
    }

    @Override
    public boolean canAttack(LivingEntity livingEntity0) {
        return this.isOwnedBy(livingEntity0) ? false : super.m_6779_(livingEntity0);
    }

    public boolean isOwnedBy(LivingEntity livingEntity0) {
        return livingEntity0 == this.m_269323_();
    }

    public boolean wantsToAttack(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        return true;
    }

    @Override
    public Team getTeam() {
        if (this.isTame()) {
            LivingEntity $$0 = this.m_269323_();
            if ($$0 != null) {
                return $$0.m_5647_();
            }
        }
        return super.m_5647_();
    }

    @Override
    public boolean isAlliedTo(Entity entity0) {
        if (this.isTame()) {
            LivingEntity $$1 = this.m_269323_();
            if (entity0 == $$1) {
                return true;
            }
            if ($$1 != null) {
                return $$1.m_7307_(entity0);
            }
        }
        return super.m_7307_(entity0);
    }

    @Override
    public void die(DamageSource damageSource0) {
        if (!this.m_9236_().isClientSide && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.m_269323_() instanceof ServerPlayer) {
            this.m_269323_().m_213846_(this.m_21231_().getDeathMessage());
        }
        super.m_6667_(damageSource0);
    }

    public boolean isOrderedToSit() {
        return this.orderedToSit;
    }

    public void setOrderedToSit(boolean boolean0) {
        this.orderedToSit = boolean0;
    }
}
package net.minecraft.world.entity.projectile;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;

public class EvokerFangs extends Entity implements TraceableEntity {

    public static final int ATTACK_DURATION = 20;

    public static final int LIFE_OFFSET = 2;

    public static final int ATTACK_TRIGGER_TICKS = 14;

    private int warmupDelayTicks;

    private boolean sentSpikeEvent;

    private int lifeTicks = 22;

    private boolean clientSideAttackStarted;

    @Nullable
    private LivingEntity owner;

    @Nullable
    private UUID ownerUUID;

    public EvokerFangs(EntityType<? extends EvokerFangs> entityTypeExtendsEvokerFangs0, Level level1) {
        super(entityTypeExtendsEvokerFangs0, level1);
    }

    public EvokerFangs(Level level0, double double1, double double2, double double3, float float4, int int5, LivingEntity livingEntity6) {
        this(EntityType.EVOKER_FANGS, level0);
        this.warmupDelayTicks = int5;
        this.setOwner(livingEntity6);
        this.m_146922_(float4 * (180.0F / (float) Math.PI));
        this.m_6034_(double1, double2, double3);
    }

    @Override
    protected void defineSynchedData() {
    }

    public void setOwner(@Nullable LivingEntity livingEntity0) {
        this.owner = livingEntity0;
        this.ownerUUID = livingEntity0 == null ? null : livingEntity0.m_20148_();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.m_9236_() instanceof ServerLevel) {
            Entity $$0 = ((ServerLevel) this.m_9236_()).getEntity(this.ownerUUID);
            if ($$0 instanceof LivingEntity) {
                this.owner = (LivingEntity) $$0;
            }
        }
        return this.owner;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        this.warmupDelayTicks = compoundTag0.getInt("Warmup");
        if (compoundTag0.hasUUID("Owner")) {
            this.ownerUUID = compoundTag0.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        compoundTag0.putInt("Warmup", this.warmupDelayTicks);
        if (this.ownerUUID != null) {
            compoundTag0.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_9236_().isClientSide) {
            if (this.clientSideAttackStarted) {
                this.lifeTicks--;
                if (this.lifeTicks == 14) {
                    for (int $$0 = 0; $$0 < 12; $$0++) {
                        double $$1 = this.m_20185_() + (this.f_19796_.nextDouble() * 2.0 - 1.0) * (double) this.m_20205_() * 0.5;
                        double $$2 = this.m_20186_() + 0.05 + this.f_19796_.nextDouble();
                        double $$3 = this.m_20189_() + (this.f_19796_.nextDouble() * 2.0 - 1.0) * (double) this.m_20205_() * 0.5;
                        double $$4 = (this.f_19796_.nextDouble() * 2.0 - 1.0) * 0.3;
                        double $$5 = 0.3 + this.f_19796_.nextDouble() * 0.3;
                        double $$6 = (this.f_19796_.nextDouble() * 2.0 - 1.0) * 0.3;
                        this.m_9236_().addParticle(ParticleTypes.CRIT, $$1, $$2 + 1.0, $$3, $$4, $$5, $$6);
                    }
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -8) {
                for (LivingEntity $$8 : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(0.2, 0.0, 0.2))) {
                    this.dealDamageTo($$8);
                }
            }
            if (!this.sentSpikeEvent) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 4);
                this.sentSpikeEvent = true;
            }
            if (--this.lifeTicks < 0) {
                this.m_146870_();
            }
        }
    }

    private void dealDamageTo(LivingEntity livingEntity0) {
        LivingEntity $$1 = this.getOwner();
        if (livingEntity0.isAlive() && !livingEntity0.m_20147_() && livingEntity0 != $$1) {
            if ($$1 == null) {
                livingEntity0.hurt(this.m_269291_().magic(), 6.0F);
            } else {
                if ($$1.m_7307_(livingEntity0)) {
                    return;
                }
                livingEntity0.hurt(this.m_269291_().indirectMagic(this, $$1), 6.0F);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        super.handleEntityEvent(byte0);
        if (byte0 == 4) {
            this.clientSideAttackStarted = true;
            if (!this.m_20067_()) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.EVOKER_FANGS_ATTACK, this.m_5720_(), 1.0F, this.f_19796_.nextFloat() * 0.2F + 0.85F, false);
            }
        }
    }

    public float getAnimationProgress(float float0) {
        if (!this.clientSideAttackStarted) {
            return 0.0F;
        } else {
            int $$1 = this.lifeTicks - 2;
            return $$1 <= 0 ? 1.0F : 1.0F - ((float) $$1 - float0) / 20.0F;
        }
    }
}
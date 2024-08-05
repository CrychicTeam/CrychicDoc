package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.entity.NoKnockbackProjectile;
import io.redspace.ironsspellbooks.api.util.Utils;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

public abstract class AoeEntity extends Projectile implements NoKnockbackProjectile {

    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(AoeEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> DATA_CIRCULAR = SynchedEntityData.defineId(AoeEntity.class, EntityDataSerializers.BOOLEAN);

    protected float damage;

    protected int duration = 600;

    protected int reapplicationDelay = 10;

    protected int durationOnUse;

    protected float radiusOnUse;

    protected float radiusPerTick;

    protected int effectDuration;

    public AoeEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_19794_ = true;
        this.f_19850_ = false;
    }

    protected float particleYOffset() {
        return 0.0F;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setEffectDuration(int effectDuration) {
        this.effectDuration = effectDuration;
    }

    public int getEffectDuration() {
        return this.effectDuration;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_19797_ > this.duration) {
            this.m_146870_();
        } else {
            if (!this.f_19853_.isClientSide) {
                if (this.f_19797_ % this.reapplicationDelay == 1) {
                    this.checkHits();
                }
                if (this.f_19797_ % 5 == 0) {
                    this.setRadius(this.getRadius() + this.radiusPerTick);
                }
            } else {
                this.ambientParticles();
            }
            this.m_146884_(this.m_20182_().add(this.m_20184_()));
        }
    }

    protected void checkHits() {
        if (!this.f_19853_.isClientSide) {
            List<LivingEntity> targets = this.f_19853_.m_45976_(LivingEntity.class, this.m_20191_().inflate(this.getInflation().x, this.getInflation().y, this.getInflation().z));
            boolean hit = false;
            float radiusSqr = this.getRadius();
            radiusSqr *= radiusSqr;
            for (LivingEntity target : targets) {
                if (this.canHitEntity(target) && (!this.isCircular() || target.m_20280_(this) < (double) radiusSqr) && this.canHitTargetForGroundContext(target)) {
                    this.applyEffect(target);
                    hit = true;
                }
            }
            if (hit) {
                this.setRadius(this.getRadius() + this.radiusOnUse);
                this.duration = this.duration + this.durationOnUse;
                this.onPostHit();
            }
        }
    }

    protected Vec3 getInflation() {
        return Vec3.ZERO;
    }

    protected boolean canHitTargetForGroundContext(LivingEntity target) {
        return target.m_20096_() || target.m_20186_() - this.m_20186_() < 0.5;
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return this.m_19749_() != null && pTarget != this.m_19749_() && !this.m_19749_().isAlliedTo(pTarget) && super.canHitEntity(pTarget);
    }

    public void onPostHit() {
    }

    public abstract void applyEffect(LivingEntity var1);

    public void ambientParticles() {
        if (this.f_19853_.isClientSide) {
            this.getParticle().ifPresent(particle -> {
                float f = this.getParticleCount();
                f = Mth.clamp(f * this.getRadius(), f / 4.0F, f * 10.0F);
                for (int i = 0; (float) i < f; i++) {
                    if (f - (float) i < 1.0F && this.f_19796_.nextFloat() > f - (float) i) {
                        return;
                    }
                    float r = this.getRadius();
                    Vec3 pos;
                    if (this.isCircular()) {
                        float distance = r * (1.0F - this.f_19796_.nextFloat() * this.f_19796_.nextFloat());
                        float theta = this.f_19796_.nextFloat() * 6.282F;
                        pos = new Vec3((double) (distance * Mth.cos(theta)), 0.2F, (double) (distance * Mth.sin(theta)));
                    } else {
                        pos = new Vec3(Utils.getRandomScaled((double) (r * 0.85F)), 0.2F, Utils.getRandomScaled((double) (r * 0.85F)));
                    }
                    Vec3 motion = new Vec3(Utils.getRandomScaled(0.03F), this.f_19796_.nextDouble() * 0.01F, Utils.getRandomScaled(0.03F)).scale((double) this.getParticleSpeedModifier());
                    this.f_19853_.addParticle(particle, this.m_20185_() + pos.x, this.m_20186_() + pos.y + (double) this.particleYOffset(), this.m_20189_() + pos.z, motion.x, motion.y, motion.z);
                }
            });
        }
    }

    protected float getParticleSpeedModifier() {
        return 1.0F;
    }

    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(DATA_RADIUS, 2.0F);
        this.m_20088_().define(DATA_CIRCULAR, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions();
            if (this.getRadius() < 0.1F) {
                this.m_146870_();
            }
        }
        super.m_7350_(pKey);
    }

    public void setRadius(float pRadius) {
        if (!this.f_19853_.isClientSide) {
            this.m_20088_().set(DATA_RADIUS, Mth.clamp(pRadius, 0.0F, 32.0F));
        }
    }

    public void setDuration(int duration) {
        if (!this.f_19853_.isClientSide) {
            this.duration = duration;
        }
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.m_20185_();
        double d1 = this.m_20186_();
        double d2 = this.m_20189_();
        super.m_6210_();
        this.m_6034_(d0, d1, d2);
    }

    public float getRadius() {
        return this.m_20088_().get(DATA_RADIUS);
    }

    public Boolean isCircular() {
        return this.m_20088_().get(DATA_CIRCULAR);
    }

    public void setCircular() {
        this.m_20088_().set(DATA_CIRCULAR, true);
    }

    public abstract float getParticleCount();

    public abstract Optional<ParticleOptions> getParticle();

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.8F);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Age", this.f_19797_);
        pCompound.putInt("Duration", this.duration);
        pCompound.putInt("ReapplicationDelay", this.reapplicationDelay);
        pCompound.putInt("DurationOnUse", this.durationOnUse);
        pCompound.putFloat("RadiusOnUse", this.radiusOnUse);
        pCompound.putFloat("RadiusPerTick", this.radiusPerTick);
        pCompound.putFloat("Radius", this.getRadius());
        pCompound.putFloat("Damage", this.getDamage());
        pCompound.putBoolean("Circular", this.isCircular());
        pCompound.putInt("EffectDuration", this.effectDuration);
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.f_19797_ = pCompound.getInt("Age");
        if (pCompound.getInt("Duration") > 0) {
            this.duration = pCompound.getInt("Duration");
        }
        if (pCompound.getInt("ReapplicationDelay") > 0) {
            this.reapplicationDelay = pCompound.getInt("ReapplicationDelay");
        }
        if (pCompound.getInt("Radius") > 0) {
            this.setRadius(pCompound.getFloat("Radius"));
        }
        if (pCompound.getInt("DurationOnUse") > 0) {
            this.durationOnUse = pCompound.getInt("DurationOnUse");
        }
        if (pCompound.getInt("RadiusOnUse") > 0) {
            this.radiusOnUse = pCompound.getFloat("RadiusOnUse");
        }
        if (pCompound.getInt("RadiusPerTick") > 0) {
            this.radiusPerTick = pCompound.getFloat("RadiusPerTick");
        }
        if (pCompound.getInt("EffectDuration") > 0) {
            this.effectDuration = pCompound.getInt("EffectDuration");
        }
        this.setDamage(pCompound.getFloat("Damage"));
        if (pCompound.getBoolean("Circular")) {
            this.setCircular();
        }
        super.readAdditionalSaveData(pCompound);
    }
}
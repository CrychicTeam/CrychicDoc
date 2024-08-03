package io.redspace.ironsspellbooks.entity.spells.blood_needle;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class BloodNeedle extends AbstractMagicProjectile {

    private static final EntityDataAccessor<Float> DATA_Z_ROT = SynchedEntityData.defineId(BloodNeedle.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> DATA_SCALE = SynchedEntityData.defineId(BloodNeedle.class, EntityDataSerializers.FLOAT);

    private static int soundTimestamp;

    public BloodNeedle(EntityType<? extends BloodNeedle> entityType, Level level) {
        super(entityType, level);
        this.m_20242_(true);
    }

    public BloodNeedle(Level levelIn, LivingEntity shooter) {
        super(EntityRegistry.BLOOD_NEEDLE.get(), levelIn);
        this.m_5602_(shooter);
    }

    public void setZRot(float zRot) {
        if (!this.f_19853_.isClientSide) {
            this.f_19804_.set(DATA_Z_ROT, zRot);
        }
    }

    public void setScale(float scale) {
        if (!this.f_19853_.isClientSide) {
            this.f_19804_.set(DATA_SCALE, scale);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(DATA_Z_ROT, 0.0F);
        this.f_19804_.define(DATA_SCALE, 1.0F);
        super.defineSynchedData();
    }

    public float getZRot() {
        return this.f_19804_.get(DATA_Z_ROT);
    }

    public float getScale() {
        return this.f_19804_.get(DATA_SCALE);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("zRot", this.getZRot());
        if (this.getScale() != 1.0F) {
            tag.putFloat("Scale", this.getScale());
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setZRot(tag.getFloat("zRot"));
        if (tag.contains("Scale")) {
            this.setScale(tag.getFloat("Scale"));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        DamageSources.applyDamage(entityHitResult.getEntity(), this.getDamage(), SpellRegistry.BLOOD_NEEDLES_SPELL.get().getDamageSource(this, this.m_19749_()));
        entityHitResult.getEntity().invulnerableTime = 0;
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        this.m_146870_();
    }

    @Override
    protected void doImpactSound(SoundEvent sound) {
        if (soundTimestamp != this.f_19797_) {
            super.doImpactSound(sound);
            soundTimestamp = this.f_19797_;
        }
    }

    @Override
    public void trailParticles() {
        for (int i = 0; i < 2; i++) {
            double speed = 0.05;
            double dx = Utils.random.nextDouble() * 2.0 * speed - speed;
            double dy = Utils.random.nextDouble() * 2.0 * speed - speed;
            double dz = Utils.random.nextDouble() * 2.0 * speed - speed;
            this.f_19853_.addParticle(ParticleHelper.BLOOD, this.m_20185_() + dx, this.m_20186_() + dy, this.m_20189_() + dz, dx, dy, dz);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.f_19853_, ParticleHelper.BLOOD, x, y, z, 15, 0.1, 0.1, 0.1, 0.18, true);
    }

    @Override
    public float getSpeed() {
        return 2.5F;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundRegistry.BLOOD_NEEDLE_IMPACT.get());
    }
}
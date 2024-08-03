package io.redspace.ironsspellbooks.entity.spells.magma_ball;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.Optional;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireBomb extends AbstractMagicProjectile {

    float aoeDamage;

    public FireBomb(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FireBomb(Level level, LivingEntity shooter) {
        this(EntityRegistry.FIRE_BOMB.get(), level);
        this.m_5602_(shooter);
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.m_20184_();
        double d0 = this.m_20185_() - vec3.x;
        double d1 = this.m_20186_() - vec3.y;
        double d2 = this.m_20189_() - vec3.z;
        for (int i = 0; i < 4; i++) {
            Vec3 random = Utils.getRandomVec3(0.2);
            this.f_19853_.addParticle(ParticleTypes.SMOKE, d0 - random.x, d1 + 0.5 - random.y, d2 - random.z, random.x * 0.5, random.y * 0.5, random.z * 0.5);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.f_19853_, ParticleTypes.LAVA, x, y, z, 30, 1.5, 0.1, 1.5, 1.0, false);
    }

    @Override
    public float getSpeed() {
        return 0.65F;
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        this.createFireField(hitresult.getLocation());
        float explosionRadius = this.getExplosionRadius();
        for (Entity entity : this.f_19853_.m_45933_(this, this.m_20191_().inflate((double) explosionRadius))) {
            double distance = entity.distanceToSqr(hitresult.getLocation());
            if (distance < (double) (explosionRadius * explosionRadius) && this.m_5603_(entity) && Utils.hasLineOfSight(this.f_19853_, hitresult.getLocation(), entity.position().add(0.0, (double) (entity.getEyeHeight() * 0.5F), 0.0), true)) {
                double p = 1.0 - Math.pow(Math.sqrt(distance) / (double) explosionRadius, 3.0);
                float damage = (float) ((double) this.damage * p);
                DamageSources.applyDamage(entity, damage, SpellRegistry.MAGMA_BOMB_SPELL.get().getDamageSource(this, this.m_19749_()));
            }
        }
        this.m_146870_();
    }

    public void createFireField(Vec3 location) {
        if (!this.f_19853_.isClientSide) {
            FireField fire = new FireField(this.f_19853_);
            fire.m_5602_(this.m_19749_());
            fire.setDuration(200);
            fire.setDamage(this.aoeDamage);
            fire.setRadius(this.getExplosionRadius());
            fire.setCircular();
            fire.m_20219_(location);
            this.f_19853_.m_7967_(fire);
        }
    }

    public void setAoeDamage(float damage) {
        this.aoeDamage = damage;
    }

    public float getAoeDamage() {
        return this.aoeDamage;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("AoeDamage", this.aoeDamage);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.aoeDamage = tag.getFloat("AoeDamage");
    }

    @Override
    protected void doImpactSound(SoundEvent sound) {
        this.f_19853_.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), sound, SoundSource.NEUTRAL, 2.0F, 1.2F + Utils.random.nextFloat() * 0.2F);
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundEvents.GENERIC_EXPLODE);
    }
}
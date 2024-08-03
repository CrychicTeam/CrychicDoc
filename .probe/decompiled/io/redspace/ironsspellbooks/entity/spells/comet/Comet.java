package io.redspace.ironsspellbooks.entity.spells.comet;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
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

public class Comet extends AbstractMagicProjectile {

    public Comet(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.m_20242_(true);
    }

    public Comet(Level pLevel, LivingEntity pShooter) {
        this(EntityRegistry.COMET.get(), pLevel);
        this.m_5602_(pShooter);
    }

    public void shoot(Vec3 rotation, float innaccuracy) {
        Vec3 offset = Utils.getRandomVec3(1.0).normalize().scale((double) innaccuracy);
        super.shoot(rotation.add(offset));
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.m_20184_();
        double d0 = this.m_20185_() - vec3.x;
        double d1 = this.m_20186_() - vec3.y;
        double d2 = this.m_20189_() - vec3.z;
        for (int i = 0; i < 2; i++) {
            Vec3 random = Utils.getRandomVec3(0.1);
            this.f_19853_.addParticle(ParticleHelper.UNSTABLE_ENDER, d0 - random.x, d1 + 0.5 - random.y, d2 - random.z, random.x * 0.5, random.y * 0.5, random.z * 0.5);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.f_19853_, ParticleHelper.UNSTABLE_ENDER, x, y, z, 25, 0.0, 0.0, 0.0, 0.18, false);
        MagicManager.spawnParticles(this.f_19853_, new BlastwaveParticleOptions(SpellRegistry.STARFALL_SPELL.get().getSchoolType().getTargetingColor(), 1.25F), x, y, z, 1, 0.0, 0.0, 0.0, 0.0, true);
    }

    @Override
    public float getSpeed() {
        return 1.85F;
    }

    @Override
    protected void doImpactSound(SoundEvent sound) {
        this.f_19853_.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), sound, SoundSource.NEUTRAL, 0.8F, 1.35F + Utils.random.nextFloat() * 0.3F);
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.f_19853_.isClientSide) {
            this.impactParticles(this.f_19790_, this.f_19791_, this.f_19792_);
            this.getImpactSound().ifPresent(this::doImpactSound);
            float explosionRadius = this.getExplosionRadius();
            for (Entity entity : this.f_19853_.m_45933_(this, this.m_20191_().inflate((double) explosionRadius))) {
                double distance = entity.distanceToSqr(hitResult.getLocation());
                if (distance < (double) (explosionRadius * explosionRadius) && this.m_5603_(entity)) {
                    DamageSources.applyDamage(entity, this.damage, SpellRegistry.STARFALL_SPELL.get().getDamageSource(this, this.m_19749_()));
                }
            }
            this.m_146870_();
        }
    }
}
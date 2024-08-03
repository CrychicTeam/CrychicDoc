package io.redspace.ironsspellbooks.entity.spells.fireball;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.network.spell.ClientboundFieryExplosionParticles;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class MagicFireball extends AbstractMagicProjectile {

    public MagicFireball(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.m_20242_(true);
    }

    public MagicFireball(Level pLevel, LivingEntity pShooter) {
        this(EntityRegistry.MAGIC_FIREBALL.get(), pLevel);
        this.m_5602_(pShooter);
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.m_20184_();
        double d0 = this.m_20185_() - vec3.x;
        double d1 = this.m_20186_() - vec3.y;
        double d2 = this.m_20189_() - vec3.z;
        int count = Mth.clamp((int) (vec3.lengthSqr() * 4.0), 1, 4);
        for (int i = 0; i < count; i++) {
            Vec3 random = Utils.getRandomVec3(0.25);
            float f = (float) i / (float) count;
            double x = Mth.lerp((double) f, d0, this.m_20185_());
            double y = Mth.lerp((double) f, d1, this.m_20186_());
            double z = Mth.lerp((double) f, d2, this.m_20189_());
            this.f_19853_.addParticle(ParticleTypes.LARGE_SMOKE, x - random.x, y + 0.5 - random.y, z - random.z, random.x * 0.5, random.y * 0.5, random.z * 0.5);
            this.f_19853_.addParticle(ParticleHelper.EMBERS, x - random.x, y + 0.5 - random.y, z - random.z, random.x * 0.5, random.y * 0.5, random.z * 0.5);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public float getSpeed() {
        return 1.15F;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.f_19853_.isClientSide) {
            this.impactParticles(this.f_19790_, this.f_19791_, this.f_19792_);
            float explosionRadius = this.getExplosionRadius();
            float explosionRadiusSqr = explosionRadius * explosionRadius;
            List<Entity> entities = this.f_19853_.m_45933_(this, this.m_20191_().inflate((double) explosionRadius));
            Vec3 losPoint = Utils.raycastForBlock(this.f_19853_, this.m_20182_(), this.m_20182_().add(0.0, 2.0, 0.0), ClipContext.Fluid.NONE).m_82450_();
            for (Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(hitResult.getLocation());
                if (distanceSqr < (double) explosionRadiusSqr && this.m_5603_(entity) && Utils.hasLineOfSight(this.f_19853_, losPoint, entity.getBoundingBox().getCenter(), true)) {
                    double p = 1.0 - distanceSqr / (double) explosionRadiusSqr;
                    float damage = (float) ((double) this.damage * p);
                    DamageSources.applyDamage(entity, damage, SpellRegistry.FIREBALL_SPELL.get().getDamageSource(this, this.m_19749_()));
                }
            }
            if (ServerConfigs.SPELL_GREIFING.get()) {
                Explosion explosion = new Explosion(this.f_19853_, null, SpellRegistry.FIREBALL_SPELL.get().getDamageSource(this, this.m_19749_()), null, this.m_20185_(), this.m_20186_(), this.m_20189_(), this.getExplosionRadius() / 2.0F, true, Explosion.BlockInteraction.DESTROY);
                if (!ForgeEventFactory.onExplosionStart(this.f_19853_, explosion)) {
                    explosion.explode();
                    explosion.finalizeExplosion(false);
                }
            }
            Messages.sendToPlayersTrackingEntity(new ClientboundFieryExplosionParticles(new Vec3(this.m_20185_(), this.m_20186_() + 0.15F, this.m_20189_()), this.getExplosionRadius()), this);
            this.m_5496_(SoundEvents.GENERIC_EXPLODE, 4.0F, (1.0F + (this.f_19853_.random.nextFloat() - this.f_19853_.random.nextFloat()) * 0.2F) * 0.7F);
            this.m_146870_();
        }
    }
}
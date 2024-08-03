package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class EchoingStrikeEntity extends AoeEntity {

    public final int waitTime = 20;

    public EchoingStrikeEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCircular();
    }

    public EchoingStrikeEntity(Level level, LivingEntity owner, float damage, float radius) {
        this(EntityRegistry.ECHOING_STRIKE.get(), level);
        this.m_5602_(owner);
        this.setRadius(radius);
        this.setDamage(damage);
    }

    @Override
    public void applyEffect(LivingEntity target) {
    }

    @Override
    public void tick() {
        if (this.f_19797_ == 20) {
            this.m_5496_(SoundRegistry.ECHOING_STRIKE.get(), 1.0F, (float) Utils.random.nextIntBetweenInclusive(8, 12) * 0.1F);
            if (!this.f_19853_.isClientSide) {
                Vec3 center = this.m_20191_().getCenter();
                MagicManager.spawnParticles(this.f_19853_, ParticleHelper.UNSTABLE_ENDER, center.x, center.y, center.z, 25, 0.0, 0.0, 0.0, 0.18, false);
                MagicManager.spawnParticles(this.f_19853_, new BlastwaveParticleOptions(SpellRegistry.ECHOING_STRIKES_SPELL.get().getSchoolType().getTargetingColor(), this.getRadius() * 0.9F), center.x, center.y, center.z, 1, 0.0, 0.0, 0.0, 0.0, true);
                float explosionRadius = this.getRadius();
                float explosionRadiusSqr = explosionRadius * explosionRadius;
                List<Entity> entities = this.f_19853_.m_45933_(this, this.m_20191_().inflate((double) explosionRadius));
                Vec3 losCenter = Utils.moveToRelativeGroundLevel(this.f_19853_, center, 2);
                losCenter = Utils.raycastForBlock(this.f_19853_, losCenter, losCenter.add(0.0, 3.0, 0.0), ClipContext.Fluid.NONE).m_82450_().add(losCenter).scale(0.5);
                for (Entity entity : entities) {
                    double distanceSqr = entity.distanceToSqr(center);
                    if (distanceSqr < (double) explosionRadiusSqr && this.m_5603_(entity) && Utils.hasLineOfSight(this.f_19853_, losCenter, entity.getBoundingBox().getCenter(), true)) {
                        double p = Mth.clamp(1.0 - distanceSqr / (double) explosionRadiusSqr + 0.4F, 0.0, 1.0);
                        float damage = (float) ((double) this.damage * p);
                        DamageSources.applyDamage(entity, damage, SpellRegistry.ECHOING_STRIKES_SPELL.get().getDamageSource(this, this.m_19749_()));
                    }
                }
            }
        } else if (this.f_19797_ > 20) {
            this.m_146870_();
        }
        if (this.f_19853_.isClientSide && this.f_19797_ < 10) {
            Vec3 position = this.m_20191_().getCenter();
            for (int i = 0; i < 3; i++) {
                Vec3 vec3 = Utils.getRandomVec3(1.0);
                vec3 = vec3.multiply(vec3).multiply((double) Mth.sign(vec3.x), (double) Mth.sign(vec3.y), (double) Mth.sign(vec3.z)).scale((double) this.getRadius()).add(position);
                Vec3 motion = position.subtract(vec3).scale(0.125);
                this.f_19853_.addParticle(ParticleHelper.UNSTABLE_ENDER, vec3.x, vec3.y - 0.5, vec3.z, motion.x, motion.y, motion.z);
            }
        }
    }

    @Override
    protected boolean canHitTargetForGroundContext(LivingEntity target) {
        return true;
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, this.getRadius() * 2.0F);
    }

    @Override
    public void ambientParticles() {
    }

    @Override
    public float getParticleCount() {
        return 0.0F;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
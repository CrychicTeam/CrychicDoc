package io.redspace.ironsspellbooks.entity.spells.cone_of_cold;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ConeOfColdProjectile extends AbstractConeProjectile {

    public ConeOfColdProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ConeOfColdProjectile(Level level, LivingEntity entity) {
        super(EntityRegistry.CONE_OF_COLD_PROJECTILE.get(), level, entity);
    }

    @Override
    public void spawnParticles() {
        Entity owner = this.m_19749_();
        if (this.f_19853_.isClientSide && owner != null) {
            Vec3 rotation = owner.getLookAngle().normalize();
            Vec3 pos = owner.position().add(rotation.scale(1.5));
            double x = pos.x;
            double y = pos.y + (double) (owner.getEyeHeight() * 0.9F);
            double z = pos.z;
            for (int i = 0; i < 10; i++) {
                double speed = this.f_19796_.nextDouble() * 0.7 + 0.15;
                double offset = 0.125;
                double ox = Math.random() * 2.0 * offset - offset;
                double oy = Math.random() * 2.0 * offset - offset;
                double oz = Math.random() * 2.0 * offset - offset;
                double angularness = 0.8;
                Vec3 randomVec = new Vec3(Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness).normalize();
                Vec3 result = rotation.scale(3.0).add(randomVec).normalize().scale(speed);
                this.f_19853_.addParticle(Math.random() > 0.15 ? ParticleHelper.SNOW_DUST : ParticleHelper.SNOWFLAKE, x + ox, y + oy, z + oz, result.x, result.y, result.z);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        DamageSources.applyDamage(entity, this.damage, SpellRegistry.CONE_OF_COLD_SPELL.get().getDamageSource(this, this.m_19749_()));
    }
}
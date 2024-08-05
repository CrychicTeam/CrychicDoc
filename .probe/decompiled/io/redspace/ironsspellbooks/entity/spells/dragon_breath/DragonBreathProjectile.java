package io.redspace.ironsspellbooks.entity.spells.dragon_breath;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class DragonBreathProjectile extends AbstractConeProjectile {

    public DragonBreathProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public DragonBreathProjectile(Level level, LivingEntity entity) {
        super(EntityRegistry.DRAGON_BREATH_PROJECTILE.get(), level, entity);
    }

    @Override
    public void spawnParticles() {
        Entity owner = this.m_19749_();
        if (this.m_9236_().isClientSide && owner != null) {
            Vec3 rotation = owner.getLookAngle().normalize();
            Vec3 pos = owner.position().add(rotation.scale(1.6));
            double x = pos.x;
            double y = pos.y + (double) (owner.getEyeHeight() * 0.9F);
            double z = pos.z;
            double speed = this.f_19796_.nextDouble() * 0.35 + 0.25;
            for (int i = 0; i < 12; i++) {
                double offset = 0.15;
                double ox = Math.random() * 2.0 * offset - offset;
                double oy = Math.random() * 2.0 * offset - offset;
                double oz = Math.random() * 2.0 * offset - offset;
                double angularness = 0.3;
                Vec3 randomVec = new Vec3(Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness).normalize();
                Vec3 result = rotation.scale(3.0).add(randomVec).normalize().scale(speed);
                this.m_9236_().addParticle(ParticleTypes.DRAGON_BREATH, x + ox, y + oy, z + oz, result.x, result.y, result.z);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (DamageSources.applyDamage(entity, this.damage, SpellRegistry.DRAGON_BREATH_SPELL.get().getDamageSource(this, this.m_19749_())) && this.f_19796_.nextFloat() < 0.3F) {
            this.createDragonBreathPuddle(entity.position());
        }
    }

    private void createDragonBreathPuddle(Vec3 pos) {
        DragonBreathPool pool = new DragonBreathPool(this.m_9236_());
        pool.m_5602_(this.m_19749_());
        pool.setDamage(this.damage);
        pool.m_20219_(pos);
        this.m_9236_().m_7967_(pool);
    }
}
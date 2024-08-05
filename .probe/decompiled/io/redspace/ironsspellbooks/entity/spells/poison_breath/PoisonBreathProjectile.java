package io.redspace.ironsspellbooks.entity.spells.poison_breath;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class PoisonBreathProjectile extends AbstractConeProjectile {

    public PoisonBreathProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public PoisonBreathProjectile(Level level, LivingEntity entity) {
        super(EntityRegistry.POISON_BREATH_PROJECTILE.get(), level, entity);
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
            double speed = this.f_19796_.nextDouble() * 0.4 + 0.45;
            for (int i = 0; i < 20; i++) {
                double offset = 0.25;
                double ox = Math.random() * 2.0 * offset - offset;
                double oy = Math.random() * 2.0 * offset - offset;
                double oz = Math.random() * 2.0 * offset - offset;
                double angularness = 0.8;
                Vec3 randomVec = new Vec3(Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness).normalize();
                Vec3 result = rotation.scale(3.0).add(randomVec).normalize().scale(speed);
                this.m_9236_().addParticle(this.f_19796_.nextFloat() < 0.25F ? ParticleHelper.ACID_BUBBLE : ParticleHelper.ACID, x + ox, y + oy, z + oz, result.x, result.y, result.z);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (DamageSources.applyDamage(entity, this.damage, SpellRegistry.POISON_BREATH_SPELL.get().getDamageSource(this, this.m_19749_())) && entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
        }
    }
}